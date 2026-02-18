package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.Collection;

@Service
public class MealService {

    private final MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal get(int id, int userId) {
        Meal meal = repository.get(id, userId);
        if (meal == null) {
            throw new NotFoundException("Meal not found id=" + id);
        }
        return meal;
    }

    public Collection<Meal> getAll(int userId) {
        return repository.getAll(userId);
    }

    public Meal create(Meal meal, int userId) {
        return repository.save(meal, userId);
    }

    public void update(Meal meal, int userId) {
        Meal updated = repository.save(meal, userId);
        if (updated == null) {
            throw new NotFoundException("Meal not found id=" + meal.getId() + ", userId=" + userId);
        }
    }

    public void delete(int id, int userId) {
        boolean deleted = repository.delete(id, userId);
        if (!deleted) {
            throw new NotFoundException("Meal not found id=" + id + ", userId=" + userId);
        }
    }
}