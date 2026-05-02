// --- New Subsystems ---
class TaxCalculator {
    double calculateTax(double price, String state) {
        // Hardcode: CA -> 8%, else 0%
        return state.equalsIgnoreCase("CA") ? price * 0.08 : 0;
    }
}

class Logger {
    void log(String message) {
        System.out.println("[LOG " + System.currentTimeMillis() + "] " + message);
    }
}

// --- Updated Facade ---
class ExtendedCheckoutFacade {
    private Inventory inventory = new Inventory();
    private Payment payment = new Payment();
    private Shipping shipping = new Shipping();
    private Email email = new Email();
    private TaxCalculator taxCalc = new TaxCalculator();
    private Logger logger = new Logger();

    public OrderResult checkout(String userId, String productId, double price, String address, String state) {
        logger.log("Start checkout for " + userId); // Log every attempt

        double tax = taxCalc.calculateTax(price, state);
        double totalAmount = price + tax;

        if (!inventory.checkStock(productId)) {
            logger.log("Failed: Out of stock");
            return new OrderResult(false, null, "Out of stock.");
        }

        if (!payment.charge(userId, totalAmount)) {
            logger.log("Failed: Payment decline");
            return new OrderResult(false, null, "Payment failed.");
        }

        inventory.reserve(productId);

        if (!shipping.isAvailable()) {
            payment.refund(userId, totalAmount);
            inventory.release(productId);
            logger.log("Failed: Shipping unavailable");
            return new OrderResult(false, null, "Shipping unavailable.");
        }

        String label = shipping.createLabel(address);
        shipping.schedulePickup(label);
        
        // Include tax in the total price shown in the email
        email.send(userId, "Order Confirmed", "Total paid (inc. tax): " + totalAmount);

        logger.log("Success: Order " + label);
        return new OrderResult(true, label, "Order success.");
    }
}


public class exercise2 {
    public static void main(String[] args) {
        ExtendedCheckoutFacade facade = new ExtendedCheckoutFacade();
        // Example checkout including state for tax calculation
        OrderResult result = facade.checkout("user456", "PHONE-007", 799.99, "456 Elm St", "CA");

        System.out.println("Status: " + (result.isSuccess() ? "SUCCESS" : "FAILED"));
        System.out.println("Message: " + result.getMessage());
        if (result.getTrackingNumber() != null) {
            System.out.println("Tracking: " + result.getTrackingNumber());
        }
    }
}