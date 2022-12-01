package com.gumtree.techassignment.query.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.gumtree.techassignment.query.constants.ErrorCodes;
import com.gumtree.techassignment.query.dto.AgeComparisonRequest;
import com.gumtree.techassignment.query.dto.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class QueryControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testAgeComparison() {
        Person source = new Person("Bill", null, null, null);
        Person target = new Person("Paul", "Robinson", null, null);
        AgeComparisonRequest ageComparisonRequest = new AgeComparisonRequest(source, target);
        ResponseEntity<Long> responseEntity = this.restTemplate.postForEntity("/query/age-comparison", ageComparisonRequest, Long.class);
        assertEquals(2862, responseEntity.getBody());
    }

    @Test
    public void testAgeComparisonWithAmbiguity() {
        Person source = new Person("Bill", null, null, null);
        Person target = new Person("Paul", null, null, null);
        AgeComparisonRequest ageComparisonRequest = new AgeComparisonRequest(source, target);

        HttpEntity<AgeComparisonRequest> request = new HttpEntity<>(ageComparisonRequest);
        ResponseEntity<JsonNode> response =
                restTemplate.exchange("/query/age-comparison", HttpMethod.POST, request, JsonNode.class);
        JsonNode map = response.getBody();
        String errorCode = map.get("code").asText();
        String errorMessage = map.get("message").asText();
        assertEquals(ErrorCodes.ADBOOK_DATA_AMBIGUITY.name(), errorCode);
        String expectedErrorMessage = "Paul => Ambiguity in the data - kindly provide the optional DOB field/lastName";
        assertEquals(expectedErrorMessage, errorMessage);
    }

    @Test
    public void testAgeComparisonInvalidEntry() {
        Person source = new Person("Bill", null, null, null);
        Person target = new Person("Alexa", null, null, null);
        AgeComparisonRequest ageComparisonRequest = new AgeComparisonRequest(source, target);

        HttpEntity<AgeComparisonRequest> request = new HttpEntity<>(ageComparisonRequest);
        ResponseEntity<JsonNode> response =
                restTemplate.exchange("/query/age-comparison", HttpMethod.POST, request, JsonNode.class);
        JsonNode map = response.getBody();
        String errorCode = map.get("code").asText();
        String errorMessage = map.get("message").asText();
        assertEquals(ErrorCodes.ADBOOK_BAD_DATA.name(), errorCode);
        String expectedErrorMessage = "Bad Data";
        assertEquals(expectedErrorMessage, errorMessage);
    }

    @Test
    public void testRetrieveCountByGender() {
        ResponseEntity<Long> responseEntity = this.restTemplate.getForEntity("/query/gender/Male/count", Long.class);
        assertEquals(4, responseEntity.getBody());

        responseEntity = this.restTemplate.getForEntity("/query/gender/Female/count", Long.class);
        assertEquals(2, responseEntity.getBody());
    }

    @Test
    public void testRetrieveOldestPerson() {
        ResponseEntity<JsonNode> response =
                restTemplate.exchange("/query/oldest-person", HttpMethod.GET, null,JsonNode.class);
        JsonNode oldestPerson = response.getBody();
        assertEquals("Wes", oldestPerson.get("firstName").asText());
        assertEquals("Jackson", oldestPerson.get("lastName").asText());
        assertEquals("MALE", oldestPerson.get("gender").asText());
        assertEquals("14/08/74",oldestPerson.get("dateOfBirth").asText());
    }
}
