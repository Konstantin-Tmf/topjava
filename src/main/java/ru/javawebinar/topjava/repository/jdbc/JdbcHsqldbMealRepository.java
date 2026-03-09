package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Repository
@Profile("hsqldb")
public class JdbcHsqldbMealRepository extends AbstractJdbcMealRepository {

    private static final RowMapper<Meal> ROW_MAPPER = (ResultSet rs, int rowNum) -> {
        Meal meal = new Meal(
                rs.getInt("id"),
                rs.getTimestamp("date_time").toLocalDateTime(),
                rs.getString("description"),
                rs.getInt("calories")
        );
        return meal;
    };

    public JdbcHsqldbMealRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(jdbcTemplate, namedParameterJdbcTemplate);
    }

    @Override
    protected Object toDbDateTime(LocalDateTime dateTime) {
        return Timestamp.valueOf(dateTime);
    }

    @Override
    protected RowMapper<Meal> rowMapper() {
        return ROW_MAPPER;
    }
}