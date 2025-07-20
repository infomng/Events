package com.events.user.entity;

import com.events.user.entity.role.Role;
import jakarta.persistence.*;
import lombok.*;

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
    @Column(name = "is_verified", nullable = false, columnDefinition = "boolean default false")
    private boolean isVerified = false;

    @Enumerated(EnumType.STRING)
    private Role role;
}

