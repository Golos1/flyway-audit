package org.history;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

public class ErrorHistoryTest {

    /**
     * Tests to see that migration error history table has been created and inserted into.
     */
    @Test
    public void testHandle(){
        ErrorHistory callback = new ErrorHistory();
        Flyway flyway = Flyway.configure().
                dataSource("jdbc:h2:file:./target/foobar", "sa", null).
        locations("classpath:error").
                cleanDisabled(false).
                callbacks(callback).
                load();
        flyway.clean();
        assertThrows(FlywayException.class, flyway::migrate);
        try(Connection connection = DriverManager.getConnection("jdbc:h2:file:./target/foobar", "sa", null)) {
            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery("SELECT * FROM FlywayMigrationErrors");
            assertTrue(results.next());
        } catch (SQLException e) {
            fail(e);
        }
    }
}
