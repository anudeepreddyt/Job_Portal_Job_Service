package com.anudeepreddy.JobPortal_Job.Service.Exception;

public class OnlyAdminException extends RuntimeException {
    public OnlyAdminException(String message) {
        super(message);
    }
}
