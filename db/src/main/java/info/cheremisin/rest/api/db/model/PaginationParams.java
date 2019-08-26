package info.cheremisin.rest.api.db.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaginationParams {

    private Integer limit;
    private Integer offset;
}
