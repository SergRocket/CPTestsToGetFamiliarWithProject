package Utils.Enums;

import com.mysql.jdbc.log.LogUtils;
import groovy.grape.GrapeIvy;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.FormattedMessageFactory;
import org.joda.time.format.DateTimeFormat;
import org.testng.Assert;
import sun.awt.windows.ThemeReader;

import javax.swing.plaf.synth.SynthTabbedPaneUI;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;

public class UtilsTest {

    private static Logger Log =  LogManager.getLogger(UtilsTest.class.getName());

    public static final ThreadLocal<String> USER_TYPE = ThreadLocal.withInitial(() -> {
        String userType = System.getProperty("userType");
        return StringUtils.isEmpty(userType) ? "prepaid" : userType;
    });

    public static final String MONTH = StringUtils.isEmpty(System.getProperty("month")) ?
            "current" : System.getProperty("month");
    public static final String YEAR = StringUtils.isEmpty(System.getProperty("year")) ?
            "current" : System.getProperty("year");
    private static Properties properties = new Properties();
    private static  final String PROPERT_FILE_NAME;

    static {
        String env = System.getProperty("environment") == null ? "uat" : System.getProperty("environment");
        PROPERT_FILE_NAME = env + ".properties";

        try(final InputStreamReader reader = new InputStreamReader(
                UtilsTest.class.getResourceAsStream("/" + PROPERT_FILE_NAME), StandardCharsets.UTF_8
        )) {
            properties.load(reader);
        } catch (IOException e){
            Log.error("Unable to read properties file", e);
        }
     }

    public static boolean isDebug() {
        return System.getProperty("debug") != null;
    }

    public static boolean isMonthlyRun() {
        return System.getProperty("monthlyRun") != null;
    }

    public static boolean isStopFilterBeforeDataBaseUpdate() {
        return System.getProperty("stopFilterBeforeDataBaseUpdate") != null;
    }

    public static boolean isForceFilters(){
        String filter = System.getProperty("filter");
        boolean isForce = filter != null && filter.equalsIgnoreCase("yes");
        if(isForce) {
            LogUtil.warnEverywhere(UtilsTest.class, "clc,lsc,sc,,sl");
        }
        return isForce;
  }

  private static Random random = ThreadLocalRandom.current();

  public static String getPropert(String key) throws NullPointerException {
      final  String propert = properties.getProperty(key);
      if(propert != null){
          return propert;
      }
      throw  new NullPointerException(String.format("Key %s in the file %s not exists", key, PROPERT_FILE_NAME));
  }

    public static String decodeText(String text) {
        Base64.Decoder decoder = Base64.getDecoder();
        return new String(decoder.decode(text));
    }

  public static String getPropertDecoded(String key) throws NullPointerException {
      return decodeText(getPropert(key));
  }

  public static boolean isUsb(){
      return USER_TYPE.get().equalsIgnoreCase("USB");
  }

  public  static String getNamefromEmail(String email){
      if (email.contains("@")){
          return email.split("@")[0];
      }
      return email;
  }

  public static long getPropertAsong(String key){
      return Long.parseLong(getPropert(key));
  }

    public static String extractRegex(String source, String regex) {
        return extractRegex(source, regex, 0);
    }

  public static String extractNumberFromString(String word){
      return UtilsTest.extractRegex(word, "(-?\\d+((,?\\d+)*\\.?\\d+)*([Ee]-\\d+)?)").replaceAll(",", "");
  }

  public static double convertToDouble(Object object){
      String extractedNum = extractNumberFromString(String.valueOf(object));
      try {
          return Double.valueOf(extractedNum);
      } catch (Exception e){
          LogUtil.logAllAround(UtilsTest.class, Level.ERROR, "Impossible convert to double the value - " + extractedNum);
          throw new IllegalArgumentException(e);
      }
  }

    public static String removeAllSpecialChar(String str) {
        return str.replaceAll("[^a-zA-Z0-9.]", "");
    }

    public static Optional<BigDecimal> toOptionalBigDecimal(Object value){
      try {
          if(!(value instanceof BigDecimal)){
              String stringValue = String.valueOf(value);
              return Optional.of(new BigDecimal(UtilsTest.extractNumberFromString(stringValue)));
          }
          return Optional.of((BigDecimal) value);
      } catch (Exception e){
          return Optional.empty();
      }
    }

    public static BigDecimal convertToBigDecimal(Object value){
      return toOptionalBigDecimal(value).orElseThrow(() -> {
          String erro = String.format("Error with converting - '%s' to big decimal", value);
          Log.error(erro);
          return new RuntimeException(erro);
      });
    }
    public static boolean hasDigit(Object value){ return toOptionalBigDecimal(value).isPresent();}
    public static boolean isZero(Object value){ return convertToBigDecimal(value).compareTo(BigDecimal.ZERO) == 0;}
    public static boolean greaterThenZero(Object value){ return convertToBigDecimal(value).compareTo(BigDecimal.ZERO) > 0;}


    public static BigDecimal convertToBigDecimal(Object value, int roundTo){
        return convertToBigDecimal(value).setScale(roundTo, RoundingMode.HALF_UP);
    }

    public static BigDecimal convertToBigDecimal(Object value, int roundTo, RoundingMode roundingMode){
      return convertToBigDecimal(value).setScale(roundTo, roundingMode);
    }

    public static  BigDecimal convertToBigDecimalRound2(Object value){
      return convertToBigDecimal(value, 2, RoundingMode.HALF_UP);
    }

    public static BigDecimal convertToBigDecimalRound10(Object value){
      return convertToBigDecimal(value, 10, RoundingMode.HALF_UP);
    }

    public static String toUTF8(String text, Charset testChars){
      return new String(text.getBytes(testChars), StandardCharsets.UTF_8);
    }

    public static String toUTF8(String text){
      return toUTF8(text, StandardCharsets.ISO_8859_1);
    }

    public static boolean sleepInMiliSec(long timeInmilisec){
      if(timeInmilisec > 0){
          try {
              Thread.sleep(timeInmilisec);
          } catch (Exception e){
              Log.info(e.getMessage());
          }
      }
      return true;
    }

    public static boolean sleep(double sec){
      return sleepInMiliSec((long) (sec * 1000));
    }

    public static  String getUniqueStr(String stringPrefix){
      return stringPrefix + System.currentTimeMillis();
    }

    public static int getRandomInt(int upperLimit){
      return random.nextInt(upperLimit);
    }

    public static <T> T getRandomElement(Collection<T> collection){
      return (T) collection.toArray()[random.nextInt(collection.size())];
    }

    public static String dateToString(TemporalAccessor date, String pattern){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern, Locale.US);
    }

    public static String gerCurrentDate(){
      return dateToString(LocalDate.now(), "YYYY-MM-dd");
    }

    @Deprecated
    public static String parseDateFromStr(String date, String pattern){
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        try {
            Date dateObject = dateFormat.parse(date);
            dateFormat = new SimpleDateFormat("MMMM, dd, yyyy", Locale.US);
            date = dateFormat.format(dateObject);
        } catch (ParseException exept){
            Log.info("Failed during formating string to another format. Parse Exeption: " + exept.getMessage());
            }
        return  date;
        }

        public static <T extends Comparable<T>> boolean lessOrEqual(T value, T valueCompareWith){
        return value.compareTo(valueCompareWith) <= 0;
    }

    public static <T extends Comparable<T>> boolean greaterOrEqual(T value, T valueCompareWith) {
        return value.compareTo(valueCompareWith) >= 0;
    }

    public static LocalTime toLocalTime(String date, String pattern) {
        return LocalTime.parse(date, DateTimeFormatter.ofPattern(pattern, Locale.US));
    }

    public static LocalDateTime toLocalDateTime(String date, String pattern){
      return LocalDateTime.parse(date, DateTimeFormatter.ofPattern(pattern, Locale.US));
    }

    public static LocalDate toLocalDate(String date, DatePattern pattern) {
        return toLocalDate(date, pattern.toString());
    }

    public static LocalDate toLocalDate(String date, String pattern) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(pattern, Locale.US));
    }

    public static String generateCurrentDate(String pattern){
      SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
      Date date = new Date();
      return dateFormat.format(date);
    }

    public static String generateNextDate(String pattern){
      SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
      Date date = new Date();
      date = DateUtils.addDays(date, 1);
      return dateFormat.format(date);
    }
    public static String defaultIfEmpty(String text, String defaultText) {
        return isEmpty(text) ? defaultText : text;
    }
    public static boolean isEmpty(String text) {
        return StringUtils.isEmpty(text) || text.equalsIgnoreCase("empty");
    }

    public static String getSubString(String completeString, String startingChar, String endchar){
      String resut = completeString.split(startingChar)[1].split(endchar)[0].replaceAll("[\">]", "");
        Log.info("Confirmation token is: " + resut);
        return resut;
    }

    public static String getStringUpToChar(String str, int n){
      String subStr = str.substring(0, Math.min(str.length(), n));
      Log.info("Stripped string is: " + subStr);
      return subStr;
    }

    public static String formatMessage(String message, Object... args) {
        return new FormattedMessageFactory().newMessage(message, args).getFormattedMessage();
    }

    public static boolean nonBlank(String obj) {
        return !obj.isEmpty();
    }

    public static <T> T require(T object, Predicate<T> condition, String message, Object... args) {
        if (!condition.test(object)) {
            Assert.fail(UtilsTest.formatMessage(message, args));
        }
        return object;
    }

    public static Set<String> getUsers(){
      Set<String> users = new HashSet<>();
      final String possiblUsers = System.getProperty("users");
      if(!StringUtils.isEmpty(possiblUsers)){
          users = Arrays.stream(possiblUsers.split(","))
                  .map(user -> user.equalsIgnoreCase("default") ? )
      }
    }

}
