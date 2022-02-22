package com.javaegitimleri.petclinic.entity;

import javax.persistence.*;
import java.util.Arrays;

@Entity
@Table(name = "hsr_image_content")
public class ImageContent extends BaseEntity {

//    @Id
//    private Long id;

    @OneToOne(mappedBy = "imageContent")
    @JoinColumn(name = "image_id")
    private Image image;

    @Lob
    private byte[] content;

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "ImageContent{" +
                "image=" + image +
                ", content=" + Arrays.toString(content) +
                '}';
    }
}
