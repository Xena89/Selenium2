import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class CrmTests {
    static WebDriver driver;
    WebDriverWait webDriverWait;
    private static final String BASE_URL = "https://crm.geekbrains.space/";

    @BeforeAll
    static void registerDriver() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setupBrowser() {
        driver = new ChromeDriver();
        webDriverWait = new WebDriverWait(driver, 5);
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        loginCrm();
    }

    @Test
    @DisplayName("Тест создание и сохранение нового проекта")
   public void createCrmProject() throws InterruptedException {
        Actions actions = new Actions(driver);
        WebElement projectMenuElement = driver.findElement(By.xpath("//a/span[contains(text(),'Проекты')]"));
        actions.moveToElement(projectMenuElement).perform();

        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a/span[contains(text(),'Все проекты')]")));
        driver.findElement(By.xpath("//a/span[contains(text(),'Все проекты')]")).click();

        webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='Создать проект']")));
        driver.findElement(By.xpath("//a[text()='Создать проект']")).click();

        webDriverWait.until(ExpectedConditions.elementToBeClickable(By.name("crm_project[name]")));
        driver.findElement(By.name("crm_project[name]")).sendKeys("sdfsffghdfghgsfееfhfgh");

        webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Укажите организацию']")));
        driver.findElement(By.xpath("//span[text()='Укажите организацию']")).click();

        driver.findElement(By.xpath("//div[@id='select2-drop']//input")).sendKeys("Все организации");

        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='select2-result-label']")));
        List<WebElement> organizationVars = driver.findElements(By.xpath("//div[@class='select2-result-label']"));
        organizationVars.get(0).click();

        Select businessUnitSelect = new Select(driver.findElement(By.xpath("//select[contains(@id,'crm_project_businessUnit')]")));
        businessUnitSelect.selectByVisibleText("Research & Development");

        Select curatorSelect = new Select(driver.findElement(By.xpath("//select[contains(@id,'crm_project_curator')]")));
        curatorSelect.selectByVisibleText("Applanatest1 Applanatest1 Applanatest1");

        Select rpSelect = new Select(driver.findElement(By.xpath("//select[contains(@id,'crm_project_rp')]")));
        rpSelect.selectByVisibleText("Applanatest Applanatest Applanatest");

        Select managerSelect = new Select(driver.findElement(By.xpath("//select[contains(@id,'crm_project_manager')]")));
        managerSelect.selectByVisibleText("Applanatest Applanatest Applanatest");


        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='select2-container select2']")));
        driver.findElement(By.xpath("//div[contains(@id, 's2id_crm_project_contactMain-uid')]/a")).click();

        webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='select2-drop']//input")));
        driver.findElement(By.xpath("//div[@id='select2-drop']//input")).sendKeys("123 123");
        driver.findElement(By.xpath("//div[@id='select2-drop']//input")).sendKeys(Keys.ENTER);

        driver.switchTo().parentFrame();
        driver.findElement(By.xpath("//button[contains(@class,\"main-group\")][contains(.,'Сохранить')]")).click();
        Thread.sleep(5000);
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[text()='Проект сохранен']")));
        assertTrue(driver.findElement(By.xpath("//*[text()='Проект сохранен']")).isDisplayed());
        assertTrue(driver.getCurrentUrl().contains("/view"));
    }

    @Test
    @DisplayName("Тест создание и сохранение контакта")
    void createCrmContaсt() {
        Actions actions = new Actions(driver);
        WebElement projectMenuElement = driver.findElement(By.xpath("//a/span[text()='Контрагенты']"));
        actions.moveToElement(projectMenuElement).perform();

        driver.findElement(By.xpath("//*[text()=\"Контактные лица\"]")).click();

        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[text()=\"Создать контактное лицо\"]")));
        driver.findElement(By.xpath("//*[text()=\"Создать контактное лицо\"]")).click();

        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[contains(@id,'crm_contact_lastName')]")));
        driver.findElement(By.xpath("//input[contains(@id,'crm_contact_lastName')]")).sendKeys("Sidorov");
        driver.findElement(By.xpath("//input[contains(@id,'crm_contact_firstName')]")).sendKeys("Sidr");
        driver.findElement(By.xpath("//input[contains(@id,'crm_contact_jobTitle')]")).sendKeys("developer");

        driver.findElement(By.xpath("//span[@class=\"select2-chosen\"]")).click();
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(.//div[@class=\"select2-result-label\"])[1]")));
        driver.findElement(By.xpath("(.//div[@class=\"select2-result-label\"])[1]")).click();

        driver.findElement(By.xpath("//button[contains(@class,\"main-group\")][contains(.,'Сохранить')]")).click();

        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[text()='Контактное лицо сохранено']")));
        assertTrue(driver.findElement(By.xpath("//*[text()='Контактное лицо сохранено']")).isDisplayed());
        assertTrue(driver.getCurrentUrl().contains("/view"));
    }

    public static void loginCrm() {
        driver.get("https://crm.geekbrains.space/user/login");
        driver.findElement(By.id("prependedInput")).sendKeys("Applanatest1");
        driver.findElement(By.id("prependedInput2")).sendKeys("Student2020!");
        driver.findElement(By.xpath("//button")).click();
    }

        @AfterEach
        void tearDown() {

        driver.quit();
        }
}