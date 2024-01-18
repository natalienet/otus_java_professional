package ru.nn.services;

import ru.nn.api.AlgorithmForGivingOutBanknotes;

import java.util.*;

public class SimpleAlgorithmForGivingOutBanknotes implements AlgorithmForGivingOutBanknotes {
    public Map<Integer, Integer> getMinNumberOfBanknotes(int sum, Map<Integer, Integer> banknotesInCells) {

        NavigableMap<Integer, Integer> numberOfBanknotes = new TreeMap<>();
        NavigableMap<Integer, Integer> descendingOrderedBanknotes =
                new TreeMap<>(banknotesInCells).descendingMap();

        int countOfBanknotes;
        int remains = sum;
        int nominal;
        for (Map.Entry<Integer, Integer> entry : descendingOrderedBanknotes.entrySet()) {
            nominal = entry.getKey();
            if (entry.getValue() > 0 && remains >= nominal) {
                countOfBanknotes = remains / nominal;
                numberOfBanknotes.put(nominal, countOfBanknotes);
                remains -= countOfBanknotes * nominal;
            }
            if (remains == 0) {
                break;
            }
        }

        if (remains != 0) {
            numberOfBanknotes.clear();
        }

        return numberOfBanknotes;
    }
}
