package org.deenu.gradle.parser.exception;

public class UnsupportedFileException extends Exception {

  public UnsupportedFileException(String message, Throwable t) {
    super(message, t);
  }

  public UnsupportedFileException(Exception exception) {
    super(exception);
  }

  public UnsupportedFileException(String message) {
    super(message);
  }
}
