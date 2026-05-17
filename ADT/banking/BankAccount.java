package banking;


public class BankAccount { 
    protected double balance = 0.0; 

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount; 
        }
    }

    public void withdraw(double amount) {
        
        if (amount > 0 && amount <= balance) {
            balance -= amount; 
        }
    }

    public double getBalance() {
        return balance; 
    }
}