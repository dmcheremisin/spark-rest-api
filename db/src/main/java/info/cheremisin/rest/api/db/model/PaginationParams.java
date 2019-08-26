package info.cheremisin.rest.api.db.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaginationParams {

    private Integer limit;
    private Integer offset;
}
