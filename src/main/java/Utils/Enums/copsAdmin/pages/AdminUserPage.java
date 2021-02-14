package Utils.Enums.copsAdmin.pages;

import Utils.Enums.AfterTestActions;
import Utils.Enums.LogUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;

import java.util.function.Supplier;

public class AdminUserPage extends AdminDetailsPage implements IVisible<AdminUserPage> {
    private static Logger Log = LogManager.getLogger(AdminUserPage.class.getName());
    /**
     * The View AWS details
     */
    private By viewAWSDetailsSelector = By.xpath("//a[contains(text(), 'View AWS Details')]");
    private By viewAccountDetailsSelector = By.xpath("//a[contains(text(), 'View Account Details')]");

    private By sidekiq = By.id("sidekiq");

    private By crontab = By.linkText("Cron");

    private By creditsLocator = By.xpath("//td[@class = 'col col-credits']");

    private By userAWSName = By.xpath("//tr[@class = 'row row-account_username']/td");

    private By userAvailableBalance = By.xpath("//tr[@class = 'row row-available_balance']/td");

    private By partnerName = By.xpath("//tr[@class = 'row row-partner_name']/td");

    private By userAWSURL = By.xpath("//tr[@class = 'row row-url']/td");

    private By currency = By.xpath("//tr[@class = 'row row-currency']/td");

    private By serviceType = By.xpath("//tr[contains(@class, 'row-service_type')]/td");

    private By userAWSIAMMail = By.xpath("//td[@class = 'col col-iam_email']");
    private By userAWSIAMPass = By.xpath("//td[@class = 'col col-encrypted_iam_password']");

    /**
     * Filter By Access Key Field.
     */
    private By filterBtn = By.xpath("//input[@value='Filter']");

    private By triggeredBy = By.xpath("//td[@class = 'col col-triggered_by']");
    private By triggeredAt = By.xpath("//td[@class = 'col col-triggered_at']");

    private By billingCycle = By.id("billing_cycle");
    private By alarms = By.id("alarms");
    private By id = By.xpath("//tr[@class='row row-id']//td");

    private By miscellaneous = By.id("miscellaneous");
    private By coupons = By.id("coupons");

    private By accountPanel = By.xpath("//div[@class='panel']/*[contains(text(),'Accounts')]/..//table");

    private By sigInCountTd(String email) {
        return By.xpath("//tr[td[contains(.,'" + email + "')]]//td[contains(@class, 'col-sign_in_count')])");
    }

    private By getAlarms(String number, String user) {
        return By.xpath("//tr[contains(@id, 'alarm')][" + number + "]/td[contains(., '" + user + "')]/../td[@class = 'col col-percentage']");
    }

    private By lastUsage = By.xpath("//td[@class='col col-last_raw_usage']");

    private By totalUsage = By.xpath("//td[@class='col col-total_usage']");

    private By resultGrid = By.xpath("//tbody//tr");

    private By filterByCloudWatchArn = By.id("q_cloudwatch_arn");

    private By filterByMonth = By.id("q_month");

    private By filterByYear = By.id("q_year");

    private By filterByNotificationPercentage = By.id("q_notification_percentage");

    private By monthlyReports = By.xpath("//tr[@class='row row-monthly_reports']//a[text()='Monthly Reports']");

    private By allMonthlyReportsTable = By.xpath("//table//td[@class='col col-report']");

    /**
     * User Details table
     */
    private By nameCell = By.xpath("//h3[.='User Details']/..//tr[contains(@class, 'row-name')]/td");
    private By phoneNumberCell = By.xpath("//h3[.='User Details']/..//tr[contains(@class, 'row-phone_number')]/td");
    private By emailCell = By.xpath("//h3[.='User Details']/..//tr[contains(@class, 'row-email')]/td");
    private By createdAtCell = By.xpath("//h3[.='User Details']/..//tr[contains(@class, 'row-created_at')]/td");

    public String getUserName() {
        return getText(nameCell);
    }

    public String getUserEmail() {
        return getText(emailCell, 60);
    }

    @Override
    public boolean isPageVisible(long... seconds) {
        return isVisible(getPanelLocatorThatEqual("User Details"), seconds);
    }

    public AdminUserPage clickOnUserTab() {
        repeatIfFails(() -> AdminHeaderBlock.instance().clickOnTab(AdminHeaderBlock.Tab.USERS));
        return this;
    }
}
