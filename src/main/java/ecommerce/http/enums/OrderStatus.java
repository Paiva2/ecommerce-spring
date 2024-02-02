package ecommerce.http.enums;

public enum OrderStatus {
    PENDING("pending"), FINISHED("finished"), CANCELLED("cancelled"), REFUNDED(
            "refunded"), PENDING_REFUND("pending_refund"), DENIED_REFUND("denied_refund");

    private String status;

    OrderStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }
}
