package com.events.modules.user.entity;

import com.events.common.abstraction.AuditableEntity;
import com.events.modules.event.entity.Event;
import com.events.modules.user.enumeration.RoleEnum;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends AuditableEntity {

    private String fullName;
    private String email;
    private String password;
    private String verificationToken;
    private String resetPasswordToken;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean isVerified;

    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean isEnabled = true;

    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean isAccountNonExpired = true;

    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean isAccountNonLocked = true;

    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean isCredentialsNonExpired = true;

    @OneToMany(mappedBy = "organizer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Event> organizedEvents = new HashSet<>();

    @ManyToMany(mappedBy = "attendees", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Event> eventsAttending;

    @ManyToMany(mappedBy = "staff", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Event> eventsStaffing;

    @Enumerated(EnumType.STRING)
    private RoleEnum role;
}

