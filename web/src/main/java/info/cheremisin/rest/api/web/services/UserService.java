package info.cheremisin.rest.api.web.services;

import info.cheremisin.rest.api.db.model.PaginationParams;
import info.cheremisin.rest.api.db.model.impl.User;

import java.util.List;

public interface UserService {

    List<User> getAll(PaginationParams pagination);

    User getById(Integer id);

    User createUser(User user);

    User updateUser(User user);

    void deleteUser(Integer id);

}
