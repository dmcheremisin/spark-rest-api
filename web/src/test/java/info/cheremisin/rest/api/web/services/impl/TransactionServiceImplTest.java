package info.cheremisin.rest.api.web.services.impl;

import info.cheremisin.rest.api.db.dao.AccountDao;
import info.cheremisin.rest.api.db.dao.TransactionDao;
import info.cheremisin.rest.api.db.dao.UserDao;
import info.cheremisin.rest.api.db.exceptions.NotEnoughMoneyException;
import info.cheremisin.rest.api.db.exceptions.UserNotFoundException;
import info.cheremisin.rest.api.db.model.PaginationParams;
import info.cheremisin.rest.api.db.model.impl.Account;
import info.cheremisin.rest.api.db.model.impl.Transaction;
import info.cheremisin.rest.api.web.services.TransactionService;
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

public class TransactionServiceImplTest {

    @Mock
    UserDao userDao;

    @Mock
    AccountDao accountDao;

    @Mock
    TransactionDao transactionDao;

    TransactionService transactionService;

    Transaction transaction;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        transaction = Transaction.builder().id(1).accountAcceptor(2).accountDonor(3).amount(new BigDecimal("10.12")).build();
        transactionService = new TransactionServiceImpl(transactionDao, accountDao, userDao);
    }

    @Test
    public void getAllTransactions() {
        List<Transaction> transactions = Arrays.asList(new Transaction(), new Transaction());
        PaginationParams params = PaginationParams.builder().offset(5).limit(2).build();
        when(userDao.checkUserExists(anyInt())).thenReturn(true);
        when(transactionDao.getAllTransactions(anyInt(), any())).thenReturn(transactions);

        List<Transaction> list = transactionService.getAllTransactions(1, params);

        verify(userDao).checkUserExists(anyInt());
        verify(transactionDao).getAllTransactions(anyInt(), any());
        assertEquals(2, list.size());
    }

    @Test(expected = UserNotFoundException.class)
    public void getAllTransactionsFail() {
        List<Transaction> transactions = Arrays.asList(new Transaction(), new Transaction());
        PaginationParams params = PaginationParams.builder().offset(5).limit(2).build();
        when(userDao.checkUserExists(anyInt())).thenReturn(false);
        when(transactionDao.getAllTransactions(anyInt(), any())).thenReturn(transactions);

        transactionService.getAllTransactions(1, params);
    }

    @Test
    public void getTransactionById() {
        when(transactionDao.getTransactionById(anyInt())).thenReturn(transaction);
        Transaction createdTransaction = transactionService.getTransactionById(1);

        verify(transactionDao).getTransactionById(anyInt());
        assertNotNull(createdTransaction);
        assertEquals(transaction.getId(), createdTransaction.getId());
        assertEquals(transaction.getAccountDonor(), createdTransaction.getAccountDonor());
        assertEquals(transaction.getAccountAcceptor(), createdTransaction.getAccountAcceptor());
        assertEquals(transaction.getAmount(), createdTransaction.getAmount());
    }

    @Test
    public void createTransaction() {
        Account account = Account.builder().id(1).balance(new BigDecimal("100")).userId(2).build();
        when(accountDao.getById(anyInt())).thenReturn(account);
        when(transactionDao.createTransaction(any())).thenReturn(transaction);

        Transaction createdTransaction = transactionService.createTransaction(transaction);
        assertNotNull(createdTransaction);
        assertEquals(transaction.getId(), createdTransaction.getId());
        assertEquals(transaction.getAccountDonor(), createdTransaction.getAccountDonor());
        assertEquals(transaction.getAccountAcceptor(), createdTransaction.getAccountAcceptor());
        assertEquals(transaction.getAmount(), createdTransaction.getAmount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void createTransactionNoUserFound() {
        Transaction equalUser = Transaction.builder().accountDonor(1).accountAcceptor(1).build();
        when(transactionDao.createTransaction(any())).thenReturn(equalUser);
        transactionService.createTransaction(equalUser);
    }

    @Test(expected = NumberFormatException.class)
    public void createTransactionLessZero() {
        transaction.setAmount(new BigDecimal("-100"));
        when(accountDao.getById(anyInt())).thenReturn(new Account());
        when(transactionDao.createTransaction(any())).thenReturn(transaction);
        transactionService.createTransaction(transaction);
    }

    @Test(expected = NotEnoughMoneyException.class)
    public void createTransactionNoMoney() {
        Account account = Account.builder().id(1).userId(2).balance(new BigDecimal("1")).build();
        when(accountDao.getById(anyInt())).thenReturn(account);
        when(transactionDao.createTransaction(any())).thenReturn(transaction);
        transactionService.createTransaction(transaction);
    }

}