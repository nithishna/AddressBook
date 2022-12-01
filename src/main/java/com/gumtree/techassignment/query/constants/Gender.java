package com.gumtree.techassignment.query.constants;

public enum Gender {
    MALE("Male"), FEMALE("Female");
    private final String value;

    private Gender(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static Gender getFromVal(String value) {
        for(Gender gender : Gender.values()) {
            if(gender.value.equalsIgnoreCase(value)) {
                return gender;
            }
        }
        throw new IllegalArgumentException(value);
    }
}
