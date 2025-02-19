package tests;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.epam.healenium.SelfHealingDriver;

import pageObjects.AmazonProductPage;
import pageObjects.AmazonSearchPage;
import pageObjects.AmazonSignInPage;

public class AmazonTests {

    WebDriver delegate;
    SelfHealingDriver driver;
    AmazonSearchPage spobject;
    AmazonSignInPage siobject;
    AmazonProductPage ppobject;

    @BeforeClass
    public void setUp() {
        delegate = new FirefoxDriver();
        driver = SelfHealingDriver.create(delegate);
        driver.manage().deleteAllCookies();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        spobject = new AmazonSearchPage(driver);
        siobject = new AmazonSignInPage(driver);
        ppobject = new AmazonProductPage(driver);
    }

    @Test(priority = 1)
    public void openAmazon() {
        driver.get("https://www.amazon.com/");
        spobject.maximizeBrowser();
    }

    @Test(priority = 2)
    public void signIn() {
        spobject.clickToSignIn();
        siobject.enterEmail("dhaksiny@buffalo.edu");
        siobject.clickContinue();
        siobject.enterPassword("Dhaksu@555");
        siobject.clickSignIn();
    }

    @Test(priority = 3)
    public void handleCaptcha() throws InterruptedException {
        try {
            driver.findElement(By.xpath("//img[@alt=\"captcha\"]"));
        } catch (Exception e) {
            Thread.sleep(2000);
        }
    }

    @Test(priority = 4)
    public void searchProduct() throws InterruptedException {
        spobject.searchProduct("Nike Shoes");
        Thread.sleep(2000);
        spobject.searchClick();
        Thread.sleep(2000);
    }

    @Test(priority = 5)
    public void chooseAndAddProductToCart() throws InterruptedException {
        ppobject.scrollToProduct();
        Thread.sleep(2000);
        ppobject.chooseProduct();
        Thread.sleep(2000);
        ppobject.sizeClick();
        Thread.sleep(2000);
        
        int i = 0;
        while(driver.findElement(By.xpath(String.format("//li[@id=\"size_name_%d\"]",i))).isEnabled() == false) {
            i++;
        }
        driver.findElement(By.xpath(String.format("//li[@id=\"size_name_%i\"]",i))).click();
        Thread.sleep(2000);
        
        ppobject.addtoCart();
        Thread.sleep(2000);
    }

    @Test(priority = 6)
    public void clearCartAndClose() throws InterruptedException {
        spobject.clearCart();
        Thread.sleep(2000);
        spobject.closeDriver();
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }
}
