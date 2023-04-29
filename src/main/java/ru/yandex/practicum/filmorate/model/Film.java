package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Film {
    private int id;
    @NotBlank(message = "Название должно содержать символы помимо пробела")
    @NotEmpty(message = "Название не может быть пустым")
    @NotNull(message = "Название не может быть null")
    private String name;
    private String description;
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма не может быть меньше или равна нулю")
    private int duration;
}
