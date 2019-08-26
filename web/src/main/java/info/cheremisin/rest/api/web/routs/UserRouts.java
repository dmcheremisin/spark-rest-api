package info.cheremisin.rest.api.web.routs;

import info.cheremisin.rest.api.db.dao.UserDao;
import info.cheremisin.rest.api.db.dao.impl.UserDaoImpl;
import info.cheremisin.rest.api.db.model.impl.User;
import info.cheremisin.rest.api.web.common.RequestParamsExtractor;
import info.cheremisin.rest.api.db.model.PaginationParams;
import info.cheremisin.rest.api.web.transformers.JsonTransformer;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.http.HttpStatus;

import static info.cheremisin.rest.api.web.common.ClassExtractor.getClassFromRequest;
import static info.cheremisin.rest.api.web.common.RequestParamsExtractor.getIdFromRequest;
import static org.eclipse.jetty.http.HttpStatus.NO_CONTENT_204;
import static spark.Spark.*;

@Slf4j
public class UserRouts {

    private static final JsonTransformer JSON_TRANSFORMER = new JsonTransformer();

    private static final UserDao userDao = UserDaoImpl.getInstance();

    public static void importUserRouts() {
        path("/api/v1", () -> {
            get("/users", (req, res) -> {
                PaginationParams pagination = RequestParamsExtractor.getPaginationParamsFromRequest(req);
                return userDao.getAll(pagination);
            }, JSON_TRANSFORMER);

            get("/users/:id", (req, res) -> userDao.getById(getIdFromRequest(req)), JSON_TRANSFORMER);

            post("/users", (req, res) -> {
                User user = getClassFromRequest(req, User.class);
                res.status(HttpStatus.CREATED_201);
                return userDao.createUser(user);
            }, JSON_TRANSFORMER);

            put("/users/:id", (req, res) -> {
                User user = getClassFromRequest(req, User.class);
                user.setId(getIdFromRequest(req));
                return userDao.updateUser(user);
            }, JSON_TRANSFORMER);

            delete("/users/:id", (req, res) -> {
                userDao.deleteUser(getIdFromRequest(req));
                res.status(NO_CONTENT_204);
                return "";
            });
        });
    }


}
