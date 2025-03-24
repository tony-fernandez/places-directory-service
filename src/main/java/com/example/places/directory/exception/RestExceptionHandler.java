package com.example.places.directory.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.example.places.directory.model.Problem;
import jakarta.validation.constraints.NotNull;
import java.time.format.DateTimeParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class RestExceptionHandler {

  @ExceptionHandler(AddressNotFoundException.class)
  public ResponseEntity<Problem> handleAddressNotFoundException(AddressNotFoundException e,
      WebRequest request) {
    return createProblem(e, NOT_FOUND);
  }

  @ExceptionHandler(DateTimeParseException.class)
  public ResponseEntity<Problem> handleDateTimeParseException(DateTimeParseException e,
      WebRequest request) {
    return createProblem(e, BAD_REQUEST);
  }

  @ExceptionHandler(DuplicateOpeningHoursException.class)
  public ResponseEntity<Problem> handleDuplicateOpeningHoursException(DuplicateOpeningHoursException e,
      WebRequest request) {
    return createProblem(e, CONFLICT);
  }

  @ExceptionHandler(ExistingPlaceException.class)
  public ResponseEntity<Problem> handleExistingPlaceException(ExistingPlaceException e,
      WebRequest request) {
    return createProblem(e, CONFLICT);
  }

  @ExceptionHandler(InvalidOpeningHoursException.class)
  public ResponseEntity<Problem> handleInvalidOpeningHoursException(InvalidOpeningHoursException e,
      WebRequest request) {
    return createProblem(e, BAD_REQUEST);
  }

  @ExceptionHandler(MissingTimeException.class)
  public ResponseEntity<Problem> handleMissingTimeException(MissingTimeException e,
      WebRequest request) {
    return createProblem(e, BAD_REQUEST);
  }


  @ExceptionHandler(OpeningHoursNotFoundException.class)
  public ResponseEntity<Problem> handleOpeningHoursNotFoundException(
      OpeningHoursNotFoundException e,
      WebRequest request) {
    return createProblem(e, NOT_FOUND);
  }

  @ExceptionHandler(PlaceNotFoundException.class)
  public ResponseEntity<Problem> placeNotFoundException(
      PlaceNotFoundException e,
      WebRequest request) {
    return createProblem(e, NOT_FOUND);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Problem> handleGlobalException(Exception e, WebRequest request) {
    return createProblem(e, INTERNAL_SERVER_ERROR);
  }

  @NotNull
  private ResponseEntity<Problem> createProblem(Exception e, HttpStatus status) {
    Problem problem = new Problem();
    problem.setCode(status.value());
    problem.setMessage(e.getMessage());
    return ResponseEntity.status(status.value()).body(problem);
  }
}
