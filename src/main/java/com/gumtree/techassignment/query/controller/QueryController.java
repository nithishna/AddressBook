package com.gumtree.techassignment.query.controller;

import com.gumtree.techassignment.query.dto.AgeComparisonRequest;
import com.gumtree.techassignment.query.dto.Person;
import com.gumtree.techassignment.query.service.QueryService;
import com.gumtree.techassignment.query.validator.AgeComparisonInputValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;

@RestController
@RequestMapping("/query")
@RequiredArgsConstructor
public class QueryController {
    private final AgeComparisonInputValidator validator;
    private final QueryService service;

    @PostMapping("/age-comparison")
    public long ageComparison(@RequestBody AgeComparisonRequest inputRequest) throws IOException, ParseException {
        validator.validateInput(inputRequest);
        return service.ageComparison(inputRequest);
    }

    @GetMapping("/gender/{value}/count")
    public long numberOfPersonByGender(@PathVariable("value") String value) {
        return service.getNumberOfPersonByGender(value);
    }

    @GetMapping("/oldest-person")
    public Person getOldestPerson() {
        return service.getOldestPerson();
    }
}
