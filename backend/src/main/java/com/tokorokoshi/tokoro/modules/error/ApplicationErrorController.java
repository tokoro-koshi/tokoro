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

import java.util.regex.Pattern;


@RestController
public class ApplicationErrorController implements ErrorController {
    private final Logger logger =
            LoggerFactory.getLogger(ApplicationErrorController.class);

    private final Pattern notFoundRegex = Pattern.compile(
            "^No static resource (.*?)\\.$"
    );

    @RequestMapping(value = "/error",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> handleError(WebRequest webRequest) {
        Object status = webRequest.getAttribute(
                RequestDispatcher.ERROR_STATUS_CODE,
                WebRequest.SCOPE_REQUEST
        );
        Object message = webRequest.getAttribute(
                RequestDispatcher.ERROR_MESSAGE,
                WebRequest.SCOPE_REQUEST
        );
        Object exception = webRequest.getAttribute(
                RequestDispatcher.ERROR_EXCEPTION,
                WebRequest.SCOPE_REQUEST
        );

        if (message == null) {
            message = "Something wrong happened";
        }

        var matcher = notFoundRegex.matcher(message.toString());
        if (matcher.find()) {
            // Extract matched URL from message by matcher
            var path = matcher.group(1);
            message = "Resource not found: /" + path;
        }

        HttpStatus httpStatus = status != null
                ? HttpStatus.valueOf(Integer.parseInt(status.toString()))
                : HttpStatus.INTERNAL_SERVER_ERROR;

        logger.error(
                "Error Status: {}, Message: {}, Exception: {}",
                status,
                message,
                exception
        );

        var jo = new JSONObject();
        jo.put("message", message.toString());
        jo.put("status", httpStatus.value());
        return new ResponseEntity<>(jo.toString(), httpStatus);
    }
}
