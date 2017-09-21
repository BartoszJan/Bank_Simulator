import org.javamoney.moneta.Money;

import javax.money.Monetary;
import javax.money.MonetaryAmount;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Bank {

    private Map<User, BigDecimal> debtorsList = new HashMap<User, BigDecimal>();
    private Map<User, BigDecimal> savingAccountList = new HashMap<User, BigDecimal>();

    public Map<User, BigDecimal> getDebtorsList() {
        return debtorsList;
    }

    public void setDebtorsList(Map<User, BigDecimal> debtorsList) {
        this.debtorsList = debtorsList;
    }

    public Map<User, BigDecimal> getSavingAccountList() {
        return savingAccountList;
    }

    public void setSavingAccountList(Map<User, BigDecimal> savingAccountList) {
        this.savingAccountList = savingAccountList;
    }

    public void withdrawMoney(User user, BigDecimal value) {
        user.setAccountValue(user.getAccountValue().subtract(Money.of(value, Monetary.getCurrency("PLN"))));
    }

    public void depositMoney(User user, BigDecimal value) {
        user.setAccountValue(user.getAccountValue().add(Money.of(value, Monetary.getCurrency("PLN"))));
    }

    public void transferMoney(User user1, User user2, BigDecimal value) {
        user1.setAccountValue(user1.getAccountValue().subtract(Money.of(value, Monetary.getCurrency("PLN"))));
        user2.setAccountValue(user2.getAccountValue().add(Money.of(value, Monetary.getCurrency("PLN"))));
    }

    public void getCredit(User user, BigDecimal value) {
        user.setAccountValue(user.getAccountValue().add(Money.of(value, Monetary.getCurrency("PLN"))));
        debtorsList.put(user, value);
    }

    public void payCredit(User user) {
        user.setAccountValue(user.getAccountValue().subtract(Money.of(debtorsList.get(user).multiply(BigDecimal.valueOf(1.1)), Monetary.getCurrency("PLN"))));
        debtorsList.remove(user);
    }

    public void putMoneyOnSavingAccount(User user, BigDecimal value) {
        user.setAccountValue(user.getAccountValue().subtract(Money.of(value, Monetary.getCurrency("PLN"))));
        savingAccountList.put(user, value);
    }

    public void withdrawMoneyFromSavingAccount(User user) {
        user.setAccountValue(user.getAccountValue().add(Money.of(savingAccountList.get(user).multiply(BigDecimal.valueOf(1.1)), Monetary.getCurrency("PLN"))));
        savingAccountList.remove(user);
    }

    public void buyEuroCurrency(User user, BigDecimal euroValue) {
        MonetaryAmount valueByPLN = Money.of(euroValue.multiply(BigDecimal.valueOf(4.27)), Monetary.getCurrency("PLN"));
        user.setAccountValue(user.getAccountValue().subtract(valueByPLN));
        MonetaryAmount valueByEuro = Money.of(euroValue, Monetary.getCurrency("EUR"));
        user.setEuroCurrencyAccountValue(user.getEuroCurrencyAccountValue().add( valueByEuro));
    }

    public void sellEuroCurrency(User user) {
        Double plnValue = user.getEuroCurrencyAccountValue().multiply(4.27).getNumber().doubleValue();
        MonetaryAmount valueByEuro = Money.of(BigDecimal.valueOf(plnValue), Monetary.getCurrency("PLN"));
        user.setAccountValue(user.getAccountValue().add(valueByEuro));
        user.setEuroCurrencyAccountValue(Money.of(0, "EUR"));
    }
}
