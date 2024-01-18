package ru.nn.model;

import java.util.ArrayList;
import java.util.List;

public class Cell {
    private final int nominal;
    private final List<Banknote> banknotes;

    public Cell(int nominal) {
        this.nominal = nominal;
        this.banknotes = new ArrayList<>();
    }

    public List<Banknote> getBanknotes() {
        return banknotes;
    }

    public void addBanknote(Banknote banknote) {
        banknotes.add(banknote);
    }

    public void takeBanknotes(int count) {
        if (banknotes.size() < count) {
            throw new ArrayIndexOutOfBoundsException();
        }

        banknotes.subList(0, count).clear();
    }

    public void takeBanknote() {
        if (banknotes.isEmpty()) {
            throw new ArrayIndexOutOfBoundsException();
        }

        banknotes.removeLast();
    }

    public int getCashBalance() {
        return nominal * banknotes.size();
    }

    public int getCountOfBanknotes() {
        return banknotes.size();
    }
}
