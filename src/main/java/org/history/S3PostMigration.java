package org.history;

import org.flywaydb.core.api.callback.Callback;
import org.flywaydb.core.api.callback.Context;
import org.flywaydb.core.api.callback.Event;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.util.Date;

/**
 * Uploads description and type of a migration to AWS S3, keyed to timestamp.
 */
public class S3PostMigration implements Callback {
    private final S3Client client;
    private final String bucketName;

    /**
     * @param bucketName the name of a preexisting S3 bucket, with versioning enabled.
     * @throws RuntimeException if bucket versioning is not enabled or there is an error communicating or authenticating with AWS.
     */
    public S3PostMigration(String bucketName){
        client = S3Client.create();
        this.bucketName = bucketName;
        GetBucketVersioningRequest getBucketVersioningRequest = GetBucketVersioningRequest.builder()
                .bucket(bucketName)
                .build();
    }

    @Override
    public boolean supports(Event event, Context context) {
        return event.equals(Event.AFTER_EACH_MIGRATE);
    }

    @Override
    public boolean canHandleInTransaction(Event event, Context context) {
        return true;
    }

    @Override
    public void handle(Event event, Context context) {
        PutObjectRequest request = PutObjectRequest.builder().bucket(bucketName)
                .key((new Date()).toString())
                .build();
        try {
            PutObjectResponse response = client.putObject(request, RequestBody.fromString(("Description: "  + context.getMigrationInfo().getDescription() + "\nType: " + context.getMigrationInfo().getType().name()) + "\nInstalled By: " + context.getMigrationInfo().getInstalledBy()));
        }catch (S3Exception e){
            throw new RuntimeException("S3 connection error: " + e.getLocalizedMessage());
        }catch (AwsServiceException e){
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    @Override
    public String getCallbackName() {
        return "S3PostMigration";
    }
}
