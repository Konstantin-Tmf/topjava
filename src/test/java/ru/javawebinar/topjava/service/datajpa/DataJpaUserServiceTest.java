package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.service.UserServiceTest;

import javax.persistence.EntityManagerFactory;
import java.util.Collections;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static ru.javawebinar.topjava.MealTestData.MEAL_MATCHER;
import static ru.javawebinar.topjava.MealTestData.meals;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(Profiles.DATAJPA)
public class DataJpaUserServiceTest extends UserServiceTest {
    @Autowired
    private EntityManagerFactory entityManagerFactory;
    @Autowired
    private UserService userService;

    @Test
    public void getWithMeals() {
        User actual = userService.getWithMeals(USER_ID);

        assertNotNull(actual);
        assertTrue(entityManagerFactory.getPersistenceUnitUtil().isLoaded(actual, "meals"));
        USER_MATCHER.assertMatch(actual, user);
        MEAL_MATCHER.assertMatch(actual.getMeals(), meals);
    }

    @Test
    public void getWithMealsForUserWithoutMeals() {
        User actual = userService.getWithMeals(GUEST_ID);

        assertNotNull(actual);
        assertTrue(entityManagerFactory.getPersistenceUnitUtil().isLoaded(actual, "meals"));
        USER_MATCHER.assertMatch(actual, guest);
        MEAL_MATCHER.assertMatch(actual.getMeals(), Collections.emptyList());
    }
}
