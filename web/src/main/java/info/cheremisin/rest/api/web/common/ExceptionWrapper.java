package info.cheremisin.rest.api.web.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

public class ExceptionWrapper {

    private static final Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    public String wrapExceptionToJson(String message) {
        HashMap<String, String> map = new HashMap<>();
        map.put("messsage", message);
        return gson.toJson(map);
    }
}
