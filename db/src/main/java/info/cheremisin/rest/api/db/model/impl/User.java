package info.cheremisin.rest.api.db.model.impl;

import info.cheremisin.rest.api.db.model.Identifiable;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class User implements Identifiable {

    private Integer id;
    private String firstName;
    private String lastName;
    private List<Account> accounts;
}
