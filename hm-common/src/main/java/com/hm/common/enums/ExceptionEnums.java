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
    BRAND_NOT_FOUND(404, "品牌查询失败"),
    BRANDDATA_IS_NULL(404,"品牌数据为空！"),
    BRANDDATA_SAVE_ERROR(500,"服务器新增品牌失败！"),
    UPLOAD_FILE_ERROR(500,"文件上傳失敗！"),
    INVALID_FILE_ERROR(400,"校验文件失败！"),
    SPU_NOT_FOUND(201, "SPU未查询到"),
    SKU_NOT_FOUND(201, "SKU未查询到"),
    INVALID_PARAM(400, "参数错误"),
    DELETE_GOODS_ERROR(500, "删除商品错误"),
    GOODS_SAVE_ERROR(500, "新增商品错误"),
    GOODS_UPDATE_ERROR(500, "商品更新失败"),
    GOODS_NOT_SALEABLE(400, "商品未上架"),
    UPDATE_SALEABLE_ERROR(500, "更新商品销售状态错误"),
    GOODS_NOT_FOUND(400, "商品未查询到"),
    STOCK_NOT_ENOUGH(500, "商品库存不足"),
    STOCK_NOT_FOUND(204, "库存查询失败"),

    INVALID_USER_DATA_TYPE(400, "参数错误"),
    SPEC_PARAM_NOT_FOUND(204, "规格参数查询失败"),
    UPDATE_SPEC_PARAM_FAILED(500, "商品规格参数更新失败"),
    DELETE_SPEC_PARAM_FAILED(500, "商品规格参数删除失败"),
    SPEC_PARAM_CREATE_FAILED(500, "新增规格参数失败"),
    USER_NOT_LOGIN(401, "用户未登录，请登录"),

    USERNAME_OR_PASSWORD_ERROR(400, "账号或密码错误"),
    PASSWORD_NOT_MATCHING(400, "密码错误"),
    USER_NOT_EXIST(404, "用户不存在"),

    SPEC_GROUP_CREATE_FAILED(500, "新增规格组失败"),
    SPEC_GROUP_NOT_FOUND(204, "规格组查询失败"),
    DELETE_SPEC_GROUP_FAILED(500, "商品规格组删除失败"),
    UPDATE_SPEC_GROUP_FAILED(500, "商品规格组更新失败"),
    ;
    private int code;
    private String msg;

}
