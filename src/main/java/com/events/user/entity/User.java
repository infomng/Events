package com.events.user.entity;

import com.events.event.entity.Event;
import com.events.user.entity.role.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullName;
    private String email;
    private String password;
    private String verificationToken;
    private String resetPasswordToken;
    @Column(name = "is_verified", nullable = false, columnDefinition = "boolean default false")
    private boolean isVerified = false;

    @OneToMany(mappedBy = "organizer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Event> organizedEvents;

    @ManyToMany(mappedBy = "attendees", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Event> eventsAttending;

    @ManyToMany(mappedBy = "staff", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Event> eventsStaffing;

    @Enumerated(EnumType.STRING)
    private Role role;
}

