package com.javaegitimleri.petclinic.entity;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.*;

//@NamedQuery(name = "findPetsByName", query = "from Pet as P where P.name like :name")
@SqlResultSetMapping(name = "petWithNameAndBirthDate", classes = @ConstructorResult(targetClass = Pet.class, columns = {
        @ColumnResult(name = "pet_name", type = String.class),
        @ColumnResult(name = "birth_date", type = Date.class)
}))
@Entity
@Table(name = "hsr_pet")
public class Pet extends BaseEntity {

    @NaturalId
    @Basic//(optional = false)
    @Column(name = "pet_name")
    private String name;

    @Column(name = "birth_date")
    @Temporal(TemporalType.DATE)
    private Date birthDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id")
    private PetType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Owner owner;

    @Fetch(FetchMode.SUBSELECT)
    //@BatchSize(size = 10)
    @OneToMany(orphanRemoval = true, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "pet_id")
    @OrderColumn(name = "visit_order")
    private List<Visit> visits = new ArrayList<>();

    @OneToMany(mappedBy = "pet")//Image sınıfındaki pet attribute'ü
    @MapKey(name = "filePath")
    //@JoinColumn(name = "pet_id")
    //@Cascade(CascadeType.DELETE_ORPHAN)
    private Map<String, Image> imagesByFilePath = new HashMap<>();

    public Pet() {

    }

    public Pet(Long id, String name, Date birthDate) {
        setId(id);
        this.name = name;
        this.birthDate = birthDate;
    }

    public Pet(String name, Date birthDate) {
        this.name = name;
        this.birthDate = birthDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public PetType getType() {
        return type;
    }

    public void setType(PetType type) {
        this.type = type;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public List<Visit> getVisits() {
        return visits;
    }

    public void setVisits(List<Visit> visits) {
        this.visits = visits;
    }

    public Map<String, Image> getImagesByFilePath() {
        return imagesByFilePath;
    }

    public void setImagesByFilePath(Map<String, Image> imagesByFilePath) {
        this.imagesByFilePath = imagesByFilePath;
    }

    @Override
    public String toString() {
        return "Pet{" +
                "id ='" + getId() + '\'' +
                "name='" + name + '\'' +
                ", birthDate=" + birthDate +
                '}';
    }
}
