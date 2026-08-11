package com.anudeepreddy.JobPortal_Job.Service.Exception;

public class JobNotFoundException extends RuntimeException {
    public JobNotFoundException(String message) {
        super(message);
    }
}
