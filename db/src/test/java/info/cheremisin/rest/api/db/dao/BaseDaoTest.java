package info.cheremisin.rest.api.db.dao;

import info.cheremisin.rest.api.db.connection.DbInitializer;

public class BaseDaoTest {

    static {
        DbInitializer dbInitializer = new DbInitializer();
        dbInitializer.initializeSchemaAndData();
    }
}
