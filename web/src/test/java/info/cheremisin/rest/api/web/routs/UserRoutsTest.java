package info.cheremisin.rest.api.web.routs;

import com.despegar.http.client.*;
import com.despegar.sparkjava.test.SparkServer;
import info.cheremisin.rest.api.db.model.impl.User;
import info.cheremisin.rest.api.web.RestApiApp;
import org.junit.ClassRule;
import org.junit.Test;
import spark.servlet.SparkApplication;

import java.util.List;

import static info.cheremisin.rest.api.web.common.ClassExtractor.classToJson;
import static info.cheremisin.rest.api.web.common.ClassExtractor.getClassFromString;
import static org.eclipse.jetty.http.HttpStatus.*;
import static org.junit.Assert.*;

public class UserRoutsTest {

    public static class TestController implements SparkApplication {
        @Override
        public void init() {
            RestApiApp.main(null);
        }
    }

    @ClassRule
    public static SparkServer<TestController> testServer = new SparkServer<>(TestController.class, 8080);

    @Test
    public void testGetUsers() throws HttpClientException {
        GetMethod get = testServer.get("/api/v1/users", false);
        get.addHeader("Accept", "application/json");
        HttpResponse httpResponse = testServer.execute(get);

        assertEquals(httpResponse.code(), OK_200);

        String body = new String(httpResponse.body());
        List<User> classFromString = getClassFromString(body, List.class);
        assertTrue(classFromString.size() == 12);
    }

    @Test
    public void testGetUserById() throws HttpClientException {
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
    public void testCreateUser() throws HttpClientException {
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
        assertEquals(13, (int) user.getId());
        assertEquals("Ramsie", user.getFirstName());
        assertEquals("Bolton", user.getLastName());
    }

    @Test
    public void testUpdateUser() throws HttpClientException {
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
    public void testDeleteUser() throws HttpClientException {
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
    public void testGetNotFoundUser() throws HttpClientException {
        GetMethod get = testServer.get("/api/v1/users/123", false);
        get.addHeader("Accept", "application/json");
        HttpResponse httpResponse = testServer.execute(get);

        assertEquals(httpResponse.code(), NOT_FOUND_404);
    }

}