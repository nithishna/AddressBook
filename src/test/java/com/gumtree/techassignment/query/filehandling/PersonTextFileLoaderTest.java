package com.gumtree.techassignment.query.filehandling;

import com.gumtree.techassignment.query.dto.Person;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PersonTextFileLoaderTest {

    private PersonTextFileLoader personTextFileLoader = new PersonTextFileLoader();

    @Test
    public void testFileLoad() throws IOException {
        FileLoader textFileLoader = personTextFileLoader.load("AddressBookTest");
        assertNotNull(textFileLoader.getIndex());
        assertEquals(textFileLoader.getIndex().size(), 5);
    }

    @Test
    public void testBlankEntryNotImpactingFileLoad() throws IOException {
        FileLoader textFileLoader = personTextFileLoader.load("AddressBookBlankSpaceTest");
        assertNotNull(textFileLoader.getIndex());
        assertEquals(textFileLoader.getIndex().size(), 5);
    }

    @Test
    public void testInvalidDateNotImpactingTheRestOfFileLoad() throws IOException {
        FileLoader textFileLoader = personTextFileLoader.load("AddressBookInvalidDateTest");
        assertNotNull(textFileLoader.getIndex());
        assertEquals(textFileLoader.getIndex().size(), 5);
    }

    @Test
    public void testInvalidFileLoad() {
        try {
            FileLoader textFileLoader = personTextFileLoader.load("dummyfile");
            fail("Should throw IOException");
        }catch(IOException ex){

        }catch(Exception ex) {
            fail("Different exception is thrown - IOException is expected. Got : " + ex.getClass().getName());
        }
    }

    @Test
    public void testPersonRetrievalByLineNumberSuccess() throws IOException, ParseException {
        PersonTextFileLoader textFileLoader = new PersonTextFileLoader(personTextFileLoader,"AddressBookTest");
        Person person = textFileLoader.findPersonByLineNumber(1);
        DateFormat df = new SimpleDateFormat("dd/MM/yy");
        assertEquals("15/01/85", df.format(person.getDateOfBirth()));
        assertEquals("Paul", person.getFirstName());
        assertEquals("Robinson", person.getLastName());
        assertEquals("Male", person.getGender().getValue());
    }

    @Test
    public void testPersonRetrievalByLineNumberFailure() throws IOException, ParseException {
        try {
            PersonTextFileLoader textFileLoader = new PersonTextFileLoader(personTextFileLoader, "AddressBookInvalidDateTest");
            Person person = textFileLoader.findPersonByLineNumber(2);
            fail("Should throw ParseException");
        }catch(ParseException ex){
        }catch(Exception ex) {
            fail("Different exception is thrown - ParseException is expected. Got : " + ex.getClass().getName());
        }
    }

    @Test
    public void testCountByGender() throws IOException {
        PersonTextFileLoader textFileLoader = new PersonTextFileLoader(personTextFileLoader,"AddressBookTest");
        assertEquals(3, textFileLoader.getCountByGender("Male"));
        assertEquals(2, textFileLoader.getCountByGender("Female"));
    }

    @Test
    public void testOldestPerson() throws IOException {
        PersonTextFileLoader textFileLoader = new PersonTextFileLoader(personTextFileLoader,"AddressBookTest");
        Person oldestPerson = textFileLoader.getOldestPerson();
        //Wes Jackson, Male, 14/08/74
        assertEquals("Wes", oldestPerson.getFirstName());
        assertEquals("Jackson", oldestPerson.getLastName());
        assertEquals("Male", oldestPerson.getGender().getValue());
        DateFormat df = new SimpleDateFormat("dd/MM/yy");
        assertEquals("14/08/74",df.format(oldestPerson.getDateOfBirth()));
    }
}
