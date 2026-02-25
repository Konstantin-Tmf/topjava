package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class JdbcMealRepository implements MealRepository {

    private static final BeanPropertyRowMapper<Meal> ROW_MAPPER = BeanPropertyRowMapper.newInstance(Meal.class);

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertMeal;

    public JdbcMealRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertMeal = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("meals")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("user_id", userId)
                    .addValue("date_time", Timestamp.valueOf(meal.getDateTime()))
                    .addValue("description", meal.getDescription())
                    .addValue("calories", meal.getCalories());

            Number newId = insertMeal.executeAndReturnKey(params);
            meal.setId(newId.intValue());
            return meal;
        } else {
            int updated = jdbcTemplate.update(
                    "UPDATE meals SET date_time=?, description=?, calories=? WHERE id=? AND user_id=?",
                    meal.getDateTime(),
                    meal.getDescription(),
                    meal.getCalories(),
                    meal.getId(),
                    userId
            );
            return updated != 0 ? meal : null;
        }

    }

    @Override
    public boolean delete(int id, int userId) {
        return jdbcTemplate.update("DELETE FROM meals WHERE id=? AND user_id=?", id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        List<Meal> meals = jdbcTemplate.query("SELECT * FROM meals WHERE id=? AND user_id=?",
                ROW_MAPPER,
                id, userId
        );
        return DataAccessUtils.singleResult(meals);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return jdbcTemplate.query("SELECT * FROM meals WHERE user_id=? ORDER BY date_time DESC, id DESC", ROW_MAPPER, userId);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return jdbcTemplate.query(
                "SELECT * FROM meals WHERE date_time >=? AND date_time < ? AND user_id=? ORDER BY date_time DESC, id DESC",
                ROW_MAPPER, startDateTime, endDateTime, userId
        );
    }
}
