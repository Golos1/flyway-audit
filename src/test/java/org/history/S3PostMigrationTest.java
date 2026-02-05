package org.history;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.fail;

public class S3PostMigrationTest {
    /**
     * Tests to see that S3PostMigration callback runs without exception.
     * Can only properly be done in CI due to usage of AWS.
     */
    @Test
    public void testHandle(){
        S3PostMigration callback;
        try {
            callback = new S3PostMigration("test-flyway-callback");
        }catch (Exception e){
            fail(e);
            return;
        }
        Flyway flyway = Flyway.configure().
                dataSource("jdbc:h2:file:./target/foobar", "sa", null).
                cleanDisabled(false).
                callbacks(callback).
                load();
        flyway.clean();
        assertDoesNotThrow(flyway::migrate);
    }
}