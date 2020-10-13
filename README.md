# Simple backend to store notes with versioning 

## Requirements
* Java 8  
* Maven  
* PostgreSQL   
* pgAdmin4  
* IntelliJ IDEA  
* Postman  

## How to start
1. Start pgAdmin and IntelliJ IDEA.  
2. Open pgAdmin and create new database.
3. Open project in your IntelliJ IDEA.
4. Check application-dev.properties in /src/main/resources and check login, password, port, database name. If you have others, change them to yours.  
5. Now open "Edit Run/Debug configurations" dialog. Add new Maven configuration and write to field command line: "spring-boot:run". Click Apply then Ok.
6. Now project is ready to start.  

## How to use
### Notes
To create note or read all current notes please use localhost:8080/notes  
If you wish to read, update or delete particular note use localhost:8080/notes/{noteId}  

### Notes history  
To get history of a particular note localhost:8080/history/{noteId} can be used.

### Tests  
Tests can be found in /src/test/java/.../MainTest

## Examples
### Create
![alt text](https://github.com/40inv/notesback/blob/main/Pictures/PostExample.png)  

### Update
![alt text](https://github.com/40inv/notesback/blob/main/Pictures/UpdateExample.png)  

### Read
![alt text](https://github.com/40inv/notesback/blob/main/Pictures/GetExample.png)  

### Delete
![alt text](https://github.com/40inv/notesback/blob/main/Pictures/DeleteExample.png)

### History
![alt text](https://github.com/40inv/notesback/blob/main/Pictures/HistoryGetExample.png)
