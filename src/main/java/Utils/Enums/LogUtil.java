package Utils.Enums;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class LogUtil {

    private static final Map<Class<?>, Logger> loggerForClasses = new ConcurrentHashMap<>();
    private static final Set<Integer> UNIQUE_LOGS = new CopyOnWriteArraySet<>();

    public static Logger logger (Class<?> clas){
        loggerForClasses.putIfAbsent(clas, LogManager.getLogger(clas.getName()));
        return loggerForClasses.get(clas);
    }

    public static void log (Class<?> clas, Level level, String info, Object... args){
        logger(clas).log(level, info, args);
    }

    public static void info (Class<?> clas, String info, Object... args){log(clas, Level.INFO, info, args);}

    public static void warn(Class<?> clas, String info, Object... args){log(clas, Level.INFO, info, args);}

    public static void error(Class<?> clas, String info, Object... args){log(clas, Level.ERROR, info, args);}

   /* public static void logUnique(Class<?> clazz, Level level, String info, Object... args) {
        Logger logger = logger(clazz);
        int hash = Objects.hash(Thread.currentThread(), clazz, level, info, Arrays.toString(args));
        if (!UNIQUE_LOGS.contains(hash)) {
            logger.log(level, info, args);
            UNIQUE_LOGS.add(hash);
        }
    }*/

    public static void logAllAround(Class<?> clas, Level level, String info, Object... args){
        log(clas, level, info, args);
    }

    public static void infoAllAround(Class<?> clas, String info, Object... args){
        logAllAround(clas, Level.INFO, info, args);
    }

    public static void warnEverywhere(Class<?> clas, String info, Object... args){
        logAllAround(clas, Level.WARN, info, args);
    }


    public static void logUnique(Class<?> clazz, Level level, String info, Object... args) {
        Logger logger = logger(clazz);
        int hash = Objects.hash(Thread.currentThread(), clazz, level, info, Arrays.toString(args));
        if (!UNIQUE_LOGS.contains(hash)) {
            logger.log(level, info, args);
            UNIQUE_LOGS.add(hash);
        }
   }
}
