package info.cheremisin.rest.api.web.common;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import spark.Request;

import java.lang.reflect.Type;
import java.util.List;

public class ClassExtractor {

    private static final Gson gson = new Gson();

    public static <T> T getClassFromRequest(Request req, Class<T> clazz) {
        return gson.fromJson(req.body(), clazz);
    }

    public static <T> T getClassFromString(String body, Class<T> clazz) {
        return gson.fromJson(body, clazz);
    }

    public static <T> List<T> getListOfClass(String body, Class<T> clazz) {
        Type typeOfT = TypeToken.getParameterized(List.class, clazz).getType();
        return gson.fromJson(body, typeOfT);
    }

    public static String classToJson(Object model) {
        return gson.toJson(model);
    }
}
