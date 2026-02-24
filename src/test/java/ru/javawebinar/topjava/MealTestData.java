package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int MEAL1_ID = START_SEQ + 3;
    public static final int MEAL2_ID = START_SEQ + 4;
    public static final int MEAL3_ID = START_SEQ + 5;
    public static final int MEAL4_ID = START_SEQ + 6;
    public static final int MEAL5_ID = START_SEQ + 7;
    public static final int MEAL6_ID = START_SEQ + 8;
    public static final int MEAL7_ID = START_SEQ + 9;

    public static final int ADMIN_MEAL1_ID = START_SEQ + 10;

    public static final int NOT_FOUND = 10;

    public static final Meal meal1 = new Meal(MEAL1_ID,
            LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0),
            "Завтрак", 500);

    public static final Meal meal2 = new Meal(MEAL2_ID,
            LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0),
            "Обед", 1000);

    public static final Meal meal3 = new Meal(MEAL3_ID,
            LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0),
            "Ужин", 500);

    public static final Meal meal4 = new Meal(MEAL4_ID,
            LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0),
            "Еда на граничное значение", 100);

    public static final Meal meal5 = new Meal(MEAL5_ID,
            LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0),
            "Завтрак", 1000);

    public static final Meal meal6 = new Meal(MEAL6_ID,
            LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0),
            "Обед", 500);

    public static final Meal meal7 = new Meal(MEAL7_ID,
            LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0),
            "Ужин", 410);

    public static final Meal adminMeal1 = new Meal(ADMIN_MEAL1_ID,
            LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0),
            "Завтрак", 800);

    public static final List<Meal> userMeals = Arrays.asList(
            meal7, meal6, meal5, meal4, meal3, meal2, meal1
    );

    public static Meal getNew() {
        return new Meal(null,
                LocalDateTime.of(2020, Month.JANUARY, 31, 7, 0),
                "Новый завтрак", 300);
    }

    public static Meal getUpdated() {
        return new Meal(MEAL1_ID,
                meal1.getDateTime(),
                "Обновленный завтрак",
                700);
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }
}
