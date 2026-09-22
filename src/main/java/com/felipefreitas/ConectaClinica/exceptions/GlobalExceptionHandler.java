package com.felipefreitas.ConectaClinica.exceptions;



import com.felipefreitas.ConectaClinica.enums.ErrorEnum;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseExceptions.class)
    public ResponseEntity<ProblemDetail> handleBaseException(BaseExceptions ex) {
        ErrorEnum error = ex.getErrorEnum();

        // Cria o padrão oficial do Spring
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatusCode.valueOf(error.getHttpStatus()),
                error.getErrorMessage()
        );

        // Adiciona o seu código de erro customizado como uma propriedade extra
        problemDetail.setProperty("errorCode", error.getErrorCode());
        problemDetail.setTitle("Erro na Regra de Negócio");

        return ResponseEntity.status(error.getHttpStatus()).body(problemDetail);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        String detalhes = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining("; "));

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatusCode.valueOf(ErrorEnum.VALOR_INVALIDO.getHttpStatus()),
                detalhes.isBlank() ? ErrorEnum.VALOR_INVALIDO.getErrorMessage() : detalhes
        );
        problemDetail.setProperty("errorCode", ErrorEnum.VALOR_INVALIDO.getErrorCode());
        problemDetail.setTitle("Erro de Validação");

        return ResponseEntity.badRequest().body(problemDetail);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ProblemDetail> handleConstraintViolation(ConstraintViolationException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatusCode.valueOf(ErrorEnum.VALOR_INVALIDO.getHttpStatus()),
                ex.getMessage()
        );
        problemDetail.setProperty("errorCode", ErrorEnum.VALOR_INVALIDO.getErrorCode());
        problemDetail.setTitle("Erro de Validação");

        return ResponseEntity.badRequest().body(problemDetail);
    }
}