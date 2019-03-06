package com.tensquare.base.exception;

import entity.Result;
import entity.StatusCode;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 公共异常处理
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 */
//@ControllerAdvice
@RestControllerAdvice
public class BaseExceptionHandler {


    @ExceptionHandler(value = Exception.class)
//    @ResponseBody
    public Result error(Exception e) {
        e.printStackTrace();
        return new Result(false, StatusCode.ERROR, e.getMessage());
    }
}
