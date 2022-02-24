package com.javaegitimleri.petclinic.service;

import com.javaegitimleri.petclinic.entity.Clinic;
import com.javaegitimleri.petclinic.entity.Owner;
import com.javaegitimleri.petclinic.repository.ClinicRepository;
import com.javaegitimleri.petclinic.repository.OwnerRepository;

import java.util.Arrays;

public class PetClinicService {

    private OwnerRepository ownerRepository;

    private ClinicRepository clinicRepository;

    public PetClinicService() {
    }

    public PetClinicService(OwnerRepository ownerRepository, ClinicRepository clinicRepository) {
        this.ownerRepository = ownerRepository;
        this.clinicRepository = clinicRepository;
    }

    public void setOwnerRepository(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    public void setClinicRepository(ClinicRepository clinicRepository) {
        this.clinicRepository = clinicRepository;
    }

    public void addNewOwners(Long clinicId, Owner... owners) {
        Clinic clinic = clinicRepository.findById(clinicId);
        Arrays.stream(owners).forEach(item -> {
            ownerRepository.create(item);
            clinic.getPeople().add(item);
        });
    }
}
