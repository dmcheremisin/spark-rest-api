package info.cheremisin.rest.api.web.routs;

import com.despegar.http.client.DeleteMethod;
import com.despegar.http.client.GetMethod;
import com.despegar.http.client.HttpClientException;
import com.despegar.http.client.HttpResponse;
import com.despegar.http.client.PostMethod;
import com.despegar.http.client.PutMethod;
import com.despegar.sparkjava.test.SparkServer;
import info.cheremisin.rest.api.db.model.impl.Account;
import info.cheremisin.rest.api.db.model.impl.Transaction;
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

public class RoutsTest {

    Account account;

    User userForTest;

    @Before
    public void setUp() throws Exception {
        account = Account.builder().id(123).userId(10).balance(new BigDecimal("45.21")).build();
        userForTest = User.builder().firstName("Ramsie").lastName("Bolton").build();
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
        assertEquals(10, list.size());

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
        assertEquals(5, list.size());

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
        String postBody = classToJson(userForTest);

        PostMethod post = testServer.post("/api/v1/users", postBody, false);
        post.addHeader("Accept", "application/json");
        HttpResponse httpResponse = testServer.execute(post);

        assertEquals(httpResponse.code(), CREATED_201);

        String body = new String(httpResponse.body());
        User user = getClassFromString(body, User.class);
        assertNotNull(user);
        assertEquals(userForTest.getFirstName(), user.getFirstName());
        assertEquals(userForTest.getLastName(), user.getLastName());
    }

    @Test
    public void updateUserTest() throws HttpClientException {
        PutMethod put = testServer.put("/api/v1/users/2", classToJson(userForTest), false);
        put.addHeader("Accept", "application/json");
        HttpResponse httpResponse = testServer.execute(put);

        assertEquals(httpResponse.code(), OK_200);

        String body = new String(httpResponse.body());
        User user = getClassFromString(body, User.class);
        assertNotNull(user);
        assertEquals(2, (int) user.getId());
        assertEquals(userForTest.getFirstName(), user.getFirstName());
        assertEquals(userForTest.getLastName(), user.getLastName());
    }

    @Test
    public void deleteUserTest() throws HttpClientException {
        PostMethod post = testServer.post("/api/v1/users", classToJson(userForTest), false);
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
        assertEquals(3, list.size());

        Account account = list.get(0);
        assertNotNull(account);
        assertEquals(1, (int) account.getId());
        assertEquals(1, (int) account.getUserId());
        assertEquals(new BigDecimal("100.23"), account.getBalance());
    }

    @Test
    public void getAllAccountsLimitAndOffsetTest() throws HttpClientException {
        GetMethod get = testServer.get("/api/v1/users/6/accounts?limit=1&offset=1", false);
        get.addHeader("Accept", "application/json");
        HttpResponse httpResponse = testServer.execute(get);

        assertEquals(httpResponse.code(), OK_200);

        String body = new String(httpResponse.body());
        List<Account> list = getListOfClass(body, Account.class);
        assertEquals(1, list.size());

        Account account = list.get(0);
        assertNotNull(account);
        assertEquals(9, (int) account.getId());
        assertEquals(6, (int) account.getUserId());
        assertEquals(new BigDecimal("11.56"), account.getBalance());
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

    @Test
    public void getTransactionsTestLimitOffset() throws HttpClientException {
        GetMethod get = testServer.get("/api/v1/users/11/transactions?limit=1&offset=1", false);
        get.addHeader("Accept", "application/json");
        HttpResponse httpResponse = testServer.execute(get);

        assertEquals(httpResponse.code(), OK_200);

        String body = new String(httpResponse.body());
        List<Transaction> list = getListOfClass(body, Transaction.class);
        assertEquals(1, list.size());

        Transaction transaction = list.get(0);
        assertNotNull(transaction);
        assertEquals(2, (int) transaction.getId());
        assertEquals(3, (int) transaction.getAccountDonor());
        assertEquals(11, (int) transaction.getAccountAcceptor());
        assertEquals(new BigDecimal("12.94"), transaction.getAmount());
    }

    @Test
    public void getTransactionsTest() throws HttpClientException {
        GetMethod get = testServer.get("/api/v1/users/11/transactions", false);
        get.addHeader("Accept", "application/json");
        HttpResponse httpResponse = testServer.execute(get);

        assertEquals(httpResponse.code(), OK_200);

        String body = new String(httpResponse.body());
        List<Transaction> list = getListOfClass(body, Transaction.class);
        assertEquals(3, list.size());

        Transaction transaction = list.get(0);
        assertNotNull(transaction);
        assertEquals(1, (int) transaction.getId());
        assertEquals(1, (int) transaction.getAccountDonor());
        assertEquals(11, (int) transaction.getAccountAcceptor());
        assertEquals(new BigDecimal("11.57"), transaction.getAmount());
    }

    @Test
    public void getTransactionTest() throws HttpClientException {
        GetMethod get = testServer.get("/api/v1/transactions/2", false);
        get.addHeader("Accept", "application/json");
        HttpResponse httpResponse = testServer.execute(get);

        assertEquals(httpResponse.code(), OK_200);

        String body = new String(httpResponse.body());
        Transaction transaction = getClassFromString(body, Transaction.class);
        assertNotNull(transaction);
        assertEquals(2, (int) transaction.getId());
        assertEquals(3, (int) transaction.getAccountDonor());
        assertEquals(11, (int) transaction.getAccountAcceptor());
        assertEquals(new BigDecimal("12.94"), transaction.getAmount());
    }

    @Test
    public void createTransaction() throws HttpClientException {
        Transaction transaction = Transaction.builder().id(1)
                .accountDonor(3)
                .accountAcceptor(2)
                .amount(new BigDecimal("10.12"))
                .build();
        PostMethod post = testServer.post("/api/v1/transactions", classToJson(transaction), false);
        post.addHeader("Accept", "application/json");
        HttpResponse httpResponse = testServer.execute(post);

        assertEquals(httpResponse.code(), CREATED_201);

        String body = new String(httpResponse.body());
        Transaction createdTransaction = getClassFromString(body, Transaction.class);
        assertNotNull(createdTransaction);
        assertEquals(transaction.getAccountDonor(), createdTransaction.getAccountDonor());
        assertEquals(transaction.getAccountAcceptor(), createdTransaction.getAccountAcceptor());
        assertEquals(transaction.getAmount(), createdTransaction.getAmount());
    }

}