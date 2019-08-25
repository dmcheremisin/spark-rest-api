package info.cheremisin.rest.api.web.common;

import com.google.gson.Gson;
import spark.Request;

public class ClassExtractor {

    private static final Gson gson = new Gson();

    public static <T> T getClassFromRequest(Request req, Class<T> objectclass) {
        return gson.fromJson(req.body(), objectclass);
    }
}
