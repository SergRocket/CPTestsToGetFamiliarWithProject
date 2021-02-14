package Utils.Enums.copsAdmin.pages;

import Utils.Enums.Data.TestData.LoginData;
import Utils.Enums.LoginUtils;
import Utils.Enums.UtilsTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class AdminLoginPage extends BasePage {
    private static Logger Log = LogManager.getLogger(AdminLoginPage.class.getName());

    /**
     * The admin email.
     */
    private By adminemailField = By.id("admin_user_email");

    /**
     * The admin password.
     */
    private By adminpasswordField = By.id("admin_user_password");

    /**
     * The admin Login button.
     */
    private By adminLoginBttn = By.name("commit");

    /**
     * Instantiates a new login page.
     *
     * @param driver the driver
     */
    public AdminLoginPage(ThreadLocal<WebDriver> driver) {
        super(driver);
        Log.info("Initializing admin login Page Objects");
    }

    public AdminLoginPage(WebDriver driver) {
        this(ThreadLocal.withInitial(() -> driver));
    }

    private AdminLoginPage openAdminUrl(String url) {
        cleanCookies();
        gotoURL(url);
        waitFor(ExpectedConditions.urlContains("admin"), 5);
        return this;
    }

    public AdminLoginPage openUrl() {
        return openAdminUrl(UtilsTest.getProperty("cloudops.cloudops.adminurl"));
    }

    /**
     * login into admin.
     */
    public AdminHomePage loginToAdmin() {
        return loginToAdmin(LoginUtils.getAdminCredentials());
    }

    public AdminHomePage loginToAdmin(LoginData loginData) {
        repeatIfFails(this::openUrl).setLogin(loginData);
        return page.get(AdminHomePage.class);
    }

    public AdminHomePage loginToAzureAdmin() {
        openAdminUrl(TestUtils.getProperty("cloudops.azure.adminurl")).setLogin(LoginUtils.getAdminAzureCredentials());
        return page.get(AdminHomePage.class);
    }

    public AdminLoginPage loginToAdminSafeInLoop() {
        String cloudOpsHost = TestUtils.extractRegex(TestUtils.getProperty("cloudops.cloudops.adminurl"), "://(.+)", 1);
        String currentHost = TestUtils.extractRegex(getCurrentUrl(), "://(.+)", 1);
        if (!cloudOpsHost.contains(currentHost)) {
            openUrl();
            if (!isTextPresentOnPage("Logout")) {
                setLogin(LoginUtils.getAdminCredentials());
            }
        }
        return this;
    }

    public AdminLoginPage setLogin(String user, String password) {
        waitForClickable(adminemailField, longWait);
        setText(adminemailField, user);
        setText(adminpasswordField, password);
        clickAndWait(adminLoginBttn);
        return this;
    }

    public AdminLoginPage setLogin(LoginData credentials) {
        return setLogin(credentials.getEmail(), credentials.getPassword());
    }
}
