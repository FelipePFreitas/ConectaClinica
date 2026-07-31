package com.felipefreitas.ConectaClinica.exceptions;

import com.felipefreitas.ConectaClinica.enums.ErrorEnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)

    public ResponseEntity<ProblemDetail> handleBaseException(BaseException ex) {
        ErrorEnum error = ex.getErrorEnum();

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatusCode.valueOf(error.getHttpStatus()),
                error.getErrorMessage()
        );

        problemDetail.setTitle("Erro de Regra de Negócio");
        problemDetail.setType(URI.create("https://conectaclinica.com.br/errors/" + error.name().toLowerCase()));
        problemDetail.setProperty("errorCode", error.getErrorCode());
        problemDetail.setProperty("timestamp", Instant.now());

        return ResponseEntity.status(error.getHttpStatus()).body(problemDetail);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationException(MethodArgumentNotValidException ex) {
        ErrorEnum error = ErrorEnum.DADOS_INVALIDOS;

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente."
        );

        Map<String, String> invalidFields = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        (mensagemAntiga, mensagemNova) -> mensagemAntiga
                ));

        problemDetail.setTitle("Erro de Validação de Dados");
        problemDetail.setType(URI.create("https://conectaclinica.com.br/errors/dados-invalidos"));
        problemDetail.setProperty("errorCode", error.getErrorCode());
        problemDetail.setProperty("invalidFields", invalidFields);
        problemDetail.setProperty("timestamp", Instant.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }
}
