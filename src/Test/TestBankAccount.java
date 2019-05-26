package Test;


import Exceptions.InsufficientFundsException;
import Exceptions.NegativeAmountException;
import Exceptions.noAccountsFoundException;
import Modules.AccountModel;
import Modules.BankAccount;
import Modules.RegularUserBase;
import Modules.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestBankAccount {
    String s = System.getProperty("user.dir") + "/src/Data/Login";
    String d = System.getProperty("user.dir") + "/src/Data/Accounts";
    AccountModel acc;
    BankAccount ba;
    RegularUserBase reg;
    User user;

    @BeforeEach
    public void runBefore() throws IOException, noAccountsFoundException {
        ba = new BankAccount();
        reg = new RegularUserBase();
        user = new User(1, "a", "b", "Regular");
        Date d = new Date();
        reg.createUser("a", "b");
        ba.createAccount(user.getUserId(), "Chequing", user);
        acc = new AccountModel(1,1, "Chequing",0,d.toString(),false);
    }

    @Test
    void testDeposit() throws IOException, NegativeAmountException {
        assertEquals(0, acc.getAmount());
        ba.deposit(500, acc);
        assertEquals(500, acc.getAmount());
        try {
            ba.deposit(-10, acc);
            fail("Should have caught an exception");
        }
        catch (NegativeAmountException ne) {

        }
        assertEquals(500, acc.getAmount());
        PrintWriter pw = new PrintWriter(s);
        pw.close();
        PrintWriter pw1 = new PrintWriter(d);
        pw1.close();
    }

    @Test
    void testWithdraw() throws IOException, NegativeAmountException {
        assertEquals(0, acc.getAmount());
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
        assertEquals(500, acc.getAmount());
        PrintWriter pw = new PrintWriter(s);
        pw.close();
        PrintWriter pw1 = new PrintWriter(d);
        pw1.close();
    }

    @Test
    void testFindAccount() throws noAccountsFoundException, FileNotFoundException {
        try {
            assertTrue(acc.equals(ba.findAccount(acc.getAccountId())));
        } catch (noAccountsFoundException ne) {
            fail ("Should not have caught this exception");
        }
        try {
            assertFalse(acc.equals(ba.findAccount(acc.getAccountId() + 5)));
            fail ("Should have caught a noAccountsFoundException");
        } catch(noAccountsFoundException ne) {

        }
        PrintWriter pw = new PrintWriter(s);
        pw.close();
        PrintWriter pw1 = new PrintWriter(d);
        pw1.close();
    }

    @Test
    void testAccountStrings() throws FileNotFoundException {
        String a = ba.getStringFormatAccount(acc);
        String comp = acc.getAccountId() + "," + acc.getUserId() + "," + acc.getAccountType()
                + "," + acc.getAmount() + "," + acc.getCreationDate() + "," + acc.getDelete();
        assertEquals(a, comp);
        String[] temp = comp.split(",");
        AccountModel acc1 = new AccountModel();
        assertTrue(acc.equals(ba.createAccountFromstringArray(temp)));
        PrintWriter pw = new PrintWriter(s);
        pw.close();
        PrintWriter pw1 = new PrintWriter(d);
        pw1.close();
    }
}
