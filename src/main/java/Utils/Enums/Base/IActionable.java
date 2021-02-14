package Utils.Enums.Base;

import Utils.Enums.AfterTestActions;
import Utils.Enums.Data.TestData.MainReferences;
import Utils.Enums.LogUtil;
import Utils.Enums.UtilsTest;
import com.mysql.jdbc.log.LogUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public interface IActionable extends MainReferences, IWaitable {

    default JavascriptExecutor js() {
        return (JavascriptExecutor) getDriver().get();
    }

    /**
     * Gets the current url.
     *
     * @return the current url
     */
    default String getCurrentUrl() {
        return getDriver().get().getCurrentUrl();
    }

    default void closeNotificationPopup() {
        By notificationFrame = By.cssSelector("iframe[name=intercom-notifications-frame]");
        if (!isInvisible(notificationFrame, 2)) {
            WebDriver driver = getDriver().get();
            driver.switchTo().frame(waitForVisible(notificationFrame));
            new Actions(driver).moveToElement(waitForPresence(By.xpath("//button[contains(.,'Clear')]"))).click().perform();
            driver.switchTo().defaultContent();

            waitForInvisible(notificationFrame);
            LogUtil.info(IActionable.class, "Notification popup is closed");
        }
    }

    default void clickSafe(By locator, long... timeout) {
        clickSafe(waitForClickable(locator, timeout));
    }

    default void clickSafe(WebElement element) {
        try {
            element.click();
        } catch (WebDriverException e) {
            try {
                closeNotificationPopup();
                scrollToElement(element);
                element.click();
            } catch (WebDriverException ex) {
                LogUtil.warn(this.getClass(), "Clicking={} with JS", element);
                javascriptClick(element);
            }
        }
    }

    default void clickAndWait(By locator, long... timeout) {
        clickSafe(locator, timeout);
    }

    default <T> T clickAndWait(By locator, Class<T> pageClass, long... timeout) {
        clickSafe(locator, timeout);
        return getPages().get(pageClass);
    }

    default void clickAndWait(WebElement element) {
        clickSafe(element);
    }

    default <T> T clickAndWait(WebElement element, Class<T> pageClass) {
        clickSafe(element);
        return getPages().get(pageClass);
    }

    /**
     * Press enter.
     */
    default void pressEnter() {
        Actions action = new Actions(getDriver().get());
        action.sendKeys(Keys.ENTER).build().perform();
    }

    default void submitInput(WebElement input) {
        input.click();
        pressEnter();
    }

    /**
     * Javascript button click.
     *
     * @param element the web element
     */
    default void javascriptClick(WebElement element) {
        js().executeScript("arguments[0].click();", element);
    }

    default void javascriptClick(By locator) {
        WebElement element = getDriver().get().findElement(locator);
        js().executeScript("arguments[0].click();", element);
    }

    default void javascriptHover(WebElement element) {
        String javascriptHover = "var evObj = document.createEvent('MouseEvents');" +
                "evObj.initMouseEvent(\"mouseover\",true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);" +
                "arguments[0].dispatchEvent(evObj);";
        js().executeScript(javascriptHover, element);
    }

    default void scrollToElement(WebElement element) {
        int elementHeight = element.getSize().getHeight();
        int i = element.getLocation().getY() - elementHeight * 4;
        js().executeScript(String.format("scroll(0, %d);", i));
    }

    default void scrollToElement(By locator) {
        scrollToElement(getDriver().get().findElement(locator));
    }

    /**
     * Sets the text.
     *
     * @param locator the locator
     * @param text    the text
     * @param seconds to wait before locator visible
     */
    default void setText(By locator, CharSequence text, long... seconds) {
        setText(waitForVisible(locator, seconds), text);
    }

    default void setText(WebElement element, CharSequence text) {
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Sets the random text.
     *
     * @param elementLocation the element location
     * @param text            the text
     */
    default void setRandomText(By elementLocation, String text) {
        setText(elementLocation, text + " " + UtilsTest.getUniqueStr(text));
    }

    default void javascriptSetText(WebElement element, String text) {
        js().executeScript("arguments[0].value='" + text + "';", element);
    }

    default String javascriptGetText(By elementLocation) {
        WebElement webl = getDriver().get().findElement(elementLocation);
        return (String) (js().executeScript("var element = arguments[0];" + "return element.textContent;", webl));
    }

    /**
     * Get inner html of element. Use if plain getText() returns empty string
     *
     * @param element WebElement
     * @return inner html
     */
    default String getInnerHTML(WebElement element) {
        String innerHTML = element.getAttribute("innerHTML");
        return StringEscapeUtils.unescapeHtml4(innerHTML); //in case of html escape characters
    }

    /**
     * Gets the text.
     *
     * @param elementLocation the element location
     * @return the text
     */
    default String getText(By elementLocation, long... seconds) {
        return waitForPresence(elementLocation, seconds).getText();
    }

    /**
     * Gets the text of element if text is empty and expectEmpty=false then get inner html of element
     * <b>try - catch checking is necessary</b>
     *
     * @param element
     * @return text, null if element not exists
     */
    default String getText(WebElement element) {
        try {
            final String text = element.getText();
            return text.isEmpty() ? getInnerHTML(element) : text;
        } catch (WebDriverException e) {
            LogUtil.warn(this.getClass(), "Not found element - " + element);
            return null;
        }
    }

    default void performWithinFrame(WebElement frame, Runnable action) {
        waitFor(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frame), 45);
        action.run();
        getDriver().get().switchTo().defaultContent();
    }

    default void performWithinFrame(String frameid, Runnable action) {
        waitFor(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameid), 45);
        action.run();
        getDriver().get().switchTo().defaultContent();
    }

    default <T> T performWithinFrame(WebElement frame, Supplier<T> action) {
        waitFor(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frame), 45);
        T t = action.get();
        getDriver().get().switchTo().defaultContent();
        return t;
    }

    default <T> T performWithinFrame(String frameid, Supplier<T> action) {
        waitFor(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameid), 45);
        T t = action.get();
        getDriver().get().switchTo().defaultContent();
        return t;
    }

    default List<WebElement> getOptions(By locator) {
        return getDriver().get().findElement(locator).findElements(By.tagName("option"));
    }

    /**
     * Select by part of visible text.
     *
     * @param locator     the locator
     * @param partialText the partial text
     */
    default void selectByPartOfVisibleText(By locator, String partialText) {
        List<WebElement> optionElements = getOptions(locator);
        boolean foundOption = false;
        for (WebElement optionElement : optionElements) {
            if (optionElement.getText().toLowerCase().contains(partialText.toLowerCase())) {
                String optionvalue = optionElement.getAttribute("value");
                new Select(getDriver().get().findElement(locator)).selectByValue(optionvalue);
                foundOption = true;
                break;
            }
        }
        Assert.assertTrue(foundOption,
                String.format("Option - '%s' not present by locator selection - '%s'", partialText,
                        locator));
    }

    default void selectFromHoverPanel(By hoverPanelLocator, String desiredText) {
        List<WebElement> allItems = waitForVisible(hoverPanelLocator).findElements(By.xpath("./a"));
        Optional<WebElement> desiredTextElement = allItems.stream().filter(element -> desiredText.equals(getText(element)))
                .findFirst();
        if (desiredTextElement.isPresent()) {
            clickSafe(desiredTextElement.get());
        } else {
            LogUtil.warn(this.getClass(), String.format("Element with %s text is not found in hover panel"));
        }
    }

    /**
     * Select by complete visible text.
     *
     * @param locator the locator
     */
    default void selectByCompleteVisibleText(By locator, String text) {
        List<WebElement> optionElements = getOptions(locator);
        boolean foundOption = false;
        for (WebElement optionElement : optionElements) {
            if (optionElement.getText().toLowerCase().equals(text.toLowerCase())) {
                String optionvalue = optionElement.getAttribute("value");
                new Select(getDriver().get().findElement(locator)).selectByValue(optionvalue);
                foundOption = true;
                break;
            }
        }
        Assert.assertTrue(foundOption,
                String.format("Option - '%s' not present by locator selection - '%s'", text,
                        locator));
    }

    /**
     * Close driver.
     */
    default void closeDriver() {
        final boolean areThereMoreTabs = getTotalTabs() > 1;
        getDriver().get().close();
        if (areThereMoreTabs) {
            switchToLastTab();
        }
    }

    /**
     * Open duplicate of current page
     *
     * @return
     */
    default void openNewTab() {
        String currentUrl = getCurrentUrl();
        js().executeScript(String.format("window.open('%s','_blank');", currentUrl));
    }

    /**
     * Switch tab.
     *
     * @param i the i
     */
    default void switchTab(int i) {
        ArrayList<String> tabs = new ArrayList<>(getDriver().get().getWindowHandles());
        getDriver().get().switchTo().window(tabs.get(i));
    }

    default void closeAndSwitchToLastTab() {
        closeDriver();
        switchToLastTab();
    }

    /**
     * Gets the current window name.
     *
     * @return the current window name
     */
    default String getCurrentWindowName() {
        return getDriver().get().getWindowHandle();
    }

    /**
     * Switch to window by name.
     *
     * @param windowName the window name
     */
    default void switchToWindowByName(String windowName) {
        getDriver().get().switchTo().window(windowName);
    }

    /**
     * Switch window and close.
     *
     * @param windowName the window name
     */
    default void switchWindowAndClose(String windowName) {
        getDriver().get().switchTo().window(windowName);
        closeDriver();
    }

    /**
     * Gets the total tabs.
     *
     * @return the total tabs
     */
    default int getTotalTabs() {
        return getDriver().get().getWindowHandles().size();
    }

    default void switchToLastTab() {
        int i = getTotalTabs();
        switchTab(i - 1);
    }

    default void repeatIfFails(Runnable action) {
        Logger Log = LogUtil.logger(this.getClass());
        try {
            action.run();
        } catch (RuntimeException | AssertionError e) {
            e.printStackTrace();
            AfterTestActions afterTestActions = new AfterTestActions();
            long count = afterTestActions.getCount();
            Log.info("Failed first condition in repeatIfFails chain - id={}", count);
            afterTestActions.captureScreenshot(String.format("failedCondition-%d", count));
            refreshPage();
            waitForPageLoad(360);
            Log.info("Run the action again");
            action.run();
        }
    }

    default <T> T repeatIfFails(Supplier<T> supplier) {
        Logger Log = LogUtil.logger(this.getClass());
        try {
            return supplier.get();
        } catch (RuntimeException | AssertionError e) {
            e.printStackTrace();
            AfterTestActions afterTestActions = new AfterTestActions();
            long count = afterTestActions.getCount();
            Log.info("Failed first condition in repeatIfFails chain - id={}", count);
            afterTestActions.captureScreenshot(String.format("failedCondition-%d", count));
            refreshPage();
            waitForPageLoad(360);
            Log.info("Run the action again");
            return supplier.get();
        }
    }

    /**
     * Refresh page.
     */
    default void refreshPage() {
        LogUtil.info(this.getClass(), "Refreshing the page");
        getDriver().get().navigate().refresh();
    }
}
