package Modules;

import java.util.Objects;

public class AccountModel {

    public int accountId;
    public int userId;
    public String accountType;
    public int amount;
    public String creationDate;
    public boolean delete;

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
}
