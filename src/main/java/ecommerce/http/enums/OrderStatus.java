package ecommerce.http.enums;

public enum OrderStatus {
    PENDING("pending"), APPROVED("approved"), CANCELLED("cancelled"), REFUNDED("refunded");

    private String status;

    OrderStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }
}
