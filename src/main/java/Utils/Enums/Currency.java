package Utils.Enums;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum  Currency {
    USD("USD", "$"), INR("INR", "₹");
    public static Currency from(String currency){
        Map<String, Currency> convertedMap = Arrays.stream(values()).collect(Collectors.toMap(Currency::toString, v-> v));
        return convertedMap.get(currency);
    }

    private String currency;
    private String symbol;

    Currency(String currency, String symbol){
        this.currency = currency;
        this.symbol = symbol;
    }
    @Override
    public String toString() {
        return currency;
    }

    public String symbol() {
        return symbol;
    }
}
