package org.datafaker;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

public class EmailFakePostMigrationTest {
    @Test
    public void testHandle(){
        EmailFakePostMigration callback = new EmailFakePostMigration("Email","customers",5, 0.8);
        Flyway flyway = Flyway.configure().
                dataSource("jdbc:h2:file:./target/foobar", "sa", null).
                cleanDisabled(false).
                callbacks(callback).
                load();
        flyway.clean();
        assertDoesNotThrow(flyway::migrate);
        try(Connection connection = DriverManager.getConnection("jdbc:h2:file:./target/foobar", "sa", null)) {
            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery("SELECT DISTINCT Email FROM customers");
            results.next();
            assertTrue(results.next());
        } catch (SQLException e) {
            fail(e);
        }
    }
}
