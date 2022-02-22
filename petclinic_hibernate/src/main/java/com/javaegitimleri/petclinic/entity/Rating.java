package com.javaegitimleri.petclinic.entity;

public enum Rating {
    STANDART(100), PREMIUM(200);

    private int value;

    Rating(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
