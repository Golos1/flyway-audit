package db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import java.sql.Statement;

public class V1__CreateCustomersTable extends BaseJavaMigration {
    @Override
    public void migrate(Context context) throws Exception {
        try (Statement statement = context.getConnection().createStatement()) {
             statement.execute("CREATE TABLE customers2(" +
                    "CustomerID INT PRIMARY KEY," +
                    "FirstName VARCHAR(25)," +
                    "LastName VARCHAR(25)," +
                     "City VARCHAR(25)," +
                     "State VARCHAR(25)" +
                     ");");
        }
    }
}
