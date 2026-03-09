package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CrudMealRepository extends JpaRepository<Meal, Integer> {
    @Modifying
    @Transactional
    @Query("DELETE FROM Meal m WHERE m.id = :id AND m.user.id = :userId")
    int deleteByIdAndUserId(@Param("id") int id, @Param("userId") int userId);

    Optional<Meal> findByIdAndUserId(int id, int userId);

    List<Meal> findAllByUserIdOrderByDateTimeDesc(int userId);

    @Query("SELECT m FROM Meal m WHERE m.user.id = :userId " +
            "AND m.dateTime >= :startDateTime AND m.dateTime < :endDateTime " +
            "ORDER BY m.dateTime DESC")
    List<Meal> getBetweenHalfOpen(@Param("startDateTime") LocalDateTime startDateTime,
                                  @Param("endDateTime") LocalDateTime endDateTime,
                                  @Param("userId") int userId);

    @Query("select m from Meal m join fetch m.user where m.id = :id and m.user.id = :userId")
    Optional<Meal> getWithUser(@Param("id") int id, @Param("userId") int userId);
}
