package ru.javawebinar.topjava.web.meal;

import org.junit.jupiter.api.Test;
import ru.javawebinar.topjava.web.AbstractControllerTest;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.TestUtil.userAuth;
import static ru.javawebinar.topjava.UserTestData.user;

class MealUIControllerTest extends AbstractControllerTest {
    private static final String UI_URL = "/profile/meals";

    @Test
    void createWithEmptyCalories() throws Exception {
        perform(post(UI_URL)
                .with(userAuth(user))
                .param("dateTime", "2020-02-01 12:00")
                .param("description", "Valid meal")
                .param("calories", ""))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString("calories")))
                .andExpect(content().string(not(containsString("dateTime"))));
    }
}
