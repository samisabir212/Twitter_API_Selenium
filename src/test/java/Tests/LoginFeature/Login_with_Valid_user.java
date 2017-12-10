package Tests.LoginFeature;

import Base.Base_api;
import org.openqa.selenium.By;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class Login_with_Valid_user extends Base_api {

    @Parameters("url")
    @Test(enabled = true)
    public void user_logs_in_with_valid_user_name_and_password_and_Navigates_to_Logged_in_HomePage(String url) {


        //fix
        fix_maxmize_deletecookies_wait();

        //click log in
        click(By.xpath(".//a[text()='Log in']"));



    }



}
