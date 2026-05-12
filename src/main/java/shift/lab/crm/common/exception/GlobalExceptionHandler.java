package shift.lab.crm.common.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import shift.lab.crm.analytics.exception.InvalidPeriodException;
import shift.lab.crm.transaction.exception.UnknownPaymentTypeException;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DomainValidationException.class)
    public ResponseEntity<ErrorResponseDto> handleDomainValidation(DomainValidationException dve){
        return buildError(
                HttpStatus.BAD_REQUEST,
                "Domain validation error",
                List.of(dve.getMessage())
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleArgumentNotValid(MethodArgumentNotValidException exc) {
        List<String> details = exc.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> {
                    if ("timeType".equals(error.getField())) {
                        return "timeType must be one of: DAY, MONTH, QUARTER, YEAR";
                    }

                    return error.getField() + ": " + error.getDefaultMessage();
                })
                .toList();

        return buildError(
                HttpStatus.BAD_REQUEST,
                "Bad request",
                details
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDto> handleHttpNotReadable(HttpMessageNotReadableException exc){
        return buildError(
                HttpStatus.BAD_REQUEST,
                "Bad request",
                List.of(exc.getMessage())
        );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNotFound(EntityNotFoundException exception){
        return buildError(
                HttpStatus.NOT_FOUND,
                "Entity was not found",
                List.of(exception.getMessage())
        );
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponseDto> handleArgumentMismatch(MethodArgumentNotValidException exception) {
        return buildError(
                HttpStatus.BAD_REQUEST,
                "Bad request",
                List.of(exception.getTitleMessageCode())
        );
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleHandlerNotFound(NoHandlerFoundException exception){
        return buildError(
                HttpStatus.NOT_FOUND,
                "This URL is not supported",
                List.of("Unsupported URL: " + exception.getRequestURL())
        );
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponseDto> handleUnsupportedHttp(HttpRequestMethodNotSupportedException exception){
        return buildError(
                HttpStatus.METHOD_NOT_ALLOWED,
                "Method is not allowed",
                List.of(exception.getMethod() + " is not allowed with this URL. Allowed are: " +
                        exception.getSupportedHttpMethods())
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDto> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String name = ex.getName();
        String type = ex.getRequiredType().getSimpleName();
        Object value = ex.getValue();
        String message = String.format("Given: '%s' is not valid '%s' '%s' data",
                value, type, name);
        return buildError(
                HttpStatus.BAD_REQUEST,
                "Bad request",
                List.of(message)
        );
    }

    @ExceptionHandler(UnknownPaymentTypeException.class)
    public ResponseEntity<ErrorResponseDto> handleUnknownPaymentType(UnknownPaymentTypeException exception){
        return buildError(
                HttpStatus.BAD_REQUEST,
                "Bad request",
                List.of(exception.getMessage())
        );
    }

    @ExceptionHandler(InvalidPeriodException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidPeriod(InvalidPeriodException exception){
        return buildError(
                HttpStatus.BAD_REQUEST,
                "Bad request",
                List.of(exception.getMessage())
        );
    }

    private ResponseEntity<ErrorResponseDto> buildError(
            HttpStatus status,
            String message,
            List<String> errorDetails
    ){
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                status.value(),
                message,
                errorDetails,
                LocalDateTime.now()
        );
        return ResponseEntity.status(status).body(errorResponseDto);
    }


    /* might be useless
    @ExceptionHandler(UnrecognizedPropertyException.class)
    public ResponseEntity<ErrorResponseDto> handleUnknownJsonField(UnrecognizedPropertyException exc){
        return buildError(
                HttpStatus.BAD_REQUEST,
                "Bad request",
                List.of("Added unrecognized property" + something...)
        );
    }
    */


}
