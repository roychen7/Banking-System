package Modules;

import Exceptions.InsufficientFundsException;
import Exceptions.NegativeAmountException;
import Exceptions.noAccountsFoundException;

import java.io.IOException;
import java.util.*;

public class BankAccount {

    private AccountFile accountfile;
    private String accountFileName;
    Date date;
    private ClassToGetNewId NewIdCalculatorObj;

    public BankAccount() throws IOException {
        accountfile = new AccountFile();
        accountFileName = System.getProperty("user.dir") + "/src/Data/Accounts";
        NewIdCalculatorObj = new ClassToGetNewId();
    }

    //EFFECTS: returns account amount
    public int getAmount(AccountModel account) {
        return account.getAmount();
    }

    //MODIFIES: this
    //EFFECTS: deducts sum from amount
    public void withdrawSum(int sum, AccountModel account) throws IOException {
        account.setAmount(account.getAmount() - sum);
        updateAccount(account.getAccountId(), getStringFormatAccount(account));
    }

    //MODIFIES: this
    //EFFECTS: adds sum to amount
    public void depositSum(int sum, AccountModel account) throws IOException {
        account.setAmount(account.getAmount() + sum);
        updateAccount(account.getAccountId(), getStringFormatAccount(account));
    }

    //MODIFIES: this
    //EFFECTS: adds sum to account amount
    public void deposit(int amount, AccountModel account) throws IOException, NegativeAmountException {
        negativeAmountThrower(amount);
        depositSum(amount, account);
    }

    //MODIFIES: this
    //EFFECTS: deducts sum from account balance
    public void withdraw(int amount, AccountModel account) throws IOException, InsufficientFundsException, NegativeAmountException {
        negativeAmountThrower(amount);
        if (amount > getAmount(account)) {
            throw new InsufficientFundsException();
        } else {
            withdrawSum(amount, account);
        }
    }

    // throws negative amount exception if amount is < 0
    public void negativeAmountThrower(int amount) throws NegativeAmountException {
        if (amount < 0) {
            throw new NegativeAmountException();
        }
    }


    // creates an account, updating it in the Accounts text file, as well as singleton's account lists, and also
    // registering it as an observer of the input user, and setting its subject to the user for Observer design pattern
    public boolean createAccount (int userId, String accountType, User user) throws IOException, noAccountsFoundException {
        int accountID = NewIdCalculatorObj.getNewId(SingletonAccount.getInstance().accountListInStrings);
        date = new Date();
        String s = accountID + "," + userId + "," + accountType + ",0," + date.toString() + ",false";
        accountfile.writeToFile(accountFileName, s);
        SingletonAccount.getInstance().accountListInStrings.add(s);
        AccountModel a = createAccountModel(s.split(","));
        SingletonAccount.getInstance().accountModelList.add(a);
        user.registerObs(a);
        a.setSubject(user);
        return true;
    }

    // returns list of accounts in SingletonAccount's lists that correspond to given userId
    public List<AccountModel> filterAccListByUserId(int userId) {
        List<AccountModel> ret = new ArrayList<>();
        for (int i = 0; i < SingletonAccount.getInstance().accountModelList.size(); i++) {
            if (SingletonAccount.getInstance().accountModelList.get(i).getUserId() == userId) {
                ret.add(SingletonAccount.getInstance().accountModelList.get(i));
            }
        }
        return ret;
    }

    // creates account from given string array, assumes list is a correct string[] to represent a bank account
    public AccountModel createAccountModel(String[] list) {
        AccountModel account = new AccountModel(Integer.parseInt(list[0]),
                Integer.parseInt(list[1]),
                list[2],
                Integer.parseInt(list[3]),
                list[4],
                Boolean.parseBoolean(list[5]));
        return account;
    }

    // returns account corresponding to accountId, throws noAccountsFoundException if account was not found
    public AccountModel findAccount(int accountId) throws noAccountsFoundException {
        for (AccountModel s : SingletonAccount.getInstance().accountModelList) {
            if (s.getAccountId() == accountId) {
                return s;
            }
        }
        throw new noAccountsFoundException();
    }

    // replaces line in Accounts at wherever the account with accountId is located with updatedRow
    public void updateAccount(int accountId, String updatedRow) throws IOException {
        int index = 0;
        for (String s : SingletonAccount.getInstance().accountListInStrings) {
            String[] accountArray = s.split(",");
            if (accountArray.length >= 6) {
                if (accountId == Integer.parseInt(accountArray[0])) {
                    SingletonAccount.getInstance().accountListInStrings.set(index, updatedRow);
                }
            }
            index++;
        }
        accountfile.writeFullListToFile(accountFileName, SingletonAccount.getInstance().accountListInStrings);
    }

    // returns string form used to store an account in Accounts
    public String getStringFormatAccount(AccountModel account) {
        return account.getAccountId() + "," + account.getUserId() + "," + account.getAccountType()
                + "," + account.getAmount() + "," + account.getCreationDate() + "," + account.getDelete();
    }

    // initializes singleton account's lists
    public void initializeSingletonAccount() throws IOException {
        SingletonAccount sa = SingletonAccount.getInstance();
        sa.accountListInStrings = accountfile.readFromFile(accountFileName);
        for (String s : sa.accountListInStrings) {
            String[] arr = s.split(",");
            sa.accountModelList.add(createAccountModel(arr));
        }
    }
}