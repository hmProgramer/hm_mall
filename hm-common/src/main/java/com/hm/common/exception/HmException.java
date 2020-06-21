package com.hm.common.exception;

import com.hm.common.enums.ExceptionEnums;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class HmException extends RuntimeException {
    private ExceptionEnums exceptionEnums;


}
