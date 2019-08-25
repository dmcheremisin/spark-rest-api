package info.cheremisin.rest.api.web.routs;

import info.cheremisin.rest.api.db.dao.UserDao;
import info.cheremisin.rest.api.db.dao.impl.UserDaoImpl;
import info.cheremisin.rest.api.db.model.impl.User;
import info.cheremisin.rest.api.web.transformers.JsonTransformer;
import lombok.extern.slf4j.Slf4j;

import static info.cheremisin.rest.api.web.common.ClassExtractor.getClassFromRequest;
import static spark.Spark.*;

@Slf4j
public class UserRouts {

    private static final JsonTransformer JSON_TRANSFORMER = new JsonTransformer();

    private static final UserDao userDao = UserDaoImpl.getInstance();

    public static void importUserRouts() {
        path("/api/v1", () -> {
            get("/users", (req, res) -> userDao.getAll(), JSON_TRANSFORMER);
            get("/users/:id", (req, res) -> {
                String id = req.params("id");
                return userDao.getById(Integer.parseInt(id));
            }, JSON_TRANSFORMER);

            post("/users", (req, res) -> {
                User user = getClassFromRequest(req, User.class);
                return userDao.createUser(user);
            }, JSON_TRANSFORMER);

            put("/users", (req, res) -> {
                User user = getClassFromRequest(req, User.class);
                return userDao.updateUser(user);
            }, JSON_TRANSFORMER);

            delete("/users/:id", (req, res) -> {
                String id = req.params("id");
                userDao.deleteUser(Integer.parseInt(id));
                return null;
            });
        });
    }
}
