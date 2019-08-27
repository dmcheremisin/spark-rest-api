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

import java.math.BigDecimal;
import java.util.List;

public class TransactionServiceImpl implements TransactionService {

    private TransactionDao transactionDao;
    private AccountDao accountDao;
    private UserDao userDao;


    public TransactionServiceImpl(TransactionDao transactionDao, AccountDao accountDao, UserDao userDao) {
        this.transactionDao = transactionDao;
        this.accountDao = accountDao;
        this.userDao = userDao;
    }

    @Override
    public List<Transaction> getAllTransactions(Integer userId, PaginationParams params) {
        if(!userDao.checkUserExists(userId)) {
            throw new UserNotFoundException(userId);
        }
        return transactionDao.getAllTransactions(userId, params);
    }

    @Override
    public Transaction getTransactionById(Integer id) {
        return transactionDao.getTransactionById(id);
    }

    @Override
    public Transaction createTransaction(Transaction transaction) {
        if(transaction.getAccountDonor().equals(transaction.getAccountAcceptor())) {
            throw new IllegalArgumentException("Account in transaction must be different");
        }
        if(transaction.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new NumberFormatException("Amount must be greater than zero");
        }
        accountDao.getById(transaction.getAccountAcceptor());// checks account exists

        Account donor = accountDao.getById(transaction.getAccountDonor());
        if(donor.getBalance().compareTo(transaction.getAmount()) < 0) {
            throw new NotEnoughMoneyException(donor.getId());
        }

        return transactionDao.createTransaction(transaction);
    }
}
