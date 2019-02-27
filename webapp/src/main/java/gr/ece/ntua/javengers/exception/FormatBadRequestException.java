package gr.ece.ntua.javengers.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Our REST API supports only json format")
public class FormatBadRequestException extends RuntimeException {

}