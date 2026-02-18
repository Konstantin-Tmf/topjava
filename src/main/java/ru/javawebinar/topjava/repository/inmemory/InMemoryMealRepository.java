package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Map<Integer, Meal>> storage = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        int defaultUserId = 1;
        MealsUtil.meals.forEach(meal -> save(meal, defaultUserId));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal == null) {
            return null;
        }

        Map<Integer, Meal> userMeals = storage.computeIfAbsent(userId, uid -> new ConcurrentHashMap<>());

        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            userMeals.put(meal.getId(), meal);
            return meal;
        }
        return userMeals.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        Map<Integer, Meal> userMeals = storage.get(userId);
        if (userMeals == null) {
            return false;
        }
        return userMeals.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        Map<Integer, Meal> userMeals = storage.get(userId);
        if (userMeals == null) {
            return null;
        }
        return userMeals.get(id);
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        Map<Integer, Meal> userMeals = storage.get(userId);

        if (userMeals == null) {
            return List.of();
        }
        List<Meal> list = new ArrayList<>(userMeals.values());
        list.sort(Comparator.comparing(Meal::getDateTime).reversed());

        return list;
    }
}



