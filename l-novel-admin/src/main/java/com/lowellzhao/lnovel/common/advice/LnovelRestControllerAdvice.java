package com.lowellzhao.lnovel.common.advice;

import com.alibaba.fastjson.JSON;
import com.lowellzhao.lnovel.common.constant.LNovelConstant;
import com.lowellzhao.lnovel.common.exception.LnovelException;
import com.lowellzhao.lnovel.common.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.Objects;

/**
 * 全局异常捕获
 *
 * @author lowellzhao
 * @since 2022-05-29
 */
@Slf4j
@ResponseStatus(HttpStatus.ACCEPTED)
@RestControllerAdvice(annotations = RestController.class)
public class LnovelRestControllerAdvice {

    /**
     * 异常日志打印
     *
     * @param e 异常
     */
    private Result<Object> logParam(Exception e) {
        // 请求标识
        String traceId = MDC.get(LNovelConstant.TRACE_ID);
        // 请求信息
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes == null) {
            return Result.error("error", "服务异常，错误编码:" + traceId);
        }
        HttpServletRequest request = servletRequestAttributes.getRequest();

        // uri
        String uri = request.getRequestURI();
        log.error("exception traceId:{} | uri:{} | msg:{}", traceId, uri, e.getMessage(), e);
        return Result.error("error", "服务异常，错误编码:" + traceId);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result<Object> exception(MissingServletRequestParameterException e) {
        this.logParam(e);
        return Result.error("缺少必要参数", "缺少参数:" + e.getParameterName());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<Object> exception(HttpMessageNotReadableException e) {
        this.logParam(e);
        return Result.error("缺少必要参数", "参数无法读取或解析");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Result<Object> exception(IllegalArgumentException e) {
        this.logParam(e);
        return Result.error("参数异常", "请求参数不正确");
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public Result<Object> exception(HttpMediaTypeNotSupportedException e) {
        Map<String, String> parameters = Objects.requireNonNull(e.getContentType()).getParameters();
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String url = request.getRequestURL().toString();
        log.error("入参异常, url:{}, 参数:{}", url, JSON.toJSONString(parameters), e);
        return Result.error();
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result<Object> exception(MethodArgumentTypeMismatchException e) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String url = request.getRequestURL().toString();
        final String platform = request.getHeader("Platform");
        log.error("url:{}, 客户端:{}, 参数名:{}, 参数值:{}", url, platform, e.getName(), e.getValue(), e);
        return Result.error();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Object> exception(MethodArgumentNotValidException e) {
        return Result.error(Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result<Object> exception(Exception e) {
        return this.logParam(e);
    }

    @ExceptionHandler(RuntimeException.class)
    public Result<Object> exception(RuntimeException e) {
        return this.logParam(e);
    }

    @ExceptionHandler(RemoteException.class)
    public Result<Object> exception(RemoteException e) {
        return this.logParam(e);
    }

    @ExceptionHandler(IllegalStateException.class)
    public Result<Object> exception(IllegalStateException e) {
        return this.logParam(e);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public Result<Object> exception(ConstraintViolationException e) {
        return this.logParam(e);
    }

    @ExceptionHandler(LnovelException.class)
    public Result<Object> exception(LnovelException e) {
        return this.logParam(e);
    }

}
