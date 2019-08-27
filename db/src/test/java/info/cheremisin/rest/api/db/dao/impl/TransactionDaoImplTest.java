package info.cheremisin.rest.api.db.dao.impl;

import info.cheremisin.rest.api.db.dao.AccountDao;
import info.cheremisin.rest.api.db.dao.BaseDaoTest;
import info.cheremisin.rest.api.db.dao.TransactionDao;
import info.cheremisin.rest.api.db.exceptions.TransactionNotFoundException;
import info.cheremisin.rest.api.db.model.PaginationParams;
import info.cheremisin.rest.api.db.model.impl.Account;
import info.cheremisin.rest.api.db.model.impl.Transaction;
import org.junit.Test;
import org.sql2o.Sql2oException;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TransactionDaoImplTest extends BaseDaoTest {

    TransactionDao transactionDao;
    AccountDao accountDao;

    public TransactionDaoImplTest() {
        transactionDao = TransactionDaoImpl.getInstance();
        accountDao = AccountDaoImpl.getInstance();
    }

    @Test
    public void getAllTransactions() {
        PaginationParams paginationParams = PaginationParams.builder().limit(20).offset(0).build();
        List<Transaction> all = transactionDao.getAllTransactions(11, paginationParams);
        assertEquals(3, all.size());

        Transaction transaction = all.get(0);
        assertEquals(1, (int) transaction.getAccountDonor());
        assertEquals(11, (int) transaction.getAccountAcceptor());
        assertEquals(new BigDecimal("11.57"), transaction.getAmount());
    }

    @Test
    public void getTransactionById() {
        Transaction transaction = transactionDao.getTransactionById(3);

        assertNotNull(transaction);
        assertEquals(11, (int) transaction.getAccountDonor());
        assertEquals(5, (int) transaction.getAccountAcceptor());
        assertEquals(new BigDecimal("22.23"), transaction.getAmount());
    }

    @Test(expected = TransactionNotFoundException.class)
    public void getTransactionByIdFail() {
        Transaction transaction = transactionDao.getTransactionById(123);
    }

    @Test
    public void createTransaction() {
        BigDecimal amount = new BigDecimal("40.05");
        Transaction transaction = Transaction.builder().accountDonor(6).accountAcceptor(5).amount(amount).build();
        Transaction createdTransaction = transactionDao.createTransaction(transaction);

        assertNotNull(createdTransaction);
        assertNotNull(createdTransaction.getId());
        assertEquals(transaction.getAccountAcceptor(), createdTransaction.getAccountAcceptor());
        assertEquals(transaction.getAccountDonor(), createdTransaction.getAccountDonor());

        Account acceptor = accountDao.getById(createdTransaction.getAccountAcceptor());
        assertEquals(createdTransaction.getAccountAcceptor(), acceptor.getId());
        assertEquals(new BigDecimal("117.03"), acceptor.getBalance());

        Account donor = accountDao.getById(createdTransaction.getAccountDonor());
        assertEquals(createdTransaction.getAccountDonor(), donor.getId());
        assertEquals(new BigDecimal("110.04"), donor.getBalance());
    }

    @Test(expected = Sql2oException.class)
    public void createTransactionFailDonor() {
        BigDecimal amount = new BigDecimal("10.12");
        Transaction transaction = Transaction.builder().accountDonor(121).accountAcceptor(1).amount(new BigDecimal("10.0")).build();
        transactionDao.createTransaction(transaction);
    }

    @Test(expected = Sql2oException.class)
    public void createTransactionFailAcceptor() {
        BigDecimal amount = new BigDecimal("10.12");
        Transaction transaction = Transaction.builder().accountDonor(1).accountAcceptor(121).amount(new BigDecimal("10.0")).build();
        transactionDao.createTransaction(transaction);
    }
}