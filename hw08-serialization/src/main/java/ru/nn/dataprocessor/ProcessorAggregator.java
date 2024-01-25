package ru.nn.dataprocessor;

import ru.nn.model.Measurement;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProcessorAggregator implements Processor {

    @Override
    public Map<String, Double> process(List<Measurement> data) {
        return data.stream().collect(Collectors.groupingBy(Measurement::name,
                        Collectors.summingDouble(Measurement::value))).entrySet().stream()
                .sorted(Map.Entry.comparingByKey()).collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));
    }
}
