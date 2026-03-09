package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.model.Meal;

import javax.persistence.EntityManagerFactory;

import static org.junit.Assert.*;
import static ru.javawebinar.topjava.MealTestData.MEAL1_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.UserTestData.user;

@ActiveProfiles("datajpa")
public class DataJpaMealServiceTest extends MealServiceTest {
    @Autowired
    private EntityManagerFactory entityManagerFactory;
    @Autowired
    private MealService mealService;

    @Test
    public void getWithUser() {
        Meal meal = mealService.getWithUser(MEAL1_ID, USER_ID);

        assertNotNull(meal);
        assertTrue(entityManagerFactory.getPersistenceUnitUtil().isLoaded(meal, "user"));
        assertNotNull(meal.getUser());

        assertEquals(USER_ID, meal.getUser().id());
        assertEquals(user.getEmail(), meal.getUser().getEmail());
    }
}
