package com.weekout.backend.Model;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "plans")
public class Plan {

    @Id
    @GeneratedValue
    private UUID id;

    // NEW: Reference to the User entity for host
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "host_user_id", nullable = true)
    private User hostUser;

    // NEW: List of joined users
    @ManyToMany
    @JoinTable(
            name = "plan_joined_users",
            joinColumns = @JoinColumn(name = "plan_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> joinedUsers = new HashSet<>();

    @Column(name = "host_id", nullable = false)
    private UUID hostId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime time;

    private String meetupPoint;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ElementCollection
    @CollectionTable(name = "plan_tags", joinColumns = @JoinColumn(name = "plan_id"))
    @Column(name = "tag")
    private List<String> tags;

    private Integer maxMembers;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Visibility visibility;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EntryMode entryMode;

    // Constructors, getters, setters, etc.

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.entryMode == null) {
            this.entryMode = EntryMode.HOSTED;
        }
    }

    // Enums defined as nested or separate classes
    public enum Visibility { PUBLIC, PRIVATE, INVITE_ONLY }
    public enum EntryMode { HOSTED, JOINED }
}
