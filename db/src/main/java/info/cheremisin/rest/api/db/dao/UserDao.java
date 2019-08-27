package info.cheremisin.rest.api.db.dao;

import info.cheremisin.rest.api.db.model.PaginationParams;
import info.cheremisin.rest.api.db.model.impl.User;

import java.util.List;

public interface UserDao {

    List<User> getAll(PaginationParams pagination);

    User getById(Integer id);

    User createUser(User user);

    User updateUser(User user);

    void deleteUser(Integer id);

    boolean checkUserExists(Integer id);

}
