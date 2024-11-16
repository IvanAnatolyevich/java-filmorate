package ru.yandex.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.model.User;
import ru.yandex.storage.mapper.UserMapper;

import java.sql.*;
import java.util.*;

@Repository
@RequiredArgsConstructor
@Primary
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    private final UserMapper userMapper;

    @Override
    public User createUser(User user) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO my_users(name, email, login, birthday) VALUES(?,?,?,?);",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, user.getName());
            ps.setObject(2, user.getEmail());
            ps.setObject(3, user.getLogin());
            ps.setObject(4, user.getBirthday());
            return ps;
        }, keyHolder);
        Long generatedId = keyHolder.getKeyAs(Long.class);
        user.setId(generatedId);

        String sqlQuery = "insert into friends(id_user, id_friend, id_status) " +
                "values (?, ?, ?);";

        Iterator<Long> iterator = user.getFriends().iterator();

        jdbcTemplate.batchUpdate(sqlQuery, new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, user.getId());
                        ps.setLong(2, iterator.next());
                        ps.setInt(3, user.getStatus());
                    }

                    @Override
                    public int getBatchSize() {
                        return user.getFriends().size();
                    }
                }
        );
        return user;
    }

    @Override
    public Collection<User> allUsers() {
        return jdbcTemplate.query("SELECT * FROM my_users;", userMapper);
    }

    @Override
    public User updateUser(User newUser) {
        String sqlQuery = "update my_users set" +
                "name = ?, email = ?, login = ?, birthday = ? " +
                "where id = ?;";
        jdbcTemplate.update(sqlQuery, newUser.getName(), newUser.getEmail(), newUser.getLogin(),
                newUser.getBirthday(), newUser.getId());

        jdbcTemplate.update("delete from friends where id = ?;", newUser.getId());

        String str = "insert into friends(id_user, id_friend, id_status) " +
                            "values (?, ?, ?);";

        Iterator<Long> iterator = newUser.getFriends().iterator();

        jdbcTemplate.batchUpdate(sqlQuery, new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, newUser.getId());
                        ps.setLong(2, iterator.next());
                        ps.setInt(3, newUser.getStatus());
                    }

                    @Override
                    public int getBatchSize() {
                        return newUser.getFriends().size();
                    }
                }
        );
        return newUser;
    }

    @Override
    public User deleteUser(User user) {
        String sqlQuery = "delete from ? where id = ?;";
        jdbcTemplate.update(sqlQuery, "my_users", user.getId());
        jdbcTemplate.update(sqlQuery, "friends", user.getId());
        return user;
    }

    public Map<Long, User> getMap() {
        Map<Long, User> map = new HashMap<>();
        Collection<User> obj = allUsers();
        for (User el: obj) {
            map.put(el.getId(), el);
        }
        return map;
    }
}
