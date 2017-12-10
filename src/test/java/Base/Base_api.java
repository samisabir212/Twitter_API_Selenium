package Base;


import org.openqa.selenium.*;
import org.openqa.selenium.Dimension;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.log4testng.Logger;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class Base_api {


    //initialize webdriver to nill
    public WebDriver driver = null;
    public JavascriptExecutor js;


    public Properties OR = new Properties();
    public Properties Config = new Properties();
    public FileInputStream fis;
    public Logger log = Logger.getLogger(Base_api.class);






    @Parameters({"useCloud", "cloudUserName", "cloudAccessKey", "useGrid",
            "plateform", "os", "browserName", "browserVersion","url"})
    @BeforeMethod
    public void setUp(@Optional("false") boolean useCloud,
                      @Optional("sami212") String cloudUserName,
                      @Optional("####") String cloudAccessKey,
                      @Optional("false") boolean useGrid,
                      @Optional("Mac") String platform,
                      @Optional("Windows 10") String os,
                      @Optional("firefox") String browserName,
                      @Optional("58") String browserVersion,
                      @Optional("http://google.com") String url
    ) throws MalformedURLException {



        //if we are using cloud enviurmment then use it else just get get local driver
        if (useCloud == true) {
            //run in cloud
            getCloudDriver(cloudUserName, cloudAccessKey, os, browserName, browserVersion);
            log.debug("useCloud driver launched");

        } else if (useGrid == true) {

            //run grid
            // objectRepositoryStreamSetup();
            getGridDriver(platform, browserName, browserVersion);
            log.debug("useGrid driver launched");

        } else {

            getLocalDriver(os, browserName);
            log.debug("Local driver Launched");

        }



        //driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        //driver.manage().timeouts().pageLoadTimeout(35, TimeUnit.SECONDS);
        //deleteCookies();
        //driver.manage().window().maximize();

        //driver.get(url);   //<-- doesnt keep history of the pages you navigated to
        // driver.navigate().to(url);



    }




    //my local driver
    public WebDriver getLocalDriver(String os, String browserName) {


        if (browserName.equalsIgnoreCase("chrome")) {

            if (os.equalsIgnoreCase("Mac")) {
                System.setProperty("webdriver.chrome.driver", "/Users/sami/Desktop/SeleniumBootCamp/Base/src/main/java/Drivers/chromedriver");

            } else if (os.equalsIgnoreCase("Win10")) {
                System.setProperty("webdriver.chrome.driver", "Windows path for chrome driver here.");

            } else if (os.equalsIgnoreCase("Linux")) {
                System.setProperty("webdriver.chrome.driver", "/Users/sami/Desktop/SeleniumBootCamp/Base/src/main/java/Drivers/chromedriverLinux");
            }
            driver = new ChromeDriver();

        } else if (browserName.equalsIgnoreCase("firefox")) {


            if (os.equalsIgnoreCase("Mac")) {
                System.setProperty("webdriver.gecko.driver", "/Users/sami/Desktop/SeleniumBootCamp/Base/src/main/java/Drivers/geckodriverMAC");

            } else if (os.equalsIgnoreCase("Win10")) {
                System.setProperty("webdriver.gecko.driver", "Windows path for chrome driver here.");

            } else if (os.equalsIgnoreCase("Linux")) {
                System.setProperty("webdriver.gecko.driver", "/Users/sami/Desktop/SeleniumBootCamp/Base/src/main/java/Drivers/geckodriverLinux");

            }

            driver = new FirefoxDriver();

        } else if (os.equalsIgnoreCase("Win")) {
            if (browserName.equalsIgnoreCase("ie")) {
                System.setProperty("webdriver.ie.driver", "IE windows path to driver here");

            }

            driver = new InternetExplorerDriver();


        } else if (os.equalsIgnoreCase("Mac")) {
            if (browserName.equalsIgnoreCase("Safari")) {

                driver = new SafariDriver();

            }


        }

        return driver;

    }

    public WebDriver getCloudDriver(String cloudUserName, String cloudAccessKey, String os, String browserName, String browserVersion) throws MalformedURLException {

        //create an instance of Desired Capablities called cap
        {
            DesiredCapabilities cap = new DesiredCapabilities();
            cap.setCapability("cloudPlateform", os);
            cap.setBrowserName(browserName);
            cap.setCapability("version", browserVersion);
            driver = new RemoteWebDriver(new URL("http://" + cloudUserName + ":" +
                    cloudAccessKey + "@ondemand.saucelabs.com:80/wd/hub"), cap);

            return driver;


        }


    }

    //add url if it doesnt work
    public WebDriver getGridDriver(String platform, String browserName, String browserVersion) throws MalformedURLException {


        //passing node url to remote driver
        String nodeURL = "http://192.168.1.175:4444/wd/hub";

        WebDriver driver = null;

        DesiredCapabilities caps = new DesiredCapabilities();

        // Platforms
        if (platform.equalsIgnoreCase("Windows")) {
            caps.setPlatform(org.openqa.selenium.Platform.WINDOWS);
        }
        if (platform.equalsIgnoreCase("MAC")) {
            caps.setPlatform(org.openqa.selenium.Platform.MAC);
        }
        if (browserName.equalsIgnoreCase("Linux")) {
            caps.setPlatform(org.openqa.selenium.Platform.LINUX);
        }

        // Browsers
        if (browserName.equalsIgnoreCase("chrome")) {
            caps = DesiredCapabilities.chrome();
        }
        if (browserName.equalsIgnoreCase("firefox")) {
            caps = DesiredCapabilities.firefox();
        }
        // Version
        caps.setVersion(browserVersion);

        driver = new RemoteWebDriver(new URL(nodeURL), caps);
        // Maximize the browserName's window
        // driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        // Open the Application
        //driver.get(url);
        return driver;


    }




    @AfterMethod
    public void tearDown() {

        //driver.close();

        driver.quit();

    }


    public void objectRepositoryStreamSetup() {


        /*
        * used for retrieving object data from Object Repository properties file
        * */
        try {
            fis = new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/ObjectRepository/OR.properties");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            OR.load(fis);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();


        }
    }



    /*******************************ACTION METHODS****************************************/


    /*******************************Before every test at launch****************************************/

    public void fix_maxmize_deletecookies_wait() {

        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(35, TimeUnit.SECONDS);
        driver.manage().deleteAllCookies();
        driver.manage().window().maximize();
    }

    /*******************************MAXIMIZE WINDOWS FOR DIFFERENT BROWSERS****************************************/

    public void maximize_IEandFirefox_Browsers() {

        driver.manage().window().maximize();


    }

    //maximize chrome
    public void maximize_ToolKit() {

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenResolution = new Dimension((int)
                toolkit.getScreenSize().getWidth(), (int)
                toolkit.getScreenSize().getHeight());

        driver.manage().window().setSize(screenResolution);

    }

    /***********************************************************************/



    /*******************************CLICKING ACTIONS****************************************/

    public void clickById(String locator) {
        driver.findElement(By.id(locator)).click();
    }

    //click by xpath
    public void clickByXpath(String locator) {
        driver.findElement(By.xpath(locator)).click();
    }

    //click by css
    public void clickByCss(String locator) {
        driver.findElement(By.cssSelector(locator)).click();
    }

    //click by locator
    public void click(By locator) {

        driver.findElement(locator).click();

    }

    /*******************************JAVA SCRIPT ACTIONS CLASS CLICKING****************************************/

    public void clickJavaScriptActionsClick(By locator) {

        WebElement element = driver.findElement(locator);
        Actions actions = new Actions(driver);
        actions.moveToElement(element).click().perform();

        /*
        * 1. The element is not visible to click.
        * 2. The page is getting refreshed before it is clicking the element.
        * 3. The element is clickable but there is a spinner/overlay on top of it

            in some cases this will happen and we have to us the Javascript Actions class
        * */
    }

    /***********************************************************************/

    /**********TYPE SEND KEYS  (typing in fields)*********/

    public void typeBy(By locator, String value) {

        driver.findElement(locator).sendKeys(value);



    }


    public void typeByCss(String locator, String value) {

        driver.findElement(By.cssSelector(locator)).sendKeys(value);
    }

    //typeing by id locator
    public void typeByID(String locator, String value) {

        driver.findElement(By.id(locator)).sendKeys(value);
    }

    //type by id and enter key
    public void typeByIdEnter(String locator, String value) {

        driver.findElement(By.id(locator)).sendKeys(value, Keys.ENTER);
    }


    //type by xpath and ENTER key
    public void typeByXpathEnter(String locator, String value) {

        driver.findElement(By.xpath(locator)).sendKeys(value, Keys.ENTER);
    }

    //type by css and ENTER key
    public void typeByCssEnter(String locator, String value) {
        driver.findElement(By.cssSelector(locator)).sendKeys(value, Keys.ENTER);
    }

    //type by xpath
    public void typeByXpath(String locator, String value) {
        driver.findElement(By.xpath(locator)).sendKeys(value);
    }

    //?????
    public void takeEnterKeys(String locator) {
        driver.findElement(By.cssSelector(locator)).sendKeys(Keys.ENTER);
    }


    /***************************Select from dropdown list***************************************/


    public void selectFromDropDownList(By locator, String text, int index) {

        Select DropDownList = new Select(driver.findElement(locator));
        DropDownList.selectByVisibleText(text);
        DropDownList.selectByIndex(index);

    }

    public void selectOptionByVisibleText(By locator, String value) {
        WebElement object = driver.findElement(locator);
        Select select = new Select(object);
        select.selectByVisibleText(value);
    }


    /*****************************CLEAR INPUTFIELD*************************************/


    public void clearInputField(By locator) {

        driver.findElement(locator).clear();
    }
    /**********************************************************/

    //pass the locator and pass the type of locator and it will automatically generate
    public WebElement getElement(String locator, String type) {


        type = type.toLowerCase();

        if (type.equals("id")) {
            System.out.println("Element found with id: " + locator);// you can change it and make it print ID by changing locator to type
            return this.driver.findElement(By.id(locator));
        }
        else if (type.equals("xpath")) {
            System.out.println("Element found with xpath: " + locator);
            return this.driver.findElement(By.xpath(locator));
        }
        else if (type.equals("css")) {
            System.out.println("Element found with xpath: " + locator);
            return this.driver.findElement(By.cssSelector(type));
        }
        else if (type.equals("linktext")) {
            System.out.println("Element found with xpath: " + locator);
            return this.driver.findElement(By.linkText(locator));
        }
        else if (type.equals("partiallinktext")) {
            System.out.println("Element found with xpath: " + locator);
            return this.driver.findElement(By.partialLinkText(type));
        }
        else {
            System.out.println("Locator type not supported");
            return null;
        }
    }

    //get Links
    public void getLinks(String locator){
        driver.findElement(By.linkText(locator)).findElement(By.tagName("a")).getText();
    }


    public java.util.List<String> getTextFromWebElements(String locator){


        java.util.List<WebElement> element = new ArrayList<WebElement>();
        java.util.List<String> text = new ArrayList<String>();
        element = driver.findElements(By.cssSelector(locator));
        for(WebElement web:element){
            text.add(web.getText());
        }

        return text;
    }



    //verifying >>>>>>><<<<<<<<<<<<>>>>>>>>>>>><<<<<<<<<<<<<>>>>>>>

    public void verifyRadioButtonSelection(String locator) {
        WebElement roundTripRadioBtn = driver.findElement(By.id(locator));

        boolean radioButton = roundTripRadioBtn.isSelected();

        System.out.println(radioButton);

        if (radioButton = true) {
            System.out.println("(Passed) Radio Button is selected");

        } else {
            System.out.println("(failed) Radio button not selected ");
        }


    }

    public void verifyTextFieldisDisplayed(String locator) {

        WebElement textField = driver.findElement(By.id(locator));
        boolean textFieldObject = textField.isDisplayed();

        if (textFieldObject = true) {
            System.out.println("(Pass) text field is present");

        } else {

            System.out.println("(Fail) Text field is not present");

        }
    }

    //verify a button is present
    public void verifyButtonIsPresent(String locator, String True, String False) {
        WebElement button = driver.findElement(By.xpath(locator));
        boolean verifyButton = button.isDisplayed();

        if (verifyButton = true) {
            System.out.println(True);

        } else {
            System.out.println(False);

        }
    }

    public void verifyURL(String ExpectedURL) {

        String url = driver.getCurrentUrl();

        Assert.assertEquals(url,ExpectedURL);

    }

    public String getCurrentPageUrl() {

        String url = driver.getCurrentUrl();

        System.out.println(url.toString());

        return url;
    }

    //***********************************************




    /*sleep*/
    public void sleepFor(int sec) throws InterruptedException {
        Thread.sleep(sec * 1000);
    }



    //***********************************************


    //get list of dropdown option1
    public void getDropDownList(String locator) {

        //if this doesnt work, use getAllOptions() method
        java.util.List<WebElement> options = driver.findElements(
                By.xpath(locator));

        java.util.List<String> text = new ArrayList<String>();
        for(int i=1; i<options.size(); i++) {
            text.add(options.get(i).getText());
        }

    }




    //get list of dropdown option2
    public java.util.List<String> getAllOptions(By by) {
        java.util.List<String> options = new ArrayList<String>();
        for (WebElement option : new Select(driver.findElement(by)).getOptions()) {
            String txt = option.getText();
            if (option.getAttribute("value") != "") options.add(option.getText());
        }
        return options;
    }






    //get list of elements by xpath
    public java.util.List<WebElement> getListOfWebElementsByXpath(String locator) {
        java.util.List<WebElement> list = new ArrayList<WebElement>();
        list = driver.findElements(By.xpath(locator));

        return list;

    }



    public java.util.List<WebElement> getListOfWebElementsByID(String locator) {
        java.util.List<WebElement> list = new ArrayList<WebElement>();
        list = driver.findElements(By.id(locator));

        System.out.println(list.toString());

        return list;
    }

    public java.util.List<WebElement> printListOfWebElementsByID(String locator) {

        WebElement element = driver.findElement(By.id(locator));
        Select sel = new Select(element);
        java.util.List<WebElement> options = sel.getOptions();
        int size = options.size();
        System.out.println("***Data from WebApp***");

        for (int i = 0; i < size; i++) {

            String optionName = options.get(i).getText();
            System.out.println(optionName);

        }


        return options;
    }


    public java.util.List<String> getListOfString(java.util.List<WebElement> list) {

        List<String> items = new ArrayList<String>();
        for (WebElement element : list) {

            items.add(element.getText()); //using the Element Text
        }
        return items;

    }


    //***********************************************

    //used to capture screen shot create file name
    public static String getRandomString(int length) {
        StringBuilder sb = new StringBuilder();
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        for (int i = 0; i < length; i++) {
            int index = (int) Math.random() * characters.length();
            sb.append(characters.charAt(index));

        }
        return sb.toString();


    }


    //***********************************************


    //handling Alert
    public boolean isAlertPresent() {

        try{
            driver.switchTo().alert();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void alertAccept() throws InterruptedException {

        WebDriver driver = null;
        Alert alert = driver.switchTo().alert();

        alert.accept();
    }


    //same as alertAccept method
    public void okAlert(){
        Alert alert = driver.switchTo().alert();
        alert.accept();
    }
    public void cancelAlert(){
        Alert alert = driver.switchTo().alert();
        alert.dismiss();
    }


    public void getAlertText(Alert verifiedText) {

        Alert text = driver.switchTo().alert();
        System.out.println("Text of the alert is : " + text);

        if (verifiedText != text) {
            System.out.println("alert does not equal : " + verifiedText);

        }

    }


    //iFrame Handle
    public void iframeHandle(WebElement element) {

        //make sure you get the id or name of the iframe and pass it as element
        //so create a variable and store the webelement object and pass it to the method parameter
        driver.switchTo().frame(element);

    }

    //counting iframe handles
    public void countIframeHandles(String tagNameLocator) {

        int iFrameElements = driver.findElements(By.tagName(tagNameLocator)).size();

        System.out.println("total count of iframes on this page is : " + iFrameElements);

    }

    public void goBackToHomeWindow(){


        driver.switchTo().defaultContent();
    }


    //Working with Window Handles
    public void getWindowHandle() {
        //returns parent window handle
        String primeWindow = driver.getWindowHandle();

    }

    //switching from parent window to child window
    public void switchParentToChildWindow() {

        Set<String> allWindows = driver.getWindowHandles();

        Iterator<String> allWindow = allWindows.iterator();

        String parentWindow = allWindow.next();

        String childWindow = allWindow.next();

        driver.switchTo().window(childWindow);



    }

    public void getAllWindowHandles() {

        Set<String> allWindows = driver.getWindowHandles();

        System.out.println(allWindows);

    }


    public void navigateBack(){


        driver.navigate().back();
    }


    public void navigateForward(){
        driver.navigate().forward();
    }


    //wait for element to be clickable by any type
    public void waitUntilClickable_UsingBy(By locator) {

        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.elementToBeClickable((locator)));


    }

    //wait for element to be clickable by xpath
    public void waitUntilClickAble(By locator){

        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.elementToBeClickable(locator));

    }


    //wait for page to load completely
    public void implicitWait(Long waitTime) {

        driver.manage().timeouts().implicitlyWait(waitTime, TimeUnit.SECONDS);


    }




    //use this as an example to all other wait types
    public void waitUntilVisible(By locator){

        WebDriverWait wait = new WebDriverWait(driver, 10);

        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));

    }

    //Explicit wait for an element to be present and then utilize it
    public WebElement waitForElement(int timeout, By locator) {

        WebElement element = null;

        try {
            //create an element object before action

            System.out.println("waiting for maximum :: " + timeout + "seconds for the element to be available");
            WebDriverWait wait = new WebDriverWait(driver, 3);
            element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            System.out.println("element appeared on the webpage");

        } catch (Exception e) {

            System.out.println("element not appeared on the webpage");

        }
        return element;

    }


    //wait for element to be selecatable by any locator using By
    public void waitUntilSelectable(By locator){

        WebDriverWait wait = new WebDriverWait(driver, 10);
        boolean element = wait.until(ExpectedConditions.elementToBeSelected(locator));

        //create an element object before action
    }


    public void mouseHoverByCSS(String locator){
        try {
            WebElement element = driver.findElement(By.cssSelector(locator));
            Actions action = new Actions(driver);
            Actions hover = action.moveToElement(element);
        }catch(Exception ex){
            System.out.println("First attempt has been done, This is second try");
            WebElement element = driver.findElement(By.cssSelector(locator));
            Actions action = new Actions(driver);
            action.moveToElement(element).perform();

        }

    }


    //mousehover by
    public void mouseHover(By locatorAttemp1, By locatorAttempt2) {

        try {
            WebElement element = driver.findElement(locatorAttemp1);
            Actions action = new Actions(driver);
            Actions hover = action.moveToElement(element);
        } catch (Exception e) {
            System.out.println("First attempt has been done, This is second try");
            WebElement element = driver.findElement(locatorAttempt2);
            Actions action = new Actions(driver);
            action.moveToElement(element).perform();
        }

    }

    public void mouseHoverByXpath(String locator){
        try {
            WebElement element = driver.findElement(By.xpath(locator));
            Actions action = new Actions(driver);
            Actions hover = action.moveToElement(element);
        }catch(Exception ex){
            System.out.println("First attempt has been done, This is second try");
            WebElement element = driver.findElement(By.cssSelector(locator));
            Actions action = new Actions(driver);
            action.moveToElement(element).perform();

        }

    }


    //drag and drop using By method
    public void dragAndDropByAnyLocatorType(By fromLocator, By toLocator) throws InterruptedException {


        WebElement fromElement1 = driver.findElement((fromLocator));
        WebElement toElement1 = driver.findElement((toLocator));

        Actions action = new Actions(driver);

        // Click and hold, move to element, release, build and perform
        action.clickAndHold(fromElement1).perform();
        sleepFor(2);
        action.moveToElement(toElement1).perform();
        sleepFor(2);
        action.release(toElement1).perform();


    }

    //Drag and drop method option1
    public void dragAndDrop(String fromLocatorXpath, String toLocatorXpath) throws InterruptedException {


        WebElement fromElement1 = driver.findElement(By.xpath(fromLocatorXpath));
        WebElement toElement1 = driver.findElement(By.xpath(toLocatorXpath));

        Actions action = new Actions(driver);

        // Click and hold, move to element, release, build and perform
        action.clickAndHold(fromElement1).perform();
        sleepFor(2);
        action.moveToElement(toElement1).perform();
        sleepFor(2);
        action.release(toElement1).perform();

    }

    //drag and drop method option2
    public void dragAndDropMethod(String fromLocatorXpath, String toLocatorXpath) {


        WebElement fromElement1 = driver.findElement(By.xpath(fromLocatorXpath));
        WebElement toElement1 = driver.findElement(By.xpath(toLocatorXpath));

        Actions actions = new Actions(driver);

        actions.dragAndDrop(fromElement1, toElement1);


    }

    //getting  coordinates of window
    public void getWindowCoordinates() {

        int xCoordinate = driver.manage().window().getPosition().getX();
        int yCoordinate = driver.manage().window().getPosition().getY();

        System.out.println("x Coordinate is " + xCoordinate);
        System.out.println("y Coordinate is " + yCoordinate);

/*
        Point point = driver.manage().window().getPosition();

        point.getX();
        point.getY();

*/

    }


    public static void moveToNewWindows(WebDriver driver, String windowTitle) {
        boolean windowExists = false;
        Set<String> windows = driver.getWindowHandles();
        for (String window : windows) {
            driver.switchTo().window(window);
            if (driver.getTitle().contains(windowTitle)) {
                windowExists = true;

                break;
            }
        }
        if (!windowExists) {
            Assert.fail(windowTitle + " Title window not exists");
        }
    }



}
