package Test;

import Exceptions.InsufficientFundsException;
import Exceptions.NegativeAmountException;
import Modules.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestUser {

   RegularUserBase reg;
   UserInfo ui;
   String s = System.getProperty("user.dir") + "/src/Data/Login";
   CommonBase b;
   List<String> userList;
   AccountFile accountfile;

    @BeforeEach
    public void runBefore() throws IOException {
        reg = new RegularUserBase();
        accountfile = new AccountFile();
        reg.createUser("abc", "def");
        reg.createUser("123", "456");
        ui = new UserInfo();
        b = new CommonBase();
        userList = accountfile.readFromFile(s);
    }

    @Test
    void testUserRow() throws IOException {
        assertEquals(0, ui.findUserRow("abc", "def"));
        assertEquals(1, ui.findUserRow("123", "456"));
        assertEquals(-1, ui.findUserRow("a", "b"));
        PrintWriter pw = new PrintWriter(s);
        pw.close();
    }

    @Test
    void testCheckUserExists() throws IOException {
        assertTrue(ui.checkUserExists("123", "456"));
        assertFalse(ui.checkUserExists("a", "b"));
        PrintWriter pw = new PrintWriter(s);
        pw.close();
    }

    @Test
    void testFindUserId() throws IOException {
        assertEquals(1, ui.findUserId("abc", "def"));
        assertEquals(2, ui.findUserId("123", "456"));
        assertEquals(-1, ui.findUserId("a", "b"));
        PrintWriter pw = new PrintWriter(s);
        pw.close();
    }

    @Test
    void testGetNewId() throws IOException {
        assertTrue(ui.checkUserExists("123", "456"));
        assertTrue(ui.checkUserExists("abc", "def"));
        assertEquals(3,b.getNewId(userList));
        PrintWriter pw = new PrintWriter(s);
        pw.close();
    }
}
