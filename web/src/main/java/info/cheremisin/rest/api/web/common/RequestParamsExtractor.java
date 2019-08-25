package info.cheremisin.rest.api.web.common;

import spark.Request;

public class RequestParamsExtractor {

    public static int getIdFromRequest(Request req) {
        String id = req.params("id");
        return Integer.parseInt(id);
    }
}
