package utils;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import config.TestConfig;

import java.time.Duration;

public class WaitHelper {
    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor js;

    public WaitHelper(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(TestConfig.getExplicitTimeout()));
        this.js = (JavascriptExecutor) driver;
    }

    /**
     * Elementin görünməsini və klik edilə bilən olmasını gözlə
     */
    public WebElement waitForElementClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Elementin mövcudluğunu gözlə
     */
    public WebElement waitForElementPresent(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /**
     * Elementin görünməsini gözlə
     */
    public WebElement waitForElementVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Səhifənin tam yüklənməsini gözlə
     */
    public void waitForPageLoad() {
        wait.until((ExpectedCondition<Boolean>) driver ->
                ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete")
        );
    }

    /**
     * AJAX sorğularının bitməsini gözlə (jQuery varsa)
     */
    public void waitForAjax() {
        try {
            wait.until((ExpectedCondition<Boolean>) driver -> {
                Boolean jQueryDefined = (Boolean) js.executeScript("return typeof jQuery != 'undefined'");
                if (jQueryDefined) {
                    return (Boolean) js.executeScript("return jQuery.active == 0");
                }
                return true;
            });
        } catch (Exception e) {
            // jQuery yoxdur, davam et
        }
    }

    /**
     * URL dəyişməsini gözlə
     */
    public boolean waitForUrlChange(String currentUrl) {
        return wait.until(ExpectedConditions.not(ExpectedConditions.urlToBe(currentUrl)));
    }

    /**
     * Retry ilə element tap
     */
    public WebElement findElementWithRetry(By locator, int maxRetries) {
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                WebElement element = waitForElementPresent(locator);
                scrollToElement(element);
                waitShort();
                return wait.until(ExpectedConditions.visibilityOf(element));
            } catch (Exception e) {
                if (attempt < maxRetries) {
                    System.out.println("         ⚠️  Cəhd " + attempt + " uğursuz, yenidən...");
                    waitMedium();
                    js.executeScript("window.scrollBy(0, 50);");
                    waitShort();
                }
            }
        }
        return null;
    }

    /**
     * Elementə scroll et
     */
    public void scrollToElement(WebElement element) {
        try {
            js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
            waitShort();
        } catch (Exception e) {
            System.out.println("         ⚠️  Scroll xətası: " + e.getMessage());
        }
    }

    // Static wait metodları
    public void waitShort() {
        waitFor(TestConfig.getShortWait());
    }

    public void waitMedium() {
        waitFor(TestConfig.getMediumWait());
    }

    public void waitLong() {
        waitFor(TestConfig.getLongWait());
    }

    private void waitFor(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
}