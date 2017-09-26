
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.javamoney.moneta.Money;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.money.Monetary;
import javax.money.MonetaryAmount;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(DataProviderRunner.class)
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

    @DataProvider
    public static Object[][] userAndValue() {
        User user1 = new User("Krzysztof Kowlalski", BigDecimal.valueOf(10000));
        User user2 = new User("Antoni Nowak", BigDecimal.valueOf(1000));
        return new Object[][] {
                {user1, BigDecimal.valueOf(1000)},
                {user2, BigDecimal.valueOf(1000)}
        };
    }

    @Test
    @UseDataProvider("userAndValue")
    public void shouldBuyAndSellEuro (final User user, final BigDecimal value) {
        MonetaryAmount valueAccountPLNBefore = user.getAccountValue();
        MonetaryAmount valueAccountEURBefore = user.getEuroCurrencyAccountValue();
        bank.buyEuroCurrency(user, value);
        MonetaryAmount valuePLN = Money.of(value.multiply(BigDecimal.valueOf(4.27)), Monetary.getCurrency("PLN"));
        assertThat(user.getAccountValue().isEqualTo(valueAccountPLNBefore.subtract(valuePLN)));
        assertThat(user.getEuroCurrencyAccountValue().isEqualTo(valueAccountEURBefore.add(Money.of(value, Monetary.getCurrency("EUR")))));
        bank.sellEuroCurrency(user);
        assertThat(user.getAccountValue().isEqualTo(valueAccountPLNBefore));
        assertThat(user.getEuroCurrencyAccountValue().isEqualTo(Money.of(0, Monetary.getCurrency("EUR"))));
    }
}
