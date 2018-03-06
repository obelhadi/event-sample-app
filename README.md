# Events Application
This is a CRUD application allows the user to manage events (create, view, update, delete, search)  
It is a developed using Spring Boot as backend and uses Elasticsearch as NoSQL document DB.
The front web part uses jQuery, JS, Bootstrap, HTML, CSS.

## How to run ?
### Elasticsearch
1. Download Elasticsearch **5.6.8**  
[https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-5.6.8.zip](https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-5.6.8.zip)
2. Unzip and run ```bin/elasticsearch``` executable
3. Wait for Elasticsearch to start  

### Spring Boot Application
1. Verify you have JVM 1.8 & Maven 3 installed
2. Run the command  
 ```mvn spring-boot:run``` 
3. This will compile and run the application.  
Once started you can go open link :  
 [http://localhost:8090/index.html](http://localhost:8090/index.html)
4. You will be on the home page of the application, start by creating a new event

### Customize Configuration
In the Spring Boot *application.yml* you can set a different application server port.     
You can also change the host/port for Elasticsearch.

### Other remarks
Sometimes after creating/updating/deleting an event we don't get the changes after redirection to the home page.  
If changes aren't impacted on the home page a simple refresh on the web browser will do the trick.


     
