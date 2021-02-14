package Utils.Enums;

import Utils.Enums.Data.TestData.MainReferences;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.testng.ITestResult;

import java.io.*;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class AfterTestActions implements MainReferences {
    private static Logger Log = LogManager.getLogger(AfterTestActions.class.getName());

    private static AtomicLong counter = new AtomicLong();

    public long getCount() {
        return counter.getAndAdd(1);
    }

    public void captureScreenshot(String fileName) {
        Log.info("Capturing Screenshots...");

        File screenshotSourceFile = ((TakesScreenshot) getDriver().get()).getScreenshotAs(OutputType.FILE);
        try {
            File screenshotPath = UtilsTest.getPathScreenshotFile(fileName + ".png").toFile();
            Log.info(screenshotPath);
            FileUtils.copyFile(screenshotSourceFile, screenshotPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void captureScreenshot(ITestResult result) {
        String params = result.getAttributeNames()
                .stream()
                .map(result::getAttribute)
                .map(Object::toString)
                .collect(Collectors.joining(","));
        params = params.isEmpty() ? "" : String.format("(%s)", params);

        String date = UtilsTest.dateToString(LocalDateTime.now(), "dd_MMMM_HH_mm_ss");
        String fileName = String.format("%s_%s%s-%s", result.getTestClass().getName(), result.getMethod().getMethodName(),
                params, date);
        captureScreenshot(fileName);
    }

    public void printConsoleErrors() {
        try {
            LogEntries logEntries = getDriver().get().manage().logs().get(LogType.BROWSER);
            if (logEntries != null) {
                for (LogEntry logEntry : logEntries) {
                    if (logEntry.getMessage().toLowerCase().contains("error")) {
                        Log.info("Error Messages in Console:" + logEntry.getTimestamp() + logEntry.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            Log.info("Could not generate browser console error logs");
        }
    }

    public void checkSysHealth() {
        try {
            ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "ping localhost");
            builder.redirectErrorStream(true);
            Process process = builder.start();
            boolean wait = process.waitFor(5, TimeUnit.MINUTES);
            if (!wait)
                LogUtil.logAllAround(this.getClass(), Level.ERROR, "FAILED PING LOCALHOST");

            StringBuilder sb = new StringBuilder();
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line = bufferedReader.readLine();
                while (line != null) {
                    sb.append("\n").append(line);
                    line = bufferedReader.readLine();
                }
            }
            LogUtil.logAllAround(this.getClass(), Level.ERROR, sb.toString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            LogUtil.logAllAround(this.getClass(), Level.ERROR, e.getMessage());
        }
    }

    public static void clearLogFile() {
        String file = System.getProperty("user.dir")
                + System.getProperty("file.separator") + UtilsTest.getProperty("selenium.logsFolder") + System.getProperty("file.separator") + "eaitest.log";
        FileWriter fw;
        try {
            fw = new FileWriter(file);
            PrintWriter pw = new PrintWriter(fw);
            pw.write("");
            pw.flush();
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
