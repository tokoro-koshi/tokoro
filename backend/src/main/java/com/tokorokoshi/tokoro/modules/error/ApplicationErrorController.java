package com.tokorokoshi.tokoro.modules.error;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(
        name = "Error",
        description = "API for handling errors (should not be used directly)"
)
@RestController
public class ApplicationErrorController implements ErrorController {
    private final Logger logger =
            LoggerFactory.getLogger(ApplicationErrorController.class);

    private final Pattern notFoundRegex =
            Pattern.compile("^No static resource (.*?)\\.$");

    @Operation(
            summary = "Handle errors",
            description = "Returns a JSON response with the error message and status code"
    )
    @RequestMapping(
            value = "/error",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
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
        String webRequestMessage = messageObj != null
                ? messageObj.toString()
                : "";

        var exception = (Exception) webRequest.getAttribute(
                RequestDispatcher.ERROR_EXCEPTION,
                WebRequest.SCOPE_REQUEST
        );
        String message = !webRequestMessage.isBlank()
                ? webRequestMessage
                : exception != null
                ? exception.getMessage()
                : "An error occurred";


        // Extract URL from message if it's a 404 error
        Matcher matcher = notFoundRegex.matcher(message);
        if (matcher.find()) {
            String path = matcher.group(1);
            message = "Resource not found: /" + path;
        }

        HttpStatus httpStatus = status != null
                ? HttpStatus.valueOf(Integer.parseInt(status.toString()))
                : HttpStatus.INTERNAL_SERVER_ERROR;

        if (logger.isErrorEnabled() && status != null && status >= 500) {
            String log = String.format(
                    "Error Status: %d, Message: %s, Exception: %s",
                    status,
                    message,
                    exception
            );
            logger.error(log);
        }

        var jo = new JSONObject();
        jo.put("message", message);
        jo.put("status", httpStatus.value());
        return new ResponseEntity<>(jo.toString(), httpStatus);
    }
}
