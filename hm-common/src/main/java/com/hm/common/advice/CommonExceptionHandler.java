package com.hm.common.advice;

import com.hm.common.enums.ExceptionEnums;
import com.hm.common.exception.HmException;
import com.hm.common.vo.ExceptionResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CommonExceptionHandler {

    //方法的返回值就是想返回页面的东西
    @ExceptionHandler(HmException.class)
    public ResponseEntity<ExceptionResult> handleException(HmException e){
        //这样写有问题，状态码只能返回400， 返回消息界面太简单
        //如何解决？
        //通过自定义异常处理
     //   return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        ExceptionEnums em = e.getExceptionEnums();
        return ResponseEntity.status(em.getCode()).body(new ExceptionResult(em));
    }
}
