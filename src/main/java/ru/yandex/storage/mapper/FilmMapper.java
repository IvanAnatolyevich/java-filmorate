package ru.yandex.storage.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.model.Film;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
public class FilmMapper implements RowMapper<Film> {

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        long id = rs.getLong("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        int duration = rs.getInt("duration");
        LocalDate releaseDate = rs.getObject("releaseDate", LocalDate.class);
        long like = rs.getLong("like_count");
        int rating = rs.getInt("rating_id");
        int genre = rs.getInt("genre_id");
        return Film.builder().name(name).id(id).description(description).releaseDate(releaseDate).duration(duration)
                .like(like).rating(rating).genre(genre).build();
    }

}
