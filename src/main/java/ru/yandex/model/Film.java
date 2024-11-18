package ru.yandex.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class Film {

    @NotBlank
    private String name;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(min = 1, max = 200)
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @Min(1)
    private int duration;
    private Long like;
    private Set<Long> userLikes;
    private int genre;
    private int rating;
}
