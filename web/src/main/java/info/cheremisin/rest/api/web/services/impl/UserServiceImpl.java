package info.cheremisin.rest.api.web.services.impl;

import info.cheremisin.rest.api.db.dao.UserDao;
import info.cheremisin.rest.api.db.model.PaginationParams;
import info.cheremisin.rest.api.db.model.impl.User;
import info.cheremisin.rest.api.web.services.UserService;

import java.util.List;

public class UserServiceImpl implements UserService {

    private UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public List<User> getAll(PaginationParams pagination) {
        return userDao.getAll(pagination);
    }

    @Override
    public User getById(Integer id) {
        return userDao.getById(id);
    }

    @Override
    public User createUser(User user) {
        return userDao.createUser(user);
    }

    @Override
    public User updateUser(User user) {
        return userDao.updateUser(user);
    }

    @Override
    public void deleteUser(Integer id) {
        userDao.deleteUser(id);
    }
}
