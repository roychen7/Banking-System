package Test;


import Exceptions.noAccountsFoundException;
import Modules.*;
import Modules.Observer;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestUser {

   RegularUserBase reg;
   SuperUserBase sup;
   String s = System.getProperty("user.dir") + "/src/Data/Login";
   String d = System.getProperty("user.dir") + "/src/Data/Accounts";
   AccountFile accountfile;
   BankAccount ba;
   User u1;
   User u2;
   User u3;

    @BeforeEach
    public void runBefore() throws IOException, noAccountsFoundException {
        reg = new RegularUserBase();
        sup = new SuperUserBase();
        accountfile = new AccountFile();
        ba = new BankAccount();
        sup.createUser("roy", "chen");
        reg.createUser("abc", "def");
        reg.createUser("123", "456");

        u1 = reg.findUser("roy", "chen");
        u2 = reg.findUser("abc", "def");
        u3 = reg.findUser("123", "456");

        ba.createAccount(1, "Savings", u1);
        ba.createAccount(1, "Savings", u1);
        ba.createAccount(2, "Chequings", u2);
        ba.createAccount(3, "Savings", u3);
    }

    @Test
    void testCheckUserExists() throws IOException {
        assertTrue(reg.checkUserExists("roy", "chen"));
        assertTrue(reg.checkUserExists("abc", "def"));
        assertFalse(reg.checkUserExists("chen", "roy"));
        Singleton.getInstance().userList = new ArrayList<>();
        Singleton.getInstance().userListInStrings = new ArrayList<>();
    }

    @Test
    void testFindUser() {
        User u = new User(1, "roy", "chen", "Super");
        assertTrue(reg.findUser("roy", "chen").equals(u));
        assertNull(reg.findUser("9", "10"));
        Singleton.getInstance().userList = new ArrayList<>();
        Singleton.getInstance().userListInStrings = new ArrayList<>();
    }

    @Test
    void testCreateUser() throws IOException {
        reg.createUser("john", "doe");
        assertTrue(Singleton.getInstance().userList.size() == 4);
        assertTrue(Singleton.getInstance().userList.get(3).equals(new User(4, "john", "doe", "Regular")));
        List<String> tempList = accountfile.readFromFile(s);
        User u = reg.findUser("john", "doe");
        assertEquals(tempList.get(3), reg.userToString(u));
        PrintWriter pw = new PrintWriter(s);
        pw.close();
        PrintWriter pw1 = new PrintWriter(d);
        pw1.close();
        Singleton.getInstance().userList = new ArrayList<>();
        Singleton.getInstance().userListInStrings = new ArrayList<>();
    }

    @Test
    void testIfUserDoesntExistThenCreate() throws IOException {
        assertFalse(reg.ifUserDoesntExistsThenCreate("roy", "chen", "Super"));
        assertTrue(Singleton.getInstance().userList.size() == 3);
        assertTrue(reg.ifUserDoesntExistsThenCreate("roy", "chon", "Regular"));
        assertTrue(Singleton.getInstance().userList.size() == 4);
        User u = reg.findUser("roy", "chon");
        assertTrue(Singleton.getInstance().userList.get(3).equals(u));
        PrintWriter pw = new PrintWriter(s);
        pw.close();
        PrintWriter pw1 = new PrintWriter(d);
        pw1.close();
        Singleton.getInstance().userList = new ArrayList<>();
        Singleton.getInstance().userListInStrings = new ArrayList<>();
    }

    @Test
    void testInitializeSingleton() throws FileNotFoundException {
        assertTrue(Singleton.getInstance().userList.size() == 3 &&
                Singleton.getInstance().userList.get(0).equals(u1) &&
                Singleton.getInstance().userList.get(1).equals(u2) &&
                Singleton.getInstance().userList.get(2).equals(u3));
        PrintWriter pw = new PrintWriter(s);
        pw.close();
        PrintWriter pw1 = new PrintWriter(d);
        pw1.close();
        Singleton.getInstance().userList = new ArrayList<>();
        Singleton.getInstance().userListInStrings = new ArrayList<>();
    }

    @Test
    void testInitUser() throws FileNotFoundException, noAccountsFoundException {
        List<Observer> listObservers = Singleton.getInstance().userList.get(0).getObservers();
        assertTrue(listObservers.size() == 2);
        List<Observer> listObservers2 = Singleton.getInstance().userList.get(1).getObservers();
        assertTrue(listObservers2.size() == 1);
        List<Observer> listObservers3 = Singleton.getInstance().userList.get(2).getObservers();
        assertTrue(listObservers3.size() == 1);
        AccountModel a1 = ba.findAccount(1);
        AccountModel a2 = ba.findAccount(2);
        AccountModel a3 = ba.findAccount(3);
        AccountModel a4 = ba.findAccount(4);
        assertTrue(listObservers.get(0).equals(a1) && listObservers.get(1).equals(a2) &&
        listObservers2.get(0).equals(a3) && listObservers3.get(0).equals(a4));
        PrintWriter pw = new PrintWriter(s);
        pw.close();
        PrintWriter pw1 = new PrintWriter(d);
        pw1.close();
        Singleton.getInstance().userList = new ArrayList<>();
        Singleton.getInstance().userListInStrings = new ArrayList<>();
    }

}
