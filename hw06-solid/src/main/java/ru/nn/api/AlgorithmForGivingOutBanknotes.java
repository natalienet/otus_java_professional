package ru.nn.api;

import java.util.Map;

public interface AlgorithmForGivingOutBanknotes {
    Map<Integer, Integer> getMinNumberOfBanknotes(int sum, Map<Integer, Integer> banknotesInCells);
}