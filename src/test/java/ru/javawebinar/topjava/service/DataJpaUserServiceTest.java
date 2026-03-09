package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.model.User;

import javax.persistence.EntityManagerFactory;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ActiveProfiles("datajpa")
public class DataJpaUserServiceTest extends UserServiceTest {
    @Autowired
    private EntityManagerFactory entityManagerFactory;
    @Autowired
    private UserService userService;

    @Test
    public void getWithMeals() {
        User user = userService.getWithMeals(USER_ID);

        assertNotNull(user);
        assertTrue(entityManagerFactory.getPersistenceUnitUtil().isLoaded(user, "meals"));

        assertEquals(meals.size(), user.getMeals().size());

        Set<Integer> mealIds = user.getMeals().stream().map(m -> m.id()).collect(Collectors.toSet());
        Set<String> descriptions = user.getMeals().stream().map(m -> m.getDescription()).collect(Collectors.toSet());

        assertTrue(mealIds.contains(meal1.id()));
        assertTrue(mealIds.contains(meal3.id()));
        assertTrue(descriptions.contains(meal1.getDescription()));
        assertTrue(descriptions.contains(meal3.getDescription()));
    }
}
