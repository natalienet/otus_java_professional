package ru.nn.domain;

public class Banknote {
    private final int nominal;

    public Banknote(int nominal) {
        this.nominal = nominal;
    }

    public int getNominal() {
        return nominal;
    }
}
