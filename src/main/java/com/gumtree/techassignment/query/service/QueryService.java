package com.gumtree.techassignment.query.service;

import lombok.RequiredArgsConstructor;
import com.gumtree.techassignment.query.dto.AgeComparisonRequest;
import com.gumtree.techassignment.query.dto.Person;
import com.gumtree.techassignment.query.filehandling.PersonTextFileLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.text.ParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class QueryService {
    private final PersonTextFileLoader personTextFileLoader;
    private final Map<String, List<Integer>> firstNameIndex;

    public long ageComparison(AgeComparisonRequest inputRequest) throws IOException, ParseException {
        Person firstPerson = retrieveFromAddressBook(inputRequest.getSource());
        Person secondPerson = retrieveFromAddressBook(inputRequest.getTarget());
        return ChronoUnit.DAYS.between(firstPerson.getDateOfBirth().toInstant(), secondPerson.getDateOfBirth().toInstant());
    }

    public long getNumberOfPersonByGender(String value) {
        return personTextFileLoader.getCountByGender(value);
    }

    public Person getOldestPerson() {
        return personTextFileLoader.getOldestPerson();
    }

    /*
    * we don't have to deal with the edge cases here as the AgeComparisonInput Validator has taken care of those
    * */
    private Person retrieveFromAddressBook(Person input) throws IOException, ParseException {
        List<Integer> indices = firstNameIndex.get(input.getFirstName());
        Person result = null;
        if(indices.size() == 1) {
            result = personTextFileLoader.findPersonByLineNumber(indices.get(0));
        }
        else {
            //if there is ambiguity based on first name use the optional parameter to land a unique person.
            //There is no need to check whether optional parameter is present - it is taken care in the validator itself. If the control flow reaches here then it means the optional parameter do exist
            for (int index : indices) {
                Person retrievedPerson = personTextFileLoader.findPersonByLineNumber(index);
                if(StringUtils.hasText(input.getLastName()) && input.getLastName().equalsIgnoreCase(retrievedPerson.getLastName())) {
                    result = retrievedPerson;
                    break;
                }
                if(input.getDateOfBirth() != null && input.getDateOfBirth().equals(retrievedPerson.getDateOfBirth())) {
                    result = retrievedPerson;
                    break;
                }
            }
        }
        return result;
    }
}
