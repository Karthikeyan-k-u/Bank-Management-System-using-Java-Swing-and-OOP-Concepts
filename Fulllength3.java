import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

// =======================
// Custom Exceptions
// =======================
class InsufficientBalanceException extends Exception {
    public InsufficientBalanceException(String msg) {
        super(msg);
    }
}

class InvalidAmountException extends Exception {
    public InvalidAmountException(String msg) {
        super(msg);
    }
}

// =======================
// Abstract Account Class
// =======================
abstract class Account {
    protected String name;
    protected double balance;

    public Account(String name, double balance) {
        this.name = name;
        this.balance = balance;
    }

    public abstract void withdraw(double amount)
            throws InsufficientBalanceException, InvalidAmountException;

    public void deposit(double amount) throws InvalidAmountException {
        if (amount <= 0)
            throw new InvalidAmountException("Amount must be positive!");
        balance += amount;
    }

    public String getDetails() {
        return name + " | Balance: " + balance;
    }
}

// =======================
// Savings Account
// =======================
class SavingsAccount extends Account {
    public SavingsAccount(String name, double balance) {
        super(name, balance);
    }

    @Override
    public void withdraw(double amount)
            throws InsufficientBalanceException, InvalidAmountException {

        if (amount <= 0)
            throw new InvalidAmountException("Invalid withdrawal amount!");

        if (amount > balance)
            throw new InsufficientBalanceException("Not enough balance!");

        balance -= amount;
    }
}

// =======================
// Current Account
// =======================
class CurrentAccount extends Account {
    private double overdraft = 500;

    public CurrentAccount(String name, double balance) {
        super(name, balance);
    }

    @Override
    public void withdraw(double amount)
            throws InsufficientBalanceException, InvalidAmountException {

        if (amount <= 0)
            throw new InvalidAmountException("Invalid withdrawal!");

        if (amount > balance + overdraft)
            throw new InsufficientBalanceException("Overdraft limit exceeded!");

        balance -= amount;
    }
}

// =======================
// Bank (Composition)
// =======================
class Bank {
    private List<Account> accounts;

    public Bank() {
        accounts = new ArrayList<>();
    }

    public void addAccount(Account acc) {
        accounts.add(acc);
    }

    public List<Account> getAccounts() {
        return accounts;
    }
}

// =======================
// GUI
// =======================
class MainFrame extends JFrame {
    private JTextField nameField, amountField;
    private JComboBox<String> typeBox;
    private JTextArea output;
    private Bank bank = new Bank();

    public MainFrame() {
        setTitle("Bank Management System");
        setSize(400, 400);
        setLayout(new FlowLayout());

        nameField = new JTextField(10);
        amountField = new JTextField(10);
        typeBox = new JComboBox<>(new String[]{"Savings", "Current"});

        JButton createBtn = new JButton("Create Account");
        JButton depositBtn = new JButton("Deposit");
        JButton withdrawBtn = new JButton("Withdraw");

        output = new JTextArea(10, 30);

        add(new JLabel("Name:"));
        add(nameField);
        add(new JLabel("Amount:"));
        add(amountField);
        add(typeBox);
        add(createBtn);
        add(depositBtn);
        add(withdrawBtn);
        add(new JScrollPane(output));

        // =======================
        // Create Account
        // =======================
        createBtn.addActionListener(e -> {
            try {
                String name = nameField.getText();
                double amt = Double.parseDouble(amountField.getText());

                Account acc;
                if (typeBox.getSelectedItem().equals("Savings")) {
                    acc = new SavingsAccount(name, amt);
                } else {
                    acc = new CurrentAccount(name, amt);
                }

                bank.addAccount(acc);
                output.append("Account Created: " + acc.getDetails() + "\n");

            } catch (Exception ex) {
                output.append("Invalid input!\n");
            }
        });

        // =======================
        // Deposit
        // =======================
        depositBtn.addActionListener(e -> {
            try {
                if (bank.getAccounts().isEmpty()) {
                    output.append("No account found!\n");
                    return;
                }

                double amt = Double.parseDouble(amountField.getText());
                Account acc = bank.getAccounts().get(0);

                acc.deposit(amt);
                output.append("Deposit successful!\n");

            } catch (InvalidAmountException ex) {
                output.append("Error: " + ex.getMessage() + "\n");
            } catch (Exception ex) {
                output.append("Invalid input!\n");
            }
        });

        // =======================
        // Withdraw
        // =======================
        withdrawBtn.addActionListener(e -> {
            try {
                if (bank.getAccounts().isEmpty()) {
                    output.append("No account found!\n");
                    return;
                }

                double amt = Double.parseDouble(amountField.getText());
                Account acc = bank.getAccounts().get(0);

                acc.withdraw(amt);
                output.append("Withdraw successful!\n");

            } catch (InsufficientBalanceException | InvalidAmountException ex) {
                output.append("Error: " + ex.getMessage() + "\n");
            } catch (Exception ex) {
                output.append("Invalid input!\n");
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}

// =======================
// Main Class
// =======================
public class Fulllength3 {
    public static void main(String[] args) {
        new MainFrame();
    }
}