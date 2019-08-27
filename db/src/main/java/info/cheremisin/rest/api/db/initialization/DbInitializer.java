package info.cheremisin.rest.api.db.initialization;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;

import static info.cheremisin.rest.api.db.connection.ConnectionPool.getSql2o;


@Slf4j
public class DbInitializer {

    public static final String SCHEMA_SQL = "schema.sql";

    public void initializeSchemaAndData() {
        try (Connection con = getSql2o().getDataSource().getConnection();
             Statement stmt = con.createStatement();) {

            URI uri = ClassLoader.getSystemResource(SCHEMA_SQL).toURI();
            final String[] array = uri.toString().split("!");
            FileSystem fs = null;
            if(array.length > 1) {
                // strange windows issue with resource file in jar, next line fixes it
                // https://stackoverflow.com/questions/22605666/java-access-files-in-jar-causes-java-nio-file-filesystemnotfoundexception
                fs = FileSystems.newFileSystem(URI.create(array[0]), new HashMap<>());
            }

            URL resource = getClass().getClassLoader().getResource(SCHEMA_SQL);
            Path path = Paths.get(resource.toURI());
            StringBuilder schemaBatch = fileToSqlBatch(path);

            splitAndAddSql(stmt, schemaBatch);

            stmt.executeBatch();
            if(fs != null) {
                fs.close();
            }
            log.info("All data is initialized successfully");
        } catch (SQLException | URISyntaxException | IOException e) {
            log.error("Something went wrong");
            throw new RuntimeException(e);
        }
    }

    private StringBuilder fileToSqlBatch(Path path) {
        try {
            List<String> lines = Files.readAllLines(path);
            StringBuilder sb = new StringBuilder();
            lines.forEach(sb::append);
            return sb;
        } catch (IOException e) {
            log.error("Cant read sql batch");
            throw new RuntimeException(e);
        }
    }

    private static void splitAndAddSql(Statement stmt, StringBuilder sqlBatch) throws SQLException {
        String[] schemaSql = sqlBatch.toString().split(";");
        for (String sql : schemaSql) {
            stmt.addBatch(sql);
        }
    }
}
