package ru.javawebinar.topjava.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.jdbc.JdbcMealRepository;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.Assert.*;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@RunWith(SpringRunner.class)
@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class JdbcMealRepositoryTest {

    @Autowired
    private JdbcMealRepository repository;

    @Test
    public void create() {
        Meal newMeal = getNew();
        Meal created = repository.save(newMeal, USER_ID);

        assertNotNull(created);
        assertNotNull(created.getId());

        newMeal.setId(created.getId());
        assertMatch(created, newMeal);
        assertMatch(repository.get(created.getId(), USER_ID), newMeal);
    }

    @Test
    public void duplicateDateTimeCreate() {
        Meal duplicate = new Meal(null, meal1.getDateTime(), "Duplicate breakfast", 300);

        assertThrows(DataAccessException.class, () -> repository.save(duplicate, USER_ID));
    }

    @Test
    public void delete() {
        assertTrue(repository.delete(MEAL1_ID, USER_ID));
        assertNull(repository.get(MEAL1_ID, USER_ID));
    }

    @Test
    public void deleteNotFound() {
        assertFalse(repository.delete(NOT_FOUND, USER_ID));
    }

    @Test
    public void deleteNotOwn() {
        assertFalse(repository.delete(ADMIN_MEAL1_ID, USER_ID));
    }

    @Test
    public void get() {
        Meal actual = repository.get(MEAL1_ID, USER_ID);
        assertMatch(actual, meal1);
    }

    @Test
    public void getNotFound() {
        assertNull(repository.get(NOT_FOUND, USER_ID));
    }

    @Test
    public void getNotOwn() {
        assertNull(repository.get(ADMIN_MEAL1_ID, USER_ID));
    }

    @Test
    public void update() {
        Meal updated = getUpdated();
        Meal actual = repository.save(updated, USER_ID);

        assertMatch(actual, updated);
        assertMatch(repository.get(MEAL1_ID, USER_ID), updated);
    }

    @Test
    public void updateNotFound() {
        Meal updated = getUpdated();
        updated.setId(NOT_FOUND);

        assertNull(repository.save(updated, USER_ID));
    }

    @Test
    public void updateNotOwn() {
        Meal updated = new Meal(ADMIN_MEAL1_ID, adminMeal1.getDateTime(), "Hacked", 1111);

        assertNull(repository.save(updated, USER_ID));
    }

    @Test
    public void getAll() {
        assertMatch(repository.getAll(USER_ID), userMeals);
    }

    @Test
    public void getBetweenHalfOpen() {
        List<Meal> actual = repository.getBetweenHalfOpen(
                LocalDateTime.of(2020, Month.JANUARY, 30, 7, 0),
                LocalDateTime.of(2020, Month.JANUARY, 31, 11, 0),
                USER_ID
        );

        assertMatch(actual, meal5, meal4, meal3, meal2, meal1);
    }
}
