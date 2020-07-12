package com.hm.upload.service;

import com.hm.common.enums.ExceptionEnums;
import com.hm.common.exception.HmException;
import com.hm.upload.config.UploadProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@EnableConfigurationProperties(UploadProperties.class)
public class UploadService {

    @Autowired
    private UploadProperties uploadProp;

    public String uploadFile(MultipartFile file) {
        String url = null;
        try {
        //1支持的文件类型
        List<String> suffixes = Arrays.asList("image/png", "image/jpg","image/jpeg");

        // 校验文件类型
        String contentType = file.getContentType();
        if (!uploadProp.getAllowTypes().contains(contentType)){
           throw new HmException(ExceptionEnums.INVALID_FILE_ERROR);
        }

        BufferedImage readImage = ImageIO.read(file.getInputStream());
        //TODO 如果文件内容不是image，那么读出来的内容为空
        if (readImage == null){
            throw new HmException(ExceptionEnums.INVALID_FILE_ERROR);
        }



        //2 保存文件到本地
        File dir = new File("D:\\BaiduNetdiskDownload\\upload",file.getOriginalFilename());


            file.transferTo(dir);
            //3 返回路径
            url = uploadProp.getBaseUrl() + file.getOriginalFilename();
        } catch (IOException e) {
           //上传失败
            log.error("上传文件失败");
            throw new HmException(ExceptionEnums.UPLOAD_FILE_ERROR);
        }
        return url;

    }
}
