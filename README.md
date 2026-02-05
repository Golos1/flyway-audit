## Summary
This repository contains callbacks used to audit flyway migrations.
These Callbacks are designed to be used by the Flyway Java API (registered with Flyway.callbacks() method.)

### S3PostMigration
Using AWS S3 objects, this callback maps timestamps to descriptions of migrations so that users can know when different mgirations were applied and what they do.

#### Details
In order to ensure idempotency, this callback doesn't create the S3 bucket, so a preexisting one must be used. 
AWS credentials are fetched from the environment for security reasons.