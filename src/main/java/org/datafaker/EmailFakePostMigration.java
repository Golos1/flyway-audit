package org.datafaker;

import net.datafaker.Faker;
import org.flywaydb.core.api.callback.Callback;
import org.flywaydb.core.api.callback.Context;
import org.flywaydb.core.api.callback.Event;

import java.sql.*;

/**
 * Replaces emails in a specific table with fake ones. This works as follows:
 * first one fake email will be generated and replaces all emails.
 * Then a number of rounds set by the user will go. Each round:
 *      1. A fake email is generated.
 *      2. Each row has a set chance of being updated with that fake email.
 */
public class EmailFakePostMigration implements Callback {
    private final Faker faker = new Faker();
    private final int numFakeEmails;
    private final double chanceOfReplacement;
    private final String emailColumn;
    private final String tableName;

    /**
     * @param emailColumn the name of the column containing email values
     * @param tableName the name of the table being updated with fake emails
     * @param numFakeEmails the number of fake emails to introduce after the first; the higher this number the more diverse the emails will be.
     * @param chanceOfReplacement the probability for each row to be updated with each email value after the first. The higher this value is,
     *                           the less pronounced the first fake email will ne in the end results.
     * @throws IllegalArgumentException if chanceOfReplacement isn't in [0,1]
     */
    public EmailFakePostMigration(String emailColumn, String tableName, int numFakeEmails, double chanceOfReplacement) throws IllegalArgumentException{
        if(chanceOfReplacement < 0 || chanceOfReplacement > 1){
            throw new IllegalArgumentException("Chance of email replacement must be in [0,1].");
        }
        this.emailColumn = emailColumn;
        this.tableName = tableName;
        this.numFakeEmails = numFakeEmails;
        this.chanceOfReplacement = chanceOfReplacement;

    }

    @Override
    public boolean supports(Event event, Context context) {return event.equals(Event.AFTER_MIGRATE);}

    @Override
    public boolean canHandleInTransaction(Event event, Context context) {
        return true;
    }

    @Override
    public void handle(Event event, Context context) {
        try {
            Connection conn =  context.getConnection();
            String randomEmail = faker.internet().emailAddress();
            PreparedStatement initialUpdate = conn.prepareStatement("UPDATE " + tableName + " SET " + emailColumn + " = ?;");
            initialUpdate.setString(1, randomEmail);
            initialUpdate.executeUpdate();
            for (int i = 0; i < numFakeEmails; i++) {
                String newEmail = faker.internet().emailAddress();
                PreparedStatement emailReplacement = conn.prepareStatement("UPDATE " + tableName + " SET " + emailColumn + " = ? WHERE RAND() < ?;");
                emailReplacement.setString(1,newEmail);
                emailReplacement.setDouble(2,chanceOfReplacement);
                emailReplacement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getCallbackName() {
        return "EmailFakePostMigration";
    }
}
