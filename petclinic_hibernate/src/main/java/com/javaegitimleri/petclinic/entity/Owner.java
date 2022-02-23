package com.javaegitimleri.petclinic.entity;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

//@DiscriminatorValue("O")
//@TypeDef(name = "ratingType", typeClass = RatingUserType.class)
@SecondaryTable(name = "hsr_address", pkJoinColumns = @PrimaryKeyJoinColumn(name = "owner_id"))
@Entity
@Table(name = "hsr_owner")
public class Owner extends Person {

//    @Column(name = "first_name")
//    private String firstName;
//
//    @Column(name = "last_name")
//    private String lastName;

    @Column(name = "age")
    private Integer age;

    //@Type(type = "ratingType")
    //@Enumerated(EnumType.ORDINAL)
    @Convert(converter = RatingAttributeConverter.class)
    @Column(name = "rating")
    private Rating rating;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.PERSIST)
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private Set<Pet> pets = new HashSet<>();

//    ValueTİp ile one-to-many ilişki
//    @ElementCollection
//    @CollectionTable(name = "hsr_owner_emails", joinColumns = @JoinColumn(name = "owner_id"))
//    @Column(name = "email")
//    private Set<String> emails = new HashSet<>();

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public Set<Pet> getPets() {
        return pets;
    }

    public void setPets(Set<Pet> pets) {
        this.pets = pets;
    }
}

