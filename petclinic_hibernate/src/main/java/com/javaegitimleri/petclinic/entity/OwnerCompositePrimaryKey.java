package com.javaegitimleri.petclinic.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "hsr_owner_composite_primary_key")
public class OwnerCompositePrimaryKey {
    @Id
    private OwnerId id;

    @Column(name = "age")
    private Integer age;

    public OwnerId getId() {
        return id;
    }

    public void setId(OwnerId id) {
        this.id = id;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}

