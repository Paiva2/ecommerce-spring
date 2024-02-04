package ecommerce.http.entities;

import java.io.Serializable;
import java.util.UUID;

import io.github.cdimascio.dotenv.Dotenv;

public class Email implements Serializable {
    private Dotenv dotenv = Dotenv.load();

    private String from;
    private String to;
    private String subject;
    private String text;
    private String toName;

    public Email() {
        this.from = dotenv.get("MAIL");
    }

    public Email(String to, String toName) {
        this.to = to;
        this.toName = toName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public void registerBuilder() {
        this.subject = "[E-commerce App] - Registered successfully!";
        this.text = "Welcome " + this.toName
                + "!, now you can make your orders, login on your profile, and much more!";
    }

    public void forgotPasswordBuilder() {
        this.subject = "[E-commerce App] - Password changed successfully!";
        this.text = "Hello " + this.toName + "!, your password has been successfully updated.";
    }

    public void requestRefundBuilder() {
        this.subject = "[E-commerce App] - Refund request";
        this.text = "Hello " + this.toName
                + "!, your refund request was made successfully, soon it will be analyzed by an admin.";
    }

    public void handleRefundBuilder(Boolean approve) {
        String deniedMsg =
                ", your refund request has been denied. Contact an admin for more details.";
        String approvedMsg =
                ", your refund request has been approved. Soon the value will be back on your wallet.";

        String refundStatus = approve ? approvedMsg : deniedMsg;

        this.subject = "[E-commerce App] - Refund status";
        this.text = "Hello " + this.toName + "! " + refundStatus;
    }

    public void handleOrderBuilder(Boolean orderMade, UUID orderNumber) {
        String orderPendingMsg = ", your order " + orderNumber
                + " will be pending due necessary amount not available on wallet, an admin will analyze it and return the status soon.";

        String orderMadeMsg = ", your order " + orderNumber + " has been successfully made.";

        String refundStatus = orderMade ? orderMadeMsg : orderPendingMsg;

        this.subject = "[E-commerce App] - Order status";
        this.text = "Hello " + this.toName + "! " + refundStatus;
    }

    public void handleOrderApprovalBuilder(Boolean orderMade, UUID orderNumber) {
        String orderPendingMsg = ", your order " + orderNumber
                + " has been denied. Contact an admin for more details.";

        String orderMadeMsg = ", your order " + orderNumber + " has been successfully approved.";

        String refundStatus = orderMade ? orderMadeMsg : orderPendingMsg;

        this.subject = "[E-commerce App] - Order status";
        this.text = "Hello " + this.toName + "! " + refundStatus;
    }

    public void newCouponBuilder(Double value, String code) {
        this.subject = "[E-commerce App] - New coupon!";
        this.text = "Hello " + this.toName
                + "! you have received a new coupon! You can use it inserting the code " + code
                + " on checkout and it will give you an discount of " + value
                + "% on the total purchase value!";
    }
}
