package com.javaegitimleri.petclinic.entity;

import javax.persistence.*;

@Entity
@Table(name = "hsr_image")
public class Image extends BaseEntity {

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "height")
    private Integer height;

    @Column(name = "width")
    private Integer width;

    @ManyToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_content_id")
    private ImageContent imageContent;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public ImageContent getImageContent() {
        return imageContent;
    }

    public void setImageContent(ImageContent imageContent) {
        this.imageContent = imageContent;
    }
}
