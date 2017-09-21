import org.javamoney.moneta.Money;
import org.junit.Test;

import javax.money.Monetary;
import javax.money.MonetaryAmount;
import java.math.BigDecimal;

import static org.junit.Assert.assertTrue;

public class BankTest {

    Bank bank = new Bank();

    @Test
    public void shouldWithdrawMoney1() {
        User user1 = new User("Krzysztof Kowlalski", BigDecimal.valueOf(10000));
        MonetaryAmount accountValueMinus150 =  user1.getAccountValue().subtract(Money.of(BigDecimal.valueOf(150), Monetary.getCurrency("PLN")));
        bank.withdrawMoney(user1, BigDecimal.valueOf(150));
        assertTrue(accountValueMinus150.equals(user1.getAccountValue()));
    }
    @Test
    public void shouldWithdrawMoney2() {
        User user1 = new User("Krzysztof Kowlalski", BigDecimal.valueOf(10000));
        bank.withdrawMoney(user1, BigDecimal.valueOf(10000));
        assertTrue(user1.getAccountValue().equals(Money.of(0, Monetary.getCurrency("PLN"))));
    }
    @Test
    public void shouldWithdrawMoney3() {
        User user1 = new User("Krzysztof Kowlalski", BigDecimal.valueOf(10000));
        bank.withdrawMoney(user1, BigDecimal.valueOf(10001));
        assertTrue(user1.getAccountValue().equals(Money.of(-1, Monetary.getCurrency("PLN"))));
    }

    @Test
    public void shouldDepositwMoney1() {
        User user1 = new User("Krzysztof Kowlalski", BigDecimal.valueOf(10000));
        bank.depositMoney(user1, BigDecimal.valueOf(150));
        assertTrue(user1.getAccountValue().equals(Money.of(10150, Monetary.getCurrency("PLN"))));
    }

    @Test
    public void shouldTransferMoney1() {
        User user1 = new User("Krzysztof Kowlalski", BigDecimal.valueOf(10000));
        User user2 = new User("Włodzimierz Nowak", BigDecimal.valueOf(10000));
        bank.transferMoney(user1, user2, BigDecimal.valueOf(150));
        assertTrue(user1.getAccountValue().equals(Money.of(9850, Monetary.getCurrency("PLN"))));
        assertTrue(user2.getAccountValue().equals(Money.of(10150, Monetary.getCurrency("PLN"))));
    }

    @Test
    public void shouldGetCredit1() {
        User user1 = new User("Krzysztof Kowlalski", BigDecimal.valueOf(10000));
        bank.getCredit(user1, BigDecimal.valueOf(1000));
        assertTrue(user1.getAccountValue().equals(Money.of(11000, Monetary.getCurrency("PLN"))));
        assertTrue(bank.getDebtorsList().containsKey(user1));
    }

    @Test
    public void shouldPayCredit1() {
        User user1 = new User("Krzysztof Kowlalski", BigDecimal.valueOf(10000));
        bank.getCredit(user1, BigDecimal.valueOf(1000));
        assertTrue(user1.getAccountValue().equals(Money.of(11000, Monetary.getCurrency("PLN"))));
        bank.payCredit(user1);
        assertTrue(user1.getAccountValue().equals(Money.of(9900, Monetary.getCurrency("PLN"))));
    }

    @Test
    public void shuldPutMoneyOnSavingAccount() {
        User user1 = new User("Krzysztof Kowlalski", BigDecimal.valueOf(10000));
        bank.putMoneyOnSavingAccount(user1, BigDecimal.valueOf(1000));
        assertTrue(user1.getAccountValue().equals(Money.of(9000, Monetary.getCurrency("PLN"))));
        assertTrue(bank.getSavingAccountList().containsKey(user1));
    }

    @Test
    public void shouldWithdrawMoneyFromSavingAccount() {
        User user1 = new User("Krzysztof Kowlalski", BigDecimal.valueOf(10000));
        bank.putMoneyOnSavingAccount(user1, BigDecimal.valueOf(1000));
        assertTrue(user1.getAccountValue().equals(Money.of(9000, Monetary.getCurrency("PLN"))));
        bank.withdrawMoneyFromSavingAccount(user1);
        assertTrue(user1.getAccountValue().equals(Money.of(10100.0, Monetary.getCurrency("PLN"))));
    }

    @Test
    public void shouldBuyAndSellEuro () {
        User user1 = new User("Krzysztof Kowlalski", BigDecimal.valueOf(10000));
        bank.buyEuroCurrency(user1, BigDecimal.valueOf(100));
        assertTrue(user1.getAccountValue().isEqualTo(Money.of(10000-(100*4.27), Monetary.getCurrency("PLN"))));
        assertTrue(user1.getEuroCurrencyAccountValue().isEqualTo(Money.of(100, Monetary.getCurrency("EUR"))));
        bank.sellEuroCurrency(user1);
        assertTrue(user1.getAccountValue().isEqualTo(Money.of(10000, Monetary.getCurrency("PLN"))));
        assertTrue(user1.getEuroCurrencyAccountValue().isEqualTo(Money.of(0, Monetary.getCurrency("EUR"))));
    }
}
