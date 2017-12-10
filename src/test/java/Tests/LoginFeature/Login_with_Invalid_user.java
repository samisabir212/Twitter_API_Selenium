package Tests.LoginFeature;

import Base.Base_api;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class Login_with_Invalid_user extends Base_api {


    @Parameters("url")
    @Test(enabled = false)
    public void when_logging_in_user_must_enter_a_valid_user_name_and_password(String url) {

    }



}
