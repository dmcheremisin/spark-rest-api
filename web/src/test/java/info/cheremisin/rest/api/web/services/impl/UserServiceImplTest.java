package info.cheremisin.rest.api.web.services.impl;

import info.cheremisin.rest.api.db.dao.UserDao;
import info.cheremisin.rest.api.db.model.PaginationParams;
import info.cheremisin.rest.api.db.model.impl.User;
import info.cheremisin.rest.api.web.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceImplTest {
    public static final String SER = "Ser";
    public static final String DAVOS = "Davos";
    public static final int ID = 123;

    @Mock
    UserDao userDao;

    UserService userService;

    User user;

    @Before
    public void setUp() throws Exception {
        user = User.builder().id(ID).firstName(SER).lastName(DAVOS).build();
        MockitoAnnotations.initMocks(this);
        userService = new UserServiceImpl(userDao);
    }

    @Test
    public void getAll() {
        when(userDao.getAll(any())).thenReturn(Arrays.asList(new User(), new User()));
        List<User> all = userService.getAll(PaginationParams.builder().offset(5).limit(2).build());

        verify(userDao).getAll(any());

        assertNotNull(all);
        assertEquals(2, all.size());
    }

    @Test
    public void getById() {
        when(userDao.getById(anyInt())).thenReturn(user);

        User user = userService.getById(1);
        verify(userDao).getById(anyInt());

        assertNotNull(user);
    }

    @Test
    public void createUser() {
        when(userDao.createUser(any())).thenReturn(user);

        User createdUser = userService.createUser(new User());
        verify(userDao).createUser(any());

        assertNotNull(createdUser);
        assertEquals(123, (int) createdUser.getId());
        assertEquals(SER, createdUser.getFirstName());
        assertEquals(DAVOS, user.getLastName());
    }

    @Test
    public void updateUser() {
        when(userDao.updateUser(any())).thenReturn(user);
        User updated = userService.updateUser(new User());
        verify(userDao).updateUser(any());

        assertNotNull(updated);
        assertEquals(123, (int) updated.getId());
        assertEquals(SER, updated.getFirstName());
        assertEquals(DAVOS, user.getLastName());
    }

    @Test
    public void deleteUser() {
        userService.deleteUser(1);
        verify(userDao).deleteUser(anyInt());
    }
}