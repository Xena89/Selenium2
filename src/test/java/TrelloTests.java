import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class TrelloTests {
    static WebDriver driver;
    WebDriverWait webDriverWait;
    private static final String BASE_URL = "https://trello.com/";

    @BeforeAll
    static void registerDriver() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setupBrowser() throws InterruptedException {
        driver = new ChromeDriver();
        webDriverWait = new WebDriverWait(driver, 5);
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        driver.get(BASE_URL);
        driver.manage().window().maximize();

        loginToTrello();
    }

    @Test
    @DisplayName("Тест переход на страницу Конфиденциальность")
    public void SwitchToPrivacyPolicy() throws InterruptedException {
        Actions actions = new Actions(driver);
        driver.findElement(By.xpath("//button[contains(@aria-label, 'Открыть меню Информация')]")).click();
        driver.findElement(By.xpath("//div//a[text()='Конфиденциальность']")).click();
        Thread.sleep(5000);
        assertTrue(driver.findElement(By.xpath("//*[text()='Информация']")).isDisplayed());
        ArrayList<String> tabs2 = new ArrayList<String> (driver.getWindowHandles());
        driver.switchTo().window(tabs2.get(1));
        assertTrue(driver.getCurrentUrl().contains("/privacy-policy"));
    }

    @Test
    @DisplayName("Тест создать новую доску")
    void AddNewBoard() {
        driver.findElement(By.xpath("//button/p[text()='Создать']")).click();
        driver.findElement(By.xpath("//button/span[text()='Создайте доску']")).click();
        driver.findElement(By.xpath("//input[@placeholder='Добавить заголовок доски']")).sendKeys("Еще одна доска");
        driver.findElement(By.xpath("//button[text()='Создать доску']")).click();
        assertTrue(driver.getCurrentUrl().contains("/boards"));
    }

    @Test
    @DisplayName("Поиск выдает релевантные значения")
    void checkSearchInput() {
        String itemForSearch = "Еще одна доска";
        driver.findElement(By.xpath("//input[@data-test-id = 'header-search-input']")).click();
        driver.findElement(By.xpath("//input[@data-test-id = 'header-search-input']")).sendKeys(itemForSearch);
        driver.findElement(By.xpath("//a[@aria-label = 'Перейти на страницу поиска']")).click();

        List<WebElement> items = new ArrayList<>(driver.findElements(By.xpath("//span[@class = 'compact-board-tile-link-text-name']")));

        for (int i = 0; i < items.size(); i++) {
            assertTrue(items.get(i).getText().contains(itemForSearch));
        }
    }

    public static void loginToTrello() throws InterruptedException {
        driver.get("https://trello.com/ru/login");
        driver.findElement(By.id("user")).sendKeys("quickusja89@hotmail.com");
        driver.findElement(By.xpath("//*[@id=\"login\"]")).click();
        Thread.sleep(5000);
        driver.findElement(By.id("password")).sendKeys("100989quick");
        driver.findElement(By.xpath("//button[contains(@id,'login-submit')]")).click();
    }
    @AfterEach
    void tearDown() {

        driver.quit();
    }
}
