## Project Setup

Import the project as a maven project!
Used Java 8 with Spring boot 2.7.5.

To build the project run **mvn clean install**. After that run the TechassignmentApplication main project to spin up the tomcat. 
I have configured Swagger UI in this project so as soon as the tomcat is launched try hitting http://localhost:8080/swagger-ui.html

## Service overview
There will be three services under QueryController to process the given requirement,
1. POST:/query/age-comparison to find out how many days the source person is older than the target person in the request object.
Input request sample json:
{
  "source": {
    "firstName": "Bill"
  },
  "target": {
    "firstName": "Paul"
  }
}
2. GET :/query/gender/{value}/count to get the count of person by gender
3. GET :/query/oldest-person to retrieve the oldest person in the file

I have kept the design extendable for reading different file types (for instance we could register different file loaders like csvFileLoader, ExcelFileLoader, XMLFileLoader to the FileManager (with fileExtension) later on without having to change anything in the current setup).
Again each FileLoader type can have a child implementation for instance TextFileLoader could have PersonTextFileLoader, TelephoneTextFileLoader etc). 

## Assumptions made for this implementation
1. The AddressBook file may grow larger over the time (so I have not loaded all the file details in memory - just created an index based on the firstName). Even though the file may have millions of records this index will be occupying will less space in memory. The index will have the address or the line number of the record for faster retrieval lateron.
2. The AddressBook file maintained within this service can be updated on runtime (as and when new entries needed to be added to it by external services) either by calling an API or by asynchronously listening to a message queue and keep on updating the oldestPerson object and the count of person by gender - thereby those values don't have to be computed for each request. 
