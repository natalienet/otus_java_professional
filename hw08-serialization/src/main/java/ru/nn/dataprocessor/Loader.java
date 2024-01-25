package ru.nn.dataprocessor;

import ru.nn.model.Measurement;

import java.util.List;

public interface Loader {

    List<Measurement> load();
}
