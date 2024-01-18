package ru.nn.model;

import ru.nn.api.AlgorithmForGivingOutBanknotes;
import ru.nn.api.OutputService;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ATM {
    private final Map<Integer, Cell> cells;
    private final OutputService outputService;
    private final AlgorithmForGivingOutBanknotes algorithm;

    public ATM(int[] nominalOfBanknotes, OutputService outputService, AlgorithmForGivingOutBanknotes algorithm) {
        this.outputService = outputService;
        this.algorithm = algorithm;
        cells = new HashMap<>();
        for (int nominal : nominalOfBanknotes) {
            cells.put(nominal, new Cell(nominal));
        }
    }

    public int getCashBalance() {
        return cells.values().stream().mapToInt(Cell::getCashBalance).sum();
    }

    public void printCashBalance() {
        outputService.printMessage(String.format("Всего наличных: %d", getCashBalance()));
    }

    public void acceptBanknotes(List<Banknote> banknotes) {
        for (Banknote banknote : banknotes) {
            if (cells.containsKey(banknote.getNominal())) {
                cells.get(banknote.getNominal()).addBanknote(banknote);
            } else {
                outputService.printMessage("Банкнота не распознана.");
            }
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
            message = "Невозможно выдать запрошенную сумму.";
        } else {
            List<String> strings = new LinkedList<>();
            for (Map.Entry<Integer, Integer> entry : banknotes.entrySet()) {
                strings.add("номинал " + entry.getKey() + " - " + entry.getValue());
            }
            message = "Выдача наличных в размере " + sum + " банкнотами: " + String.join(", ", strings);
        }
        return message;
    }
}
