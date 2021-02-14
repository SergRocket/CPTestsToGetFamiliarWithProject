package Utils.Enums.copsAdmin.pages;

import Utils.Enums.UtilsTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

public class AdminHeaderBlock extends BasePage implements AdminPageBlockSelectors  {
    private static Logger log = LogManager.getLogger(AdminHeaderBlock.class.getName());

    private AdminHeaderBlock(ThreadLocal<WebDriver> driver) {
        super(driver);
    }

    public static AdminHeaderBlock instance() {
        return new AdminHeaderBlock(MainReferences.getStaticDriver());
    }

    public enum Tab {
        USERS("Users"),
        MISCELLANEOUS("Miscellaneous"),
        AWS_DATA("AWS data"),
        AWS_MIGRATIONS("Aws Migrations"),
        BILLING_CYCLE("Billing Cycle"),
        AZURE("Azure"),
        RESOURCES("Resources"),
        MONTHLY_COST_AND_USAGES("Monthly Cost And Usages"),
        AWS_ACCOUNTS("AWS Accounts");

        private String tab;

        Tab(String tab) {
            this.tab = tab;
        }

        @Override
        public String toString() {
            return tab;
        }
    }

    private By getTab(Tab tab) {
        return By.xpath(String.format("//li[%s]/a", getSelectorId(tab.toString())));
    }

    private By getInnerTab(Tab tab, String tabName) {
        String tabLocator = UtilsTest.selectorToString(getTab(tab));
        return By.xpath(String.format("%s/..//li[%s]/a", tabLocator, getSelectorId(tabName)));
    }

    public void clickOnTab(Tab tab) {
        WebElement tabElement = waitForPresence(getTab(tab));
        String url = tabElement.getAttribute("href");
        clickAndWait(tabElement);
        waitUrlToBe(url);
    }

    private void waitUrlToBe(String url) {
        waitForPageLoad(20);
        waitFor(driver -> url.equals(getCurrentUrl()), 3);
    }

    /**
     * @param tab      use enum for upper tab
     * @param innerTab use inner tab name as string in any case
     */
    public void clickOnInnerTab(Tab tab, String innerTab) {
        By innerTabSelector = getInnerTab(tab, innerTab);
        String innerTabLocator = TestUtils.selectorToString(innerTabSelector);
        String url = waitForPresence(By.xpath(innerTabLocator)).getAttribute("href");
        try {
            //hover tab
            moveToElement(waitForPresence(getTab(tab), 15));
            //click inner tab
            clickAndWait(innerTabSelector);
            waitUrlToBe(url);
            log.info(String.format("Clicking tabs - %s->%s", tab, innerTab));
        } catch (WebDriverException e) {
            log.warn("The error occurs while clicking tab - " + e.getMessage());
            gotoURL(url);
        }
    }
}
