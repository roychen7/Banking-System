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
    Date date = new Date();
    private CommonBase commonbase = new CommonBase();

    public BankAccount() throws IOException {
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

    public String getStringFormatAccount(AccountModel account) {
        return account.getAccountId() + "," + account.getUserId() + "," + account.getAccountType()
                + "," + account.getAmount() + "," + account.getCreationDate() + "," + account.getDelete();
    }
}