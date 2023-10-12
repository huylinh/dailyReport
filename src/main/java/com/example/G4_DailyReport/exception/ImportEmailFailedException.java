package com.example.G4_DailyReport.exception;

import com.example.G4_DailyReport.validation.ErrorDetail;

import java.util.List;

public class ImportEmailFailedException extends Exception {
    private final List<ErrorDetail> errorDetails;
    public ImportEmailFailedException(List<ErrorDetail> errorDetails) {
        this.errorDetails = errorDetails;
    }

    @Override
    public String getMessage() {
        StringBuilder message = new StringBuilder();
        for (ErrorDetail errorDetail : errorDetails) {
            message.append(errorDetail.getMessage()).append("\n");
        }
        return message.toString();
    }
}
