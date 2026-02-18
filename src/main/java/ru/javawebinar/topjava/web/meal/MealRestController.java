package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Collection;
import java.util.List;

import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController extends AbstractMealController {

    public List<MealTo> getAll() {
        int userId = authUserId();
        Collection<Meal> meals = super.getAll(userId);
        return MealsUtil.getTos(meals, MealsUtil.DEFAULT_CALORIES_PER_DAY);
    }

    public Meal get(int id) {
        return super.get(id, authUserId());
    }

    public Meal create(Meal meal) {
        return super.create(meal, authUserId());
    }

    public void update(Meal meal, int id) {
        super.update(meal, id, authUserId());
    }

    public void delete(int id) {
        super.delete(id, authUserId());
    }
}