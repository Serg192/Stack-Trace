package com.sstohnij.stacktraceqabackendv0.exception.custom;

public class NotValidRequestParameter extends RuntimeException{
    public NotValidRequestParameter(String msg) {
        super(msg);
    }
}
