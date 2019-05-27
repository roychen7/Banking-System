package Test;


import Exceptions.InsufficientFundsException;
import Exceptions.NegativeAmountException;
import Exceptions.noAccountsFoundException;
import Modules.*;
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
        reg.createUser("a", "b");
        user = reg.findUser("a", "b");
        ba.createAccount(user.getUserId(), "Chequing", user);
        ba.createAccount(user.getUserId(), "Savings", user);
        acc = ba.findAccount(1);
        SingletonAccount initAcc = SingletonAccount.getInstance();
        Singleton initUser = Singleton.getInstance();
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
        SingletonAccount.getInstance().accountModelList = new ArrayList<>();
        SingletonAccount.getInstance().accountListInStrings = new ArrayList<>();
        Singleton.getInstance().userList = new ArrayList<>();
        Singleton.getInstance().userListInStrings = new ArrayList<>();
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
        SingletonAccount.getInstance().accountModelList = new ArrayList<>();
        SingletonAccount.getInstance().accountListInStrings = new ArrayList<>();
        Singleton.getInstance().userList = new ArrayList<>();
        Singleton.getInstance().userListInStrings = new ArrayList<>();
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
        SingletonAccount.getInstance().accountModelList = new ArrayList<>();
        SingletonAccount.getInstance().accountListInStrings = new ArrayList<>();
        Singleton.getInstance().userList = new ArrayList<>();
        Singleton.getInstance().userListInStrings = new ArrayList<>();
    }

    @Test
    void testAccountStrings() throws FileNotFoundException {
        String a = ba.getStringFormatAccount(acc);
        String comp = acc.getAccountId() + "," + acc.getUserId() + "," + acc.getAccountType()
                + "," + acc.getAmount() + "," + acc.getCreationDate() + "," + acc.getDelete();
        assertEquals(a, comp);
        String[] temp = comp.split(",");
        assertTrue(acc.equals(ba.createAccountModel(temp)));
        PrintWriter pw = new PrintWriter(s);
        pw.close();
        PrintWriter pw1 = new PrintWriter(d);
        pw1.close();
        SingletonAccount.getInstance().accountModelList = new ArrayList<>();
        SingletonAccount.getInstance().accountListInStrings = new ArrayList<>();
        Singleton.getInstance().userList = new ArrayList<>();
        Singleton.getInstance().userListInStrings = new ArrayList<>();
    }

    @Test
    void testcreateAccount() throws IOException, noAccountsFoundException {
        ba.createAccount(user.getUserId(), "Savings", user);
        AccountModel acc1 = ba.findAccount(3);
        List<AccountModel> testList = ba.filterAccListByUserId(user.getUserId());
        assertTrue(testList.size() == 3 && testList.get(2).equals(acc1));
        PrintWriter pw = new PrintWriter(s);
        pw.close();
        PrintWriter pw1 = new PrintWriter(d);
        pw1.close();
        SingletonAccount.getInstance().accountModelList = new ArrayList<>();
        SingletonAccount.getInstance().accountListInStrings = new ArrayList<>();
        Singleton.getInstance().userList = new ArrayList<>();
        Singleton.getInstance().userListInStrings = new ArrayList<>();
    }

    @Test
    void testFilterAccounts() throws IOException, noAccountsFoundException {
        List<AccountModel> tempList = ba.filterAccListByUserId(user.getUserId());
        AccountModel tempAcc1 = ba.findAccount(1);
        AccountModel tempAcc2 = ba.findAccount(2);
        assertTrue(tempList.size() == 2 && tempList.get(0).equals(tempAcc1) &&
                tempList.get(1).equals(tempAcc2));

        reg.createUser("test1", "test2");
        User temp = reg.findUser("test1", "test2");
        ba.createAccount(temp.getUserId(),"Savings", temp);
        tempList = ba.filterAccListByUserId(temp.getUserId());
        AccountModel tempAcc3 = ba.findAccount(3);
        assertTrue(tempList.size() == 1 && tempList.get(0).equals(tempAcc3));
        PrintWriter pw = new PrintWriter(s);
        pw.close();
        PrintWriter pw1 = new PrintWriter(d);
        pw1.close();
        SingletonAccount.getInstance().accountModelList = new ArrayList<>();
        SingletonAccount.getInstance().accountListInStrings = new ArrayList<>();
        Singleton.getInstance().userList = new ArrayList<>();
        Singleton.getInstance().userListInStrings = new ArrayList<>();
    }

    @Test
    void testUpdateAccount() throws IOException {
        AccountFile af = new AccountFile();
        ba.updateAccount(2, "asdf");
        List<String> tempList = af.readFromFile(d);
        assertTrue(tempList.get(1).equals("asdf"));
        PrintWriter pw = new PrintWriter(s);
        pw.close();
        PrintWriter pw1 = new PrintWriter(d);
        pw1.close();
    }

    @Test
    void testSingletonAccountInit() throws noAccountsFoundException, FileNotFoundException {
        AccountModel tempAcc1 = ba.findAccount(1);
        AccountModel tempAcc2 = ba.findAccount(2);

        assertTrue(SingletonAccount.getInstance().accountModelList.size() == 2 &&
        SingletonAccount.getInstance().accountModelList.get(0).equals(tempAcc1) &&
        SingletonAccount.getInstance().accountModelList.get(1).equals(tempAcc2));
        PrintWriter pw = new PrintWriter(s);
        pw.close();
        PrintWriter pw1 = new PrintWriter(d);
        pw1.close();
        SingletonAccount.getInstance().accountModelList = new ArrayList<>();
        SingletonAccount.getInstance().accountListInStrings = new ArrayList<>();
        Singleton.getInstance().userList = new ArrayList<>();
        Singleton.getInstance().userListInStrings = new ArrayList<>();
    }
}
