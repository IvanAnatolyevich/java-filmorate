package ru.yandex.storage;

import ru.yandex.model.Film;

import java.util.Collection;
import java.util.Map;

public interface FilmStorage {

    Film createFilm(Film film);

    Collection<Film> allFilms();

    Film updateFilm(Film newFilm);

    Film deleteFilm(Film film);

    Map<Long, Film> getFilms();
}
