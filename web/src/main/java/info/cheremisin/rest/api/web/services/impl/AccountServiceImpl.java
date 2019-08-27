package info.cheremisin.rest.api.web.services.impl;

import info.cheremisin.rest.api.db.dao.AccountDao;
import info.cheremisin.rest.api.db.dao.UserDao;
import info.cheremisin.rest.api.db.exceptions.UserNotFoundException;
import info.cheremisin.rest.api.db.model.PaginationParams;
import info.cheremisin.rest.api.db.model.impl.Account;
import info.cheremisin.rest.api.web.services.AccountService;

import java.util.List;

public class AccountServiceImpl implements AccountService {

    private AccountDao accountDao;
    private UserDao userDao;

    public AccountServiceImpl(AccountDao accountDao, UserDao userDao) {
        this.accountDao = accountDao;
        this.userDao = userDao;
    }

    @Override
    public List<Account> getAll(Integer userId, PaginationParams pagination) {
        if(!userDao.checkUserExists(userId)) {
            throw new UserNotFoundException(userId);
        }
        return accountDao.getAll(userId, pagination);
    }

    @Override
    public Account getById(Integer id) {
        return accountDao.getById(id);
    }

    @Override
    public Account createAccount(Account account) {
        return accountDao.createAccount(account);
    }

    @Override
    public Account updateAccount(Account account) {
        if(!userDao.checkUserExists(account.getUserId())) {
            throw new UserNotFoundException(account.getUserId());
        }
        return accountDao.updateAccount(account);
    }

    @Override
    public void deleteAccount(Integer id) {
        accountDao.deleteAccount(id);
    }
}
