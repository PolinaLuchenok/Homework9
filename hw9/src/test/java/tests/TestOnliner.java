package tests;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import pages.CatalogPage;
import pages.MainPage;

class TestOnliner {
	private static WebDriver driver;
    private static ChromeOptions chromeOptions = new ChromeOptions();
    private static Logger log = Logger.getLogger(TestOnliner.class);
    private MainPage mainPage;
    private CatalogPage catalogPage;
    
	@BeforeEach
    public void openOnliner() {
		chromeOptions.setBinary("C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe");
		System.setProperty("webdriver.chrome.driver","C:\\drivers\\chromedriver_win32\\chromedriver.exe");
		
		Map<String, String> mobile = new HashMap<String, String>();
		mobile.put("deviceName","Samsung Galaxy S8+");
		chromeOptions.setExperimentalOption("mobileEmulation", mobile);
		log.debug("mobile emulation is used");
		
		driver = new ChromeDriver(chromeOptions);
        driver.get("https://onliner.by");
        log.info("site opened");
        
        mainPage = new MainPage(driver);
        catalogPage = new CatalogPage(driver);
        mainPage.openCatalog();
        log.info("catalog opened");
        log.debug("catalog opened");
    }

	@Test
	@Severity(SeverityLevel.NORMAL)
	public void testAvailableSection() {
		try {
			Assertions.assertAll(
        		() -> Assertions.assertTrue(catalogPage.getNameSection("Onlíner Prime")),
        		() -> Assertions.assertTrue(catalogPage.getNameSection("Электроника")),
        		() -> Assertions.assertTrue(catalogPage.getNameSection("Компьютеры и сети")),
        		() -> Assertions.assertTrue(catalogPage.getNameSection("Бытовая техника")),
        		() -> Assertions.assertTrue(catalogPage.getNameSection("На каждый день")),
        		() -> Assertions.assertTrue(catalogPage.getNameSection("Стройка и ремонт")),
        		() -> Assertions.assertTrue(catalogPage.getNameSection("Дом и сад")),
        		() -> Assertions.assertTrue(catalogPage.getNameSection("Авто и мото")),
        		() -> Assertions.assertTrue(catalogPage.getNameSection("Красота и спорт")),
        		() -> Assertions.assertTrue(catalogPage.getNameSection("Детям и мама"))); //здесь намерено сделана ошибка в названии секции
		} catch (Throwable ex) {
	            log.error(
	                    String.format("Failed ", ex));
	            throw ex;
	    }
	    log.info("Assertion passed");
	}
	
	@Test
	@Severity(SeverityLevel.NORMAL)
	public void testItemsSection() {
		catalogPage.openComputerSection();
		log.info("section opened");
		log.debug("section opened");
		try {
			Assertions.assertAll(
        		() -> Assertions.assertTrue(catalogPage.getNameItem("Ноутбуки, компьютеры, мониторы")),
        		() -> Assertions.assertTrue(catalogPage.getNameItem("Комплектующие")),
        		() -> Assertions.assertTrue(catalogPage.getNameItem("Хранение данных")),
        		() -> Assertions.assertTrue(catalogPage.getNameItem("Сетевое оборудование")));
		} catch (Throwable ex) {
            log.error(
                    String.format("Failed ", ex));
            throw ex;
    }
    log.info("Assertion passed");
	}

	@Test
	@Severity(SeverityLevel.NORMAL)
	void testElementsItem() {
		catalogPage.openComputerSection();
		log.info("section opened");
		log.debug("section opened");
		catalogPage.openItemAccessories();
		log.info("item opened");
		log.debug("item opened");
        
        for (WebElement name : catalogPage.findNamesElements()) {
        	Assertions.assertFalse(name.getText().isEmpty());
        }
        
        for (WebElement desc : catalogPage.findDescriptionElements()) {
        	Assertions.assertAll(
        			() -> Assertions.assertTrue(catalogPage.showQuantity(desc)),
        			() -> Assertions.assertTrue(catalogPage.showMinPrice(desc))
            );
        }
        log.info("Assertion passed");
	}
		
	@AfterEach
	public void cleanUp() {
		driver.quit();
    }
}

