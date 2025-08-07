package com.hotelcrm.crmapp.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AddCompanyTest {

    private static WebDriver driver;
    private static final String BASE_URL = "http://localhost:4200";

    @BeforeAll
    static void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().window().maximize();
    }

    @AfterAll
    static void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @Order(1)
    void shouldAddNewCompany_whenDataIsValid() {
        final String companyName = "A-Test Company";

        driver.get(BASE_URL + "/login");
        driver.findElement(By.id("username")).sendKeys("specialist2");
        driver.findElement(By.id("password")).sendKeys("password123");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.urlContains("/dashboard"));

        driver.get(BASE_URL + "/dashboard/companies");

        By addBtn = By.cssSelector("button[mat-flat-button][color='primary']");
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(addBtn)).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[formcontrolname='name']")));

        driver.findElement(By.cssSelector("input[formcontrolname='name']")).sendKeys(companyName);
        driver.findElement(By.cssSelector("input[formcontrolname='taxId']")).sendKeys("1234567890");

        driver.findElement(By.cssSelector("mat-select[formcontrolname='industry']")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("mat-option"))).click();

        driver.findElement(By.cssSelector("input[formcontrolname='email']")).sendKeys("test@company.com");
        driver.findElement(By.cssSelector("input[formcontrolname='phoneNumber']")).sendKeys("+48123456789");
        driver.findElement(By.cssSelector("input[formcontrolname='website']")).sendKeys("https://test.com");
        driver.findElement(By.cssSelector("input[formcontrolname='address']")).sendKeys("Main Street 1");
        driver.findElement(By.cssSelector("input[formcontrolname='postalCode']")).sendKeys("00-001");
        driver.findElement(By.cssSelector("input[formcontrolname='city']")).sendKeys("Warsaw");
        driver.findElement(By.cssSelector("input[formcontrolname='country']")).sendKeys("Poland");

        driver.findElement(By.cssSelector("button[type='submit']")).click();

        wait.until(ExpectedConditions.urlContains("/dashboard/companies"));

        WebElement table = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table")));
        String tableText = table.getText();

        Assertions.assertTrue(tableText.contains(companyName),
                "Table should contain the added company name: " + companyName);
    }
}