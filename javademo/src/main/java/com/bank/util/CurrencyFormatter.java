package com.bank.util;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Utility class for currency formatting.
 */
public final class CurrencyFormatter {
    private static final NumberFormat USD_FORMAT = NumberFormat.getCurrencyInstance(Locale.US);
    private static final NumberFormat EUR_FORMAT = NumberFormat.getCurrencyInstance(Locale.FRANCE);
    private static final NumberFormat GBP_FORMAT = NumberFormat.getCurrencyInstance(Locale.UK);

    private CurrencyFormatter() {
        throw new AssertionError("Utility class cannot be instantiated");
    }

    public static String formatUSD(BigDecimal amount) {
        return USD_FORMAT.format(amount);
    }

    public static String formatEUR(BigDecimal amount) {
        return EUR_FORMAT.format(amount);
    }

    public static String formatGBP(BigDecimal amount) {
        return GBP_FORMAT.format(amount);
    }

    public static String format(BigDecimal amount, Locale locale) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(locale);
        return formatter.format(amount);
    }

    public static String formatWithSymbol(BigDecimal amount, String symbol) {
        return symbol + " " + String.format("%,.2f", amount);
    }
}

