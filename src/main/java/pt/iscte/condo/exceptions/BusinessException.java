package pt.iscte.condo.exceptions;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
public class BusinessException extends RuntimeException {

    private final String message;
    private Throwable cause;

}
