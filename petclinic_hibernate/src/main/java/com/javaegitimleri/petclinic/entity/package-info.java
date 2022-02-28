@org.hibernate.annotations.NamedQuery(name = "findPetsByName", query = "from Pet as P where P.name like :name")
package com.javaegitimleri.petclinic.entity;
