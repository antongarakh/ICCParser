package com.exadel.test.exceptions;


public class ParserException extends Exception {
    public ParserException(Exception e) {
        super(e.getClass().toString());
    }
}
