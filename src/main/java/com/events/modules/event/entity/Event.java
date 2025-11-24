package com.events.modules.event.entity;

import com.events.modules.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.Set;
import com.events.modules.event.enumeration.EventStatusEnum;

@Entity
@Table(name = "events")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private Boolean isPublic;
    private Boolean isFree;
    private Boolean isFreeEntry;
    private String invitationCode;

    @Column(nullable = false)
    private String location;
    private Double latitude;
    private Double longitude;

    @Column(nullable = false)
    private LocalDateTime startDate;
    @Column(nullable = false)
    private LocalDateTime endDate;

    private Integer totalTickets;
    private Integer availableTickets;
    private Double price;
    private LocalDateTime ticketSalesStartDate;
    private LocalDateTime ticketSalesEndDate;

    @Enumerated(EnumType.STRING)
    private EventStatusEnum status;

    @ManyToMany
    @JoinTable(
            name = "event_attendees",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> attendees;

    @ManyToMany
    @JoinTable(
            name = "event_staff",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> staff;

    @ManyToOne
    @JoinColumn(name = "organizer_id")
    private User organizer;
}
