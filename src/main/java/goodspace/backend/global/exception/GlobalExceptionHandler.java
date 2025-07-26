package goodspace.backend.global.exception;

import io.jsonwebtoken.MalformedJwtException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.sql.SQLException;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<String> handleNotFound(NoHandlerFoundException exception) {
        log.trace("[ERROR RESPONSE] handleNotFound", exception);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 엔드포인트입니다");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleBeanValidation(MethodArgumentNotValidException exception) {
        log.info("[ERROR RESPONSE] bean validation", exception);

        FieldError fieldError = exception.getBindingResult()
                .getFieldErrors()
                .get(0);

        return ResponseEntity.status(BAD_REQUEST).body(fieldError.getDefaultMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFound(EntityNotFoundException exception) {
        log.trace("[ERROR RESPONSE] entity not found", exception);

        return ResponseEntity.status(UNPROCESSABLE_ENTITY).body(exception.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException exception) {
        log.trace("[ERROR RESPONSE] illegal argument", exception);

        return ResponseEntity.status(BAD_REQUEST).body("Illegal argument: " + exception.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalState(IllegalStateException exception) {
        log.trace("[ERROR RESPONSE] illegal state", exception);

        return ResponseEntity.status(BAD_REQUEST).body("Illegal state: " + exception.getMessage());
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<String> handleIllegalJwt(MalformedJwtException exception) {
        log.trace("[ERROR RESPONSE] malformed jwt", exception);

        return ResponseEntity.status(UNAUTHORIZED).body("부적절한 JWT 토큰입니다: " + exception.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleIllegalJson(HttpMessageNotReadableException exception) {
        log.trace("[ERROR RESPONSE] http message not readable", exception);

        return ResponseEntity.status(BAD_REQUEST).body("JSON 파싱에 실패했습니다: " + exception.getMessage());
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<String> handleSQLException(SQLException exception) {
        log.warn("[ERROR RESPONSE] sql exception", exception);

        return ResponseEntity.status(CONFLICT).body("SQL exception: " + exception.getMessage());
    }

    @ExceptionHandler({DataIntegrityViolationException.class, ConstraintViolationException.class})
    public ResponseEntity<String>  handleDataIntegrityViolation(DataIntegrityViolationException exception) {
        log.warn("[ERROR RESPONSE] data integrity violation", exception);

        return ResponseEntity.status(CONFLICT).body("DB 무결성 제약조건에 위반됩니다: " + exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handle(Exception exception) {
        log.error("[ERROR RESPONSE] unexpected exception", exception);

        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body("예상하지 못한 예외가 발생했습니다: " + exception.getMessage());
    }
}
