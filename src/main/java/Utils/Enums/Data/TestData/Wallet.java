package Utils.Enums.Data.TestData;

import Utils.Enums.Currency;
import Utils.Enums.UtilsTest;

import java.math.BigDecimal;

public class Wallet {
    private BigDecimal credits;
    private BigDecimal awsCredits;
    private BigDecimal availableBalance;
    private Currency currency;

    public BigDecimal getCredits() {
        return credits;
    }

    public void setCredits(String credits) {
        this.credits = UtilsTest.convertToBigDecimal(credits);
    }

    public BigDecimal getAWSCredits() {
        return awsCredits;
    }

    public void setAWSCredits(String awsCredits) {
        this.awsCredits = UtilsTest.convertToBigDecimal(awsCredits);
    }

    public BigDecimal getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(String availableBalance) {
        this.availableBalance = UtilsTest.convertToBigDecimal(availableBalance);
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = Currency.from(currency);
    }
}
