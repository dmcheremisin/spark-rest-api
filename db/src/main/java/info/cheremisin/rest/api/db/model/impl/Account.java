package info.cheremisin.rest.api.db.model.impl;

import info.cheremisin.rest.api.db.model.Identifiable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account implements Identifiable {

    private Integer id;
    private Integer userId;
    private BigDecimal balance;

}
