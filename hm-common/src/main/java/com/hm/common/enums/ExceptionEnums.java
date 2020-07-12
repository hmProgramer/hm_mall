package com.hm.common.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum  ExceptionEnums {   //枚举是具有固定实例个数的对象
    PRICE_CANNOT_BE_NULL(400,"价格不能为空！"),
    CATEGORYDATA_IS_NULL(404,"商品分类数据为空！"),
    BRANDDATA_IS_NULL(404,"品牌数据为空！"),
    BRANDDATA_SAVE_ERROR(500,"服务器新增品牌失败！"),
    UPLOAD_FILE_ERROR(500,"文件上傳失敗！"),
    INVALID_FILE_ERROR(400,"校验文件失败！"),
    ;
    private int code;
    private String msg;

}
