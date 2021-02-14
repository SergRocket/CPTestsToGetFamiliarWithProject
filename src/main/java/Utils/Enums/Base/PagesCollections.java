package Utils.Enums.Base;

import Utils.Enums.LogUtil;
import Utils.Enums.copsAdmin.pages.AdminLoginPage;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class PagesCollections {
    private Logger Log = LogManager.getLogger(PagesCollections.class.getName());

    /**
     * The driver.
     */
    protected ThreadLocal<WebDriver> driver;

    /**
     * Instantiates a new page collection.
     *
     * @param driver the driver
     */
    public PagesCollections(ThreadLocal<WebDriver> driver) {
        this.driver = driver;
    }

    /**
     * Gets the login page.
     *
     * @return the login page
     */
    public LoginPage getLoginPage() {
        return get(LoginPage.class);
    }

    public AdminLoginPage getAdminLoginPage() {
        return get(AdminLoginPage.class);
    }

    public AdminReservedInstances getAdminReservedInstances() {
        return get(AdminReservedInstances.class);
    }

    public SignUpPage getSignUpPage() {
        return get(SignUpPage.class);
    }

    public AdminUserPage getAdminUserPage() {
        return get(AdminUserPage.class);
    }

    public AdminSidekiqPage getAdminSidekiqPage() {
        return get(AdminSidekiqPage.class);
    }

    public HomePage getHomePage() {
        return get(HomePage.class);
    }

    public BillsPage getBillsPage() {
        return get(BillsPage.class);
    }

    public InvoicesPage getInvoicePage() {
        return get(InvoicesPage.class);
    }

    public DashboardPage getDashboardPage() {
        return get(DashboardPage.class);
    }

    public ImpersonateHomePage getImpersonateHomePage() {
        return get(ImpersonateHomePage.class);
    }

    public AWSLoginPage getAWSLoginPage() {
        return get(AWSLoginPage.class);
    }

    public CostExplorerPage getCostExplorerPage() {
        return get(CostExplorerPage.class);
    }

    public AWSLoginPage getAwsLoginPage() {
        return get(AWSLoginPage.class);
    }

    public AWSDashboardPage getAwsDashboardPage() {
        return get(AWSDashboardPage.class);
    }

    public AWSMyBillingDashboard getAwsMyBillingDashboardPage() {
        return get(AWSMyBillingDashboard.class);
    }

    public ReportsPage getReportsPage() {
        return get(ReportsPage.class);
    }

    public CostAllocationTagsReportsPage getCostAllocationTagsReportsPage() {
        return get(CostAllocationTagsReportsPage.class);
    }

    public CostAndUsageCSVsPage getCostAndUsageCSVsPage() {
        return get(CostAndUsageCSVsPage.class);
    }

    public AdminAcmLoginPage getAcmLoginPage() {
        return get(AdminAcmLoginPage.class);
    }

    public ACMCustomerAccountPage getACMCustomerAccountPage() {
        return get(ACMCustomerAccountPage.class);
    }

    public TransactionHistoryPage getTransactionHistoryPage() {
        return get(TransactionHistoryPage.class);
    }

    public JoinNetworkPage getJoinNetworkPage() {
        return get(JoinNetworkPage.class);
    }

    public AddResellerPage getAddResellerPage() {
        return get(AddResellerPage.class);
    }

    public AdminResellerDetailsPage getAdminResellerDetailsPage() {
        return get(AdminResellerDetailsPage.class);
    }

    public AdminPartnerPage getAdminPartnerPage() {
        return get(AdminPartnerPage.class);
    }

    public AdminDistributorDetailsPage getAdminDistributorDetailsPage() {
        return get(AdminDistributorDetailsPage.class);
    }

    public AdminBatchesPage getAdminBatchesPage() {
        return get(AdminBatchesPage.class);
    }

    public AdminBatchDetailsPage getAdminBatchDetailsPage() {
        return get(AdminBatchDetailsPage.class);
    }

    public PartnerBillsPage getPartnerBillsPage() {
        return get(PartnerBillsPage.class);
    }

    public AdminCustomerAccountPage getAdminCustomerAccountPage() {
        return get(AdminCustomerAccountPage.class);
    }

    public AdminBillingAccountsPage getAdminBillingAccountsPage() {
        return get(AdminBillingAccountsPage.class);
    }

    public UBSOnbardingPage getUBSonbardingPage() {
        return get(UBSOnbardingPage.class);

    }

    public UBSNewAccountPage getUBSNewAccountPage() {
        return get(UBSNewAccountPage.class);
    }

    public PartnerDashboardPage getPartnerDashboardPage() {
        return get(PartnerDashboardPage.class);
    }

    public ManageProfilesPage getManageProfilesPage() {
        return get(ManageProfilesPage.class);
    }

    public MyAccountPage getMyAccountPage() {
        return get(MyAccountPage.class);
    }

    public RechargePage getRechargePage() {
        return get(RechargePage.class);
    }

    public BatchManagementPage getBatchManagementPage() {
        return get(BatchManagementPage.class);
    }

    public AllocateFundsPage getAllocateFundsPage() {
        return get(AllocateFundsPage.class);
    }

    public AWSAlarmPage getAWSAlarmPage() {
        return get(AWSAlarmPage.class);
    }

    public PrepaidPartnerReportsPage getPrepaidPartnerReportsPage() {
        return get(PrepaidPartnerReportsPage.class);
    }

    public SocialMediaLoginPage getSocialMediaLoginPage(SocialMedia socialMedia) {
        SocialMediaLoginPage socialPage;
        switch (socialMedia) {
            case GOOGLE:
                socialPage = get(GoogleLoginPage.class);
                break;
            case LINKEDIN:
                socialPage = get(LinkedinLoginPage.class);
                break;
            default:
                throw new NoSuchElementException(String.format("Such media=%s, not supported yet", socialMedia));
        }
        return socialPage;
    }

    public EditPartnerPage getEditPartnerPage() {
        return get(EditPartnerPage.class);
    }

    public ManageResellersPage getManageResellersPage() {
        return get(ManageResellersPage.class);
    }

    private final Map<Integer, Object> classObjectHolder = new ConcurrentHashMap<>();

    public <T> T getNew(Class<T> pageClass) {
        T optionalPageObject;
        try {
            optionalPageObject = PageFactory.initElements(driver.get(), pageClass);
        } catch (RuntimeException e) {
            try {
                Constructor<T> constructor = pageClass.getConstructor(ThreadLocal.class);
                optionalPageObject = constructor.newInstance(driver);
            } catch (InstantiationException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e1) {
                e1.printStackTrace();
                LogUtil.logAllAround(this.getClass(), Level.ERROR, "NOT POSSIBLE initialize - " + pageClass);
                return null;
            }
        }
        return optionalPageObject;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(Class<T> pageClass) {
        int classKey = Objects.hash(pageClass, ((ChromeDriver) driver.get()).getSessionId());
        Object optionalPageObject = classObjectHolder.get(classKey);
        if (optionalPageObject == null) {
            optionalPageObject = getNew(pageClass);
            classObjectHolder.put(classKey, optionalPageObject);
        }
        return (T) classObjectHolder.get(classKey);
    }
}
