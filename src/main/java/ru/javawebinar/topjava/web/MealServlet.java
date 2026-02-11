package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MealServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<Meal> meals = MealsUtil.meals;
        int caloriesPerDay = MealsUtil.CALORIES_PER_DAY;

        Map<LocalDate, Integer> sumByDate = new HashMap<>();
        for (Meal meal : meals) {
            LocalDate date = meal.getDateTime().toLocalDate();
            sumByDate.put(date, sumByDate.getOrDefault(date, 0) + meal.getCalories());

        }

        List<MealTo> mealTo = new ArrayList<>();

        for (Meal meal : meals) {
            int daySum = sumByDate.getOrDefault(meal.getDateTime().toLocalDate(), 0);
            boolean excess = daySum > caloriesPerDay;

            mealTo.add(new MealTo(meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess));
        }

        request.setAttribute("meals", mealTo);
        request.getRequestDispatcher("/meals.jsp").forward(request, response);

//        request.getRequestDispatcher("/users.jsp").forward(request, response);
    }
}
