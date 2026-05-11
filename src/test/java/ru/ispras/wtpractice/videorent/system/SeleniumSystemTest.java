package ru.ispras.wtpractice.videorent.system;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import ru.ispras.wtpractice.videorent.AbstractTestcontainers;

import java.nio.file.Path;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestPropertySource(properties = "app.upload.dir=build/test-uploads/images")
@Sql(scripts = "/sql/test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class SeleniumSystemTest extends AbstractTestcontainers {

    @LocalServerPort
    private int port;

    @Value("${user.dir}")
    private String projectDir;

    private final WebDriver driver = new HtmlUnitDriver(true);

    @AfterEach
    void closeBrowser() {
        driver.quit();
    }

    @Test
    void dashboardNavigationAndSearchUseCases() {
        open("/");

        assertTitleContains("Видеопрокат");
        assertPageContains("Дашборд");
        assertPageContains("Клиенты");
        assertPageContains("Фильмы");
        assertPageContains("Доступно");

        click(By.linkText("Клиенты"));
        assertTitleContains("Клиенты");
        assertPageContains("Александр Невский");
        assertPageContains("Сол Гудман");

        type(By.name("search"), "Омаха");
        new Select(driver.findElement(By.name("by"))).selectByValue("address");
        click(By.cssSelector("button[type='submit']"));

        assertPageContains("Сол Гудман");
        assertPageDoesNotContain("Александр Невский");

        click(By.linkText("Фильмы"));
        type(By.name("search"), "Неизвестный фильм");
        click(By.cssSelector("button[type='submit']"));

        assertPageContains("Фильмов не найдено");
        assertPageDoesNotContain("Максимальный удар");
    }

    @Test
    void clientDetailsEditAndDeleteUseCases() {
        open("/clients");
        click(By.linkText("Александр Невский"));

        assertTitleContains("Александр Невский");
        assertPageContains("Лос-Анджелес, Голливуд");
        assertPageContains("+79990000001");
        assertPageContains("Сейчас на руках");
        assertPageContains("Максимальный удар");
        assertPageContains("История");
        assertPageContains("Выдача");
        assertPageContains("Возврат");

        click(By.linkText("Редактировать"));
        assertPageContains("Редактировать клиента");
        type(By.id("fullName"), "");
        click(By.cssSelector("button[type='submit']"));

        assertPageContains("Заполните имя, телефон и адрес клиента");

        type(By.id("fullName"), "Александр Невский Selenium");
        type(By.id("phoneNumber"), "+79990000999");
        type(By.id("address"), "Лос-Анджелес, тестовый адрес");
        click(By.cssSelector("button[type='submit']"));

        waitUntilUrlContains("/clients/1");
        assertPageContains("Александр Невский Selenium");
        assertPageContains("+79990000999");
        assertPageContains("Лос-Анджелес, тестовый адрес");

        open("/clients/2");
        assertPageContains("Сол Гудман");
        acceptConfirmAndClick(By.cssSelector("form[action='/clients/2/delete'] button[type='submit']"));

        waitUntilUrlContains("/clients");
        assertPageContains("Клиенты");
        assertPageDoesNotContain("Сол Гудман");
    }

    @Test
    void movieCreateUseCaseShowsValidationErrorAndThenCreatesMovie() {
        open("/movies/new");

        assertPageContains("Новый фильм");
        click(By.cssSelector("button[type='submit']"));

        assertPageContains("Заполните название, режиссёра, студию и дату выхода не раньше 01.01.1900");
        assertPageContains("Новый фильм");

        type(By.id("name"), "Тестовый фильм Selenium");
        type(By.id("director"), "Тестовый режиссёр");
        type(By.id("company"), "Selenium Studio");
        type(By.id("releaseDate"), "2024-05-20");
        click(By.cssSelector("button[type='submit']"));

        waitUntilUrlContains("/movies/");
        assertPageContains("Тестовый фильм Selenium");
        assertPageContains("Тестовый режиссёр");
        assertPageContains("Selenium Studio");
        assertPageContains("20.05.2024");
    }

    @Test
    void movieDetailsSearchEditAndDeleteUseCases() {
        open("/movies");
        assertPageContains("Максимальный удар");
        assertPageContains("Разборка в Маниле");

        type(By.name("search"), "Марк Дакаскос");
        new Select(driver.findElement(By.name("by"))).selectByValue("director");
        click(By.cssSelector("button[type='submit']"));

        assertPageContains("Разборка в Маниле");
        assertPageDoesNotContain("Максимальный удар");

        open("/movies");
        type(By.name("search"), "Hollywood Storm");
        new Select(driver.findElement(By.name("by"))).selectByValue("company");
        click(By.cssSelector("button[type='submit']"));

        assertPageContains("Разборка в Маниле");
        assertPageContains("Нападение на Рио Браво");
        assertPageDoesNotContain("Максимальный удар");

        open("/movies/1");
        assertTitleContains("Максимальный удар");
        assertPageContains("Анджей Бартковяк");
        assertPageContains("Czar Pictures");
        assertPageContains("Экземпляров");
        assertPageContains("Доступно");
        assertPageContains("Выдано");
        assertPageContains("История выдач");

        click(By.linkText("Редактировать"));
        assertPageContains("Редактировать фильм");
        type(By.id("releaseDate"), "1899-12-31");
        click(By.cssSelector("button[type='submit']"));

        assertPageContains("Заполните название, режиссёра, студию и дату выхода не раньше 01.01.1900");

        type(By.id("name"), "Максимальный удар Selenium");
        type(By.id("director"), "Тестовый режиссёр фильма");
        type(By.id("company"), "Тестовая студия фильма");
        type(By.id("releaseDate"), "2025-01-10");
        click(By.cssSelector("button[type='submit']"));

        waitUntilUrlContains("/movies/1");
        assertPageContains("Максимальный удар Selenium");
        assertPageContains("Тестовый режиссёр фильма");
        assertPageContains("Тестовая студия фильма");
        assertPageContains("10.01.2025");

        open("/movies/3");
        assertPageContains("Нападение на Рио Браво");
        acceptConfirmAndClick(By.cssSelector("form[action='/movies/3/delete'] button[type='submit']"));

        waitUntilUrlContains("/movies");
        assertPageContains("Фильмы");
        assertPageDoesNotContain("Нападение на Рио Браво");
    }

    @Test
    void clientCreateUseCaseShowsValidationErrorAndThenCreatesClient() {
        open("/clients/new");

        assertPageContains("Новый клиент");
        click(By.cssSelector("button[type='submit']"));

        assertPageContains("Заполните имя, телефон, адрес и загрузите фото клиента");
        assertPageContains("Новый клиент");

        type(By.id("fullName"), "Иван Selenium");
        type(By.id("phoneNumber"), "+79991234567");
        type(By.id("address"), "Москва, Selenium проспект, 1");
        uploadFile(By.id("image"), Path.of(projectDir, "docs", "img.png"));
        click(By.cssSelector("button[type='submit']"));

        waitUntilUrlContains("/clients/");
        assertPageContains("Иван Selenium");
        assertPageContains("+79991234567");
        assertPageContains("Москва, Selenium проспект, 1");

        click(By.linkText("Клиенты"));
        type(By.name("search"), "Иван Selenium");
        click(By.cssSelector("button[type='submit']"));

        assertPageContains("Иван Selenium");
        assertPageDoesNotContain("Сол Гудман");
    }

    @Test
    void issueAndReturnUseCaseChangesVisibleClientData() {
        open("/clients/2");
        click(By.linkText("Выдать"));

        assertPageContains("Выдача экземпляра");
        assertEquals("2", new Select(driver.findElement(By.id("clientId"))).getFirstSelectedOption().getAttribute("value"));
        new Select(driver.findElement(By.id("clientId"))).selectByValue("2");
        new Select(driver.findElement(By.id("exemplarId"))).selectByValue("4");
        click(By.cssSelector("button[type='submit']"));

        waitUntilUrlContains("/clients/2");
        assertPageContains("Сол Гудман");
        assertPageContains("Разборка в Маниле");
        assertPageContains("Выдача");
        assertPageDoesNotContain("Ничего не выдано");

        ((JavascriptExecutor) driver).executeScript("window.confirm = function() { return true; };");
        click(By.cssSelector("form[action='/return'] button[type='submit']"));

        waitUntilPageContains("Ничего не выдано");
        assertPageContains("Ничего не выдано");
        assertPageContains("Возврат");
    }

    @Test
    void issueBoundaryShowsNoticeWhenNoExemplarsAreAvailable() {
        issueAvailableExemplar("1");
        issueAvailableExemplar("3");
        issueAvailableExemplar("4");

        open("/issue");

        assertPageContains("Нет доступных экземпляров для выдачи");
        assertTrue(driver.findElements(By.cssSelector("form[action='/issue']")).isEmpty());
    }

    private void open(String path) {
        driver.get("http://localhost:" + port + path);
    }

    private void click(By by) {
        driver.findElement(by).click();
    }

    private void type(By by, String value) {
        WebElement element = driver.findElement(by);
        element.clear();
        element.sendKeys(value);
    }

    private void waitUntilUrlContains(String urlPart) {
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(webDriver -> webDriver.getCurrentUrl().contains(urlPart));
    }

    private void waitUntilPageContains(String expectedText) {
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(webDriver -> webDriver.getPageSource().contains(expectedText));
    }

    private void uploadFile(By by, Path filePath) {
        WebElement input = driver.findElement(by);
        ((JavascriptExecutor) driver).executeScript("arguments[0].style.display = 'block';", input);
        input.sendKeys(filePath.toString());
    }

    private void acceptConfirmAndClick(By by) {
        ((JavascriptExecutor) driver).executeScript("window.confirm = function() { return true; };");
        click(by);
    }

    private void issueAvailableExemplar(String exemplarId) {
        open("/issue");
        new Select(driver.findElement(By.id("clientId"))).selectByValue("2");
        new Select(driver.findElement(By.id("exemplarId"))).selectByValue(exemplarId);
        click(By.cssSelector("button[type='submit']"));
        waitUntilUrlContains("/clients/2");
    }

    private void assertTitleContains(String expectedText) {
        assertTrue(driver.getTitle().contains(expectedText),
                () -> "Expected title to contain '" + expectedText + "', actual title: " + driver.getTitle());
    }

    private void assertPageContains(String expectedText) {
        assertTrue(driver.getPageSource().contains(expectedText),
                () -> "Expected page to contain '" + expectedText + "' at " + driver.getCurrentUrl());
    }

    private void assertPageDoesNotContain(String unexpectedText) {
        assertFalse(driver.getPageSource().contains(unexpectedText),
                () -> "Expected page not to contain '" + unexpectedText + "' at " + driver.getCurrentUrl());
    }

}
