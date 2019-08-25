package info.cheremisin.rest.api.db.dao;

import info.cheremisin.rest.api.db.model.impl.User;

import java.util.List;

public interface UserDao {

    List<User> getAll();

    User getById(Integer id);

    User createUser(User user);

    User updateUser(User user);

    void deleteUser(Integer id);

}
