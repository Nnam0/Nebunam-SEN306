package banking;

/**
 * Architectural case study demonstrating why composition fails when 
 * you need to change the fundamental validation of an unmodifiable parent
 */
public class BrokenOverdraft { 
    private BankAccount account = new BankAccount(); // Using composition to hold an internal reference 

    public void withdraw(double amount) {
        double current = account.getBalance(); // Fetch balance through public API getter 
        
        if (current - amount >= -500) { 
            // FAILURE POINT: This delegation triggers BankAccount.withdraw(). 
            // The method inside BankAccount blocks withdrawals if amount > balance,
            // preventing the account from going negative
            account.withdraw(amount); 
        }
    }
}