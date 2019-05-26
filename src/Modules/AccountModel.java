package Modules;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class AccountModel implements Observer {

    private int accountId;
    private int userId;
    private String accountType;
    private int amount;
    private String creationDate;
    private boolean delete;
    private Subject subject;

    public AccountModel(int accountId, int userId, String accountType, int amount, String creationDate, boolean delete) {
        this.accountId = accountId;
        this.userId = userId;
        this.accountType = accountType;
        this.amount = amount;
        this.creationDate = creationDate;
        this.delete = delete;
    }

    public AccountModel() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountModel that = (AccountModel) o;
        return accountId == that.accountId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId);
    }

    public int getAccountId() {
        return accountId;
    }

    public int getUserId() {
        return userId;
    }
    public String getAccountType() {
        return accountType;
    }
    public int getAmount() {
        return amount;
    }
    public String getCreationDate() {
        return creationDate;
    }
    public boolean getDelete() {
        return delete;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }
    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }
    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    @Override
    public void update(Subject sub) throws IOException {
        SingletonAccount.getInstance().accountModelList.remove(this);
        SingletonAccount.getInstance().accountListInStrings.remove(getStringFormatAccount());
        Files.write(Paths.get(System.getProperty("user.dir") + "/src/Data/Accounts"), SingletonAccount.getInstance().accountListInStrings, Charset.defaultCharset());
    }

    @Override
    public void setSubject(Subject sub) {
        this.subject = sub;
    }

    public String getStringFormatAccount() {
        return accountId + "," + userId + "," + accountType
                + "," + amount + "," + creationDate + "," + delete;
    }
}
