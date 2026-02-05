package db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import java.sql.ResultSet;
import java.sql.Statement;

public class V1_CreatePersonTable  extends BaseJavaMigration {
    @Override
    public void migrate(Context context) throws Exception {
        try (Statement statement = context.getConnection().createStatement()) {
             statement.execute("CREATE TABLE customers(" +
                    "CustomerID INT PRIMARY KEY," +
                    "FirstName VARCHAR(25)," +
                    "LastName VARCHAR(25)" +
                    ");");
        }
    }
}
