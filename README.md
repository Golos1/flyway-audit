## Summary
More classes planned, currently only S3PostMigrationimplemented
In order to audit database migrations later, this callback keys the date/time of each migration to its description and type.
In order to ensure idempotency, this callback doesn't create the S3 bucket, so a preexisting one must be used. 
AWS credentials are fetched from the environment for security reasons.

### Usage
The callback is registered with flyway.callbacks() as normal, and throws RuntimeExceptions if there is an error working with AWS.