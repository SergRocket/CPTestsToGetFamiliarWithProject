package Utils.Enums.Data.TestData;

import Utils.Enums.ObjectUtils;
import org.apache.xmlbeans.UserType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static java.math.BigDecimal.ZERO;

public class AccountData {
    private String accountName;
    private String customerGuid;
    private BigDecimal monthUsage = ZERO;
    private String id;
    private boolean isActive;
    private String createdAt;
    private String type;
    private String state;
    private String awsType;
    private UserType userType;
    private String currency;
    private String parentAccount;
    private String iamEmail;
    private String email;
    private String batch;
    private String accountUsername;
    private MonthlyReport monthlyReport;
    private double exchangeRate;
    private double previousMonthExchangeRate;
    private double discount;
    private List<SavingsPlanAdmin> savingsPlans = new LinkedList<>();
    private List<ServiceConfig> serviceConfigs = new ArrayList<>();
    private Map<String, String> bills = new HashMap<>();
    private EC2Usage ec2Usage;
    private String customerName;
    private BillingAccountData billingAccountData;

    private UserData userData; //related user

    public AccountData() {

    }

    public static AccountData fromUsername(String accountUsername) {
        AccountData accountData = new AccountData();
        accountData.setAccountUsername(accountUsername);
        return accountData;
    }

    public static AccountData fromName(String accountName) {
        AccountData accountData = new AccountData();
        accountData.setAccountName(accountName);
        return accountData;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = UserType.from(userType);
    }

    public void setServiceType(String userType) {
        this.userType = UserType.from(userType);
    }

    public String getParentAccount() {
        return parentAccount;
    }

    public void setParentAccount(String parentAccount) {
        this.parentAccount = parentAccount;
    }

    public String getAccountUsername() {
        return accountUsername;
    }

    public void setAccountUsername(String accountUsername) {
        this.accountUsername = accountUsername;
    }

    public double getExchangeRate() {
        return exchangeRate;
    }

    /**
     * @return new BigDecimal obj
     */
    public BigDecimal getExchangeRateD() {
        return BigDecimal.valueOf(exchangeRate);
    }

    public void setExchangeRate(double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public void setExchangeRate(String exchangeRate) {
        this.exchangeRate = Double.valueOf(exchangeRate);
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public void setDiscount(String discount) {
        setDiscount(Double.parseDouble(discount));
    }

    public EC2Usage getEc2Usage() {
        return ec2Usage;
    }

    public void setEc2Usage(EC2Usage ec2Usage) {
        this.ec2Usage = ec2Usage;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Map<String, String> getBills() {
        return bills;
    }

    public Map<String, BigDecimal> getBillsForCloudOps() {
        Map<String, BigDecimal> billsForCloud = TestUtils.toBillMap(bills);
        billsForCloud.replaceAll((k, v) -> v.multiply(BigDecimal.valueOf(exchangeRate)));
        return billsForCloud;
    }

    public void setBills(Map<String, String> bills) {
        this.bills = bills;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getIamEmail() {
        return iamEmail;
    }

    public void setIamEmail(String iamEmail) {
        this.iamEmail = iamEmail;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBillUrl() {
        return monthlyReport.getZipFileUrl();
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public Cloud getCloudProvider() {
        return Cloud.fromAccountType(type);
    }

    public boolean isAWSAccount() {
        return getCloudProvider() == Cloud.AWS;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean isActivated() {
        return state.equalsIgnoreCase("activated");
    }

    public void setIsActive(boolean active) {
        isActive = active;
    }

    public void setIsActive(String active) {
        isActive = active.equalsIgnoreCase("yes");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BillingAccountData getBillingAccountData() {
        return billingAccountData;
    }

    public void setBillingAccountData(BillingAccountData billingAccountData) {
        this.billingAccountData = billingAccountData;
    }

    public List<SavingsPlanAdmin> getSavingsPlans() {
        return savingsPlans;
    }

    public void setSavingsPlans(List<SavingsPlanAdmin> savingsPlans) {
        this.savingsPlans = savingsPlans;
    }

    public void addSavingsPlan(SavingsPlanAdmin savingsPlan) {
        this.savingsPlans.add(savingsPlan);
    }

    public LocalDateTime getUpdateTimeBill() {
        return monthlyReport.getUpdatedAtTime();
    }

    public LocalDateTime getUpdateTimeBillDebug() {
        return monthlyReport.getUpdatedAtTimeDebug();
    }

    public BigDecimal getMonthUsage() {
        return monthUsage;
    }

    public void setMonthUsage(BigDecimal monthUsage) {
        this.monthUsage = monthUsage;
    }

    public boolean hasMonthUsage() {
        return TestUtils.greaterThanZero(monthUsage);
    }

    public String getBillDebugUrl() {
        return monthlyReport.getZipFileUrlDebug();
    }

    public boolean isUBS() {
        return userType == UserType.UBS;
    }

    public String getAwsType() {
        return awsType;
    }

    public void setAwsType(String awsType) {
        this.awsType = awsType;
    }

    public void setMasterAccount(String masterAccount) {
        this.accountUsername = masterAccount;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<ServiceConfig> getServiceConfigs() {
        return serviceConfigs;
    }

    public void setServiceConfigs(List<ServiceConfig> serviceConfigs) {
        this.serviceConfigs = serviceConfigs;
    }

    public double getPreviousMonthExchangeRate() {
        return previousMonthExchangeRate;
    }

    public void setPreviousMonthExchangeRate(double previousMonthExchangeRate) {
        this.previousMonthExchangeRate = previousMonthExchangeRate;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setMonthlyReport(MonthlyReport monthlyReport) {
        this.monthlyReport = monthlyReport;
    }

    public String getCustomerGuid() {
        return customerGuid;
    }

    public void setCustomerGuid(String customerGuid) {
        this.customerGuid = customerGuid;
    }

    public static class EC2Usage {

        private BigDecimal actualServicePrice;
        private BigDecimal unblendedCost;
        private BigDecimal expectedFeeCostSummed;
        private BigDecimal actualFeeCostSummed;
        private BigDecimal expectedOnDemandCostSummed;
        private BigDecimal actualOnDemandCostSummed;
        private BigDecimal actualRiCostSummed;
        private List<BillingData> calculatedPurchaseList;
        private List<ExpectedRI> expectedRIList;

        public List<ExpectedRI> getExpectedRIList() {
            return expectedRIList;
        }

        public void setExpectedRIList(List<ExpectedRI> expectedRIList) {
            this.expectedRIList = expectedRIList;
        }

        public BigDecimal getActualServicePrice() {
            return actualServicePrice;
        }

        public void setActualServicePrice(BigDecimal actualServicePrice) {
            this.actualServicePrice = actualServicePrice;
        }

        public BigDecimal getUnblendedCost() {
            return unblendedCost;
        }

        public void setUnblendedCost(BigDecimal unblendedCost) {
            this.unblendedCost = unblendedCost;
        }

        public BigDecimal getExpectedFeeCostSummed() {
            return expectedFeeCostSummed;
        }

        public void setExpectedFeeCostSummed(List<BigDecimal> expectedFeeCostList) {
            this.expectedFeeCostSummed = expectedFeeCostList.stream()
                    .reduce(BigDecimal::add)
                    .orElse(ZERO);
        }

        public BigDecimal getActualFeeCostSummed() {
            return actualFeeCostSummed;
        }

        public void setActualFeeCostSummed(List<BigDecimal> actualFeeCostList) {
            this.actualFeeCostSummed = actualFeeCostList.stream()
                    .reduce(BigDecimal::add)
                    .orElse(ZERO);
        }

        public BigDecimal getExpectedOnDemandCostSummed() {
            return expectedOnDemandCostSummed;
        }

        public void setExpectedOnDemandCostSummed(List<BigDecimal> expectedOnDemandCostList) {
            this.expectedOnDemandCostSummed = expectedOnDemandCostList.stream()
                    .reduce(BigDecimal::add)
                    .orElse(ZERO);
        }

        public BigDecimal getActualOnDemandCostSummed() {
            return actualOnDemandCostSummed;
        }

        public void setActualOnDemandCostSummed(List<BigDecimal> actualOnDemandCostList) {
            this.actualOnDemandCostSummed = actualOnDemandCostList.stream()
                    .reduce(BigDecimal::add)
                    .orElse(ZERO);
        }

        public BigDecimal getActualRiCostSummed() {
            return actualRiCostSummed;
        }

        public void setActualRiCostSummed(List<BigDecimal> actualRiCostList) {
            this.actualRiCostSummed = actualRiCostList.stream()
                    .reduce(BigDecimal::add)
                    .orElse(ZERO);
        }

        public List<BillingData> getCalculatedPurchaseList() {
            return calculatedPurchaseList;
        }

        public void setCalculatedPurchaseList(List<BillingData> calculatedPurchaseList) {
            this.calculatedPurchaseList = calculatedPurchaseList;
        }

        public BigDecimal getExpectedBill() {
            return expectedFeeCostSummed.add(expectedOnDemandCostSummed);
        }

        public BigDecimal getActualBill() {
            return actualFeeCostSummed.add(actualOnDemandCostSummed).add(actualRiCostSummed);
        }
    }

    public AccountData expandWith(AccountData accountData) {
        return ObjectUtils.expandObjectProperties(this, accountData);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountData that = (AccountData) o;
        return Objects.equals(accountName, that.getAccountName()) || Objects.equals(accountUsername, that.getAccountUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountName, accountUsername);
    }

    @Override
    public String toString() {
        return ObjectUtils.createToString(accountUsername, accountName);
    }
}
