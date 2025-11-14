package tests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import config.TestConfig;
import model.ButtonConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.*;
import utils.EmailSender;
import utils.ExtentReportManager;
import utils.WaitHelper;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class UniversityButtonsFullTest {

    WebDriver driver;
    JavascriptExecutor js;
    WaitHelper waitHelper;

    private static ExtentReports extent;
    private ExtentTest suiteTest;
    private ExtentTest pageTest;
    private ExtentTest universityTest;
    private ExtentTest buttonTest;

    // ✅ Tip-safe button konfiqurasiyası
    private static final List<ButtonConfig> UNIVERSITY_BUTTONS = new ArrayList<>();

    static {
        UNIVERSITY_BUTTONS.add(new ButtonConfig("Admission Requirements",
                "/html/body/section[1]/div/div[2]/div[3]/a", "Qəbul tələbləri səhifəsi", false));
        UNIVERSITY_BUTTONS.add(new ButtonConfig("Apply Now",
                "/html/body/section[1]/div/div[2]/div[3]/button", "Müraciət forması (Modal)", false));
        UNIVERSITY_BUTTONS.add(new ButtonConfig("Rankings - View More",
                "/html/body/section[2]/a", "Reytinqlər səhifəsi", false));
        UNIVERSITY_BUTTONS.add(new ButtonConfig("Programs - View More",
                "/html/body/div[4]/section/div/div[3]/a", "Proqramlar səhifəsi", false));
        UNIVERSITY_BUTTONS.add(new ButtonConfig("Admission Req - View More",
                "/html/body/section[4]/a", "Qəbul tələbləri ətraflı", false));
        UNIVERSITY_BUTTONS.add(new ButtonConfig("Galleries - View More",
                "/html/body/section[5]/div[2]/section/div[2]/a", "Qalereya səhifəsi", false));
        UNIVERSITY_BUTTONS.add(new ButtonConfig("Dormitories - View More",
                "/html/body/section[6]/a", "Yataqxanalar səhifəsi", false));
        UNIVERSITY_BUTTONS.add(new ButtonConfig("International Students",
                "/html/body/section[7]/div[2]/div/a[1]", "Beynəlxalq tələbələr", false));
        UNIVERSITY_BUTTONS.add(new ButtonConfig("Campuses - View More",
                "/html/body/div[5]/section/a", "Kampuslar (2+ kampus)", true,
                "/html/body/div[5]/section/div[1]/div[1]/div[2]/a",
                "/html/body/div[5]/section/div[1]/div[2]/div[2]/a"));
        UNIVERSITY_BUTTONS.add(new ButtonConfig("Transportation Options",
                "/html/body/section[8]/div[1]/a", "Nəqliyyat variantları", false));
        UNIVERSITY_BUTTONS.add(new ButtonConfig("Visa Support",
                "/html/body/section[9]/div[2]/a", "Viza dəstəyi", false));
        UNIVERSITY_BUTTONS.add(new ButtonConfig("FAQs - View More",
                "/html/body/section[10]/a", "Tez-tez verilən suallar", false));
        UNIVERSITY_BUTTONS.add(new ButtonConfig("Reviews - View More",
                "/html/body/section[11]/a", "Rəylər səhifəsi", false));
    }

    @BeforeSuite
    public void setupSuite() {
        extent = ExtentReportManager.createInstance();
        suiteTest = extent.createTest("🎓 Bütün Universitetlərin Daxili Button Testləri",
                "4 səhifədəki bütün universitetlərin daxilindəki buttonların test edilməsi");
    }

    @BeforeTest
    public void setup() {
        ChromeOptions options = new ChromeOptions();
        boolean headless = TestConfig.isHeadless();

        options.addArguments("--disable-extensions");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-infobars");
        options.addArguments("--no-first-run");

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
        waitHelper = new WaitHelper(driver);

        // ✅ Config-dən timeout-lar
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(TestConfig.getPageLoadTimeout()));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(TestConfig.getImplicitTimeout()));

        if (!headless) {
            driver.manage().window().maximize();
        }

        suiteTest.pass("<span style='color: #ffffff !important;'>✅ Browser uğurla başladıldı</span>");
    }

    @DataProvider(name = "universityPages")
    public Object[][] universityPagesData() {
        return new Object[][] {
                {1, TestConfig.getBaseUrl(), 12},
                {2, TestConfig.getBaseUrl() + "?page=2", 12},
                {3, TestConfig.getBaseUrl() + "?page=3", 12},
                {4, TestConfig.getBaseUrl() + "?page=4", 5}
        };
    }

    @Test(dataProvider = "universityPages")
    public void testUniversityPage(int pageNumber, String url, int universityCount) {
        pageTest = suiteTest.createNode("📄 Səhifə " + pageNumber + " - Universitetlər",
                "Səhifə " + pageNumber + "-dəki universitetlərin button testləri");
        testUniversitiesPage(pageNumber, url, universityCount);
    }

    private void testUniversitiesPage(int pageNumber, String url, int universityCount) {
        System.out.println("\n\n" + "═".repeat(80));
        System.out.println("📄 SƏHIFƏ " + pageNumber + " - UNİVERSİTET BUTTON TESTLƏRİ BAŞLADI");
        System.out.println("═".repeat(80) + "\n");

        pageTest.info("<span style='color: #ffffff !important;'>🔗 URL: " + url + "</span>");
        pageTest.info("<span style='color: #ffffff !important;'>📊 Test ediləcək universitet sayı: " + universityCount + "</span>");

        driver.get(url);
        waitHelper.waitForPageLoad();
        waitHelper.waitMedium();

        int totalUniversitiesSuccess = 0;
        int totalUniversitiesError = 0;

        for (int i = 1; i <= universityCount; i++) {
            System.out.println("\n" + "━".repeat(80));
            System.out.println("🏛️  SƏHIFƏ " + pageNumber + " - UNİVERSİTET " + i + "/" + universityCount);
            System.out.println("━".repeat(80));

            try {
                String xpath = pageNumber == 1
                        ? "/html/body/div[3]/section/div/div/div[1]/div[" + i + "]/a/div[1]"
                        : "/html/body/div[3]/section/div/div/div[1]/div[" + i + "]/a";

                String universityName = "Universitet " + i;
                try {
                    String nameXpath = pageNumber == 1
                            ? "/html/body/div[3]/section/div/div/div[1]/div[" + i + "]/a/div[2]/h3"
                            : "/html/body/div[3]/section/div/div/div[1]/div[" + i + "]/a//h3";

                    WebElement nameElement = driver.findElement(By.xpath(nameXpath));
                    universityName = nameElement.getText();
                } catch (Exception e) {
                    // Default
                }

                universityTest = pageTest.createNode("🏛️ " + universityName,
                        "Səhifə " + pageNumber + " - " + universityName + " daxili button testləri");

                System.out.println("   📍 Universitet: " + universityName);
                universityTest.info("<span style='color: #ffffff !important;'>📍 Test edilir: " + universityName + "</span>");

                WebElement uniElement = waitHelper.waitForElementClickable(By.xpath(xpath));
                js.executeScript("arguments[0].click();", uniElement);
                waitHelper.waitForPageLoad();
                waitHelper.waitLong();

                String universityUrl = driver.getCurrentUrl();
                System.out.println("   🔗 URL: " + universityUrl);
                universityTest.info("<span style='color: #ffffff !important;'>🔗 URL: <a href='" + universityUrl + "' target='_blank' style='color: #3498db !important;'>" + universityUrl + "</a></span>");

                waitHelper.waitForPageLoad();
                waitHelper.waitForAjax();
                waitHelper.waitMedium();

                boolean universitySuccess = testUniversityButtons(universityUrl);

                if (universitySuccess) {
                    totalUniversitiesSuccess++;
                } else {
                    totalUniversitiesError++;
                }

                System.out.println("   🔙 Səhifə " + pageNumber + " - Universitet listinə qayıdılır...");
                driver.get(url);
                waitHelper.waitForPageLoad();
                waitHelper.waitMedium();

                // ✅ Smart scroll - elementin özünə scroll et
                try {
                    WebElement currentUniElement = driver.findElement(By.xpath(xpath));
                    waitHelper.scrollToElement(currentUniElement);
                } catch (Exception e) {
                    // Fallback: eski üsul
                    int scrollY = (i > 8) ? 200 : (i > 4) ? 100 : 0;
                    js.executeScript("window.scrollTo(0, " + scrollY + ");");
                }
                waitHelper.waitShort();

            } catch (Exception e) {
                totalUniversitiesError++;
                System.err.println("   ❌ Universitet " + i + " açılarkən xəta: " + e.getMessage());

                universityTest = pageTest.createNode("🏛️ Universitet " + i, "Xəta baş verdi");
                String errorDetails = "Universitet açılarkən xəta:\n\n" + e.getMessage() + "\n\nStack Trace:\n" + getStackTraceString(e);
                ExtentReportManager.logFailWithDetails(universityTest, "Universitet açılmadı", errorDetails);

                try {
                    driver.get(url);
                    waitHelper.waitForPageLoad();
                    waitHelper.waitMedium();
                } catch (Exception e2) {
                    System.err.println("   ❌ Geri dönmə xətası: " + e2.getMessage());
                }
            }
        }

        printPageResults(pageNumber, universityCount, totalUniversitiesSuccess, totalUniversitiesError);
    }

    private boolean testUniversityButtons(String universityUrl) {
        waitHelper.waitForPageLoad();

        int successCount = 0;
        int errorCount = 0;
        int skippedCount = 0;
        int totalButtonsTested = 0;

        for (int i = 0; i < UNIVERSITY_BUTTONS.size(); i++) {
            ButtonConfig buttonConfig = UNIVERSITY_BUTTONS.get(i);

            if (buttonConfig.isDynamic() && buttonConfig.getName().contains("Campuses")) {
                System.out.println("\n      🔍 Campuses Dinamik Yoxlama");

                WebElement mainCampusButton = findButton(buttonConfig.getXpath());

                if (mainCampusButton != null) {
                    waitHelper.scrollToElement(mainCampusButton);
                }

                if (mainCampusButton != null && mainCampusButton.isDisplayed()) {
                    System.out.println("         ✅ Əsas Campuses View More tapıldı");
                    buttonTest = universityTest.createNode("🔘 " + buttonConfig.getName() + " (2+ kampus)",
                            "Əsas campuses view more");

                    totalButtonsTested++;
                    if (testSingleButton(mainCampusButton, buttonConfig.getName(), buttonConfig.getXpath(), universityUrl)) {
                        successCount++;
                    } else {
                        errorCount++;
                    }

                } else {
                    System.out.println("         ⚠️  Əsas button yoxdur, ayrı kampuslar yoxlanır...");

                    List<String> alternatives = buttonConfig.getAlternativeXPaths();
                    for (int c = 0; c < alternatives.size(); c++) {
                        WebElement campusButton = findButton(alternatives.get(c));

                        if (campusButton != null) {
                            waitHelper.scrollToElement(campusButton);
                        }

                        if (campusButton != null && campusButton.isDisplayed()) {
                            System.out.println("         ✅ Campus " + (c + 1) + " View More tapıldı");
                            buttonTest = universityTest.createNode("🔘 Campus " + (c + 1) + " - View More",
                                    "Campus " + (c + 1) + " səhifəsi");

                            totalButtonsTested++;
                            if (testSingleButton(campusButton, "Campus " + (c + 1) + " View More", alternatives.get(c), universityUrl)) {
                                successCount++;
                            } else {
                                errorCount++;
                            }
                        } else {
                            System.out.println("         ⚠️  Campus " + (c + 1) + " button tapılmadı");
                        }
                    }

                    if (totalButtonsTested == i) {
                        System.out.println("         ⚠️  Heç bir campuses buttonu tapılmadı (Skip)");
                        buttonTest = universityTest.createNode("🔘 Campuses", "Campuses yoxdur");
                        buttonTest.warning("<span style='color: #ffffff !important;'>⚠️ Skip (campuses yoxdur)</span>");
                        skippedCount++;
                        totalButtonsTested++;
                    }
                }

            } else {
                buttonTest = universityTest.createNode("🔘 " + buttonConfig.getName(), buttonConfig.getDescription());
                totalButtonsTested++;

                try {
                    System.out.println("\n      🔘 Button " + (i + 1) + "/" + UNIVERSITY_BUTTONS.size() + ": " + buttonConfig.getName());
                    buttonTest.info("<span style='color: #ffffff !important;'>🔍 Test: " + buttonConfig.getName() + "</span>");

                    WebElement button = findButton(buttonConfig.getXpath());

                    if (button == null) {
                        System.out.println("         ⚠️  Skip (tapılmadı)");
                        buttonTest.warning("<span style='color: #ffffff !important;'>⚠️ Skip</span>");
                        skippedCount++;
                        continue;
                    }

                    waitHelper.scrollToElement(button);

                    if (!button.isDisplayed()) {
                        System.out.println("         ⚠️  Skip (görünmür)");
                        buttonTest.warning("<span style='color: #ffffff !important;'>⚠️ Skip</span>");
                        skippedCount++;
                        continue;
                    }

                    highlightElement(button);

                    if (buttonConfig.getName().equals("Apply Now")) {
                        testApplyNowButton(button);
                        successCount++;
                        System.out.println("         ✅ Uğurlu");
                        continue;
                    }

                    if (testSingleButton(button, buttonConfig.getName(), buttonConfig.getXpath(), universityUrl)) {
                        successCount++;
                    } else {
                        errorCount++;
                    }

                } catch (Exception e) {
                    errorCount++;
                    System.out.println("         ❌ Xəta: " + e.getMessage());
                    ExtentReportManager.logFailWithDetails(buttonTest, buttonConfig.getName() + " xətası", e.getMessage());
                }
            }
        }

        double successRate = totalButtonsTested > 0 ? (successCount * 100.0 / totalButtonsTested) : 0;

        System.out.println("\n   📊 Universitet Button Nəticəsi:");
        System.out.println("      Test edilən: " + totalButtonsTested);
        System.out.println("      ✅ Uğurlu:    " + successCount);
        System.out.println("      ❌ Xətalı:    " + errorCount);
        System.out.println("      ⚠️  Keçirildi:" + skippedCount);
        System.out.println("      📈 Uğur:      " + String.format("%.1f", successRate) + "%");

        String summary = String.format(
                "<div style='background: #2c3e50; padding: 10px; border-radius: 5px; margin: 5px 0;'>" +
                        "<table style='width: 100%%;'>" +
                        "<tr><td style='color: #ffffff !important;'>📊 Test edilən:</td><td style='color: #ffffff !important;'><strong>%d button</strong></td></tr>" +
                        "<tr><td style='color: #ffffff !important;'>✅ Uğurlu:</td><td style='color: #2ecc71 !important;'><strong>%d</strong></td></tr>" +
                        "<tr><td style='color: #ffffff !important;'>❌ Xətalı:</td><td style='color: #e74c3c !important;'><strong>%d</strong></td></tr>" +
                        "<tr><td style='color: #ffffff !important;'>⚠️ Keçirildi:</td><td style='color: #f39c12 !important;'><strong>%d</strong></td></tr>" +
                        "<tr><td style='color: #ffffff !important;'>📈 Uğur:</td><td style='color: #ffffff !important;'><strong>%.1f%%</strong></td></tr>" +
                        "</table></div>",
                totalButtonsTested, successCount, errorCount, skippedCount, successRate
        );

        universityTest.info(summary);

        return errorCount == 0;
    }

    /**
     * ✅ Config-dən retry count istifadə edir
     */
    private WebElement findButton(String xpath) {
        return waitHelper.findElementWithRetry(By.xpath(xpath), TestConfig.getRetryCount());
    }

    private boolean testSingleButton(WebElement button, String buttonName, String xpath, String universityUrl) {
        try {
            String beforeUrl = driver.getCurrentUrl();
            js.executeScript("arguments[0].click();", button);
            buttonTest.pass("<span style='color: #ffffff !important;'>✅ Tıklandı</span>");

            waitHelper.waitForUrlChange(beforeUrl);
            waitHelper.waitForPageLoad();
            waitHelper.waitForAjax();
            waitHelper.waitMedium();

            String afterUrl = driver.getCurrentUrl();

            if (!afterUrl.equals(beforeUrl)) {
                buttonTest.info("<span style='color: #ffffff !important;'>📍 URL: <a href='" + afterUrl + "' target='_blank' style='color: #3498db !important;'>" + afterUrl + "</a></span>");

                boolean pageLoaded = checkPageLoaded(buttonTest, buttonName);

                System.out.println("         🔙 Geri: " + universityUrl);
                driver.get(universityUrl);
                waitHelper.waitForPageLoad();
                waitHelper.waitForAjax();
                waitHelper.waitMedium();

                if (pageLoaded) {
                    System.out.println("         ✅ Uğurlu");
                    buttonTest.pass("<span style='color: #ffffff !important;'>✅ Uğurlu</span>");
                    return true;
                } else {
                    System.out.println("         ❌ Səhifə yüklənmədi");
                    buttonTest.fail("<span style='color: #ffffff !important;'>❌ Səhifə yüklənmədi</span>");
                    return false;
                }
            } else {
                System.out.println("         ⚠️  URL dəyişmədi");
                buttonTest.warning("<span style='color: #ffffff !important;'>⚠️ URL dəyişmədi</span>");
                return false;
            }
        } catch (Exception e) {
            System.out.println("         ❌ Xəta: " + e.getMessage());
            buttonTest.fail("<span style='color: #ffffff !important;'>❌ Xəta</span>");
            try {
                driver.get(universityUrl);
                waitHelper.waitForPageLoad();
                waitHelper.waitMedium();
            } catch (Exception e2) {}
            return false;
        }
    }

    private void testApplyNowButton(WebElement applyButton) {
        try {
            js.executeScript("arguments[0].click();", applyButton);
            buttonTest.pass("<span style='color: #ffffff !important;'>✅ Modal açıldı</span>");
            waitHelper.waitMedium();

            try {
                WebElement closeButton = waitHelper.waitForElementClickable(By.xpath("/html/body/div[7]/button"));
                highlightElement(closeButton);
                js.executeScript("arguments[0].click();", closeButton);
                buttonTest.pass("<span style='color: #ffffff !important;'>✅ Modal bağlandı</span>");
            } catch (Exception e) {
                driver.findElement(By.tagName("body")).sendKeys(Keys.ESCAPE);
                buttonTest.warning("<span style='color: #ffffff !important;'>⚠️ ESC ilə bağlandı</span>");
            }

            waitHelper.waitShort();

        } catch (Exception e) {
            buttonTest.fail("<span style='color: #ffffff !important;'>❌ Modal xətası</span>");
        }
    }

    private void highlightElement(WebElement element) {
        try {
            String originalStyle = element.getAttribute("style");
            js.executeScript(
                    "arguments[0].setAttribute('style', 'border: 3px solid yellow; box-shadow: 0 0 10px yellow;');",
                    element
            );
            waitHelper.waitShort();
            js.executeScript(
                    "arguments[0].setAttribute('style', '" + (originalStyle != null ? originalStyle : "") + "');",
                    element
            );
        } catch (Exception e) {
            // Ignore
        }
    }

    private boolean checkPageLoaded(ExtentTest test, String buttonName) {
        try {
            String[] errorMessages = {
                    "Something went wrong",
                    "Page not found",
                    "404",
                    "Not Found",
                    "No data available",
                    "No data",
                    "Error occurred",
                    "Oops"
            };

            for (String errorMsg : errorMessages) {
                try {
                    WebElement errorElement = driver.findElement(By.xpath("//*[contains(text(), '" + errorMsg + "')]"));
                    if (errorElement.isDisplayed()) {
                        test.fail("<span style='color: #ffffff !important;'>❌ Xəta mesajı: '" + errorMsg + "'</span>");
                        System.out.println("         ❌ XƏTA MESAJI: " + errorMsg);
                        return false;
                    }
                } catch (Exception e) {
                    // Bu xəta mesajı yoxdur
                }
            }

            String title = driver.getTitle();
            if (title == null || title.isEmpty()) {
                test.fail("<span style='color: #ffffff !important;'>❌ Səhifə başlığı boşdur</span>");
                return false;
            }
            test.pass("<span style='color: #ffffff !important;'>✅ Başlıq: " + title + "</span>");

            boolean hasContent = isElementVisible(By.tagName("h1")) ||
                    isElementVisible(By.tagName("h2")) ||
                    isElementVisible(By.tagName("main")) ||
                    isElementVisible(By.tagName("section"));

            if (!hasContent) {
                test.fail("<span style='color: #ffffff !important;'>❌ Səhifədə məzmun yoxdur</span>");
                return false;
            }
            test.pass("<span style='color: #ffffff !important;'>✅ Səhifə məzmunu mövcuddur</span>");

            return true;

        } catch (Exception e) {
            test.fail("<span style='color: #ffffff !important;'>❌ Yoxlama xətası: " + e.getMessage() + "</span>");
            return false;
        }
    }

    private void printPageResults(int pageNumber, int total, int success, int error) {
        double successRate = total > 0 ? (success * 100.0 / total) : 0;

        System.out.println("\n\n" + "═".repeat(80));
        System.out.println("📊 SƏHIFƏ " + pageNumber + " NƏTİCƏSİ");
        System.out.println("═".repeat(80));
        System.out.println("   📌 Ümumi universitet:  " + total);
        System.out.println("   ✅ Uğurlu:             " + success);
        System.out.println("   ❌ Xətalı:             " + error);
        System.out.println("   📈 Uğur faizi:         " + String.format("%.1f", successRate) + "%");
        System.out.println("═".repeat(80) + "\n");

        String summary = String.format(
                "<div style='background: #34495e; padding: 15px; border-radius: 8px; margin: 10px 0;'>" +
                        "<h4 style='color: #ffffff !important;'>📊 Səhifə %d Nəticəsi</h4>" +
                        "<table style='width: 100%%;'>" +
                        "<tr><td style='color: #ffffff !important;'>📌 Ümumi:</td><td style='color: #ffffff !important;'><strong>%d universitet</strong></td></tr>" +
                        "<tr><td style='color: #ffffff !important;'>✅ Uğurlu:</td><td><strong style='color: #2ecc71 !important;'>%d universitet</strong></td></tr>" +
                        "<tr><td style='color: #ffffff !important;'>❌ Xətalı:</td><td><strong style='color: #e74c3c !important;'>%d universitet</strong></td></tr>" +
                        "<tr><td style='color: #ffffff !important;'>📈 Uğur faizi:</td><td style='color: #ffffff !important;'><strong>%.1f%%</strong></td></tr>" +
                        "</table></div>",
                pageNumber, total, success, error, successRate
        );

        pageTest.info(summary);

        if (successRate >= 90) {
            pageTest.pass("<span style='color: #ffffff !important;'>🎉 Səhifə " + pageNumber + " əla!</span>");
        } else if (successRate >= 70) {
            pageTest.warning("<span style='color: #ffffff !important;'>⚠️ Səhifə " + pageNumber + " yaxşı</span>");
        } else {
            pageTest.fail("<span style='color: #ffffff !important;'>❌ Səhifə " + pageNumber + " problemli</span>");
        }
    }

    private boolean isElementVisible(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
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

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            System.out.println("\n🔚 Browser bağlanır...\n");
            driver.quit();
        }
    }

    @AfterSuite
    public void tearDownSuite() {
        System.out.println("\n\n" + "═".repeat(80));
        System.out.println("🏁 BÜTÜN TESTLƏR TAMAMLANDI!");
        System.out.println("   📊 4 səhifə × 41 universitet test edildi");
        System.out.println("   🔘 Dinamik button sayı (13-14 arası)");
        System.out.println("═".repeat(80) + "\n");

        ExtentReportManager.flush();

        try {
            System.out.println("📧 Report email ilə göndərilir...\n");

            File reportsDir = new File(System.getProperty("user.dir") + "/test-reports/");

            if (!reportsDir.exists() || !reportsDir.isDirectory()) {
                System.err.println("❌ test-reports qovluğu tapılmadı!");
                return;
            }

            // ✅ Timestamp-ə görə sort et
            File[] reportFiles = reportsDir.listFiles((dir, name) ->
                    name.startsWith("UniversityTest_Report_") && name.endsWith(".html")
            );

            if (reportFiles == null || reportFiles.length == 0) {
                System.err.println("❌ Heç bir report fayl tapılmadı!");
                System.err.println("   Qovluq: " + reportsDir.getAbsolutePath());
                return;
            }

            // ✅ Fayl adından timestamp çıxart və müqayisə et
            File latestReport = reportFiles[0];
            long latestTimestamp = extractTimestampFromFilename(latestReport.getName());

            for (File file : reportFiles) {
                long fileTimestamp = extractTimestampFromFilename(file.getName());
                if (fileTimestamp > latestTimestamp) {
                    latestReport = file;
                    latestTimestamp = fileTimestamp;
                }
            }

            System.out.println("✅ Report faylı tapıldı:");
            System.out.println("   📄 Ad: " + latestReport.getName());
            System.out.println("   📁 Yol: " + latestReport.getAbsolutePath());
            System.out.println("   📊 Ölçü: " + (latestReport.length() / 1024) + " KB");
            System.out.println("   🕐 Yaradılma: " + new java.text.SimpleDateFormat("dd.MM.yyyy HH:mm:ss")
                    .format(new java.util.Date(latestReport.lastModified())));
            System.out.println();

            EmailSender.sendReport(latestReport.getAbsolutePath());

        } catch (Exception e) {
            System.err.println("❌ Email göndərmə xətası: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * ✅ Fayl adından timestamp çıxar
     * Format: UniversityTest_Report_20251114_090846.html
     */
    private long extractTimestampFromFilename(String filename) {
        try {
            // UniversityTest_Report_20251114_090846.html -> 20251114090846
            String timestampStr = filename
                    .replace("UniversityTest_Report_", "")
                    .replace(".html", "")
                    .replace("_", "");
            return Long.parseLong(timestampStr);
        } catch (Exception e) {
            return 0L;
        }
    }
}