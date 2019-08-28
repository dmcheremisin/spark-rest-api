package info.cheremisin.rest.api.web;

import info.cheremisin.rest.api.db.exceptions.AccountNotFoundException;
import info.cheremisin.rest.api.db.exceptions.NotEnoughMoneyException;
import info.cheremisin.rest.api.db.exceptions.TransactionNotFoundException;
import info.cheremisin.rest.api.db.exceptions.UserNotFoundException;
import info.cheremisin.rest.api.db.initialization.DbInitializer;
import info.cheremisin.rest.api.web.common.ExceptionWrapper;
import org.apache.log4j.Logger;
import org.eclipse.jetty.http.HttpStatus;
import spark.Response;

import static info.cheremisin.rest.api.web.routs.AccountRouts.importAccountRouts;
import static info.cheremisin.rest.api.web.routs.TransactionRouts.importTransactionRouts;
import static info.cheremisin.rest.api.web.routs.UserRouts.importUserRouts;
import static spark.Spark.after;
import static spark.Spark.before;
import static spark.Spark.exception;
import static spark.Spark.notFound;
import static spark.Spark.port;

public class RestApiApp {
    private final static Logger logger = Logger.getLogger(RestApiApp.class);

    public static final String API_ROOT_PATH = "/api/v1";

    public static void main(String[] args) {
        initializeData();

        port(8080);
        importRouts();
        before((req, res) -> {
            logger.info("IP: " + req.ip());
            logger.info("Path: " + req.pathInfo());
            logger.info("Headers: " + req.headers());
            logger.info("Body: " + req.body());
        });

        after((request, response) -> response.type("application/json; charset=UTF-8"));

        enableExceptionHandling();
    }

    private static void initializeData() {
        DbInitializer stubInitializer = new DbInitializer();
        stubInitializer.initializeSchemaAndData();
    }

    private static void importRouts() {
        importUserRouts();
        importAccountRouts();
        importTransactionRouts();
    }

    private static void enableExceptionHandling() {
        ExceptionWrapper exceptionWrapper = new ExceptionWrapper();

        notFound((req, res) -> res.body() != null ? res.body() : "Something went wrong");

        exception(UserNotFoundException.class, (e, req, res) -> {
            logAndAnswerError(exceptionWrapper, res, e.getMessage());
        });
        exception(AccountNotFoundException.class, (e, req, res) -> {
            logAndAnswerError(exceptionWrapper, res, e.getMessage());
        });
        exception(NotEnoughMoneyException.class, (e, req, res) -> {
            logAndAnswerError(exceptionWrapper, res, e.getMessage());
        });
        exception(TransactionNotFoundException.class, (e, req, res) -> {
            logAndAnswerError(exceptionWrapper, res, e.getMessage());
        });
        exception(NumberFormatException.class, (e, req, res) -> {
            logAndAnswerError(exceptionWrapper, res, e.getMessage());
        });
        exception(IllegalArgumentException.class, (e, req, res) -> {
            logAndAnswerError(exceptionWrapper, res, e.getMessage());
        });
        exception(Exception.class, (e, req, res) -> {
            logger.error(e.getMessage());
        });
    }

    private static void logAndAnswerError(ExceptionWrapper exceptionWrapper, Response res, String message) {
        logger.error(message);
        res.body(exceptionWrapper.wrapExceptionToJson(message));
        res.status(HttpStatus.NOT_FOUND_404);
        res.type("application/json; charset=UTF-8");
    }


}

