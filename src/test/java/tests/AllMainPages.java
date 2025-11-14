package tests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import utils.ExtentReportManager;

import java.time.Duration;

public class AllMainPages {

    WebDriver driver;
    JavascriptExecutor js;
    WebDriverWait wait;

    // ExtentReports
    private static ExtentReports extent;
    private ExtentTest suiteTest;
    private ExtentTest menuTest;
    private ExtentTest buttonTest;

    // Menu buttonları - Adları sonra dəyişə bilərsiniz
    private static final String[][] MENU_BUTTONS = {
            {"Universities", "/html/body/header/div/nav/div/ul/li[1]/a", "Universitetlər səhifəsi"},
            {"Programs", "/html/body/header/div/nav/div/ul/li[2]/a", "Proqramlar səhifəsi"},
            {"Blogs", "/html/body/header/div/nav/div/ul/li[3]/a", "Ölkələr səhifəsi"},
            {"Visa Support", "/html/body/header/div/nav/div/ul/li[4]/a", "Xidmətlər səhifəsi"},
            {"About", "/html/body/header/div/nav/div/ul/li[5]/a", "Haqqımızda səhifəsi"},
            {"Contact", "/html/body/header/div/nav/div/ul/li[6]/a", "Əlaqə səhifəsi"}
    };

    @BeforeSuite
    public void setupSuite() {
        extent = ExtentReportManager.createInstance();
        suiteTest = extent.createTest("🧭 StudyLeo Ana Menyu Testləri", "Bütün əsas menyu buttonlarının test edilməsi");
    }

    @BeforeTest
    public void setup() {
        ChromeOptions options = new ChromeOptions();
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "true"));

        if (headless) {
            System.out.println("🚀 HEADLESS MODE aktivdir\n");
            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--disable-gpu");
            options.addArguments("--window-size=1920,1080");
            suiteTest.info("<span style='color: #ffffff !important;'>Browser: Chrome Headless Mode</span>");
        } else {
            System.out.println("🖥️ NORMAL MODE aktivdir\n");
            suiteTest.info("<span style='color: #ffffff !important;'>Browser: Chrome Normal Mode</span>");
        }

        driver = new ChromeDriver(options);
        js = (JavascriptExecutor) driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        if (!headless) {
            driver.manage().window().maximize();
        }

        suiteTest.pass("<span style='color: #ffffff !important;'>✅ Browser uğurla başladıldı</span>");
    }

    @Test
    public void testAllMenuButtons() {
        menuTest = suiteTest.createNode("📋 Ana Menyu Buttonları", "Bütün menyu buttonlarının tıklama və naviqasiya testi");

        System.out.println("\n" + "🎯".repeat(30));
        System.out.println("ANA MENYU BUTTONLARI TESTİ BAŞLADI");
        System.out.println("🎯".repeat(30) + "\n");

        // Ana səhifəyə get
        driver.get("https://studyleo.com/");
        menuTest.info("<span style='color: #ffffff !important;'>🌐 Ana səhifə açıldı: https://studyleo.com/</span>");
        waitFor(2000);

        int successCount = 0;
        int errorCount = 0;
        long startTime = System.currentTimeMillis();

        // Hər bir buttonu test et
        for (int i = 0; i < MENU_BUTTONS.length; i++) {
            String buttonName = MENU_BUTTONS[i][0];
            String xpath = MENU_BUTTONS[i][1];
            String description = MENU_BUTTONS[i][2];

            buttonTest = menuTest.createNode("🔘 " + buttonName + " Button", description);

            try {
                System.out.println("🔍 Button " + (i + 1) + "/" + MENU_BUTTONS.length + ": " + buttonName);
                buttonTest.info("<span style='color: #ffffff !important;'>🔍 Test başladı: " + buttonName + "</span>");

                // Ana səhifəyə qayıt (təkrar istifadə üçün)
                if (i > 0) {
                    driver.get("https://studyleo.com/");
                    waitFor(1500);
                    buttonTest.info("<span style='color: #ffffff !important;'>🏠 Ana səhifəyə qayıdıldı</span>");
                }

                String beforeUrl = driver.getCurrentUrl();
                System.out.println("   📍 Əvvəlki URL: " + beforeUrl);
                buttonTest.info("<span style='color: #ffffff !important;'>📍 Əvvəlki URL: " + beforeUrl + "</span>");

                // Buttonu tap və tıkla
                WebElement button = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));

                // Scroll to element
                js.executeScript("arguments[0].scrollIntoView({block: 'center'});", button);
                waitFor(500);

                // JavaScript ilə tıkla
                js.executeScript("arguments[0].click();", button);
                buttonTest.pass("<span style='color: #ffffff !important;'>✅ " + buttonName + " buttonuna tıklandı</span>");
                System.out.println("   ✅ Tıklama uğurlu");

                // URL dəyişməsini gözlə
                wait.until(ExpectedConditions.not(ExpectedConditions.urlToBe(beforeUrl)));
                waitFor(2000);

                String afterUrl = driver.getCurrentUrl();
                System.out.println("   📍 Yeni URL: " + afterUrl);
                buttonTest.info("<span style='color: #ffffff !important;'>📍 Yeni URL: <a href='" + afterUrl + "' target='_blank' style='color: #3498db !important;'>" + afterUrl + "</a></span>");

                // Səhifənin yükləndiyini yoxla
                if (checkPageLoaded(buttonTest, buttonName)) {
                    System.out.println("   ✅ Səhifə uğurla yükləndi\n");
                    buttonTest.pass("<span style='color: #ffffff !important;'>✅ TEST UĞURLU</span>");
                    successCount++;
                } else {
                    System.out.println("   ❌ Səhifə düzgün yüklənmədi\n");
                    buttonTest.fail("<span style='color: #ffffff !important;'>❌ TEST UĞURSUZ</span>");
                    errorCount++;
                }

            } catch (org.openqa.selenium.TimeoutException e) {
                errorCount++;
                System.out.println("   ❌ TIMEOUT XƏTASI: Element tapılmadı\n");

                String errorDetails = "Button: " + buttonName + "\n" +
                        "XPath: " + xpath + "\n\n" +
                        "Xəta: " + e.getMessage() + "\n\n" +
                        "Stack Trace:\n" + getStackTraceString(e);

                ExtentReportManager.logFailWithDetails(
                        buttonTest,
                        "Timeout xətası: " + buttonName + " buttonu tapılmadı",
                        errorDetails
                );

            } catch (org.openqa.selenium.ElementClickInterceptedException e) {
                errorCount++;
                System.out.println("   ❌ ELEMENT TIKLANMADI\n");

                String errorDetails = "Button: " + buttonName + "\n" +
                        "Səbəb: Başqa element tıklamanı bloklayır\n\n" +
                        "Xəta: " + e.getMessage() + "\n\n" +
                        "Stack Trace:\n" + getStackTraceString(e);

                ExtentReportManager.logFailWithDetails(
                        buttonTest,
                        buttonName + " buttonu tıklanamadı",
                        errorDetails
                );

            } catch (Exception e) {
                errorCount++;
                System.out.println("   ❌ ÜMUMI XƏTA\n");

                String errorDetails = "Button: " + buttonName + "\n" +
                        "Xəta tipi: " + e.getClass().getSimpleName() + "\n\n" +
                        "Mesaj: " + e.getMessage() + "\n\n" +
                        "Stack Trace:\n" + getStackTraceString(e);

                ExtentReportManager.logFailWithDetails(
                        buttonTest,
                        buttonName + " testində ümumi xəta",
                        errorDetails
                );
            }
        }

        long endTime = System.currentTimeMillis();
        long duration = (endTime - startTime) / 1000;
        double successRate = MENU_BUTTONS.length > 0 ? (successCount * 100.0 / MENU_BUTTONS.length) : 0;

        // Konsol nəticələri
        System.out.println("\n" + "=".repeat(70));
        System.out.println("📊 ANA MENYU BUTTONLARI TEST NƏTİCƏSİ");
        System.out.println("=".repeat(70));
        System.out.println("   📌 Ümumi:        " + MENU_BUTTONS.length + " button");
        System.out.println("   ✅ Uğurlu:       " + successCount + " button");
        System.out.println("   ❌ Xətalı:       " + errorCount + " button");
        System.out.println("   📈 Uğur faizi:   " + String.format("%.1f", successRate) + "%");
        System.out.println("   ⏱️  Müddət:       " + duration + " saniyə");
        System.out.println("   ⚡ Orta sürət:   " + String.format("%.1f", (double)duration / MENU_BUTTONS.length) + " s/button");
        System.out.println("=".repeat(70) + "\n");

        // ExtentReport nəticələri
        String summary = String.format(
                "<div style='background: #34495e; padding: 15px; border-radius: 8px; margin: 10px 0;'>" +
                        "<h4 style='color: #ffffff !important; margin: 0 0 10px 0;'>📊 Test Nəticəsi</h4>" +
                        "<table style='width: 100%%;'>" +
                        "<tr><td style='color: #ffffff !important;'>📌 Ümumi:</td><td style='color: #ffffff !important;'><strong>%d button</strong></td></tr>" +
                        "<tr><td style='color: #ffffff !important;'>✅ Uğurlu:</td><td><strong style='color: #2ecc71 !important;'>%d button</strong></td></tr>" +
                        "<tr><td style='color: #ffffff !important;'>❌ Xətalı:</td><td><strong style='color: #e74c3c !important;'>%d button</strong></td></tr>" +
                        "<tr><td style='color: #ffffff !important;'>📈 Uğur faizi:</td><td style='color: #ffffff !important;'><strong>%.1f%%</strong></td></tr>" +
                        "<tr><td style='color: #ffffff !important;'>⏱️ Müddət:</td><td style='color: #ffffff !important;'><strong>%d saniyə</strong></td></tr>" +
                        "<tr><td style='color: #ffffff !important;'>⚡ Orta sürət:</td><td style='color: #ffffff !important;'><strong>%.1f s/button</strong></td></tr>" +
                        "</table></div>",
                MENU_BUTTONS.length, successCount, errorCount, successRate, duration,
                MENU_BUTTONS.length > 0 ? (double)duration / MENU_BUTTONS.length : 0
        );

        menuTest.info(summary);

        if (successRate >= 90) {
            menuTest.pass("<span style='color: #ffffff !important;'>🎉 Əla nəticə! Bütün buttonlar işləyir. Uğur faizi: " + String.format("%.1f", successRate) + "%</span>");
        } else if (successRate >= 70) {
            menuTest.warning("<span style='color: #ffffff !important;'>⚠️ Yaxşı, amma təkmilləşdirilə bilər. Uğur faizi: " + String.format("%.1f", successRate) + "%</span>");
        } else {
            menuTest.fail("<span style='color: #ffffff !important;'>❌ Ciddi problemlər var! Uğur faizi: " + String.format("%.1f", successRate) + "%</span>");
        }
    }

    /**
     * Səhifənin düzgün yükləndiyini yoxlayır
     */
    private boolean checkPageLoaded(ExtentTest test, String buttonName) {
        try {
            // 1. Səhifə başlığını yoxla
            String title = driver.getTitle();
            if (title == null || title.isEmpty()) {
                test.fail("<span style='color: #ffffff !important;'>❌ Səhifə başlığı boşdur</span>");
                return false;
            }
            test.pass("<span style='color: #ffffff !important;'>✅ Başlıq: " + title + "</span>");

            // 2. URL-in düzgün olduğunu yoxla
            String url = driver.getCurrentUrl();
            if (url.equals("https://studyleo.com/") || url.equals("https://studyleo.com")) {
                test.fail("<span style='color: #ffffff !important;'>❌ URL dəyişmədi, hələ ana səhifədədir</span>");
                return false;
            }

            // 3. Xəta mesajı yoxla
            if (isElementVisible(By.xpath("//*[contains(text(), 'Something went wrong')]")) ||
                    isElementVisible(By.xpath("//*[contains(text(), '404')]")) ||
                    isElementVisible(By.xpath("//*[contains(text(), 'Not Found')]"))) {
                test.fail("<span style='color: #ffffff !important;'>❌ Səhifədə xəta mesajı var</span>");
                return false;
            }

            // 4. Məzmun var mı
            boolean hasH1 = isElementVisible(By.tagName("h1"));
            boolean hasH2 = isElementVisible(By.tagName("h2"));
            boolean hasContent = isElementVisible(By.tagName("main")) ||
                    isElementVisible(By.tagName("section"));

            if (!hasH1 && !hasH2 && !hasContent) {
                test.fail("<span style='color: #ffffff !important;'>❌ Səhifədə məzmun yoxdur</span>");
                return false;
            }
            test.pass("<span style='color: #ffffff !important;'>✅ Səhifə məzmunu mövcuddur</span>");

            // 5. H1 mətnini göstər
            try {
                WebElement h1 = driver.findElement(By.tagName("h1"));
                String h1Text = h1.getText();
                if (h1Text != null && !h1Text.trim().isEmpty()) {
                    test.info("<span style='color: #ffffff !important;'>📄 Səhifə başlığı: <strong>" + h1Text + "</strong></span>");
                }
            } catch (Exception e) {
                // H1 yoxdursa problem deyil
            }

            return true;

        } catch (Exception e) {
            test.fail("<span style='color: #ffffff !important;'>❌ Yoxlama zamanı xəta: " + e.getMessage() + "</span>");
            return false;
        }
    }

    private boolean isElementVisible(By locator) {
        try {
            WebElement element = driver.findElement(locator);
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    private String getStackTraceString(Exception e) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : e.getStackTrace()) {
            sb.append(element.toString()).append("\n");
        }
        return sb.toString();
    }

    private void waitFor(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            System.out.println("\n🔚 Browser bağlanır...\n");
            driver.quit();
        }
    }

    @AfterSuite
    public void tearDownSuite() {
        System.out.println("\n🏁 Bütün testlər tamamlandı!");
        ExtentReportManager.flush();
    }
}