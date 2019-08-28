package info.cheremisin.rest.api.db.dao.impl;

import info.cheremisin.rest.api.db.actions.BalanceAction;
import info.cheremisin.rest.api.db.connection.ConnectionPool;
import info.cheremisin.rest.api.db.dao.TransactionDao;
import info.cheremisin.rest.api.db.exceptions.TransactionNotFoundException;
import info.cheremisin.rest.api.db.model.PaginationParams;
import info.cheremisin.rest.api.db.model.impl.Transaction;
import org.apache.log4j.Logger;
import org.sql2o.Connection;

import java.math.BigDecimal;
import java.util.List;

import static info.cheremisin.rest.api.db.connection.ConnectionPool.getConnection;
import static info.cheremisin.rest.api.db.connection.ConnectionPool.getSql2o;

public class TransactionDaoImpl implements TransactionDao {

    private final static Logger logger = Logger.getLogger(ConnectionPool.class);
    private static final String START_TRANSACTION = "Start transaction creation: donor = %s , acceptor = %s , amount = %s ";
    private static final String END_TRANSACTION = "Created transaction: id = %d , donor = %s , acceptor = %s , amount = %s ";

    private static TransactionDao transactionDao;

    private TransactionDaoImpl() {
    }

    public static TransactionDao getInstance() {
        if (transactionDao == null) {
            synchronized (TransactionDaoImpl.class) {
                if(transactionDao == null) {
                    transactionDao = new TransactionDaoImpl();
                }
            }
        }
        return transactionDao;
    }

    @Override
    public List<Transaction> getAllTransactions(Integer userId, PaginationParams params) {
        try(Connection connection = getConnection()) {
            String sql = "SELECT * FROM transactions WHERE account_donor=:userId OR account_acceptor=:userId LIMIT :limit OFFSET :offset ";
            List<Transaction> transactions = connection.createQuery(sql)
                    .addParameter("userId", userId)
                    .addParameter("limit", params.getLimit())
                    .addParameter("offset", params.getOffset())
                    .executeAndFetch(Transaction.class);
            return transactions;
        }
    }

    @Override
    public Transaction getTransactionById(Integer id) {
        try(Connection connection = getConnection()) {
            List<Transaction> transactions = connection.createQuery("SELECT * FROM transactions WHERE id=:id")
                    .addParameter("id", id)
                    .executeAndFetch(Transaction.class);
            if(transactions.size() == 0) {
                throw new TransactionNotFoundException(id);
            }
            return transactions.get(0);
        }
    }

    @Override
    public synchronized Transaction createTransaction(Transaction transaction) {
        logger.info(String.format(START_TRANSACTION, transaction.getAccountDonor(), transaction.getAccountAcceptor(), transaction.getAmount()));
        transaction.setId(null);
        Integer key;
        try(Connection connection = getSql2o().beginTransaction()) {
            key = (Integer) connection.createQuery("INSERT INTO transactions VALUES (:id, :accountDonor, :accountAcceptor, :amount);", true)
                    .bind(transaction)
                    .executeUpdate()
                    .getKey();
            updateAccountBalance(connection, transaction.getAccountDonor(), BalanceAction.SUBSTRACT, transaction.getAmount());
            updateAccountBalance(connection, transaction.getAccountAcceptor(), BalanceAction.ADD, transaction.getAmount());
            connection.commit();
        }
        logger.info(String.format(END_TRANSACTION, key, transaction.getAccountDonor(), transaction.getAccountAcceptor(), transaction.getAmount()));
        return getTransactionById(key);
    }

    private void updateAccountBalance(Connection connection, Integer accountId, BalanceAction balanceAction, BigDecimal amount) {
        connection.createQuery("UPDATE accounts SET balance=balance " + balanceAction.getAction() + " :amount WHERE id=:id")
                .addParameter("id", accountId)
                .addParameter("amount", amount)
                .executeUpdate();
    }
}
