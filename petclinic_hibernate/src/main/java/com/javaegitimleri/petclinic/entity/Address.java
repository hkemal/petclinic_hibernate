package com.javaegitimleri.petclinic.entity;

import javax.persistence.*;

@Embeddable
public class Address {

    @Column(table = "hsr_address")
    private String street;

    @Column(table = "hsr_address")
    private String phone;

    @Column(name = "phone_type", table = "hsr_address")
    private PhoneType phoneType;

    @ManyToOne
    @JoinColumn(name = "city_id", table = "hsr_address", foreignKey = @ForeignKey(name = "fk_address_city"))
    private City city;

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public PhoneType getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(PhoneType phoneType) {
        this.phoneType = phoneType;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}

