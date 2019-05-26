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
    private CommonBase commonbase;

    public BankAccount() throws IOException {
        accountfile = new AccountFile();
        accountFileName = System.getProperty("user.dir") + "/src/Data/Accounts";
        commonbase = new CommonBase();
    }

    public AccountModel findById(int accountId) {
        for (AccountModel am : SingletonAccount.getInstance().accountModelList) {
            if (am.getAccountId() == accountId) {
                return am;
            }
        }
        return null;
    }

    public String findByIdString(int accountId) {
        for (String s : SingletonAccount.getInstance().accountListInStrings) {
            String[] temp = s.split(",");
            if (Integer.parseInt(temp[0]) == accountId) {
                return s;
            }
        }
        return "";
    }

    public void updateFile() throws IOException {
        accountfile.writeFullListToFile(accountFileName,SingletonAccount.getInstance().accountListInStrings);
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


    public boolean createAccount (int userId, String accountType, User user) throws IOException, noAccountsFoundException {
        int accountID = commonbase.getNewId(SingletonAccount.getInstance().accountListInStrings);
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

    public List<AccountModel> getAccountList(int userId) {
        List<AccountModel> accountsWithSameUserId = new ArrayList();
        for (String s : SingletonAccount.getInstance().accountListInStrings) {
            String[] accountArray = s.split(",");
            if (accountArray.length >= 2) {
                if (Integer.parseInt(accountArray[1]) == userId) {
                    accountsWithSameUserId.add(getAccountModelFromString(s));
                }
            }
        }
        return accountsWithSameUserId;
    }

    public List<AccountModel> filterAccListByUserId(int userId) {
        List<AccountModel> ret = new ArrayList<>();
        for (int i = 0; i < SingletonAccount.getInstance().accountModelList.size(); i++) {
            if (SingletonAccount.getInstance().accountModelList.get(i).getUserId() == userId) {
                ret.add(SingletonAccount.getInstance().accountModelList.get(i));
            }
        }
        return ret;
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
        for (String s : SingletonAccount.getInstance().accountListInStrings) {
            String[] accountArray = s.split(",");
            if (accountArray.length >= 1) {
                if (Integer.parseInt(accountArray[0]) == accountId) {
                    return createAccountFromstringArray(accountArray);
                }
            }
        }
        throw new noAccountsFoundException();
    }

    public AccountModel createAccountFromstringArray(String[] accountArray) {
        AccountModel newAccount = new AccountModel(Integer.parseInt(accountArray[0]),
                Integer.parseInt(accountArray[1]), accountArray[2]
                , Integer.parseInt(accountArray[3]), accountArray[4],
                Boolean.parseBoolean(accountArray[5]));
        return newAccount;
    }

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

    public String getStringFormatAccount(AccountModel account) {
        return account.getAccountId() + "," + account.getUserId() + "," + account.getAccountType()
                + "," + account.getAmount() + "," + account.getCreationDate() + "," + account.getDelete();
    }

    public void initializeSingletonAccount() throws IOException {
        SingletonAccount sa = SingletonAccount.getInstance();
        sa.accountListInStrings = accountfile.readFromFile(accountFileName);
        for (String s : sa.accountListInStrings) {
            String[] arr = s.split(",");
            sa.accountModelList.add(createAccountFromstringArray(arr));
        }
    }
}