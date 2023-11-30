package ru.nn.domain;

public class TestStatistics {
    private int totalTests = 0;
    private int successfulTests = 0;
    private int failedTests = 0;

    public int getTotalTests() {
        return totalTests;
    }

    public void setTotalTests(int allTests) {
        this.totalTests = allTests;
    }

    public int getSuccessfulTests() {
        return successfulTests;
    }

    public void setSuccessfulTests(int successfulTests) {
        this.successfulTests = successfulTests;
    }

    public int getFailedTests() {
        return failedTests;
    }

    public void setFailedTests(int failedTests) {
        this.failedTests = failedTests;
    }
}
