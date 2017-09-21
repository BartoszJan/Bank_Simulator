import org.javamoney.moneta.Money;

import javax.money.Monetary;
import javax.money.MonetaryAmount;
import java.math.BigDecimal;

public class User {

    private String name;
    private MonetaryAmount accountValue;
    private MonetaryAmount euroCurrencyAccountValue = Money.of(0, Monetary.getCurrency("EUR"));

    public User(String name, BigDecimal accountValue) {
        this.name = name;
        this.accountValue = Money.of(accountValue, Monetary.getCurrency("PLN"));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MonetaryAmount getAccountValue() {
        return accountValue;
    }

    public void setAccountValue(MonetaryAmount accountValue) {
        this.accountValue = accountValue;
    }

    public MonetaryAmount getEuroCurrencyAccountValue() {
        return euroCurrencyAccountValue;
    }

    public void setEuroCurrencyAccountValue(MonetaryAmount euroCurrencyAccountValue) {
        this.euroCurrencyAccountValue = euroCurrencyAccountValue;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", accountValue=" + accountValue +
                '}';
    }
}
