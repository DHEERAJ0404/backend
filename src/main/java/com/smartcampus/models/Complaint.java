package com.smartcampus.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;

@Entity
@Table(name = "complaints")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Complaint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({ "password", "complaints" })
    private User user;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.PENDING;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // Additional tracking fields
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assigned_to")
    @JsonIgnoreProperties({ "password", "complaints" })
    private User assignedTo;

    @Column(nullable = false)
    private String priority = "Medium";

    @Column(nullable = false)
    private String location = "General";

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "complaint_upvotes",
        joinColumns = @JoinColumn(name = "complaint_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonIgnoreProperties({"password", "complaints"})
    private java.util.Set<User> upvoters = new java.util.HashSet<>();

    @OneToMany(mappedBy = "complaint", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnoreProperties("complaint")
    private java.util.List<Comment> comments = new java.util.ArrayList<>();

    public enum Status {
        PENDING,
        ASSIGNED,
        IN_PROGRESS,
        RESOLVED,
        REJECTED
    }
}
