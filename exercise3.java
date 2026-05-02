// --- Legacy Class (Do not modify this!) ---
class LegacyOrderProcessor {
    // **DO NOT MODIFY THIS CLASS**
    public void processOrder(String customerEmail, String itemCode, double amount, String deliveryAddress) {
        Inventory inv = new Inventory();
        Payment pay = new Payment();
        Shipping ship = new Shipping();
        Email email = new Email();

        if (!inv.checkStock(itemCode)) {
            System.out.println("Out of stock");
            return;
        }
        if (!pay.charge(customerEmail, amount)) {
            System.out.println("Payment fail");
            return;
        }
        inv.reserve(itemCode);
        String label = ship.createLabel(deliveryAddress);
        ship.schedulePickup(label);
        email.send(customerEmail, "Order", "Shipped");
        System.out.println("Order complete");
    }
}


class LegacyOrderFacade {
    
    private LegacyOrderProcessor legacyProcessor;

    public LegacyOrderFacade() {
        this.legacyProcessor = new LegacyOrderProcessor();
    }

    /**
     * Provides a clean placeOrder method as requested
     */
    public void placeOrder(String email, String item, double price, String address) {
        
        legacyProcessor.processOrder(email, item, price, address);
    }

    public static void main(String[] args) {
        LegacyOrderFacade facade = new LegacyOrderFacade();
        facade.placeOrder("nnamdi.ebunam@pau.edu.ng", "BOOK-101", 50.0, "PAU Campus");
    }
}

public class exercise3 {
    public static void main(String[] args) {
        LegacyOrderFacade facade = new LegacyOrderFacade();
        facade.placeOrder("nnamdi.ebunam@pau.edu.ng", "BOOK-101", 50.0, "PAU Campus");
    }
}
