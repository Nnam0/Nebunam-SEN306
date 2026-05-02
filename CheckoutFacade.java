// --- Subsystems ---
class Inventory {
    boolean checkStock(String productId) { return true; }
    void reserve(String productId) { System.out.println("Reserved " + productId); }
    void release(String productId) { System.out.println("Released " + productId); }
}

class Payment {
    boolean charge(String userId, double amount) { return true; }
    void refund(String userId, double amount) { System.out.println("Refunded " + amount); }
}

class Shipping {
    String createLabel(String address) { return "TRK" + System.currentTimeMillis(); }
    void schedulePickup(String label) { System.out.println("Pickup scheduled for " + label); }
    boolean isAvailable() { return true; } // For rollback demonstration
}

class Email {
    void send(String to, String subject, String body) { System.out.println("Email to " + to + ": " + subject); }
}

// --- Helper Class for Results ---
class OrderResult {
    private final boolean success;
    private final String trackingNumber;
    private final String message;

    public OrderResult(boolean success, String trackingNumber, String message) {
        this.success = success;
        this.trackingNumber = trackingNumber;
        this.message = message;
    }

    public boolean isSuccess() { return success; }
    public String getTrackingNumber() { return trackingNumber; }
    public String getMessage() { return message; }
}

// --- The Facade Implementation ---
public class CheckoutFacade {
    private Inventory inventory;
    private Payment payment;
    private Shipping shipping;
    private Email email;

    public CheckoutFacade() {
        this.inventory = new Inventory();
        this.payment = new Payment();
        this.shipping = new Shipping();
        this.email = new Email();
    }

    public OrderResult checkout(String userId, String productId, double price, String address) {
        // 1. Check Stock
        if (!inventory.checkStock(productId)) {
            return new OrderResult(false, null, "Item out of stock.");
        }

        // 2. Process Payment
        if (!payment.charge(userId, price)) {
            return new OrderResult(false, null, "Payment failed.");
        }

        // 3. Reserve Inventory (Only after successful payment)
        inventory.reserve(productId);

        // 4. Shipping Check (Rollback Point)
        if (!shipping.isAvailable()) {
            // If shipping fails, we must undo previous steps
            payment.refund(userId, price);
            inventory.release(productId);
            return new OrderResult(false, null, "Shipping unavailable. Payment refunded and item released.");
        }

        // 5. Success Path
        String label = shipping.createLabel(address);
        shipping.schedulePickup(label);
        email.send(userId, "Order Confirmed", "Your order is on the way! Tracking: " + label);

        return new OrderResult(true, label, "Order processed successfully.");
    }

    // Main method to test the logic
    public static void main(String[] args) {
        CheckoutFacade checkoutSystem = new CheckoutFacade();
        OrderResult result = checkoutSystem.checkout("user123", "LAPTOP-001", 1200.00, "123 PAU Way");
        
        System.out.println("Status: " + (result.isSuccess() ? "SUCCESS" : "FAILED"));
        System.out.println("Message: " + result.getMessage());
    }
}