package ru.yandex.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.storage.GenreRepository;
import ru.yandex.exception.NotFoundException;
import ru.yandex.model.Genre;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreRepository genreRepository;


    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    public Genre getGenreById(long id) {
        return genreRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Жанр с id=%s не найден", id)));
    }

    public boolean existsById(long id) {
        return genreRepository.findById(id).isPresent();
    }
}