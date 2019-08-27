package info.cheremisin.rest.api.db.dao.impl;

import info.cheremisin.rest.api.db.dao.AccountDao;
import info.cheremisin.rest.api.db.exceptions.AccountNotFoundException;
import info.cheremisin.rest.api.db.model.PaginationParams;
import info.cheremisin.rest.api.db.model.impl.Account;
import org.sql2o.Connection;

import java.util.List;

import static info.cheremisin.rest.api.db.connection.ConnectionPool.getConnection;

public class AccountDaoImpl implements AccountDao {

    private static AccountDao accountDao;

    private AccountDaoImpl() {
    }

    public static AccountDao getInstance() {
        if (accountDao == null) {
            synchronized (UserDaoImpl.class) {
                if(accountDao == null) {
                    accountDao = new AccountDaoImpl();
                }
            }
        }
        return accountDao;
    }

    @Override
    public List<Account> getAll(Integer userId, PaginationParams pagination) {
        try(Connection connection = getConnection()) {
            String sql = "SELECT * FROM accounts WHERE user_id=:userId LIMIT :limit OFFSET :offset ";
            List<Account> accounts = connection.createQuery(sql)
                    .addParameter("userId", userId)
                    .addParameter("limit", pagination.getLimit())
                    .addParameter("offset", pagination.getOffset())
                    .executeAndFetch(Account.class);
            return accounts;
        }
    }

    @Override
    public Account getById(Integer id) {
        try(Connection connection = getConnection()) {
            List<Account> accounts = connection.createQuery("SELECT * FROM accounts WHERE id=:id")
                    .addParameter("id", id)
                    .executeAndFetch(Account.class);
            if(accounts.size() == 0) {
                throw new AccountNotFoundException(id);
            }
            return accounts.get(0);
        }
    }

    @Override
    public Account createAccount(Account account) {
        account.setId(null);
        try(Connection connection = getConnection()) {
            Integer key = (Integer) connection.createQuery("INSERT INTO accounts VALUES (:id, :userId, :balance);", true)
                    .bind(account)
                    .executeUpdate()
                    .getKey();
            return getById(key);
        }
    }

    @Override
    public Account updateAccount(Account account) {
        try(Connection connection = getConnection()) {
            connection.createQuery("UPDATE accounts SET user_id=:userId, balance=:balance WHERE id=:id")
                    .bind(account)
                    .executeUpdate();
            return getById(account.getId());
        }
    }

    @Override
    public void deleteAccount(Integer id) {
        try(Connection connection = getConnection()) {
            connection.createQuery("DELETE FROM accounts WHERE id=:id")
                    .addParameter("id", id)
                    .executeUpdate();
        }
    }
}
