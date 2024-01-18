package ru.nn;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.nn.api.AlgorithmForGivingOutBanknotes;
import ru.nn.api.OutputService;
import ru.nn.model.ATM;
import ru.nn.model.Banknote;
import ru.nn.services.SimpleAlgorithmForGivingOutBanknotes;
import ru.nn.services.ConsoleOutputService;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ATMTest {
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final OutputService service = new ConsoleOutputService();
    private final AlgorithmForGivingOutBanknotes algorithm =
            new SimpleAlgorithmForGivingOutBanknotes();
    private ATM atm;

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStream));
        atm = new ATM(new int[]{50, 100, 200, 500, 1000, 2000, 5000}, service, algorithm);
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }

    @Test
    @DisplayName("Проверка внесения наличных")
    void acceptingBanknotesTest() {
        assertThat(atm.getCashBalance()).isEqualTo(0);

        List<Banknote> banknotes = createBanknotes();

        atm.acceptBanknotes(banknotes);

        assertThat(atm.getCashBalance()).isEqualTo(14450);

        banknotes.clear();
        banknotes.addAll(createBanknotesWithNominal(10, 1));

        atm.acceptBanknotes(banknotes);

        assertThat(outputStream.toString().trim()).isEqualTo("Банкнота не распознана.");
        assertThat(atm.getCashBalance()).isEqualTo(14450);
    }

    @Test
    @DisplayName("Проверка выдачи наличных")
    void successfulGivingOutCashTest() {
        List<Banknote> banknotes = createBanknotes();
        atm.acceptBanknotes(banknotes);

        atm.giveOutCash(1450);
        assertThat(outputStream.toString().trim()).isEqualTo("Выдача наличных в размере 1450 " +
                "банкнотами: номинал 50 - 1, номинал 200 - 2, номинал 1000 - 1");
        assertThat(atm.getCashBalance()).isEqualTo(13000);

        outputStream.reset();

        atm.giveOutCash(850);
        assertThat(outputStream.toString().trim()).isEqualTo("Невозможно выдать запрошенную сумму.");
        assertThat(atm.getCashBalance()).isEqualTo(13000);
    }

    List<Banknote> createBanknotes() {
        List<Banknote> banknotes = new ArrayList<>();
        banknotes.addAll(createBanknotesWithNominal(2000, 5));
        banknotes.addAll(createBanknotesWithNominal(1000, 3));
        banknotes.addAll(createBanknotesWithNominal(500, 1));
        banknotes.addAll(createBanknotesWithNominal(200, 2));
        banknotes.addAll(createBanknotesWithNominal(100, 5));
        banknotes.addAll(createBanknotesWithNominal(50, 1));
        return banknotes;
    }

    List<Banknote> createBanknotesWithNominal(int nominal, int count) {
        List<Banknote> banknotes = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            banknotes.add(new Banknote(nominal));
        }
        return banknotes;
    }
}
