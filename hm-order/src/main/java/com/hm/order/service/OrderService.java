package com.hm.order.service;

import com.hm.auth.entity.UserInfo;
import com.hm.common.enums.ExceptionEnums;
import com.hm.common.exception.HmException;
import com.hm.common.utils.IdWorker;
import com.hm.item.dto.CartDto;
import com.hm.item.pojo.Sku;
import com.hm.order.client.AddressClient;
import com.hm.order.client.GoodsClient;
import com.hm.order.dto.AddressDTO;
import com.hm.order.dto.OrderDto;
import com.hm.order.dto.OrderStatusEnum;
import com.hm.order.filter.LoginInterceptor;
import com.hm.order.mapper.OrderDetailMapper;
import com.hm.order.mapper.OrderMapper;
import com.hm.order.mapper.OrderStatusMapper;
import com.hm.order.pojo.Order;
import com.hm.order.pojo.OrderDetail;
import com.hm.order.pojo.OrderStatus;
import com.hm.order.utils.PayHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Service
@Slf4j
public class OrderService {
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private OrderStatusMapper orderStatusMapper;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private PayLogService payLogService;



    @Autowired
    private PayHelper payHelper;

    @Transactional
    public Long createOrder(OrderDto orderDto) {


        //新增订单
        long orderId = idWorker.nextId();
        //完善订单信息
        Order order = new Order();
        order.setCreateTime(new Date());
        order.setOrderId(orderId);
        order.setPaymentType(orderDto.getPaymentType());
        order.setPostFee(0L);  //// TODO 调用物流信息，根据地址计算邮费

        //获取登录用户
        UserInfo user = LoginInterceptor.getLoginUser();
        //填充订单中的用户信息
        order.setUserId(user.getId());
        order.setBuyerNick(user.getName());
        order.setBuyerRate(false);  //卖家为留言

        //新增收货人地址信息
        //todo 收货人信息应该从数据库的物流信息表中获取
        AddressDTO addressDTO = AddressClient.findById(orderDto.getAddressId());
        if (addressDTO == null) {
            log.info("【创建订单】创建订单失败，orderId" + orderId);
            // 商品不存在，抛出异常
            throw new HmException(ExceptionEnums.RECEIVER_ADDRESS_NOT_FOUND);
        }

        order.setReceiver(addressDTO.getName());
        order.setReceiverAddress(addressDTO.getAddress());
        order.setReceiverCity(addressDTO.getCity());
        order.setReceiverDistrict(addressDTO.getDistrict());
        order.setReceiverMobile(addressDTO.getPhone());
        order.setReceiverZip(addressDTO.getZipCode());
        order.setReceiverState(addressDTO.getState());

        //新增付款金额
        //todo 通过流处理把orderDto转换成map
        Map<Long, Integer> skuNumMap = orderDto.getCarts().stream().collect(Collectors.toMap(c -> c.getSkuId(), c -> c.getNum()));

        //根据skuid找到订单详细信息
        List<Sku> skuList = goodsClient.querySkusByIds(new ArrayList<>(skuNumMap.keySet()));

        Double totalPay = 0.0;
        //填充orderDetail
        ArrayList<OrderDetail> orderDetails = new ArrayList<>();
        for (Sku sku:skuList) {
            Integer num = skuNumMap.get(sku);
            Double price = sku.getPrice();
            //todo 这里应该是+=符号
            totalPay += price*num;

            //填充订单详情
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setOwnSpec(sku.getOwnSpec());
            orderDetail.setSkuId(sku.getId());
            orderDetail.setTitle(sku.getTitle());
            orderDetail.setNum(num);
            orderDetail.setPrice(sku.getPrice().longValue());

            orderDetail.setImage(StringUtils.substringBefore(sku.getImages(), ","));
            orderDetails.add(orderDetail);
        }

        //保存实际金额
        //todo 注意实际的价格应该是加上邮费后的价格
        order.setActualPay(totalPay.longValue()+order.getPostFee());
        //保存总金额
        order.setTotalPay(totalPay.longValue());
        //新增订单状态
        //保存order
        int count = orderMapper.insertSelective(order);
        //todo 注意当创建订单失败时 即异常抛出就会回滚
        if (count != 1) {
            log.info("【创建订单】创建订单失败，orderId" + orderId);
            // 商品不存在，抛出异常
            throw new HmException(ExceptionEnums.CREATE_ORDER_ERROR);
        }

        //保存detail
        count = orderDetailMapper.insertList(orderDetails);
        if (count != orderDetails.size()) {
            log.info("【创建订单】创建订单失败，orderId" + orderId);
            // 商品不存在，抛出异常
            throw new HmException(ExceptionEnums.CREATE_ORDER_ERROR);
        }

        //填充order status
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(orderId);
        orderStatus.setStatus(OrderStatusEnum.INIT.value());
        orderStatus.setCreateTime(new Date());
        //保存orderStatus
        orderStatusMapper.insertSelective(orderStatus);
        //减库存
        //todo 注意这里使用的是同步的方法来减库存，而且这里用了取巧的方式将商品服务中的减库存操作
        //todo 放在了方法的后面，一旦减库存失败那么创建订单也就会回滚
        List<CartDto> cartDtoList = orderDto.getCarts();
        goodsClient.decreaseStock(cartDtoList);

        return orderId;
    }


    public String generateUrl(Long orderId) {
        //根据订单ID查询订单
        Order order = queryById(orderId);
        //判断订单状态
        if (order.getOrderStatus().getStatus() != OrderStatusEnum.INIT.value()) {
            throw new HmException(ExceptionEnums.ORDER_STATUS_EXCEPTION);
        }

        //todo 这里传入一份钱，用于测试使用，实际中使用订单中的实付金额
        String url = payHelper.createPayUrl(orderId, "乐优商城测试", /*order.getActualPay()*/1L);
        if (StringUtils.isBlank(url)) {
            throw new HmException(ExceptionEnums.CREATE_PAY_URL_ERROR);
        }

        //生成支付日志
        payLogService.createPayLog(orderId, order.getActualPay());

        return url;

    }
    public Order queryById(Long orderId) {
        Order order = orderMapper.selectByPrimaryKey(orderId);
        if (order == null) {
            throw new HmException(ExceptionEnums.ORDER_NOT_FOUND);
        }
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderId(orderId);
        List<OrderDetail> orderDetails = orderDetailMapper.select(orderDetail);
        order.setOrderDetails(orderDetails);
        OrderStatus orderStatus = orderStatusMapper.selectByPrimaryKey(orderId);
        order.setOrderStatus(orderStatus);
        return order;
    }


}
