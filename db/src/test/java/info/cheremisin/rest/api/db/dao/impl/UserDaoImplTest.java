package info.cheremisin.rest.api.db.dao.impl;

import info.cheremisin.rest.api.db.dao.BaseDaoTest;
import info.cheremisin.rest.api.db.dao.UserDao;
import info.cheremisin.rest.api.db.model.PaginationParams;
import info.cheremisin.rest.api.db.model.impl.User;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class UserDaoImplTest extends BaseDaoTest {

    UserDao userDao;

    @Before
    public void setUp() throws Exception {
        userDao = UserDaoImpl.getInstance();
    }

    @Test
    public void testGetById() {
        User user = userDao.getById(1);
        assertNotNull(user);
        assertEquals(1, (int) user.getId());
        assertEquals("Tyrion", user.getFirstName());
        assertEquals("Lannister", user.getLastName());
    }

    @Test
    public void testGetAll() {
        List<User> all = userDao.getAll(PaginationParams.builder().limit(10).offset(1).build());
        assertEquals(10, all.size());
        User user = all.get(0);
        assertNotNull(user);
        assertEquals(2, (int) user.getId());
        assertEquals("Cersei", user.getFirstName());
        assertEquals("Lannister", user.getLastName());
    }

    @Test
    public void createUser() {
        User user = User.builder().id(123).firstName("Rob").lastName("Stark").build();
        User daoUser = userDao.createUser(user);
        assertNotNull(daoUser);
        assertEquals(13, (int) daoUser.getId());
        assertEquals("Rob", daoUser.getFirstName());
        assertEquals("Stark", daoUser.getLastName());
    }

    @Test
    public void updateUser() {
        User user = User.builder().id(10).firstName("Rob").lastName("Stark").build();
        User updatedUser = userDao.updateUser(user);
        assertNotNull(updatedUser);
        assertEquals(10, (int) updatedUser.getId());
        assertEquals("Rob", updatedUser.getFirstName());
        assertEquals("Stark", updatedUser.getLastName());
    }

    @Test
    public void deleteUser() {
        User user = User.builder().firstName("Rob").lastName("Stark").build();
        User createdUser = userDao.createUser(user);
        userDao.deleteUser(createdUser.getId());
    }

}