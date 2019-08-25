package info.cheremisin.rest.api.db.model.impl;

import info.cheremisin.rest.api.db.model.Identifiable;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Account implements Identifiable {

    private Integer id;
    private Integer userId;
    private BigDecimal balance;

}
