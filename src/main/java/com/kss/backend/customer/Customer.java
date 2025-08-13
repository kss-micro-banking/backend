package com.kss.backend.customer;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.kss.backend.branch.Branch;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

/**
 * Customer
 */
@Entity
public class Customer {

    @Embeddable
    public record CustomerNextOfKin(String name, String phoneNubmer, String email) {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private String phoneNumber;

    private String email;

    private String location;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "nokName")),
            @AttributeOverride(name = "phoneNumber", column = @Column(name = "nokPhoneNumber")),
            @AttributeOverride(name = "email", column = @Column(name = "nokEmail")),
    })
    private CustomerNextOfKin nextOfKin;

    @ManyToOne
    private Branch branch;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Customer() {
    }

    public Customer(
            String name, String phoneNumber,
            String email, String location,
            CustomerNextOfKin nextOfKin,
            Branch branch) {

        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.location = location;
        this.nextOfKin = nextOfKin;
        this.branch = branch;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public CustomerNextOfKin getNextOfKin() {
        return nextOfKin;
    }

    public void setNextOfKin(CustomerNextOfKin nextOfKin) {
        this.nextOfKin = nextOfKin;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

}
