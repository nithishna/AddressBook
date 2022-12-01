package com.gumtree.techassignment.query.validator;

import com.gumtree.techassignment.query.dto.AgeComparisonRequest;
import com.gumtree.techassignment.query.dto.Person;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(MockitoExtension.class)
public class AgeComparisonInputValidatorTest {

    private Map<String, List<Integer>> firstNameIndexMock = new HashMap<>();

    private AgeComparisonInputValidator inputValidator = new AgeComparisonInputValidator(firstNameIndexMock);

    @Test
    public void testValidatorWhenNoDataFound() {
        Person source = new Person("Sam", null, null, null);
        Person target = new Person("Phill", null, null, null);
        AgeComparisonRequest inputRequest = new AgeComparisonRequest(source, target);
        firstNameIndexMock.put("Sam", Arrays.asList(1));
        try {
            inputValidator.validateInput(inputRequest);
            fail("Should throw IllegalArgumentException");
        }catch(IllegalArgumentException ex) {
        }catch(Exception ex){
            fail("Different exception is thrown - IllegalArgumentException is expected. Got : " + ex.getClass().getName());
        }
    }

    @Test
    public void testValidatorWhenAmbiguity() {
        Person source = new Person("Sam", null, null, null);
        Person target = new Person("Phill", null, null, null);
        AgeComparisonRequest inputRequest = new AgeComparisonRequest(source, target);
        firstNameIndexMock.clear();
        firstNameIndexMock.put("Sam", Arrays.asList(1,2));
        firstNameIndexMock.put("Phill", Arrays.asList(3));
        try {
            inputValidator.validateInput(inputRequest);
            fail("Should throw UnsupportedOperationException");
        }catch(UnsupportedOperationException ex) {
        }catch(Exception ex){
            fail("Different exception is thrown - UnsupportedOperationException is expected. Got : " + ex.getClass().getName());
        }
    }

    @Test
    public void testValidatorWhenAmbiguityAndAdditionalFields() throws ParseException {
        Person source = new Person("Sam", "DD", null, null);
        DateFormat formatter = new SimpleDateFormat("dd/MM/yy");
        Date dob = formatter.parse("10/03/90");
        Person target = new Person("Phill", null, null, dob);
        AgeComparisonRequest inputRequest = new AgeComparisonRequest(source, target);
        firstNameIndexMock.clear();
        firstNameIndexMock.put("Sam", Arrays.asList(1,2));
        firstNameIndexMock.put("Phill", Arrays.asList(3));
        try {
            inputValidator.validateInput(inputRequest);
        }catch(UnsupportedOperationException ex) {
            fail("Should not throw UnsupportedOperationException");
        }catch(Exception ex){
            fail("Should not throw Exception. Got : " + ex.getClass().getName());
        }
    }
}
