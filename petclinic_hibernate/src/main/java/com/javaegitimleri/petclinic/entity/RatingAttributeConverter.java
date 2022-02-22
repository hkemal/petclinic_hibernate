package com.javaegitimleri.petclinic.entity;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = false)
public class RatingAttributeConverter implements AttributeConverter<Rating, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Rating attribute) {
        if (attribute == null) return null;
        return attribute.getValue();
    }

    @Override
    public Rating convertToEntityAttribute(Integer dbData) {
        if (dbData == null) return null;
        else if (dbData.equals(100)) return Rating.STANDART;
        else if (dbData.equals(200)) return Rating.PREMIUM;
        else return null;
    }
}
