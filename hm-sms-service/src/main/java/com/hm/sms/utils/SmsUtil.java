package com.hm.sms.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.hm.common.utils.JsonUtils;
import com.hm.sms.properties.SmsProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@EnableConfigurationProperties(SmsProperties.class)
@Component
public class SmsUtil {

    @Autowired
    private SmsProperties prop;

    @Autowired
    private StringRedisTemplate redisTemplate;

    //产品名称:云通信短信API产品,开发者无需替换
    private  static final String product = "Dysmsapi";
    //产品域名,开发者无需替换
    private  static final String domain = "dysmsapi.aliyuncs.com";
    private static final String SMS_PREFIX = "sms:phone:";
    private static final long SMS_MIN_INTERVAL_IN_MILLIS = 60000;


    public SendSmsResponse sendSms(String signature, String templateCode, String phone, Map<String, Object> params) {
        try {

            String key = SMS_PREFIX + phone;
            
            //限流操作
            //1 从redis中取出时间戳
            String lastTime = redisTemplate.opsForValue().get(key);
            //2 redis中有当前手机号的值
           if(StringUtils.isNotBlank(lastTime)) {
               long last = Long.valueOf(lastTime);
               if(System.currentTimeMillis()-last < SMS_MIN_INTERVAL_IN_MILLIS){
                   //Redis中发送信息的手机号为超过1min,则返回空，不进行短信发送
                   log.info("【短信服务】短信发送频率过高，被拦截，手机号：{}", phone);
                   return null;
               }
            }
            //可自助调整超时时间
            System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
            System.setProperty("sun.net.client.defaultReadTimeout", "10000");

            //初始化acsClient,暂不支持region化
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", prop.getAccessKeyId(), prop.getAccessKeySecret());
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
            IAcsClient acsClient = new DefaultAcsClient(profile);

            //组装请求对象-具体描述见控制台-文档部分内容
            SendSmsRequest request = new SendSmsRequest();
            request.setMethod(MethodType.POST);
            //必填:待发送手机号
            request.setPhoneNumbers(phone);
            //必填:短信签名-可在短信控制台中找到
            request.setSignName(signature);
            //必填:短信模板-可在短信控制台中找到
            request.setTemplateCode(templateCode);
            //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
            request.setTemplateParam(JsonUtils.toString(params));

            //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
            //request.setSmsUpExtendCode("90997");

            //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
            request.setOutId("123456");

            //hint 此处可能会抛出异常，注意catch
            SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);

            redisTemplate.opsForValue().set(key,String.valueOf(System.currentTimeMillis()),1, TimeUnit.MINUTES);
            return sendSmsResponse;
        } catch (Exception e) {
            log.error("【短信服务】发送信息失败，手机号码：{}", phone);
            return null;
        }
    }
}
