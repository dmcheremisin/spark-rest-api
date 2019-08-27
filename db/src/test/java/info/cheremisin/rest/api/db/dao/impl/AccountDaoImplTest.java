package info.cheremisin.rest.api.db.dao.impl;

import info.cheremisin.rest.api.db.dao.AccountDao;
import info.cheremisin.rest.api.db.dao.BaseDaoTest;
import info.cheremisin.rest.api.db.exceptions.AccountNotFoundException;
import info.cheremisin.rest.api.db.model.PaginationParams;
import info.cheremisin.rest.api.db.model.impl.Account;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AccountDaoImplTest extends BaseDaoTest {

    AccountDao accountDao;
    Account account;

    public AccountDaoImplTest() {
        this.accountDao = AccountDaoImpl.getInstance();
    }

    @Before
    public void setUp() throws Exception {
        account = Account.builder().id(12).userId(2).balance(new BigDecimal("100.5")).build();
    }

    @Test
    public void getAll() {
        PaginationParams paginationParams = PaginationParams.builder().limit(2).offset(1).build();
        List<Account> all = accountDao.getAll(1, paginationParams);
        assertEquals(2, all.size());

        Account account = all.get(0);
        assertEquals(2, (int) account.getId());
        assertEquals(1, (int) account.getUserId());
        assertEquals(new BigDecimal("11.76"), account.getBalance());
    }

    @Test
    public void getById() {
        Account account = accountDao.getById(5);
        assertNotNull(account);
        assertEquals(5, (int) account.getId());
        assertEquals(3, (int) account.getUserId());
        assertEquals(new BigDecimal("76.98"), account.getBalance());
    }

    @Test
    public void createAccount() {
        Account createdAccount = accountDao.createAccount(account);
        assertNotNull(createdAccount);
        assertEquals(account.getUserId(), createdAccount.getUserId());
        assertEquals(account.getBalance(), createdAccount.getBalance());
    }

    @Test
    public void updateAccount() {
        Account updatedAccount = accountDao.updateAccount(this.account);
        assertNotNull(updatedAccount);
        assertEquals(account.getId(), updatedAccount.getId());
        assertEquals(account.getUserId(), updatedAccount.getUserId());
        assertEquals(account.getBalance(), updatedAccount.getBalance());
    }

    @Test(expected = AccountNotFoundException.class)
    public void deleteAccount() {
        Account createdAccount = accountDao.createAccount(this.account);
        accountDao.deleteAccount(createdAccount.getId());
        accountDao.getById(createdAccount.getId());
    }
}