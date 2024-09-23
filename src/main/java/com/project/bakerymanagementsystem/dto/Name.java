package com.project.bakerymanagementsystem.dto;

import lombok.Data;

import jakarta.persistence.*;
@Data
@Embeddable
public class Name {
    String firstName;
    String lastName;

    public Name(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Name() {}

    @Override
    public String toString() {
        return firstName+" "+lastName;
    }
}
