package com.jay.instagram.controller.advice;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@ControllerAdvice
public class FileUploadExceptionAdvice {
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseBody
    public JSONObject handleMaxUploadSizeExceededException(HttpServletResponse response, Exception ex){
        log.warn("[MaxUploadSizeExceededException]: {}",ex.getMessage());
        response.setStatus(HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE);
        JSONObject responseJson = new JSONObject();
        responseJson.put("message", "File exceed limit!");
        return responseJson;
    }
}