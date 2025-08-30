package mini.integration.lib.module.exception;

import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import mini.integration.lib.module.exception.handling.BusinessRuleException;
import mini.integration.lib.module.exception.handling.InvalidArgumentException;
import mini.integration.lib.module.exception.handling.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@RestControllerAdvice
@Log4j2
public class ExceptionControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNoResource(ResourceNotFoundException ex) {
        return createResponseEntity(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidArgumentException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidArgument(InvalidArgumentException ex) {
        return createResponseEntity(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ExceptionResponse> handleBusinessRule(BusinessRuleException ex) {
        return createResponseEntity(ex, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleGeneric(Exception ex) {
        logger.error(ex.getMessage(), ex);
        return createResponseEntity(new Throwable("General Exception"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ExceptionResponse> createResponseEntity(Throwable ex, HttpStatusCode code) {
        logger.info(ex.getMessage(), ex);
        return ResponseEntity.status(code).body(ExceptionResponse.builder().code(code)
                        .errors(List.of(ExceptionDetailResponse.builder().message(ex.getMessage()).build())).build());
    }

    /**
     * Handle BadCredentialsException. Triggered when validation with hibernate validator fails.
     *
     * @param ex BadCredentialsException
     * @param request WebRequest
     * @return CommonResult
     */
    @NonNull
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, @NonNull HttpHeaders headers,
                    @NonNull HttpStatusCode status, @NonNull WebRequest request) {
        logger.info(ex.getMessage(), ex);
        List<ExceptionDetailResponse> errors = ex.getBindingResult().getAllErrors().stream().map(
                        error -> ExceptionDetailResponse.builder().field(((FieldError) error).getField()).message(error.getDefaultMessage()).build())
                        .toList();

        ExceptionResponse exceptionResponse = ExceptionResponse.builder().code(HttpStatus.BAD_REQUEST).errors(errors).build();

        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * @apiNote handle when unknown property has been request API
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status,
                    WebRequest request) {
        logger.info(ex.getMessage(), ex);
        String message = ex.getMostSpecificCause().toString();
        List<ExceptionDetailResponse> errors = List.of(ExceptionDetailResponse.builder()
                        .message(message.substring(message.indexOf(":") + 1, message.indexOf("\"", message.indexOf("\"") + 2))).build());

        ExceptionResponse exceptionResponse = ExceptionResponse.builder().code(HttpStatus.BAD_REQUEST).errors(errors).build();

        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
}
