Import the project as a maven project!
Used Java 8 with Spring boot 2.7.5.

To build the project run mvn clean install. After that run the TechassignmentApplication main project to spin up the tomcat. 
I have configured Swagger UI in this project so as soon as the tomcat is launched try hitting http://localhost:8080/swagger-ui.html

There will be three services under QueryController to process the given requirement,
1. POST:/query/age-comparison to find out how many days the source person is older than the target person in the request object.
2. GET :/query/gender/{value}/count to get the count of person by gender
3. GET :/query/oldest-person to retrieve the oldest person in the file

I have kept the design extendable for reading different file types (for instance we could register different file loaders like csvFileLoader, ExcelFileLoader, XMLFileLoader to the FileManager later on without having to change anything in the current setup).
Again each FileLoader type can have a child implementation for instance PersonTextFileLoader, TelephoneTextFileLoader etc). 
