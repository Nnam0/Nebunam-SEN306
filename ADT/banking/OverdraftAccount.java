package banking;


public class OverdraftAccount extends BankAccount { 
    private static final double OVERDRAFT_LIMIT = -500; 

    @Override
    public void withdraw(double amount) {
        if (amount <= 0) {
            return; 
        }
        
       
        double newBalance = balance - amount; 
        
        if (newBalance >= OVERDRAFT_LIMIT) {
            balance = newBalance; 
            System.out.println("Withdrew " + amount + ", new balance: " + balance); 
        } else {
            System.out.println("Overdraft limit exceeded"); 
        }
    }

    @Override
    public void deposit(double amount) {
        super.deposit(amount); 
        System.out.println("Deposited " + amount + ", new balance: " + balance); 
    }
}