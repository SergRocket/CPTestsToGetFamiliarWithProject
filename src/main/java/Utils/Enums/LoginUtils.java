package Utils.Enums;

import Utils.Enums.Base.PagesCollections;
import Utils.Enums.Data.TestData.LoginData;
import Utils.Enums.Data.TestData.MainReferences;
import Utils.Enums.Data.TestData.UserData;
import Utils.Enums.copsAdmin.pages.AdminUserPage;
import com.mysql.jdbc.log.LogUtils;
import org.apache.logging.log4j.Level;
import org.apache.xmlbeans.UserType;
import Utils.Enums.Currency;

public class LoginUtils {
    public static final String DEFAULT_PASSWORD = "password";
    public static String getEmailToLogin(){return getCloudOpsCredentials().getEmail();}
    public static String getEmailToLogin(Job job) {
        return getCloudOpsCredentials(job).getEmail();
    }
    public static String getPasswordToLogin() {
        return getCloudOpsCredentials().getPassword();
    }

    public enum Job{
        NONE, REPORT, MANAGE_COST_AND_CAPACITY
    }

    public static LoginData getCloudOpsCredentials(){return getCloudOpsCredentials(Job.NONE);}

    public static LoginData getCloudOpsCredentials(Currency currency){
        return getCloudOpsCredentials(Job.NONE, UtilsTest.USER_TYPE.get(), currency);
    }

    public static LoginData getCloudOpsCredentials(Job job){
        return getCloudOpsCredentials(job, UtilsTest.USER_TYPE.get(), Currency.INR);
    }

    public static LoginData getCloudOpsCredentials(Job job, UserType userType, Currency currency){
        return getCloudOpsCredentials(job, userType.toString(), Currency.INR);
    }

    public static LoginData getCloudOpsCredentials(Job job, String userType, Currency currency) {
        LogUtil.logUnique(LoginUtils.class, Level.INFO, "USER TYPE - " + userType);
        userType = userType.toLowerCase();
        String userJob = "";
        if (job != Job.NONE)
            userJob = job.name().toLowerCase().replace("_", "") + ".";
        String userCurrency = "";
        if (currency != Currency.INR)
            userCurrency = currency.name().toLowerCase() + ".";
        String email = UtilsTest.getPropertDecoded(String.format("cloudops.%s.%s%susername", userType, userCurrency,
                userJob));
        String password = UtilsTest.getPropertDecoded(String.format("cloudops.%s.%s%spassword", userType, userCurrency,
                userJob));
        return new UserData(email, password);
    }

    public static LoginData getAdminAzureCreds(){
        String adminUsername = UtilsTest.decodeText(UtilsTest.getPropert("cloudops.azure.adminusername"));
        String adminPassword = UtilsTest.decodeText(UtilsTest.getPropert("cloudops.azure.adminpassword"));
        return new UserData(adminUsername, adminPassword);
    }

    public static LoginData getAdminCreds(){
        String adminUsername = UtilsTest.decodeText(UtilsTest.getPropert("cloudops.cloudops.adminusername"));
        String adminPassword = UtilsTest.decodeText(UtilsTest.getPropert("cloudops.cloudops.adminpassword"));
        return new UserData(adminUsername, adminPassword);
    }

    public static LoginData getCloudAzureCreds(){
        String userType = UtilsTest.isUsb() ? "usb" : "predaid";
        String usernamePropertyKey = String.format("cloudops.azure.%s.username", userType);
        return new UserData(UtilsTest.decodeText(UtilsTest.getPropert(usernamePropertyKey)));
    }

    public static LoginData getCloudOpsProdCreds(){
        return new UserData(UtilsTest.getPropertDecoded("cloudops.cloudops.adminusernameFull"),
        UtilsTest.getPropertDecoded("cloudops.prod.password"));
        }

    public static LoginData getFullPriviligeAdminCreds(){
        if(Env.isProduction()){
            String email = UtilsTest.getPropertDecoded("cloudops.cloudops.adminusernameFull");
            String password = UtilsTest.getPropertDecoded("cloudops.cloudops.adminprodpasswordFull");
            return new UserData(email, password);
        } else {
            return getAdminCreds();
        }
    }

    public  static LoginData getAdminAAACreds(){
        String email = UtilsTest.getPropertDecoded("cloudops.cloudops.adminusernameAAA");
        String password = UtilsTest.getPropertDecoded("cloudops.cloudops.adminpasswordAAA");
        return new UserData(email, password);
    }

    public static String resolveEmail(String userIdentif){
        PagesCollections pages = MainReferences.getStaticPages();
        if(!userIdentif.contains("@")){
            pages.getAdminLoginPage().loginToAdmin();
            AdminUserPage adminUserPage = pages.getAdminUserPage().clickOnuserTab().searchUserToViewEditDelete(userIdentif, AdminActions.VIEW);
            userIdentif = adminUserPage.getUserEmail();
        }
        return userIdentif;
    }

}
