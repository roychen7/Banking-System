package Modules;

import Exceptions.InsufficientFundsException;
import Exceptions.NegativeAmountException;
import Exceptions.noAccountsFoundException;

import java.io.IOException;
import java.util.*;

public class BankAccount {

    private AccountFile accountfile = new AccountFile();
    private String accountFileName = System.getProperty("user.dir") + "/src/Data/Accounts";
    private List<String> accountList = accountfile.readFromFile(accountFileName);
    private AccountModel _accountModel;
    Date date = new Date();
    Map TMap = new HashMap<>();
    private CommonBase commonbase = new CommonBase();

    public BankAccount(AccountModel accountModel) throws IOException {
        _accountModel = accountModel;
    }

    public BankAccount() throws IOException {
    }

    //EFFECTS: returns account amount
    public int getAmount(AccountModel account) {
        return account.amount;
    }

    //MODIFIES: this
    //EFFECTS: deducts sum from amount
    public void withdrawSum(int sum, AccountModel account) throws IOException {
        account.amount = account.amount - sum;
        updateAccount(account.accountId, getStringFormatAccount(account));
    }

    //MODIFIES: this
    //EFFECTS: adds sum to amount
    public void depositSum(int sum, AccountModel account) throws IOException {
        account.amount = account.amount + sum;
        updateAccount(account.accountId, getStringFormatAccount(account));
    }

    //MODIFIES: this
    //EFFECTS: adds sum to account amount
    public void deposit(int amount, AccountModel account) throws IOException, NegativeAmountException {
        negativeAmountThrower(amount);
        depositSum(amount, account);
        withdrawDepositPrinter("deposited");
    }

    //MODIFIES: this
    //EFFECTS: deducts sum from account balance
    public void withdraw(int amount, AccountModel account) throws IOException, InsufficientFundsException, NegativeAmountException {
        negativeAmountThrower(amount);
        if (amount > getAmount(account)) {
            throw new InsufficientFundsException();
        } else {
            withdrawSum(amount, account);
            withdrawDepositPrinter("withdrawn");
        }
    }

    public void withdrawDepositPrinter(String type) {
        System.out.println("Your money has been " + type + ".");
    }

    public void negativeAmountThrower(int amount) throws NegativeAmountException {
        if (amount < 0) {
            throw new NegativeAmountException();
        }
    }


    public boolean createAccount(int userId, String accountType) throws IOException, noAccountsFoundException {
        int accountID = commonbase.getNewId(accountList);
        String s = accountID + "," + userId + "," + accountType + ",0," + date.toString() + ",false";
        accountfile.writeToFile(accountFileName, s);
        AccountModel account = getAccountModelFromString(s);
        accountList.add(s);
        return true;

    }

    public List<AccountModel> getAccountList(int userId) {
        List<AccountModel> accountsWithSameUserId = new ArrayList();
        for (String s : accountList) {
            String[] accountArray = s.split(",");
            if (accountArray.length >= 2) {
                if (Integer.parseInt(accountArray[1]) == userId) {
                    accountsWithSameUserId.add(getAccountModelFromString(s));
                }
            }
        }
        return accountsWithSameUserId;
    }

    public AccountModel getAccountModelFromString(String accountString) {
        String[] list = accountString.split(",");
        return createAccountModel(list);
    }

    public AccountModel createAccountModel(String[] list) {
        AccountModel account = new AccountModel(Integer.parseInt(list[0]),
                Integer.parseInt(list[1]),
                list[2],
                Integer.parseInt(list[3]),
                list[4],
                Boolean.parseBoolean(list[5]));
        return account;
    }


    public AccountModel findAccount(int accountId) throws noAccountsFoundException {
        AccountModel newAccount = new AccountModel();
        for (String s : accountList) {
            String[] accountArray = s.split(",");
            if (accountArray.length >= 1) {
                if (Integer.parseInt(accountArray[0]) == accountId) {
                    return createAccountFromstringArray(accountArray, newAccount);
                }
            }
        }
        throw new noAccountsFoundException();
    }

    public AccountModel createAccountFromstringArray(String[] accountArray, AccountModel newAccount) {
        newAccount = new AccountModel(Integer.parseInt(accountArray[0]),
                Integer.parseInt(accountArray[1]), accountArray[2]
                , Integer.parseInt(accountArray[3]), accountArray[4],
                Boolean.parseBoolean(accountArray[5]));
        return newAccount;
    }

    public void updateAccount(int accountId, String updatedRow) throws IOException {
        int index = 0;
        for (String s : accountList) {
            String[] accountArray = s.split(",");
            if (accountArray.length >= 6) {
                if (accountId == Integer.parseInt(accountArray[0])) {
                    accountList.set(index, updatedRow);
                }
            }
            index++;
        }
        accountfile.writeFullListToFile(accountFileName, accountList);
    }

    public boolean deleteAccount(int accountId, AccountModel account) throws IOException {
        int index = 0;
        for (String s : accountList) {
            String[] accountArray = s.split(",");
            if (accountArray.length >= 6) {
                if (accountId == Integer.parseInt(accountArray[0])) {
                    String updatedRow = accountArray[0] + "," + accountArray[1] + "," + accountArray[2]
                            + "," + accountArray[3] + "," + accountArray[4] + ",true";
                    updateAccount(accountId, updatedRow);
                    account.delete = true;
                    return true;
                }
            }
            index++;
        }
        return false;
    }

    public String getStringFormatAccount(AccountModel account) {
        return account.accountId + "," + account.userId + "," + account.accountType
                + "," + account.amount + "," + account.creationDate + "," + account.delete;
    }
}