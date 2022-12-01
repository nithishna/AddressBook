package com.gumtree.techassignment.query.service;

import com.gumtree.techassignment.query.dto.AgeComparisonRequest;
import com.gumtree.techassignment.query.dto.Person;
import com.gumtree.techassignment.query.filehandling.PersonTextFileLoader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class QueryServiceTest {
    private QueryService queryService;
    private PersonTextFileLoader personTextFileLoader = new PersonTextFileLoader();
    @Test
    public void testAgeComparisonWithoutAmbiguity() throws IOException, ParseException {
        PersonTextFileLoader textFileLoader = new PersonTextFileLoader(personTextFileLoader,"AddressBookTest");
        queryService = new QueryService(textFileLoader, textFileLoader.getIndex());
        Person source = new Person("Bill", null, null, null);
        Person target = new Person("Paul", null, null, null);
        AgeComparisonRequest ageComparisonRequest = new AgeComparisonRequest(source, target);
        long actual = queryService.ageComparison(ageComparisonRequest);
        assertEquals(2862, actual);
    }

    @Test
    public void testAgeComparisonWithAmbiguity() throws IOException, ParseException {
        PersonTextFileLoader textFileLoader = new PersonTextFileLoader(personTextFileLoader,"AddressBookAmbiguityTest");
        queryService = new QueryService(textFileLoader, textFileLoader.getIndex());
        Person source = new Person("Bill", null, null, null);
        Person target = new Person("Paul", "Robinson", null, null);
        AgeComparisonRequest ageComparisonRequest = new AgeComparisonRequest(source, target);
        long actual = queryService.ageComparison(ageComparisonRequest);
        assertEquals(2862, actual);
    }

    @Test
    public void testOldestPerson() throws IOException {
        PersonTextFileLoader textFileLoader = new PersonTextFileLoader(personTextFileLoader,"AddressBookTest");
        queryService = new QueryService(textFileLoader, textFileLoader.getIndex());
        Person oldestPerson = queryService.getOldestPerson();
        assertEquals("Wes", oldestPerson.getFirstName());
        assertEquals("Jackson", oldestPerson.getLastName());
        assertEquals("Male", oldestPerson.getGender().getValue());
        DateFormat df = new SimpleDateFormat("dd/MM/yy");
        assertEquals("14/08/74",df.format(oldestPerson.getDateOfBirth()));
    }

    @Test
    public void testNumberOfPersonsByGender() throws IOException {
        PersonTextFileLoader textFileLoader = new PersonTextFileLoader(personTextFileLoader,"AddressBookTest");
        queryService = new QueryService(textFileLoader, textFileLoader.getIndex());
        assertEquals(3, queryService.getNumberOfPersonByGender("Male"));
        assertEquals(2, queryService.getNumberOfPersonByGender("Female"));
    }
}
