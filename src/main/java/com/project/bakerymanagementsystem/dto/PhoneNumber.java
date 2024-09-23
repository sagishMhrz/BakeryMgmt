package com.project.bakerymanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class PhoneNumber {
    private String title;
    private String number;
}
