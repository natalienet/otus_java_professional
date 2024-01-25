package ru.nn.dataprocessor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import ru.nn.model.Measurement;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ResourcesFileLoader implements Loader {
    private final String fileName;

    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Measurement> load() {
        try (var inputStreamReader = new InputStreamReader(Objects.requireNonNull(
                ResourcesFileLoader.class.getClassLoader().getResourceAsStream(fileName)));
             var jsonReader = new JsonReader(inputStreamReader)) {
            Gson gson = new Gson();
            Type typeOfMeasurementList = new TypeToken<ArrayList<Measurement>>() {
            }.getType();
            return gson.fromJson(jsonReader, typeOfMeasurementList);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }
}
