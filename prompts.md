## Prompt for DB change
remove h2 as the database and add postgress db , create the start up script for the DDL by looking at the Entoty models, the name of the db is huddb and the schema is hut

###Response

H2 and SQLite have been removed and replaced with PostgreSQL in your Maven dependencies, application properties, and Java config. The database is now set to huddb with schema hut. A startup DDL script (schema-postgres.sql) for the login_sessions table in schema hut has been created, and the table creation logic in LoginApiPlugin is updated for PostgreSQL.

No errors were found in the updated files. You can now proceed to create the huddb database and schema in your PostgreSQL instance, and run the DDL script if needed. Let me know if you want instructions for initializing the database or further automation!


