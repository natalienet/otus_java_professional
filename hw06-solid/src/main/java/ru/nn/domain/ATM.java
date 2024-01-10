package ru.nn.domain;

import ru.nn.api.AlgorithmForCalcMinNumberOfBanknotes;
import ru.nn.api.OutputService;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ATM {
    private final Map<Integer, Cell> cells;
    private final OutputService outputService;
    private final AlgorithmForCalcMinNumberOfBanknotes algorithm;

    public ATM(List<Integer> nominalOfBanknotes, OutputService outputService, AlgorithmForCalcMinNumberOfBanknotes algorithm) {
        this.outputService = outputService;
        this.algorithm = algorithm;
        cells = new HashMap<>();
        for (Integer nominal : nominalOfBanknotes) {
            cells.put(nominal, new Cell(nominal));
        }
    }

    public int getCashBalance() {
        return cells.values().stream().mapToInt(Cell::getCashBalance).sum();
    }

    public void printCashBalance() {
        outputService.printMessage(String.format("Cash balance: %d", getCashBalance()));
    }

    public void acceptBanknotes(List<Banknote> banknotes) {
        for (Banknote banknote : banknotes) {
            cells.get(banknote.getNominal()).addBanknote(banknote);
        }
    }

    public void giveOutCash(int sum) {
        Map<Integer, Integer> countOfBanknotesInCells = new HashMap<>();
        for (Map.Entry<Integer, Cell> entry : cells.entrySet()) {
            countOfBanknotesInCells.put(entry.getKey(), entry.getValue().getCountOfBanknotes());
        }

        var banknotes = algorithm.getMinNumberOfBanknotes(sum, countOfBanknotesInCells);

        String message = createMessageAboutIssuanceOfCash(banknotes, sum);
        outputService.printMessage(message);

        for (Map.Entry<Integer, Integer> entry : banknotes.entrySet()) {
            cells.get(entry.getKey()).takeBanknotes(entry.getValue());
        }
    }

    private String createMessageAboutIssuanceOfCash(Map<Integer, Integer> banknotes, int sum) {
        String message;
        if (banknotes.isEmpty()) {
            message = "It's not possible to issue the requested amount of cash.";
        } else {
            List<String> strings = new LinkedList<>();
            for (Map.Entry<Integer, Integer> entry : banknotes.entrySet()) {
                strings.add("nominal " + entry.getKey() + " - " + entry.getValue());
            }
            message = "Issuance of cash in the amount of " + sum + " banknotes with: " + String.join(", ", strings);
        }
        return message;
    }
}
