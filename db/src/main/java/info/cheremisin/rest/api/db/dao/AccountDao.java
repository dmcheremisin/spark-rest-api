package info.cheremisin.rest.api.db.dao;

import info.cheremisin.rest.api.db.model.PaginationParams;
import info.cheremisin.rest.api.db.model.impl.Account;

import java.util.List;

public interface AccountDao {

    List<Account> getAll(Integer userId, PaginationParams pagination);

    Account getById(Integer id);

    Account createAccount(Account account);

    Account updateAccount(Account account);

    void deleteAccount(Integer id);

}
