import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {

        Bank bank = new Bank();
        User user1 = new User("Kowlaski", BigDecimal.valueOf(10000));
        bank.getCredit(user1,BigDecimal.valueOf(10000));
        User user2 = new User("Nowak", BigDecimal.valueOf(10000));
        bank.getCredit(user2,BigDecimal.valueOf(10000));
        System.out.println(bank.getDebtorsList().toString());
        bank.payCredit(user1);
        System.out.println(bank.getDebtorsList().toString());

        User user3 = new User("Wiśniewski", BigDecimal.valueOf(10000));
        bank.buyEuroCurrency(user3, BigDecimal.valueOf(100));
        System.out.println(user3);
        System.out.println(user3.getEuroCurrencyAccountValue());
        bank.sellEuroCurrency(user3);
        System.out.println(user3);
        System.out.println(user3.getEuroCurrencyAccountValue());
    }
}
