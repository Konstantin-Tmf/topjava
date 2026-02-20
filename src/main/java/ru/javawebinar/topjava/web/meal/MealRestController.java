package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.ValidationUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

@Controller
public class MealRestController {
    private final Logger log = LoggerFactory.getLogger(MealRestController.class);

    private final MealService mealService;

    public MealRestController(MealService mealService) {
        this.mealService = mealService;
    }

    public List<MealTo> getAll() {
        int userId = SecurityUtil.authUserId();
        int caloriesPerDay = SecurityUtil.authUserCaloriesPerDay();
        log.info("getAll for userId={}", userId);

        Collection<Meal> meals = mealService.getAll(userId);
        return MealsUtil.getTos(meals, caloriesPerDay);
    }

    public List<MealTo> getFiltered(LocalDate startDate, LocalTime startTime,
                                    LocalDate endDate, LocalTime endTime) {
        int userId = SecurityUtil.authUserId();
        int caloriesPerDay = SecurityUtil.authUserCaloriesPerDay();

        log.info("getFiltered for userId={}, startDate={}, startTime={}, endDate={}, endTime={}",
                userId, startDate, startTime, endDate, endTime);

        List<Meal> meals = mealService.getBetween(startDate, endDate, userId);

        return MealsUtil.getFilteredTos(meals, caloriesPerDay, startTime, endTime);
    }

    public Meal get(int id) {
        int userId = SecurityUtil.authUserId();
        log.info("get id={} for userId={}", id, userId);
        return mealService.get(id, SecurityUtil.authUserId());
    }

    public Meal create(Meal meal) {
        int userId = SecurityUtil.authUserId();
        log.info("create {} for userId={}", meal, userId);
        ValidationUtil.checkIsNew(meal);
        return mealService.create(meal, userId);
    }

    public void update(Meal meal, int id) {
        int userId = SecurityUtil.authUserId();
        log.info("update {} with id={} for userId={}", meal, id, userId);
        ValidationUtil.assureIdConsistent(meal, id);
        mealService.update(meal, userId);
    }

    public void delete(int id) {
        int userId = SecurityUtil.authUserId();
        log.info("delete id={} for userId={}", id, userId);
        mealService.delete(id, userId);
    }
}