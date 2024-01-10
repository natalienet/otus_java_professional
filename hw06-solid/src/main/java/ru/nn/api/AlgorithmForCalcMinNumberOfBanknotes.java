package ru.nn.api;

import ru.nn.domain.Cell;

import java.util.Map;

public interface AlgorithmForCalcMinNumberOfBanknotes {
    Map<Integer, Integer> getMinNumberOfBanknotes(int sum, Map<Integer, Integer> banknotesInCells);
}