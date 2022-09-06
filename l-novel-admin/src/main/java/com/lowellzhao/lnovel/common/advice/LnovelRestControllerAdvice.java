package com.lowellzhao.lnovel.common.advice;

import com.alibaba.fastjson.JSON;
import com.lowellzhao.lnovel.common.exception.LnovelException;
import com.lowellzhao.lnovel.common.vo.Result;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;
import java.util.Enumeration;
import java.util.HashMap;
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
    @SneakyThrows
    private void logParam(Exception e) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        // 地址
        String url = request.getRequestURL().toString();
        // 请求参数
        Map<String, String[]> parameterMap = request.getParameterMap();
        // json参数
        String param = "";
        int contentLength = request.getContentLength();
        if (contentLength > 0) {
            byte[] buffer = new byte[contentLength];
            for (int i = 0; i < contentLength; i++) {
                int read = request.getInputStream().read(buffer, i, contentLength - i);
                if (read == -1) {
                    break;
                }
                i += read;
            }
            param = new String(buffer, StandardCharsets.UTF_8);
        }
        // 请求头
        Map<String, String> headerMap = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        String nextElement = headerNames.nextElement();
        while (StringUtils.isNotBlank(nextElement)) {
            String header = request.getHeader(nextElement);
            headerMap.put(nextElement, header);
            nextElement = headerNames.nextElement();
        }
        log.error("exception url:{};\nform参数:{};\nbody参数:{};\n请求头:{}",
                url, JSON.toJSONString(parameterMap), param, JSON.toJSONString(headerMap), e);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result<Object> exception(MissingServletRequestParameterException e) {
        logParam(e);
        return Result.error("缺少必要参数", "缺少参数:" + e.getParameterName());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<Object> exception(HttpMessageNotReadableException e) {
        logParam(e);
        return Result.error("缺少必要参数", "参数无法读取或解析");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Result<Object> exception(IllegalArgumentException e) {
        logParam(e);
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
        logParam(e);
        return Result.error();
    }

    @ExceptionHandler(RuntimeException.class)
    public Result<Object> exception(RuntimeException e) {
        logParam(e);
        return Result.error();
    }

    @ExceptionHandler(RemoteException.class)
    public Result<Object> exception(RemoteException e) {
        logParam(e);
        return Result.error();
    }

    @ExceptionHandler(IllegalStateException.class)
    public Result<Object> exception(IllegalStateException e) {
        logParam(e);
        return Result.error("参数异常", "请求参数不正确");
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public Result<Object> exception(ConstraintViolationException e) {
        logParam(e);
        return Result.error("请求参数异常", e.getMessage());
    }

    @ExceptionHandler(LnovelException.class)
    public Result<Object> exception(LnovelException e) {
        logParam(e);
        return Result.error(e.getMessage());
    }

}
