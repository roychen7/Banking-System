package Test;

import Exceptions.InsufficientFundsException;
import Exceptions.NegativeAmountException;
import Modules.AccountModel;
import Modules.BankAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestBankAccount {
    AccountModel acc;
    BankAccount ba;

    @BeforeEach
    public void runBefore() throws IOException {
        Date d = new Date();
        acc = new AccountModel(1,1, "Chequing",0,d.toString(),false);
        ba = new BankAccount();
    }

    @Test
    void testDeposit() throws IOException, NegativeAmountException {
        assertEquals(0, acc.amount);
        ba.deposit(500, acc);
        assertEquals(500, acc.amount);
        try {
            ba.deposit(-10, acc);
            fail("Should have caught an exception");
        }
        catch (NegativeAmountException ne) {

        }
        assertEquals(500, acc.amount);
        String s = System.getProperty("user.dir") + "/src/Data/Accounts";
    }

    @Test
    void testWithdraw() throws IOException, NegativeAmountException {
        assertEquals(0, acc.amount);
        try {
            ba.withdraw(10, acc);
            fail("Should have caught an insufficient funds exception");
        }
        catch (InsufficientFundsException ie) {

        }
        try {
            ba.withdraw(-10, acc);
            fail ("Should have caught a negative amount exception");
        } catch (InsufficientFundsException ie) {
            fail ("Should have caught a negative amount exception instead");
        } catch (NegativeAmountException ne) {

        }
        ba.deposit(500, acc);
        assertEquals(500, acc.amount);
    }
}
