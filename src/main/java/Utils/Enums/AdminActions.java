package Utils.Enums;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum  AdminActions {
    VIEW("View"),
    EDIT("Edit"),
    DELETE("Delete");

    private String operation;

    AdminActions(String operation) {
        this.operation = operation;
    }

    public static AdminActions from(String operation) {
        Map<String, AdminActions> convertedMap = Arrays.stream(values())
                .collect(Collectors.toMap(AdminActions::toString, v -> v));
        return convertedMap.get(operation);
    }

    @Override
    public String toString() {
        return operation;
    }
}
