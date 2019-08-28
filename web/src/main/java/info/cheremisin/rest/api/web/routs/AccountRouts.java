package info.cheremisin.rest.api.web.routs;

import info.cheremisin.rest.api.db.dao.impl.AccountDaoImpl;
import info.cheremisin.rest.api.db.dao.impl.UserDaoImpl;
import info.cheremisin.rest.api.db.model.PaginationParams;
import info.cheremisin.rest.api.db.model.impl.Account;
import info.cheremisin.rest.api.web.common.RequestParamsExtractor;
import info.cheremisin.rest.api.web.services.AccountService;
import info.cheremisin.rest.api.web.services.impl.AccountServiceImpl;
import info.cheremisin.rest.api.web.transformers.JsonTransformer;
import org.eclipse.jetty.http.HttpStatus;

import static info.cheremisin.rest.api.web.RestApiApp.API_ROOT_PATH;
import static info.cheremisin.rest.api.web.common.ClassExtractor.getClassFromRequest;
import static info.cheremisin.rest.api.web.common.RequestParamsExtractor.getIdFromRequest;
import static org.eclipse.jetty.http.HttpStatus.NO_CONTENT_204;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.path;
import static spark.Spark.post;
import static spark.Spark.put;

public class AccountRouts {

    private static final JsonTransformer JSON_TRANSFORMER = new JsonTransformer();

    private static final AccountService accountService = new AccountServiceImpl(AccountDaoImpl.getInstance(), UserDaoImpl.getInstance());

    public static void importAccountRouts() {
        path(API_ROOT_PATH, () -> {
            get("/users/:id/accounts", (req, res) -> {
                PaginationParams pagination = RequestParamsExtractor.getPaginationParamsFromRequest(req);
                return accountService.getAll(getIdFromRequest(req), pagination);
            }, JSON_TRANSFORMER);

            get("/accounts/:id", (req, res) -> accountService.getById(getIdFromRequest(req)), JSON_TRANSFORMER);

            post("/accounts", (req, res) -> {
                Account account = getClassFromRequest(req, Account.class);
                res.status(HttpStatus.CREATED_201);
                return accountService.createAccount(account);
            }, JSON_TRANSFORMER);

            put("/accounts/:id", (req, res) -> {
                Account account = getClassFromRequest(req, Account.class);
                account.setId(getIdFromRequest(req));
                return accountService.updateAccount(account);
            }, JSON_TRANSFORMER);

            delete("/accounts/:id", (req, res) -> {
                accountService.deleteAccount(getIdFromRequest(req));
                res.status(NO_CONTENT_204);
                return "";
            });
        });
    }


}
