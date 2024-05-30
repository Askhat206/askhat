package kz.aptekaplus.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name="products")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Product {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name="sub_category_id", nullable=false)
    private SubCategory subCategory;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "id_registration_number")
    private String idRegistrationNumber;


    @Column(name = "international_name")
    private String internationalName;


    @Column(name = "country_producer")
    private String countryProducer;


    @Column(name = "expiration_date")
    private Double expirationDate;

    @Column(name = "dosage_form")
    private String dosageForm;


    @Column(name = "structure")
    private String structure;

    @Column(name = "price")
    private Double price;


    @ManyToMany(mappedBy = "basket")
    private List<User> busket;
}
