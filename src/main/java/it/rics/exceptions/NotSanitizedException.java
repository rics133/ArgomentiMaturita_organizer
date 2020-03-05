package it.rics.exceptions;

public class NotSanitizedException extends Exception {
  public NotSanitizedException(){
    super("QUERY NOT SANITIZED");
  }
}
