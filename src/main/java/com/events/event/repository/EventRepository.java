package com.events.event.repository;

import com.events.event.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("SELECT e FROM Event e WHERE " +
            "FUNCTION('earth_distance', ll_to_earth(e.latitude, e.longitude), ll_to_earth(?1, ?2)) <= ?3")
    List<Event> findByLocationNear(Double latitude, Double longitude, Double radiusInMeters);
}
