package info.cheremisin.rest.api.web.transformers;

import com.google.gson.Gson;
import spark.ResponseTransformer;

import static info.cheremisin.rest.api.web.common.ClassExtractor.classToJson;

public class JsonTransformer implements ResponseTransformer {

    @Override
    public String render(Object model) {
        return classToJson(model);
    }

}
