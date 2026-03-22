package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.*;

import static ru.javawebinar.topjava.util.ValidationUtil.validate;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final String DELETE_ROLES = "DELETE FROM user_role WHERE user_id=?";
    private static final String INSERT_ROLE = "INSERT INTO user_role (role, user_id) VALUES (?, ?)";
    private static final String SELECT_ROLES_BY_USER_ID = "SELECT role FROM user_role WHERE user_id=?";
    private static final String SELECT_ALL_ROLES = "SELECT user_id, role FROM user_role";

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        validate(user);
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else if (namedParameterJdbcTemplate.update("""
                   UPDATE users SET name=:name, email=:email, password=:password, 
                   registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                """, parameterSource) == 0) {
            return null;
        } else {
            jdbcTemplate.update(DELETE_ROLES, user.getId());
        }

        insertRoles(user);

        return user;
    }

    private void insertRoles(User user) {
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            return;
        }

        List<Role> roles = new ArrayList<>(user.getRoles());

        jdbcTemplate.batchUpdate(
                INSERT_ROLE,
                roles,
                roles.size(),
                (ps, role) -> {
                    ps.setString(1, role.name());
                    ps.setInt(2, user.getId());
                }
        );
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE id=?", ROW_MAPPER, id);
        User user = DataAccessUtils.singleResult(users);
        if (user != null) {
            user.setRoles(getRoles(user.getId()));
        }
        return user;
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        User user = DataAccessUtils.singleResult(users);
        if (user != null) {
            user.setRoles(getRoles(user.getId()));
        }
        return user;
    }

    private Set<Role> getRoles(int userId) {
        List<Role> roles = jdbcTemplate.query(
                SELECT_ROLES_BY_USER_ID,
                (rs, rowNum) -> Role.valueOf(rs.getString("role")),
                userId
        );
        return roles.isEmpty() ? EnumSet.noneOf(Role.class) : EnumSet.copyOf(roles);
    }

    @Override
    public List<User> getAll() {
        List<User> users = jdbcTemplate.query("SELECT * FROM users ORDER BY name, email", ROW_MAPPER);
        Map<Integer, Set<Role>> rolesMap = getAllRolesMap();

        for (User user : users) {
            user.setRoles(rolesMap.getOrDefault(user.getId(), EnumSet.noneOf(Role.class)));
        }

        return users;
    }

    private Map<Integer, Set<Role>> getAllRolesMap() {
        Map<Integer, Set<Role>> result = new HashMap<>();

        jdbcTemplate.query(SELECT_ALL_ROLES, rs -> {
            int userId = rs.getInt("user_id");
            Role role = Role.valueOf(rs.getString("role"));

            result.computeIfAbsent(userId, id -> EnumSet.noneOf(Role.class)).add(role);
        });

        return result;
    }
}
