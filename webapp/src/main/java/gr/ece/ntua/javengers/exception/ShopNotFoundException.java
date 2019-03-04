package gr.ece.ntua.javengers.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "The shop that you've requested has not been found")
public class ShopNotFoundException extends RuntimeException {
}
