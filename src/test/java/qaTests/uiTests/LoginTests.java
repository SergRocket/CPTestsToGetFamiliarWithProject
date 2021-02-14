package qaTests.uiTests;

import Utils.Enums.SocialMedia;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.DataProvider;

public class LoginTests extends BaseTest {
    private  static Logger Log = LogManager.getFormatterLogger(LoginTests.class.getName());
    private static String curEmail = "";
    private static String password = "";

    @DataProvider(name = "loginToSocialMedia")
    public Object [][] socialMediaTypes(){
        return new Object[][]{
                {SocialMedia.LINKEDIN}};
    }

    @DataProvider(name = "invalidLoginCombination")
    public Object[][] invalidLoginCombination(){
        String existedEmail;
        String password;

        if(Env.is)

    }
}
