package ecommerce.http.enums;

public enum OrderStatus {
    PENDING("pending"), APPROVED("approved"), FINISHED("finished"), CANCELLED(
            "cancelled"), REFOUNDED("refounded");

    private String status;

    OrderStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }
}
