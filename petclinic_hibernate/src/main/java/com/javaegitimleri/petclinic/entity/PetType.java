package com.javaegitimleri.petclinic.entity;

import org.hibernate.annotations.BatchSize;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@BatchSize(size = 10)
@Entity
@Table(name = "hsr_pet_type")
public class PetType extends BaseEntity {

    @Column(name = "name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "PetType{" +
                "name='" + name + '\'' +
                '}';
    }
}
