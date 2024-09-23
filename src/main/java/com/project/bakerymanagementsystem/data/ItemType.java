package com.project.bakerymanagementsystem.data;

public enum ItemType {
    FOOD("Food"), BEVERAGE("Beverage");

    String typeText;

    ItemType(String typeText) {
        this.typeText = typeText;
    }

    public String getTypeText() {
        return this.typeText;
    }
}
