package com.gumtree.techassignment.exceptionHandler;

import com.gumtree.techassignment.query.constants.ErrorCodes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class BaseExceptionHandler {
    @ExceptionHandler(value = IOException.class)
    public ResponseEntity<Map<String, String>> handleIOException(IOException ex){
        Map<String, String> errorCode = new HashMap<>();
        errorCode.put("code", ErrorCodes.ADBOOK_INVALID_FILE.name());
        errorCode.put("message", ex.getMessage());
        return new ResponseEntity<>(errorCode, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = ParseException.class)
    public ResponseEntity<Map<String, String>> handleParseException(ParseException ex){
        Map<String, String> errorCode = new HashMap<>();
        errorCode.put("code", ErrorCodes.ADBOOK_INVALID_ENTRY.name());
        errorCode.put("message", ex.getMessage());
        return new ResponseEntity<>(errorCode, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex){
        Map<String, String> errorCode = new HashMap<>();
        errorCode.put("code", ErrorCodes.ADBOOK_BAD_DATA.name());
        errorCode.put("message", ex.getMessage());
        return new ResponseEntity<>(errorCode, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = UnsupportedOperationException.class)
    public ResponseEntity<Map<String, String>> handleUnsupportedOperationException(UnsupportedOperationException ex){
        Map<String, String> errorCode = new HashMap<>();
        errorCode.put("code", ErrorCodes.ADBOOK_DATA_AMBIGUITY.name());
        errorCode.put("message", ex.getMessage());
        return new ResponseEntity<>(errorCode, HttpStatus.CONFLICT);
    }

}
