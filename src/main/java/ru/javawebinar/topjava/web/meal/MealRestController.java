package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.ValidationUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Controller
public class MealRestController {

    private final MealService mealService;

    public MealRestController(MealService mealService) {
        this.mealService = mealService;
    }

    public List<MealTo> getAll() {
        int userId = SecurityUtil.authUserId();
        int caloriesPerDay = SecurityUtil.authUserCaloriesPerDay();
        Collection<Meal> meals = mealService.getAll(userId);
        return MealsUtil.getTos(meals, caloriesPerDay);
    }

    public List<MealTo> getFiltered(LocalDate startDate, LocalTime startTime,
                                    LocalDate endDate, LocalTime endTime) {
        int userId = SecurityUtil.authUserId();
        int caloriesPerDay = SecurityUtil.authUserCaloriesPerDay();
        List<Meal> meals = mealService.getAll(userId);

        List<Meal> byDateRage = new ArrayList<>();
        for (Meal meal : meals) {
            LocalDate date = meal.getDate();
            if (!date.isBefore(startDate) && !date.isAfter(endDate)) {
                byDateRage.add(meal);
            }
        }

        Map<LocalDate, Integer> sumByDate = new HashMap<>();
        for (Meal meal : byDateRage) {
            LocalDate date = meal.getDate();
            Integer current = sumByDate.get(date);
            sumByDate.put(date, (current == null ? 0 : current) + meal.getCalories());
        }

        List<MealTo> result = new ArrayList<>();
        for (Meal meal : byDateRage) {
            if (DateTimeUtil.isBetweenHalfOpen(meal.getTime(), startTime, endTime)) {
                boolean excess = sumByDate.get(meal.getDate()) > caloriesPerDay;
                result.add(new MealTo(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess));
            }
        }
        return result;
    }

    public Meal get(int id) {
        return mealService.get(id, SecurityUtil.authUserId());
    }

    public Meal create(Meal meal) {
        return mealService.create(meal, SecurityUtil.authUserId());
    }

    public void update(Meal meal, int id) {
        ValidationUtil.assureIdConsistent(meal, id);
        mealService.update(meal, SecurityUtil.authUserId());
    }

    public void delete(int id) {
        mealService.delete(id, SecurityUtil.authUserId());
    }
}