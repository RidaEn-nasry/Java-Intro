
## 1. Creating a database, tables and inserting data 


1 . install postgresql if not installed
    - brew install postgresql
2 . create a database
    - createdb <database_name>
3 . connect to the database
    - psql <database_name>
4 . run the sql script
    - \i <path_to_sql_script>
5 . check if the tables are created
    - \dt
6 . check if the data is inserted
    - select * from <table_name>;
7 . exit the database
    - \q

## 2. Running the application

1. compile the application
    - mvn clean install
2. run the application
    - mvn exec:java

    



