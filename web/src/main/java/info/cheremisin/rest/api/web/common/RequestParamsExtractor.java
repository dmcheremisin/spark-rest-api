package info.cheremisin.rest.api.web.common;

import info.cheremisin.rest.api.db.model.PaginationParams;
import spark.Request;
import spark.utils.StringUtils;

public class RequestParamsExtractor {

    public static int getIdFromRequest(Request req) {
        String id = req.params("id");
        return Integer.parseInt(id);
    }

    public static PaginationParams getPaginationParamsFromRequest(Request req) {
        String limit = req.queryParams("limit");
        String offset = req.queryParams("offset");

        PaginationParams paginationParams = new PaginationParams();
        paginationParams.setLimit(StringUtils.isNotBlank(limit) ? Integer.parseInt(limit) : 10);
        paginationParams.setOffset(StringUtils.isNotBlank(offset) ? Integer.parseInt(offset) : 0);

        return paginationParams;
    }
}
