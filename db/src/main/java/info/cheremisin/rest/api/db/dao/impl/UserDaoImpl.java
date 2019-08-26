package info.cheremisin.rest.api.db.dao.impl;

import info.cheremisin.rest.api.db.dao.UserDao;
import info.cheremisin.rest.api.db.exceptions.UserNotFoundException;
import info.cheremisin.rest.api.db.model.PaginationParams;
import info.cheremisin.rest.api.db.model.impl.Account;
import info.cheremisin.rest.api.db.model.impl.User;
import org.sql2o.Connection;

import java.util.List;

import static info.cheremisin.rest.api.db.connection.ConnectionPool.getConnection;

public class UserDaoImpl implements UserDao {

    private static UserDao userDao;

    public static UserDao getInstance() {
        if (userDao == null) {
            synchronized (UserDaoImpl.class) {
                if(userDao == null) {
                    userDao = new UserDaoImpl();
                }
            }
        }
        return userDao;
    }

    @Override
    public List<User> getAll(PaginationParams pagination) {
        try(Connection connection = getConnection()) {
            String sql = "SELECT * FROM users LIMIT :limit OFFSET :offset ";
            List<User> users = connection.createQuery(sql)
                    .bind(pagination)
                    .executeAndFetch(User.class);
            users.forEach(u -> u.setAccounts(getUserAccounts(connection, u)));
            return users;
        }
    }

    private List<Account> getUserAccounts(Connection connection, User u) {
        return connection.createQuery("SELECT * FROM accounts WHERE user_id=:id")
                .addParameter("id", u.getId())
                .executeAndFetch(Account.class);
    }

    @Override
    public User getById(Integer id) {
        try(Connection connection = getConnection()) {
            List<User> users = connection.createQuery("SELECT * FROM users WHERE id=:user_id")
                    .addParameter("user_id", id)
                    .executeAndFetch(User.class);
            users.forEach(u -> u.setAccounts(getUserAccounts(connection, u)));
            if(users.size() == 0) {
                throw new UserNotFoundException("User not found with id = " + id);
            }
            return users.get(0);
        }
    }

    @Override
    public User createUser(User user) {
        user.setId(null);
        try(Connection connection = getConnection()) {
            Integer key = (Integer) connection.createQuery("INSERT INTO users VALUES (:id, :firstName, :lastName);", true)
                    .bind(user)
                    .executeUpdate()
                    .getKey();
            return getById(key);
        }
    }

    @Override
    public User updateUser(User user) {
        try(Connection connection = getConnection()) {
            connection.createQuery("UPDATE users SET first_name=:firstName, last_name=:lastName WHERE id=:id")
                    .bind(user)
                    .executeUpdate();
            return getById(user.getId());
        }
    }

    @Override
    public void deleteUser(Integer id) {
        try(Connection connection = getConnection()) {
            connection.createQuery("DELETE FROM users WHERE id=:id")
                    .addParameter("id", id)
                    .executeUpdate();
        }
    }
}
