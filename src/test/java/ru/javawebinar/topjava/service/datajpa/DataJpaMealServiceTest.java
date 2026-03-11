package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.service.MealServiceTest;

import javax.persistence.EntityManagerFactory;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(Profiles.DATAJPA)
public class DataJpaMealServiceTest extends MealServiceTest {
    @Autowired
    private EntityManagerFactory entityManagerFactory;
    @Autowired
    private MealService mealService;

    @Test
    public void getWithUser() {
        Meal actual = mealService.getWithUser(MEAL1_ID, USER_ID);

        assertNotNull(actual);
        assertTrue(entityManagerFactory.getPersistenceUnitUtil().isLoaded(actual, "user"));
        MEAL_MATCHER.assertMatch(actual, meal1);
        USER_MATCHER.assertMatch(actual.getUser(), user);
    }
}
