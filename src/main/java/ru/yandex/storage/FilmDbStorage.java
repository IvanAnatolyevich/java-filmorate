package ru.yandex.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.model.Film;
import ru.yandex.storage.mapper.FilmMapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

@Repository
@RequiredArgsConstructor
@Primary
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FilmMapper filmMapper;

    @Override
    public Film createFilm(Film film) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO films(name, description, releaseDate, duration, like_count, rating_id) " +
                            "VALUES(?,?,?,?,?,?);", Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, film.getName());
            ps.setObject(2, film.getDescription());
            ps.setObject(3, film.getReleaseDate());
            ps.setObject(4, film.getDuration());
            ps.setObject(5, film.getLike());
            ps.setObject(6, film.getRating());
            return ps;
        }, keyHolder);
        Long generatedId = keyHolder.getKeyAs(Long.class);
        film.setId(generatedId);

        String sqlQuery = "insert into userLikes(id_film, id_user) " +
                "values (?, ?);";

        Iterator<Long> iterator = film.getUserLikes().iterator();

        jdbcTemplate.batchUpdate(sqlQuery, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, film.getId());
                ps.setLong(2, iterator.next());
            }

            @Override
            public int getBatchSize() {
                return film.getUserLikes().size();
            }
                }
        );
        return film;

    }

    @Override
    public Collection<Film> allFilms() {
        return getFilms().values();
    }

    @Override
    public Film updateFilm(Film newFilm) {
        String sqlQuery = "update films set" +
                "name = ?, description = ?, releaseDate = ?, duration = ?, like_count = ?, rating_id = ? " +
                "where id = ?;";
        jdbcTemplate.update(sqlQuery, newFilm.getName(), newFilm.getDescription(), newFilm.getReleaseDate(),
                newFilm.getDuration(), newFilm.getLike(), newFilm.getRating(), newFilm.getId());

        jdbcTemplate.update("delete from userLikes where id = ?;", newFilm.getId());
        sqlQuery = "insert into userLikes(id_film, id_user) " +
                "values (?, ?);";

        Iterator<Long> iterator = newFilm.getUserLikes().iterator();
        jdbcTemplate.batchUpdate(sqlQuery, new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, newFilm.getId());
                        ps.setLong(2, iterator.next());
                    }

                    @Override
                    public int getBatchSize() {
                        return newFilm.getUserLikes().size();
                    }
                }
        );

        return newFilm;
    }

    @Override
    public Film deleteFilm(Film film) {
        jdbcTemplate.update("delete from films where id = ?;", film.getId());
        jdbcTemplate.update("delete from userLikes where film_id = ?;", film.getId());
        return film;
    }

    public Map<Long, Film> getFilms() {
        Map<Long, Film> allFilms = new HashMap<>();

        List<Film> films = jdbcTemplate.query("SELECT * FROM films;", filmMapper);

        for (Film film : films) {
            String sql = "SELECT * FROM userLikes WHERE film_id = ?;";

            Set<Long> userLikes = new HashSet<>();
            jdbcTemplate.query(sql, new Object[]{film.getId()}, new ResultSetExtractor<Void>() {
                @Override
                public Void extractData(ResultSet rs) throws SQLException, DataAccessException {
                    while (rs.next()) {
                        userLikes.add(rs.getLong("user_id"));
                    }
                    return null;
                }
            });

            film.setUserLikes(userLikes);

            allFilms.put(film.getId(), film);
        }

        return allFilms;
    }
}
