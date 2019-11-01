## Technical requirements
- [x] The data exchange between the client and server must be carried out using **TCP**.
- [x] The server should use input/output streams.
- [x] The client should use the network channel.
- [x] Use `java.sql.DriverManager` to connect to the database.
- [x] The username and password for connecting to the database should be specified in the arguments of the `getConnection()` method.
- [x] Use `javax.sql.rowset.FilteredRowSet` to get query results.
- [x] Collective data deletion and insertion operations must be implemented using transactions.
- [x] Single data modification operations must be implemented using the `CallableStatement.execute()` method.

## How-to-run
* Server database
    ```sh
    cd ServerDataBase
    mvn compile 
    mvn exec:java
    ```
* Client database
    ```sh
    cd ClientDataBase
    mvn compile 
    mvn exec:java
    ```
    
## Results
After completing this laboratory work I found out how to internationalize application using Resource Bundle and create ORM with the reflection.

## Next actions
- [ ] Add command to "How-to-run" section that will help to start standalone PostgreSQL Server in Docker with port forwaring
