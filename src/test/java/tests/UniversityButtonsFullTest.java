package tests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
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
import utils.EmailSender;
import utils.ExtentReportManager;

import java.io.File;
import java.time.Duration;

public class UniversityButtonsFullTest {

    WebDriver driver;
    JavascriptExecutor js;
    WebDriverWait wait;

    private static ExtentReports extent;
    private ExtentTest suiteTest;
    private ExtentTest pageTest;
    private ExtentTest universityTest;
    private ExtentTest buttonTest;

    private static final Object[][] UNIVERSITY_BUTTONS = {
            {"Admission Requirements", "/html/body/section[1]/div/div[2]/div[3]/a", "Qəbul tələbləri səhifəsi", false, new String[]{}},
            {"Apply Now", "/html/body/section[1]/div/div[2]/div[3]/button", "Müraciət forması (Modal)", false, new String[]{}},
            {"Rankings - View More", "/html/body/section[2]/a", "Reytinqlər səhifəsi", false, new String[]{}},
            {"Programs - View More", "/html/body/div[4]/section/div/div[3]/a", "Proqramlar səhifəsi", false, new String[]{}},
            {"Admission Req - View More", "/html/body/section[4]/a", "Qəbul tələbləri ətraflı", false, new String[]{}},
            {"Galleries - View More", "/html/body/section[5]/div[2]/section/div[2]/a", "Qalereya səhifəsi", false, new String[]{}},
            {"Dormitories - View More", "/html/body/section[6]/a", "Yataqxanalar səhifəsi", false, new String[]{}},
            {"International Students", "/html/body/section[7]/div[2]/div/a[1]", "Beynəlxalq tələbələr", false, new String[]{}},

            {"Campuses - View More", "/html/body/div[5]/section/a", "Kampuslar (2+ kampus)", true, new String[]{
                    "/html/body/div[5]/section/div[1]/div[1]/div[2]/a",
                    "/html/body/div[5]/section/div[1]/div[2]/div[2]/a"
            }},

            {"Transportation Options", "/html/body/section[8]/div[1]/a", "Nəqliyyat variantları", false, new String[]{}},
            {"Visa Support", "/html/body/section[9]/div[2]/a", "Viza dəstəyi", false, new String[]{}},
            {"FAQs - View More", "/html/body/section[10]/a", "Tez-tez verilən suallar", false, new String[]{}},
            {"Reviews - View More", "/html/body/section[11]/a", "Rəylər səhifəsi", false, new String[]{}}
    };

    @BeforeSuite
    public void setupSuite() {
        extent = ExtentReportManager.createInstance();
        suiteTest = extent.createTest("🎓 Bütün Universitetlərin Daxili Button Testləri",
                "4 səhifədəki bütün universitetlərin daxilindəki buttonların test edilməsi");
    }

    @BeforeTest
    public void setup() {
        ChromeOptions options = new ChromeOptions();
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "true"));

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
        wait = new WebDriverWait(driver, Duration.ofSeconds(12));

        if (!headless) {
            driver.manage().window().maximize();
        }

        suiteTest.pass("<span style='color: #ffffff !important;'>✅ Browser uğurla başladıldı</span>");
    }

    @Test(priority = 1)
    public void testPage1Universities() {
        pageTest = suiteTest.createNode("📄 Səhifə 1 - Universitetlər", "Səhifə 1-dəki universitetlərin button testləri");
        testUniversitiesPage(1, "https://studyleo.com/en/universities", 12);
    }

    @Test(priority = 2)
    public void testPage2Universities() {
        pageTest = suiteTest.createNode("📄 Səhifə 2 - Universitetlər", "Səhifə 2-dəki universitetlərin button testləri");
        testUniversitiesPage(2, "https://studyleo.com/en/universities?page=2", 12);
    }

    @Test(priority = 3)
    public void testPage3Universities() {
        pageTest = suiteTest.createNode("📄 Səhifə 3 - Universitetlər", "Səhifə 3-dəki universitetlərin button testləri");
        testUniversitiesPage(3, "https://studyleo.com/en/universities?page=3", 12);
    }

    @Test(priority = 4)
    public void testPage4Universities() {
        pageTest = suiteTest.createNode("📄 Səhifə 4 - Universitetlər", "Səhifə 4-dəki universitetlərin button testləri");
        testUniversitiesPage(4, "https://studyleo.com/en/universities?page=4", 5);
    }

    private void testUniversitiesPage(int pageNumber, String url, int universityCount) {
        System.out.println("\n\n" + "═".repeat(80));
        System.out.println("📄 SƏHIFƏ " + pageNumber + " - UNİVERSİTET BUTTON TESTLƏRİ BAŞLADI");
        System.out.println("═".repeat(80) + "\n");

        pageTest.info("<span style='color: #ffffff !important;'>🔗 URL: " + url + "</span>");
        pageTest.info("<span style='color: #ffffff !important;'>📊 Test ediləcək universitet sayı: " + universityCount + "</span>");

        driver.get(url);
        waitFor(1900);

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

                WebElement uniElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
                js.executeScript("arguments[0].click();", uniElement);
                waitFor(2500);

                String universityUrl = driver.getCurrentUrl();
                System.out.println("   🔗 URL: " + universityUrl);
                universityTest.info("<span style='color: #ffffff !important;'>🔗 URL: <a href='" + universityUrl + "' target='_blank' style='color: #3498db !important;'>" + universityUrl + "</a></span>");

                waitFor(1000);

                boolean universitySuccess = testUniversityButtons(universityUrl);

                if (universitySuccess) {
                    totalUniversitiesSuccess++;
                } else {
                    totalUniversitiesError++;
                }

                System.out.println("   🔙 Səhifə " + pageNumber + " - Universitet listinə qayıdılır...");
                driver.get(url);
                waitFor(1900);

                // ✅ 4-lü Grid Məntiqli Scroll
                int scrollY = (i > 8) ? 200 : (i > 4) ? 100 : 0;
                String scrollInfo = (i > 8) ? "Sətir 3 (200px)" : (i > 4) ? "Sətir 2 (100px)" : "Sətir 1 (0px)";
                js.executeScript("window.scrollTo(0, " + scrollY + ");");
                System.out.println("   📜 " + scrollInfo);
                waitFor(500);

            } catch (Exception e) {
                totalUniversitiesError++;
                System.err.println("   ❌ Universitet " + i + " açılarkən xəta: " + e.getMessage());

                universityTest = pageTest.createNode("🏛️ Universitet " + i, "Xəta baş verdi");
                String errorDetails = "Universitet açılarkən xəta:\n\n" + e.getMessage() + "\n\nStack Trace:\n" + getStackTraceString(e);
                ExtentReportManager.logFailWithDetails(universityTest, "Universitet açılmadı", errorDetails);

                try {
                    driver.get(url);
                    waitFor(1900);
                    int scrollY = (i > 8) ? 200 : (i > 4) ? 100 : 0;
                    js.executeScript("window.scrollTo(0, " + scrollY + ");");
                    waitFor(500);
                } catch (Exception e2) {
                    System.err.println("   ❌ Geri dönmə xətası: " + e2.getMessage());
                }
            }
        }

        printPageResults(pageNumber, universityCount, totalUniversitiesSuccess, totalUniversitiesError);
    }

    private boolean testUniversityButtons(String universityUrl) {
        int successCount = 0;
        int errorCount = 0;
        int skippedCount = 0;
        int totalButtonsTested = 0;

        for (int i = 0; i < UNIVERSITY_BUTTONS.length; i++) {
            String buttonName = (String) UNIVERSITY_BUTTONS[i][0];
            String primaryXPath = (String) UNIVERSITY_BUTTONS[i][1];
            String description = (String) UNIVERSITY_BUTTONS[i][2];
            boolean isDynamic = (boolean) UNIVERSITY_BUTTONS[i][3];
            String[] alternativeXPaths = (String[]) UNIVERSITY_BUTTONS[i][4];

            if (isDynamic && buttonName.contains("Campuses")) {
                System.out.println("\n      🔍 Campuses Dinamik Yoxlama");

                WebElement mainCampusButton = findButton(primaryXPath);

                if (mainCampusButton != null) {
                    scrollToElement(mainCampusButton); // ✅ Avtomatik scroll
                }

                if (mainCampusButton != null && mainCampusButton.isDisplayed()) {
                    System.out.println("         ✅ Əsas Campuses View More tapıldı");
                    buttonTest = universityTest.createNode("🔘 " + buttonName + " (2+ kampus)", "Əsas campuses view more");

                    totalButtonsTested++;
                    if (testSingleButton(mainCampusButton, buttonName, primaryXPath, universityUrl)) {
                        successCount++;
                    } else {
                        errorCount++;
                    }

                } else {
                    System.out.println("         ⚠️  Əsas button yoxdur, ayrı kampuslar yoxlanır...");

                    for (int c = 0; c < alternativeXPaths.length; c++) {
                        WebElement campusButton = findButton(alternativeXPaths[c]);

                        if (campusButton != null) {
                            scrollToElement(campusButton); // ✅ Avtomatik scroll
                        }

                        if (campusButton != null && campusButton.isDisplayed()) {
                            System.out.println("         ✅ Campus " + (c + 1) + " View More tapıldı");
                            buttonTest = universityTest.createNode("🔘 Campus " + (c + 1) + " - View More",
                                    "Campus " + (c + 1) + " səhifəsi");

                            totalButtonsTested++;
                            if (testSingleButton(campusButton, "Campus " + (c + 1) + " View More", alternativeXPaths[c], universityUrl)) {
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
                buttonTest = universityTest.createNode("🔘 " + buttonName, description);
                totalButtonsTested++;

                try {
                    System.out.println("\n      🔘 Button " + (i + 1) + "/" + UNIVERSITY_BUTTONS.length + ": " + buttonName);
                    buttonTest.info("<span style='color: #ffffff !important;'>🔍 Test: " + buttonName + "</span>");

                    WebElement button = findButton(primaryXPath);

                    if (button == null) {
                        System.out.println("         ⚠️  Skip (tapılmadı)");
                        buttonTest.warning("<span style='color: #ffffff !important;'>⚠️ Skip</span>");
                        skippedCount++;
                        continue;
                    }

                    // ✅ Buttona avtomatik scroll et
                    scrollToElement(button);
                    waitFor(500);

                    if (!button.isDisplayed()) {
                        System.out.println("         ⚠️  Skip (görünmür)");
                        buttonTest.warning("<span style='color: #ffffff !important;'>⚠️ Skip</span>");
                        skippedCount++;
                        continue;
                    }

                    highlightElement(button);

                    if (buttonName.equals("Apply Now")) {
                        testApplyNowButton(button);
                        successCount++;
                        System.out.println("         ✅ Uğurlu");
                        continue;
                    }

                    if (testSingleButton(button, buttonName, primaryXPath, universityUrl)) {
                        successCount++;
                    } else {
                        errorCount++;
                    }

                } catch (Exception e) {
                    errorCount++;
                    System.out.println("         ❌ Xəta: " + e.getMessage());
                    ExtentReportManager.logFailWithDetails(buttonTest, buttonName + " xətası", e.getMessage());
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

    private WebElement findButton(String xpath) {
        try {
            return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
        } catch (Exception e) {
            return null;
        }
    }

    private boolean testSingleButton(WebElement button, String buttonName, String xpath, String universityUrl) {
        try {
            String beforeUrl = driver.getCurrentUrl();
            js.executeScript("arguments[0].click();", button);
            buttonTest.pass("<span style='color: #ffffff !important;'>✅ Tıklandı</span>");
            waitFor(1900);

            String afterUrl = driver.getCurrentUrl();

            if (!afterUrl.equals(beforeUrl)) {
                buttonTest.info("<span style='color: #ffffff !important;'>📍 URL: <a href='" + afterUrl + "' target='_blank' style='color: #3498db !important;'>" + afterUrl + "</a></span>");

                boolean pageLoaded = checkPageLoaded(buttonTest, buttonName);

                System.out.println("         🔙 Geri: " + universityUrl);
                driver.get(universityUrl);
                waitFor(1900);

                // ✅ Səhifə yüklənməsini gözlə
                try {
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
                } catch (Exception e) {
                    System.out.println("         ⚠️  Səhifə yavaş yüklənir...");
                }

                waitFor(800);

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
                waitFor(1900);
            } catch (Exception e2) {}
            return false;
        }
    }

    private void testApplyNowButton(WebElement applyButton) {
        try {
            js.executeScript("arguments[0].click();", applyButton);
            buttonTest.pass("<span style='color: #ffffff !important;'>✅ Modal açıldı</span>");
            waitFor(1900);

            try {
                WebElement closeButton = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("/html/body/div[7]/button")
                ));
                highlightElement(closeButton);
                js.executeScript("arguments[0].click();", closeButton);
                buttonTest.pass("<span style='color: #ffffff !important;'>✅ Modal bağlandı</span>");
            } catch (Exception e) {
                driver.findElement(By.tagName("body")).sendKeys(Keys.ESCAPE);
                buttonTest.warning("<span style='color: #ffffff !important;'>⚠️ ESC ilə bağlandı</span>");
            }

            waitFor(1000);

        } catch (Exception e) {
            buttonTest.fail("<span style='color: #ffffff !important;'>❌ Modal xətası</span>");
        }
    }

    /**
     * ✅ Buttona avtomatik scroll edir
     */
    private void scrollToElement(WebElement element) {
        try {
            js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
            waitFor(800);
        } catch (Exception e) {
            System.out.println("         ⚠️  Scroll xətası: " + e.getMessage());
        }
    }

    private void highlightElement(WebElement element) {
        try {
            String originalStyle = element.getAttribute("style");
            js.executeScript(
                    "arguments[0].setAttribute('style', 'border: 3px solid yellow; box-shadow: 0 0 10px yellow;');",
                    element
            );
            waitFor(500);
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

            File[] reportFiles = reportsDir.listFiles((dir, name) ->
                    name.startsWith("UniversityTest_Report_") && name.endsWith(".html")
            );

            if (reportFiles == null || reportFiles.length == 0) {
                System.err.println("❌ Heç bir report fayl tapılmadı!");
                System.err.println("   Qovluq: " + reportsDir.getAbsolutePath());
                return;
            }

            File latestReport = reportFiles[0];
            for (File file : reportFiles) {
                if (file.lastModified() > latestReport.lastModified()) {
                    latestReport = file;
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
}