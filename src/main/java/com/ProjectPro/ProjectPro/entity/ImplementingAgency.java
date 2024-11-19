package com.ProjectPro.ProjectPro.entity;

import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "implementing_agencies")
public class ImplementingAgency {

    // create fields
    // annotate them with column names
    // create a relationship between sub-directorate and project
    // create a relationship between sub-directorate and directorate
    // create constructors
    // create getters and setters
    // and the toString method

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @NotNull
    private int id;

    @Column(name = "name")
    private String name;


    @Column(name = "created_at")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @CreationTimestamp
    private Date createdAt;

    @Column(name = "updated_at")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @UpdateTimestamp
    private Date updatedAt;

    @Column(name = "description")
    private String description;

    @OneToOne(mappedBy = "agency")
    private Directorate directorate;

    public ImplementingAgency() {
    }

    public ImplementingAgency(String name, Date createdAt, Date updatedAt, String description) {
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Directorate getDirectory() {
        return directorate;
    }

    public void setDirectory(Directorate directorate) {
        this.directorate = directorate;
    }

    @Override
    public String toString() {
        return "ImplementingAgency{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", description='" + description + '\'' +
                '}';
    }
}
