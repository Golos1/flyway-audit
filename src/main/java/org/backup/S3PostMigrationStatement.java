package org.backup;

import org.flywaydb.core.api.callback.Callback;
import org.flywaydb.core.api.callback.Context;
import org.flywaydb.core.api.callback.Event;
import org.flywaydb.core.api.callback.Statement;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

/**
 * Uploads SQL of statement to a preexisting S3 bucket after each migration statement.
 */
public class S3PostMigrationStatement implements Callback {
    private final S3Client client;
    private final String bucketName;

    /**
     * @param bucketName the name of a preexisting S3 bucket, with versioning enabled.
     * @throws RuntimeException if bucket versioning is not enabled or there is an error communicating or authenticating with AWS.
     */
    public S3PostMigrationStatement(String bucketName){
        client = S3Client.create();
        this.bucketName = bucketName;
        GetBucketVersioningRequest getBucketVersioningRequest = GetBucketVersioningRequest.builder()
                .bucket(bucketName)
                .build();
        try {
            GetBucketVersioningResponse response = client.getBucketVersioning(getBucketVersioningRequest);
            if(!response.status().equals(BucketVersioningStatus.ENABLED)){
                throw new RuntimeException("Bucket Versioning must be enabled on bucket " + bucketName + " for it to be used.");
            }
        }catch (AwsServiceException e){
            throw new RuntimeException(e.getLocalizedMessage());
        }

    }

    @Override
    public boolean supports(Event event, Context context) {
        return (event.equals(Event.AFTER_EACH_MIGRATE_STATEMENT));
    }

    @Override
    public boolean canHandleInTransaction(Event event, Context context) {
        return true;
    }

    @Override
    public void handle(Event event, Context context) {
        Statement statement = context.getStatement();
        PutObjectRequest request = PutObjectRequest.builder().bucket(bucketName)
                .key(context.getMigrationInfo().getScript())
                .build();
        try {
            PutObjectResponse response = client.putObject(request, RequestBody.fromString(statement.getSql()));
        }catch (S3Exception e){
            throw new RuntimeException("S3 connection error: " + e.getLocalizedMessage());
        }catch (AwsServiceException e){
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    @Override
    public String getCallbackName() {
        return "S3PostMigrationStatement";
    }
}
