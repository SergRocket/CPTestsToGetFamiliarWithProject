package Utils.Enums;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum  Env {
    UPGRADE_CLOUD("upgarade"),
    STAGING("staging"),
    UAT("uat"),
    PRODUCTION("production"),
    DEMO("demo");
    Env(String env){this.env=env;}
    private String env;

    public static Env currentEnv(){
        Map<String, Env> convertedMap = Arrays.stream(values()).collect(Collectors.toMap(Env::toString, Function.identity()));
        return convertedMap.get(getEnv());
    }

    public static boolean isStaging(){return currentEnv() == STAGING;}
    public static boolean isDemo(){return currentEnv() == DEMO;}
    public static boolean isProduction(){ return currentEnv() == PRODUCTION;}
    public static boolean isUat(){return currentEnv() == UAT;}

    public String getEnv(){
        String uRl =
    }
}
