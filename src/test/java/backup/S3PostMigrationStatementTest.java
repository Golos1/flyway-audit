package backup;

import org.backup.S3PostMigrationStatement;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class S3PostMigrationStatementTest {
    @Test
    public void testHandle(){
        Flyway flyway = Flyway.configure().
                dataSource("jdbc:h2:file:./test/dbfile", "sa", null).
                callbacks(new S3PostMigrationStatement("test-flyway-callback")).
                load();
        assertDoesNotThrow(flyway::migrate);
    }
}