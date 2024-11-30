package com.tokorokoshi.tokoro.modules.error;

import jakarta.servlet.RequestDispatcher;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


@RestController
public class ApplicationErrorController implements ErrorController {
    private final Logger logger =
            LoggerFactory.getLogger(ApplicationErrorController.class);

    private final Pattern notFoundRegex =
            Pattern.compile("^No static resource (.*?)\\.$");

    @RequestMapping(value = "/error",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> handleError(WebRequest webRequest) {
        Object statusObj = webRequest.getAttribute(
                RequestDispatcher.ERROR_STATUS_CODE,
                WebRequest.SCOPE_REQUEST
        );
        Integer status = statusObj != null
                ? Integer.parseInt(statusObj.toString())
                : null;

        Object messageObj = webRequest.getAttribute(
                RequestDispatcher.ERROR_MESSAGE,
                WebRequest.SCOPE_REQUEST
        );
        String message = messageObj != null
                ? messageObj.toString()
                : "Something wrong happened";

        Object exception = webRequest.getAttribute(
                RequestDispatcher.ERROR_EXCEPTION,
                WebRequest.SCOPE_REQUEST
        );

        // Extract URL from message if it's a 404 error
        Matcher matcher = notFoundRegex.matcher(message);
        if (matcher.find()) {
            String path = matcher.group(1);
            message = "Resource not found: /" + path;
        }

        HttpStatus httpStatus = status != null
                ? HttpStatus.valueOf(Integer.parseInt(status.toString()))
                : HttpStatus.INTERNAL_SERVER_ERROR;

        if (logger.isErrorEnabled() &&
                (exception != null || (status != null && status >= 500)))
            logger.error("Error Status: {}, Message: {}, Exception: {}",
                    status,
                    message,
                    exception);

        var jo = new JSONObject();
        jo.put("message", message);
        jo.put("status", httpStatus.value());
        return new ResponseEntity<>(jo.toString(), httpStatus);
    }
}
