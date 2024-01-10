package ru.nn;

import ru.nn.api.AlgorithmForCalcMinNumberOfBanknotes;
import ru.nn.api.OutputService;
import ru.nn.domain.ATM;
import ru.nn.domain.Banknote;
import ru.nn.services.AlgorithmForCalcMinNumberOfBanknotesImpl;
import ru.nn.services.ConsoleOutputService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class MainApp {
    public static void main(String[] args) {
        AlgorithmForCalcMinNumberOfBanknotes algorithm = new AlgorithmForCalcMinNumberOfBanknotesImpl();
        OutputService service = new ConsoleOutputService();
        ATM atm = new ATM(new ArrayList<>(Arrays.asList(5000, 2000, 1000, 500, 200, 100, 50)),
                service, algorithm);
        atm.acceptBanknotes(createBanknotes(2000, 5));
        atm.acceptBanknotes(createBanknotes(1000, 3));
        atm.acceptBanknotes(createBanknotes(500, 1));
        atm.acceptBanknotes(createBanknotes(200, 4));
        atm.acceptBanknotes(createBanknotes(100, 5));
        atm.acceptBanknotes(createBanknotes(50, 2));
        atm.printCashBalance();
        atm.giveOutCash(1450);
        atm.printCashBalance();


    }

    static List<Banknote> createBanknotes(int nominal, int count) {
        List<Banknote> banknotes = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            banknotes.add(new Banknote(nominal));
        }
        return banknotes;
    }
}
