package com.gumtree.techassignment.query.validator;

import com.gumtree.techassignment.query.dto.AgeComparisonRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AgeComparisonInputValidator {

    private final Map<String, List<Integer>> firstNameIndex;

    public void validateInput(AgeComparisonRequest inputRequest) {
        List<Integer> sourceFileIndex = firstNameIndex.getOrDefault(inputRequest.getSource().getFirstName(), null);
        List<Integer> targetFileIndex = firstNameIndex.getOrDefault(inputRequest.getTarget().getFirstName(), null);
        boolean isSourceOptionalParameter = StringUtils.hasText(inputRequest.getSource().getLastName()) || inputRequest.getSource().getDateOfBirth() != null;
        boolean isTargetOptionalParameter = StringUtils.hasText(inputRequest.getTarget().getLastName()) || inputRequest.getTarget().getDateOfBirth() != null;
        if(sourceFileIndex == null || targetFileIndex == null)
            throw new IllegalArgumentException("Bad Data");
        if(sourceFileIndex.size() > 1 && !isSourceOptionalParameter)
            throw new UnsupportedOperationException(String.format("%s => Ambiguity in the data - kindly provide the optional DOB field/lastName", inputRequest.getSource().getFirstName()));
        if(targetFileIndex.size() > 1 && !isTargetOptionalParameter)
            throw new UnsupportedOperationException(String.format("%s => Ambiguity in the data - kindly provide the optional DOB field/lastName", inputRequest.getTarget().getFirstName()));
    }
}
