package gr.ece.ntua.javengers.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "This action is forbidden for not authorized users")
public class ForbiddenException extends RuntimeException {
}
