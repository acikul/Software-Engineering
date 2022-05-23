package hr.fer.fringilla.fringillasport;

//import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

@SpringBootTest
public class SeleniumCreateBadLocationTest {

	@Test
	void testCreateLocation() {

		System.setProperty("webdriver.chrome.driver", "/Users/Luka/Applications/WebDriver/chromedriver");
		WebDriver driver = new ChromeDriver();

		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.get("http://localhost:3000/prijava");

		WebElement element = driver
				.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div[1]/form/div[1]/div[2]/input"));
		element.sendKeys("athlete");
		element = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div[1]/form/div[2]/div[2]/input"));
		element.sendKeys("athlete");
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div[1]/form/button")).click();

		WebDriverWait wait = new WebDriverWait(driver, 5);
		Alert alert = wait.until(ExpectedConditions.alertIsPresent());
		alert.accept();

		// Stvori lokaciju
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[1]/nav/ul/li[3]/a")).click();

		// ime lokacije
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/form/input")).sendKeys("TestnaLokacija");
		// Zavrsi
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/form/h3")).click();

		Alert alert3 = wait.until(ExpectedConditions.alertIsPresent());
		boolean checker = alert3.getText().contains("pogreške");

		// String redirURL = driver.getCurrentUrl();
		// System.out.println(redirURL);
		// boolean compRes= redirURL.contains("podaci");

		assertTrue(checker);

		driver.quit();
	}
}
