package info.cheremisin.rest.api.db.dao;

import info.cheremisin.rest.api.db.model.PaginationParams;
import info.cheremisin.rest.api.db.model.impl.Transaction;

import java.util.List;

public interface TransactionDao {

    List<Transaction> getAllTransactions(Integer userId, PaginationParams params);

    Transaction getTransactionById(Integer id);

    Transaction createTransaction(Transaction transaction);
}
