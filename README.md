## Summary
In order to audit database migrations later, 
this callback keys the name of each script in a Flyway migration to an object in a single S3 bucket, with each successful SQL statement being put as a new version of that object. In order to ensure idempotency, this callback doesn't create the S3 bucket, so a preexisting one with versioning turned on must be used. AWS credentials are fetched from the environment for securitu reasons.

### Usage
The callback is registered with flyway.callbacks() as normal, and throws RuntimeExceptions if there is an error working with AWS.