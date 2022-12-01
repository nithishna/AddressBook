package com.gumtree.techassignment.query.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = false)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Getter
@ToString
@RequiredArgsConstructor
public class AgeComparisonRequest {
    @NotNull(message = "source person info is mandatory")
    private final Person source;
    @NotNull(message = "target person info is mandatory")
    private final Person target;
}
