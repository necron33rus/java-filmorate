package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.validator.LaterThan;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Film {

    private Long id;

    @NotBlank(message = "Название должно содержать символы помимо пробела")
    private String name;

    @Size(max = 200, message = "Длина описания не должна быть больше 200 символов")
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @LaterThan(message = "Дата релиза раньше 28.12.1895г")
    private LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма не может быть меньше или равна нулю")
    private int duration;

    private Set<Long> likes = new HashSet<>();
}
