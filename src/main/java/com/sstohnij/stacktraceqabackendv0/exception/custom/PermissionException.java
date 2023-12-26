package com.sstohnij.stacktraceqabackendv0.exception.custom;

public class PermissionException extends RuntimeException{

    public PermissionException(String msg){
        super(msg);
    }
}
