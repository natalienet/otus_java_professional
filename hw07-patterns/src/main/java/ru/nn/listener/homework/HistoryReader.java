package ru.nn.listener.homework;

import java.util.Optional;
import ru.nn.model.Message;

public interface HistoryReader {

    Optional<Message> findMessageById(long id);
}
