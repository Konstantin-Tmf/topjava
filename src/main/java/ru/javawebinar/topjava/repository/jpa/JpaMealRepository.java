package ru.javawebinar.topjava.repository.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JpaMealRepository implements MealRepository {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    @Override
    public Meal save(Meal meal, int userId) {
        meal.setUser(em.getReference(User.class, userId));

        if (meal.isNew()) {
            em.persist(meal);
            return meal;
        }
        return get(meal.getId(), userId) == null ? null : em.merge(meal);
    }

    @Transactional
    @Override
    public boolean delete(int id, int userId) {
        int updated = em.createQuery(
                        "DELETE FROM Meal m WHERE m.id=:id AND m.user.id=:userId")
                .setParameter("id", id)
                .setParameter("userId", userId)
                .executeUpdate();
        return updated != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        List<Meal> meals = em.createQuery(
                        "SELECT m FROM Meal m WHERE m.id=:id AND m.user.id=:userId", Meal.class)
                .setParameter("id", id)
                .setParameter("userId", userId)
                .getResultList();

        return meals.isEmpty() ? null : meals.get(0);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return em.createQuery(
                        "SELECT m FROM Meal m WHERE m.user.id=:userId ORDER BY m.dateTime DESC", Meal.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return em.createQuery("SELECT m FROM Meal m " +
                                "WHERE m.user.id=:userId AND m.dateTime>=:start AND m.dateTime<:end " +
                                "ORDER BY m.dateTime DESC",
                        Meal.class)
                .setParameter("userId", userId)
                .setParameter("start", startDateTime)
                .setParameter("end", endDateTime)
                .getResultList();
    }
}