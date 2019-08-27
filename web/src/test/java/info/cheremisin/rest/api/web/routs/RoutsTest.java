package info.cheremisin.rest.api.web.routs;

import com.despegar.http.client.DeleteMethod;
import com.despegar.http.client.GetMethod;
import com.despegar.http.client.HttpClientException;
import com.despegar.http.client.HttpResponse;
import com.despegar.http.client.PostMethod;
import com.despegar.http.client.PutMethod;
import com.despegar.sparkjava.test.SparkServer;
import info.cheremisin.rest.api.db.model.impl.Account;
import info.cheremisin.rest.api.db.model.impl.User;
import info.cheremisin.rest.api.web.RestApiApp;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import spark.servlet.SparkApplication;

import java.math.BigDecimal;
import java.util.List;

import static info.cheremisin.rest.api.web.common.ClassExtractor.classToJson;
import static info.cheremisin.rest.api.web.common.ClassExtractor.getClassFromString;
import static info.cheremisin.rest.api.web.common.ClassExtractor.getListOfClass;
import static org.eclipse.jetty.http.HttpStatus.CREATED_201;
import static org.eclipse.jetty.http.HttpStatus.NOT_FOUND_404;
import static org.eclipse.jetty.http.HttpStatus.NO_CONTENT_204;
import static org.eclipse.jetty.http.HttpStatus.OK_200;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class RoutsTest {

    Account account;

    @Before
    public void setUp() throws Exception {
        account = Account.builder().id(123).userId(10).balance(new BigDecimal("45.21")).build();
    }

    public static class TestController implements SparkApplication {
        @Override
        public void init() {
            RestApiApp.main(null);
        }
    }

    @ClassRule
    public static SparkServer<TestController> testServer = new SparkServer<>(TestController.class, 8080);

    @Test
    public void getUsersTest() throws HttpClientException {
        GetMethod get = testServer.get("/api/v1/users", false);
        get.addHeader("Accept", "application/json");
        HttpResponse httpResponse = testServer.execute(get);

        assertEquals(httpResponse.code(), OK_200);

        String body = new String(httpResponse.body());
        List<User> list = getListOfClass(body, User.class);
        assertTrue(list.size() == 10);

        User user = list.get(0);
        assertNotNull(user);
        assertEquals(1, (int) user.getId());
        assertEquals("Tyrion", user.getFirstName());
        assertEquals("Lannister", user.getLastName());
    }

    @Test
    public void getUsersLimitAndOffsetTest() throws HttpClientException {
        GetMethod get = testServer.get("/api/v1/users?limit=5&offset=2", false);
        get.addHeader("Accept", "application/json");
        HttpResponse httpResponse = testServer.execute(get);

        assertEquals(httpResponse.code(), OK_200);

        String body = new String(httpResponse.body());
        List<User> list = getListOfClass(body, User.class);
        assertTrue(list.size() == 5);

        User user = list.get(0);
        assertNotNull(user);
        assertEquals(3, (int) user.getId());
        assertEquals("Jaime", user.getFirstName());
        assertEquals("Lannister", user.getLastName());
    }

    @Test
    public void getUserByIdTest() throws HttpClientException {
        GetMethod get = testServer.get("/api/v1/users/1", false);
        get.addHeader("Accept", "application/json");
        HttpResponse httpResponse = testServer.execute(get);

        assertEquals(httpResponse.code(), OK_200);

        String body = new String(httpResponse.body());
        User user = getClassFromString(body, User.class);
        assertNotNull(user);
        assertEquals(1, (int) user.getId());
        assertEquals("Tyrion", user.getFirstName());
        assertEquals("Lannister", user.getLastName());
    }

    @Test
    public void createUserTest() throws HttpClientException {
        User ramsieBolton = new User();
        ramsieBolton.setFirstName("Ramsie");
        ramsieBolton.setLastName("Bolton");
        String postBody = classToJson(ramsieBolton);

        PostMethod post = testServer.post("/api/v1/users", postBody, false);
        post.addHeader("Accept", "application/json");
        HttpResponse httpResponse = testServer.execute(post);

        assertEquals(httpResponse.code(), CREATED_201);

        String body = new String(httpResponse.body());
        User user = getClassFromString(body, User.class);
        assertNotNull(user);
        assertEquals("Ramsie", user.getFirstName());
        assertEquals("Bolton", user.getLastName());
    }

    @Test
    public void updateUserTest() throws HttpClientException {
        User ramsieBolton = new User();
        ramsieBolton.setFirstName("Ramsie");
        ramsieBolton.setLastName("Bolton");
        String postBody = classToJson(ramsieBolton);

        PutMethod put = testServer.put("/api/v1/users/2", postBody, false);
        put.addHeader("Accept", "application/json");
        HttpResponse httpResponse = testServer.execute(put);

        assertEquals(httpResponse.code(), OK_200);

        String body = new String(httpResponse.body());
        User user = getClassFromString(body, User.class);
        assertNotNull(user);
        assertEquals(2, (int) user.getId());
        assertEquals("Ramsie", user.getFirstName());
        assertEquals("Bolton", user.getLastName());
    }

    @Test
    public void deleteUserTest() throws HttpClientException {
        User ramsieBolton = new User();
        ramsieBolton.setFirstName("Bran");
        ramsieBolton.setLastName("Stark");
        String postBody = classToJson(ramsieBolton);

        PostMethod post = testServer.post("/api/v1/users", postBody, false);
        post.addHeader("Accept", "application/json");
        HttpResponse httpResponse = testServer.execute(post);

        assertEquals(httpResponse.code(), CREATED_201);

        String body = new String(httpResponse.body());
        User user = getClassFromString(body, User.class);

        DeleteMethod delete = testServer.delete("/api/v1/users/" + user.getId(), false);
        delete.addHeader("Accept", "application/json");
        httpResponse = testServer.execute(delete);

        assertEquals(httpResponse.code(), NO_CONTENT_204);
    }

    @Test
    public void getNotFoundUserTest() throws HttpClientException {
        GetMethod get = testServer.get("/api/v1/users/123", false);
        get.addHeader("Accept", "application/json");
        HttpResponse httpResponse = testServer.execute(get);

        assertEquals(httpResponse.code(), NOT_FOUND_404);
    }

    @Test
    public void getAllAccountsTest() throws HttpClientException {
        GetMethod get = testServer.get("/api/v1/users/1/accounts", false);
        get.addHeader("Accept", "application/json");
        HttpResponse httpResponse = testServer.execute(get);

        assertEquals(httpResponse.code(), OK_200);

        String body = new String(httpResponse.body());
        List<Account> list = getListOfClass(body, Account.class);
        assertTrue(list.size() == 3);

        Account account = list.get(0);
        assertNotNull(account);
        assertEquals(1, (int) account.getId());
        assertEquals(1, (int) account.getUserId());
        assertEquals(new BigDecimal("100.23"), account.getBalance());
    }

    @Test
    public void getAllAccountsLimitAndOffsetTest() throws HttpClientException {
        GetMethod get = testServer.get("/api/v1/users/1/accounts?limit=1&offset=1", false);
        get.addHeader("Accept", "application/json");
        HttpResponse httpResponse = testServer.execute(get);

        assertEquals(httpResponse.code(), OK_200);

        String body = new String(httpResponse.body());
        List<Account> list = getListOfClass(body, Account.class);
        assertTrue(list.size() == 1);

        Account account = list.get(0);
        assertNotNull(account);
        assertEquals(2, (int) account.getId());
        assertEquals(1, (int) account.getUserId());
        assertEquals(new BigDecimal("11.76"), account.getBalance());
    }

    @Test
    public void getAccountByIdTest() throws HttpClientException {
        GetMethod get = testServer.get("/api/v1/accounts/5", false); // 76.98
        get.addHeader("Accept", "application/json");
        HttpResponse httpResponse = testServer.execute(get);

        assertEquals(httpResponse.code(), OK_200);

        String body = new String(httpResponse.body());
        Account account = getClassFromString(body, Account.class);
        assertNotNull(account);
        assertEquals(5, (int) account.getId());
        assertEquals(3, (int) account.getUserId());
        assertEquals(new BigDecimal("76.98"), account.getBalance());
    }

    @Test
    public void createAccountTest() throws HttpClientException {
        PostMethod post = testServer.post("/api/v1/accounts", classToJson(account), false);
        post.addHeader("Accept", "application/json");
        HttpResponse httpResponse = testServer.execute(post);

        assertEquals(httpResponse.code(), CREATED_201);

        String body = new String(httpResponse.body());
        Account createdAccount = getClassFromString(body, Account.class);
        assertNotNull(createdAccount);
        assertEquals(account.getUserId(), createdAccount.getUserId());
        assertEquals(account.getBalance(), createdAccount.getBalance());
    }

    @Test
    public void updateAccountTest() throws HttpClientException {
        PutMethod put = testServer.put("/api/v1/accounts/6", classToJson(account), false);
        put.addHeader("Accept", "application/json");
        HttpResponse httpResponse = testServer.execute(put);

        assertEquals(httpResponse.code(), OK_200);

        String body = new String(httpResponse.body());
        Account updatedAccount = getClassFromString(body, Account.class);
        assertNotNull(updatedAccount);
        assertEquals(6, (int) updatedAccount.getId());;
        assertEquals(account.getUserId(), updatedAccount.getUserId());
        assertEquals(account.getBalance(), updatedAccount.getBalance());
    }

    @Test
    public void deleteAccountTest() throws HttpClientException {
        PostMethod post = testServer.post("/api/v1/accounts", classToJson(account), false);
        post.addHeader("Accept", "application/json");
        HttpResponse httpResponse = testServer.execute(post);

        assertEquals(httpResponse.code(), CREATED_201);

        String body = new String(httpResponse.body());
        Account createdAccount = getClassFromString(body, Account.class);

        DeleteMethod delete = testServer.delete("/api/v1/accounts/" + createdAccount.getId(), false);
        delete.addHeader("Accept", "application/json");
        httpResponse = testServer.execute(delete);

        assertEquals(httpResponse.code(), NO_CONTENT_204);
    }

    @Test
    public void getNotFoundAccountTest() throws HttpClientException {
        GetMethod get = testServer.get("/api/v1/accounts/123", false);
        get.addHeader("Accept", "application/json");
        HttpResponse httpResponse = testServer.execute(get);

        assertEquals(httpResponse.code(), NOT_FOUND_404);
    }

}