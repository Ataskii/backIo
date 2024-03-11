package io.backQL.BackIo.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.Collection;
import java.util.Date;

@Data
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity // Creating table out of class
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String address;
    private String phone;
    private String type;
    private String status;
    private String imageUrl;
    private Date createdAt;
    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER, cascade = CascadeType.ALL) // One represent Customer Many represent invoices so one customer can have many invoices in database
    private Collection<Invoice> invoices;
}
