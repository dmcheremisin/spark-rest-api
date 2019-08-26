package info.cheremisin.rest.api.db.dao.impl;

import info.cheremisin.rest.api.db.dao.BaseDaoTest;
import info.cheremisin.rest.api.db.dao.UserDao;
import info.cheremisin.rest.api.db.exceptions.UserNotFoundException;
import info.cheremisin.rest.api.db.model.PaginationParams;
import info.cheremisin.rest.api.db.model.impl.User;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class UserDaoImplTest extends BaseDaoTest {

    private static final String ROB = "Rob";
    private static final String STARK = "Stark";

    UserDao userDao;

    public UserDaoImplTest() {
        userDao = UserDaoImpl.getInstance();
    }

    @Test
    public void getAll() {
        List<User> all = userDao.getAll(PaginationParams.builder().limit(10).offset(1).build());
        assertEquals(10, all.size());
        User user = all.get(0);
        assertNotNull(user);
        assertEquals(2, (int) user.getId());
        assertEquals("Cersei", user.getFirstName());
        assertEquals("Lannister", user.getLastName());
    }

    @Test
    public void getById() {
        User user = userDao.getById(1);
        assertNotNull(user);
        assertEquals(1, (int) user.getId());
        assertEquals("Tyrion", user.getFirstName());
        assertEquals("Lannister", user.getLastName());
    }

    @Test
    public void createUser() {
        User user = User.builder().id(123).firstName(ROB).lastName(STARK).build();
        User daoUser = userDao.createUser(user);
        assertNotNull(daoUser);
        assertEquals(ROB, daoUser.getFirstName());
        assertEquals(STARK, daoUser.getLastName());
    }

    @Test
    public void updateUser() {
        User user = User.builder().id(10).firstName(ROB).lastName(STARK).build();
        User updatedUser = userDao.updateUser(user);
        assertNotNull(updatedUser);
        assertEquals(10, (int) updatedUser.getId());
        assertEquals(ROB, updatedUser.getFirstName());
        assertEquals(STARK, updatedUser.getLastName());
    }

    @Test(expected = UserNotFoundException.class)
    public void deleteUser() {
        User user = User.builder().firstName(ROB).lastName(STARK).build();
        User createdUser = userDao.createUser(user);
        userDao.deleteUser(createdUser.getId());
        User deletedUser = userDao.getById(createdUser.getId());
    }

}