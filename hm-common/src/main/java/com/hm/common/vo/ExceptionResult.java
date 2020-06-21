package com.hm.common.vo;

import com.hm.common.enums.ExceptionEnums;
import lombok.Data;

@Data
public class ExceptionResult {
    private int status;
    private String message;
    private  Long timeStamp;

    public ExceptionResult(ExceptionEnums em){
        this.message = em.getMsg();
        this.status = em.getCode();
        this.timeStamp = System.currentTimeMillis();
    }
}
