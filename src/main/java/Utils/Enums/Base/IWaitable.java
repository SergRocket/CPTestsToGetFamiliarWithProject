package Utils.Enums.Base;

import Utils.Enums.Data.TestData.MainReferences;
import Utils.Enums.LogUtil;
import Utils.Enums.UtilsTest;
import com.amazonaws.waiters.Waiter;
import org.apache.logging.log4j.Level;
import org.awaitility.core.ConditionTimeoutException;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.apache.commons.io.FileUtils.waitFor;

public interface IWaitable extends MainReferences {
    default long getTimeOut(long... seconds) {
        return seconds.length == 0 ? 10 : seconds[0];
    }

    /**
     * Fluent wait
     * <p>
     * Example to use - waitFor(driver->getCurrentUrl().contains("dashboard"),15));
     * Wait for current url to contains dashboard in 15 seconds
     * </p>
     *
     * @param seconds       to wait, default 10 seconds
     * @param funcCondition please see the syntax in the methods below
     */
    default <T extends Function<? super WebDriver, V>, V> V waitFor(T funcCondition, long... seconds) {
        return getWaiter(getTimeOut(seconds)).waitFor(funcCondition);
    }

    default void awaitFor(Callable<Boolean> condition, long timeout, TimeUnit unit) {
        Waiter.await(timeout, unit).awaitFor(condition);
    }

    default void awaitFor(Callable<Boolean> condition, long... timeout) {
        awaitFor(condition, getTimeOut(timeout), TimeUnit.SECONDS);
    }

    default boolean isAwaitedFor(Callable<Boolean> condition, long timeout, TimeUnit unit) {
        try {
            awaitFor(condition, timeout, unit);
            return true;
        } catch (ConditionTimeoutException e) {
            return false;
        }
    }

    default boolean isAwaitedFor(Callable<Boolean> condition, long... timeout) {
        return isAwaitedFor(condition, getTimeOut(timeout), TimeUnit.SECONDS);
    }

    /**
     * Check LAZY presence without timeout, common use to check child element's presence
     * For e.g. isPresent(()->someElement.findElement(By.xpath("//a")))
     *
     * @param elementSupplier
     * @return
     */
    default boolean isPresent(Supplier<WebElement> elementSupplier) {
        try {
            elementSupplier.get().getTagName();
            return true;
        } catch (WebDriverException e) {
            return false;
        }
    }

    default boolean isPresent(WebElement webElement) {
        try {
            webElement.getTagName();
            return true;
        } catch (WebDriverException e) {
            return false;
        }
    }

    default boolean isPresent(By locator, long... seconds) {
        try {
            waitForPresence(locator, seconds);
            return true;
        } catch (WebDriverException e) {
            return false;
        }
    }

    /**
     * @param locator
     * @param seconds
     * @return null if not element with such locator found
     */
    default WebElement waitForPresenceOrNull(By locator, long seconds) {
        return waitForOptionalPresence(locator, seconds).orElse(null);
    }

    default List<WebElement> waitForAllPresence(By locator, long... seconds) {
        try {
            return waitFor(ExpectedConditions.presenceOfAllElementsLocatedBy(locator), seconds);
        } catch (WebDriverException e) {
            return Collections.emptyList();
        }
    }

    default WebElement waitForPresence(By locator, long... seconds) {
        return waitFor(ExpectedConditions.presenceOfElementLocated(locator), seconds);
    }

    default Optional<WebElement> waitForOptionalPresence(By locator, long... seconds) {
        final List<WebElement> possibleElement = waitForAllPresence(locator, seconds);
        return possibleElement.isEmpty() ? Optional.empty() : Optional.of(possibleElement.get(0));
    }

    default Optional<WebElement> waitForOptionalVisibility(By locator, long... seconds) {
        return isVisible(locator, seconds) ? Optional.of(waitForVisible(locator, seconds)) : Optional.empty();
    }

    default <T> WebElement waitForVisible(T locator, long... seconds) {
        if (locator instanceof By) {
            return waitFor(ExpectedConditions.visibilityOfElementLocated((By) locator), seconds);
        } else {
            return waitFor(ExpectedConditions.visibilityOf((WebElement) locator), seconds);
        }
    }

    default <T> WebElement waitForClickable(T locator, long... seconds) {
        if (locator instanceof By) {
            return waitFor(ExpectedConditions.elementToBeClickable((By) locator), seconds);
        } else {
            return waitFor(ExpectedConditions.elementToBeClickable((WebElement) locator), seconds);
        }
    }

    default boolean waitForStale(WebElement element, long... seconds) {
        return waitFor(ExpectedConditions.stalenessOf(element), seconds);
    }

    default boolean waitForNotStale(WebElement element, long... seconds) {
        return waitFor(ExpectedConditions.not(ExpectedConditions.stalenessOf(element)), seconds);
    }

    default <T> T performActionOnRandomlyStaleElement(WebElement element, Function<WebElement, T> action) {
        T result = null;
        for (int i = 0; i < getTimeOut()*1000; i+=50) {
            try {
                result = action.apply(element);
                break;
            } catch (StaleElementReferenceException e) {
                UtilsTest.sleepInMiliSec(50);
            }
        }
        return result;
    }

    default boolean isStale(WebElement element) {
        try {
            element.isEnabled();
            return false;
        } catch (WebDriverException e) {
            return true;
        }
    }

    default void waitForNotStale(By locator, long... seconds) {
        awaitFor(() -> !isStale(getDriver().get().findElement(locator)), seconds);
    }

    default boolean isTextPresentOnPage(String text, long... seconds) {
        try {
            return waitFor(ExpectedConditions.textToBePresentInElementLocated(By.tagName("body"), text), seconds);
        } catch (TimeoutException e) {
            return false;
        }
    }

    default boolean isTextAbsentOnPage(String text, long... seconds) {
        try {
            return waitFor(ExpectedConditions.not(ExpectedConditions.textToBePresentInElementLocated(By.tagName("body"), text)),
                    seconds);
        } catch (TimeoutException e) {
            return false;
        }
    }

    default void waitForFileDownload(String fileName, long... minutes) {
        LogUtil.info(this.getClass(), "Wait for download file='{}'", fileName);
        long timeout = minutes.length > 0 ? minutes[0] : 30;
        awaitFor(() -> DataUtils.isFileExist(fileName), timeout, TimeUnit.MINUTES);
    }

    default <T> boolean isVisible(T locator, long... seconds) {
        boolean visible;
        try {
            waitForVisible(locator, seconds);
            visible = true;
        } catch (WebDriverException e) {
            visible = false;
        }
        return visible;
    }

    default boolean isClickable(By by, long... seconds) {
        boolean clickable;
        try {
            waitForClickable(by, seconds);
            clickable = true;
        } catch (WebDriverException e) {
            clickable = false;
        }
        return clickable;
    }

    default boolean isClickable(WebElement element, long... seconds) {
        boolean clickable;
        try {
            waitForClickable(element, seconds);
            clickable = true;
        } catch (WebDriverException e) {
            clickable = false;
        }
        return clickable;
    }

    default boolean waitForInvisible(By locator, long... seconds) {
        return waitFor(ExpectedConditions.invisibilityOfElementLocated(locator), seconds);
    }

    default boolean isInvisible(By locator, long... seconds) {
        try {
            return waitForInvisible(locator, seconds);
        } catch (TimeoutException e) {
            return false;
        }
    }

    /**
     * Gets the element if visible.
     *
     * @param locator
     * @return the element if visible
     */
    default WebElement waitForVisibleOrNull(By locator, long... seconds) {
        return waitForOptionalVisibility(locator, seconds).orElse(null);
    }

    /**
     * Checks if is alert present.
     *
     * @return True if JavaScript Alert is present on the page otherwise false
     */
    default boolean isAlertPresent(long... seconds) {
        try {
            waitFor(ExpectedConditions.alertIsPresent(), seconds);
            getDriver().get().switchTo().alert();
            return true;
        } catch (WebDriverException ex) {
            return false;
        }
    }

    default boolean isTitleWithText(String text, long... seconds) {
        try {
            return waitFor(ExpectedConditions.titleContains(text), seconds);
        } catch (TimeoutException ex) {
            return false;
        }
    }

    default boolean isScriptAbsent(long... seconds) {
        return isInvisible(By.cssSelector("#loader"), seconds);
    }

    default void waitForScriptReady(long... seconds) {
        long sec = seconds.length > 0 ? seconds[0] : 10;
        waitFor(driver -> isScriptAbsent(seconds), sec);
    }

    default <T> boolean areElementsPresent(final List<T> elementsLocators, long... seconds) {
        boolean result = true;
        for (T locator : elementsLocators) {
            boolean isPresent = locator instanceof By ? isPresent((By) locator, seconds) : isPresent((WebElement) locator);
            if (!isPresent) {
                result = false;
                LogUtil.logAllAround(this.getClass(), Level.ERROR, "Element with <{}> locator is not visible",
                        locator.toString());
            }
        }
        return result;
    }

    default <T> boolean areElementsVisible(final List<T> elementsLocators, long... seconds) {
        boolean result = true;
        for (T locator : elementsLocators) {
            if (!isVisible(locator, seconds)) {
                result = false;
                LogUtil.logAllAround(this.getClass(), Level.ERROR, "Element with <{}> locator is not visible",
                        locator.toString());
            }
        }
        return result;
    }

    JavascriptExecutor js();

    default boolean isPageCompleted() {
        return js().executeScript("return document.readyState").equals("complete");
    }

    default void waitForPageLoad(long... timeout) {
        waitFor(driver -> isPageCompleted(), timeout);
        waitForScriptReady(timeout);
    }

    default void waitForJQueryEnd(long... timeout) {
        waitFor(driver -> (Long)js().executeScript("return jQuery.active") == 0, timeout);
    }
}
