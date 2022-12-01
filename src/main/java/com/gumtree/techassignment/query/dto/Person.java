package com.gumtree.techassignment.query.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.gumtree.techassignment.query.constants.Gender;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = false)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Getter
@ToString
@RequiredArgsConstructor
public class Person implements Serializable {
    @NotNull(message = "First name is mandatory")
    private final String firstName;
    private final String lastName;
    private final Gender gender;

    @JsonFormat(pattern="dd/MM/yy")
    private final Date dateOfBirth;
}
