package org.deenu.gradle.parser.exception;

public class ParserFailedException extends Exception {

  public ParserFailedException(String message, Throwable t) {
    super(message, t);
  }

  public ParserFailedException(Exception exception) {
    super(exception);
  }

  public ParserFailedException(String message) {
    super(message);
  }
}
