package Utils.Enums;

public enum  DatePattern {
    ADMIN_DEFAULT("MMMM dd, yyyy HH:mm"),
    REPORTS_DATE_DEFAULT("MMM dd, yyyy"),
    COST_AND_USAGE("MMMM dd, yyyy"),
    yyyy_MM_dd("yyyy-MM-dd");

    private String pattern;

    DatePattern(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public String toString() {
        return pattern;
    }
}
