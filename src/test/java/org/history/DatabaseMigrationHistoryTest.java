package org.history;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseMigrationHistoryTest {

    /**
     * Tests to see that migration history table has been created and inserted into.
     */
    @Test
    public void testHandle(){
        DatabaseMigrationHistory callback = new DatabaseMigrationHistory();
        Flyway flyway = Flyway.configure().
                dataSource("jdbc:h2:file:./target/foobar", "sa", null).
                cleanDisabled(false).
                callbacks(callback).
                load();
        flyway.clean();
        assertDoesNotThrow(flyway::migrate);
        try(Connection connection = DriverManager.getConnection("jdbc:h2:file:./target/foobar", "sa", null)) {
            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery("SELECT COUNT(*) AS numRows FROM FlywayMigrations ");
            assertTrue(results.getInt("numRows") > 0);
        } catch (SQLException e) {
            fail(e);
        }
    }
}
