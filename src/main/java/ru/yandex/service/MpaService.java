package ru.yandex.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.storage.MpaRepository;
import ru.yandex.exception.NotFoundException;

import ru.yandex.model.Mpa;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class MpaService {
    private final MpaRepository mpaRepository;

    public List<Mpa> getAllMpa() {
        return mpaRepository.findAll();
    }

    public Mpa getMpaById(long id) {
        return mpaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Рейтинг с id=%s не найден", id)));
    }

    public boolean existsById(long id) {
        return mpaRepository.findById(id).isPresent();
    }
}
