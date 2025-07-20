package com.events.event.entity;

import com.events.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    private String location;
    private Double latitude;
    private Double longitude;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private Integer availableTickets;
    private Double price;

    private boolean isValidated;

    @ManyToOne
    @JoinColumn(name = "organizer_id")
    private User organizer;
}
