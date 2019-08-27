package info.cheremisin.rest.api.web.routs;

import info.cheremisin.rest.api.db.dao.impl.AccountDaoImpl;
import info.cheremisin.rest.api.db.dao.impl.TransactionDaoImpl;
import info.cheremisin.rest.api.db.dao.impl.UserDaoImpl;
import info.cheremisin.rest.api.db.model.PaginationParams;
import info.cheremisin.rest.api.db.model.impl.Transaction;
import info.cheremisin.rest.api.web.common.RequestParamsExtractor;
import info.cheremisin.rest.api.web.services.TransactionService;
import info.cheremisin.rest.api.web.services.impl.TransactionServiceImpl;
import info.cheremisin.rest.api.web.transformers.JsonTransformer;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.http.HttpStatus;

import static info.cheremisin.rest.api.web.RestApiApp.API_ROOT_PATH;
import static info.cheremisin.rest.api.web.common.ClassExtractor.getClassFromRequest;
import static info.cheremisin.rest.api.web.common.RequestParamsExtractor.getIdFromRequest;
import static spark.Spark.get;
import static spark.Spark.path;
import static spark.Spark.post;

@Slf4j
public class TransactionRouts {

    private static final JsonTransformer JSON_TRANSFORMER = new JsonTransformer();

    private static final TransactionService transactionService =
            new TransactionServiceImpl(TransactionDaoImpl.getInstance(), AccountDaoImpl.getInstance(), UserDaoImpl.getInstance());

    public static void importTransactionRouts() {
        path(API_ROOT_PATH, () -> {
            get("/users/:id/transactions", (req, res) -> {
                PaginationParams pagination = RequestParamsExtractor.getPaginationParamsFromRequest(req);
                return transactionService.getAllTransactions(getIdFromRequest(req), pagination);
            }, JSON_TRANSFORMER);

            get("/transactions/:id", (req, res) -> transactionService.getTransactionById(getIdFromRequest(req)), JSON_TRANSFORMER);

            post("/transactions", (req, res) -> {
                Transaction transaction = getClassFromRequest(req, Transaction.class);
                res.status(HttpStatus.CREATED_201);
                return transactionService.createTransaction(transaction);
            }, JSON_TRANSFORMER);
        });
    }


}
