package info.cheremisin.rest.api.web.services.impl;

import info.cheremisin.rest.api.db.dao.AccountDao;
import info.cheremisin.rest.api.db.dao.UserDao;
import info.cheremisin.rest.api.db.exceptions.UserNotFoundException;
import info.cheremisin.rest.api.db.model.PaginationParams;
import info.cheremisin.rest.api.db.model.impl.Account;
import info.cheremisin.rest.api.web.services.AccountService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AccountServiceImplTest {

    @Mock
    UserDao userDao;

    @Mock
    AccountDao accountDao;

    Account account;

    AccountService accountService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        accountService = new AccountServiceImpl(accountDao, userDao);
        account = Account.builder().id(123).userId(5).balance(new BigDecimal("45.21")).build();
    }

    @Test
    public void getAll() {
        when(accountDao.getAll(anyInt(), any())).thenReturn(Arrays.asList(new Account(), new Account()));
        when(userDao.checkUserExists(anyInt())).thenReturn(true);
        List<Account> all = accountService.getAll(1, PaginationParams.builder().offset(5).limit(2).build());

        verify(accountDao).getAll(anyInt(), any());
        assertNotNull(all);
        assertEquals(2, all.size());
    }

    @Test(expected = UserNotFoundException.class)
    public void getAllUserNotFound() {
        when(accountDao.getAll(anyInt(), any())).thenReturn(Arrays.asList(new Account(), new Account()));
        when(userDao.checkUserExists(anyInt())).thenReturn(false);
        accountService.getAll(1, PaginationParams.builder().offset(5).limit(2).build());

        verify(accountDao).getAll(anyInt(), any());
        verify(userDao).checkUserExists(anyInt());
    }

    @Test
    public void getById() {
        when(accountDao.getById(anyInt())).thenReturn(account);
        Account accountById = accountService.getById(1);

        verify(accountDao).getById(anyInt());
        assertNotNull(accountById);
        assertEquals(account.getId(), accountById.getId());
        assertEquals(account.getUserId(), accountById.getUserId());
        assertEquals(account.getBalance(), accountById.getBalance());
    }

    @Test
    public void createAccount() {
        when(accountDao.createAccount(any())).thenReturn(account);
        Account createdAccount = accountService.createAccount(new Account());

        verify(accountDao).createAccount(any());
        assertNotNull(createdAccount);
        assertEquals(account.getId(), createdAccount.getId());
        assertEquals(account.getUserId(), createdAccount.getUserId());
        assertEquals(account.getBalance(), createdAccount.getBalance());
    }

    @Test
    public void updateAccount() {
        when(accountDao.updateAccount(any())).thenReturn(account);
        when(userDao.checkUserExists(anyInt())).thenReturn(true);
        Account updatedAccount = accountService.updateAccount(new Account());

        verify(accountDao).updateAccount(any());
        assertNotNull(updatedAccount);;
        assertEquals(account.getId(), updatedAccount.getId());
        assertEquals(account.getUserId(), updatedAccount.getUserId());
        assertEquals(account.getBalance(), updatedAccount.getBalance());
    }

    @Test(expected = UserNotFoundException.class)
    public void updateAccountUserNotFound() {
        when(accountDao.updateAccount(any())).thenReturn(account);
        when(userDao.checkUserExists(anyInt())).thenReturn(false);
        accountService.updateAccount(new Account());
    }

    @Test
    public void deleteAccount() {
        accountService.deleteAccount(1);
        verify(accountDao).deleteAccount(anyInt());
    }
}