package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.ValidationUtil;

import java.util.Collection;

public abstract class AbstractMealController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MealService mealService;

    public Collection<Meal> getAll(int userId) {
        log.info("getAll for useId={}", userId);
        return mealService.getAll(userId);
    }

    public Meal get(int id, int userId) {
        log.info("get mealId={} for userId={}", id, userId);
        return mealService.get(id, userId);
    }

    public Meal create(Meal meal, int userId) {
        log.info("create {} for userId={}", meal, userId);
        ValidationUtil.checkIsNew(meal);
        return mealService.create(meal, userId);
    }

    public void update(Meal meal, int id, int userId) {
        log.info("update {} with mealId={} for userId={}", meal, id, userId);
        ValidationUtil.assureIdConsistent(meal, id);
        mealService.update(meal, userId);
    }

    public void delete(int id, int userId) {
        log.info("delete mealId={} for userId={}", id, userId);
        mealService.delete(id, userId);
    }
}
