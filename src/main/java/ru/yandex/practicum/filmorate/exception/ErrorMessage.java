package ru.yandex.practicum.filmorate.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class ErrorMessage {

    private String message;
    private Map<String, List<String>> messageCollection;

     public ErrorMessage(Map<String, List<String>> messageCollection) {
        this.messageCollection = messageCollection;
    }
}