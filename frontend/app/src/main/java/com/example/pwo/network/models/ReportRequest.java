package com.example.pwo.network.models;

public class ReportRequest {
    private String reason;

    public ReportRequest(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
