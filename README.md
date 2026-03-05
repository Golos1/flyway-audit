## Summary
This repository contains callbacks used to audit flyway migrations.
These Callbacks are designed to be used by the Flyway Java API (registered with Flyway.callbacks() method.)

### S3PostMigration
Using AWS S3 objects, this callback maps timestamps to descriptions of migrations so that users can know when different mgirations were applied and what they do.

#### Details
In order to ensure idempotency, this callback doesn't create the S3 bucket, so a preexisting one must be used. 
AWS credentials are fetched from the environment for security reasons.

### DatabaseMigrationHistory
Creates/inserts into a table of database migration history using the same connection as the migration.

#### Details
For queries referencing this table, its name is FlywayMigrations.

### ErrorHistory
Creates/inserts into a table of migration error history using the same connection as the migration.

#### Details
For queries referencing this table, its name is FlywayMigrationErrors.

### SSMPostBaseline
Sets an AWS ParamStore parameter value equal to the baseline version and description equal to baseline description.

### EmailFakePostMigration
Anonymizes emails in a chosen column of a chosen table after the migrate command is complete.