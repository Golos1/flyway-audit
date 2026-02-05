package org.backup;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.fail;

public class S3PostMigrationStatementTest {
    @Test
    public void testHandle(){
        S3PostMigrationStatement callback;
        try {
            callback = new S3PostMigrationStatement("test-flyway-callback");
        }catch (Exception e){
            fail(e);
            return;
        }
        Flyway flyway = Flyway.configure().
                dataSource("jdbc:h2:file:./target/foobar", "sa", null).
                callbacks(callback).
                locations("../../../../java/db/migration").
                load();
        assertDoesNotThrow(flyway::migrate);
    }
}