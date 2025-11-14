package tests;

public class Main {

    public static void main(String[] args) {
        System.out.println("🚀 Testlər başlayır...\n");

        // Ana menyu testləri
        AllMainPages menuTests = new AllMainPages();
        menuTests.setupSuite();
        menuTests.setup();
        menuTests.testAllMenuButtons();
        menuTests.tearDown();
        menuTests.tearDownSuite();

        System.out.println("\n---\n");

        // Universitet testləri
        universities uniTests = new universities();
        uniTests.setupSuite();
        uniTests.setup();
        uniTests.universitiesPage1();
        uniTests.universitiesPage2();
        uniTests.universitiesPage3();
        uniTests.universitiesPage4();
        uniTests.tearDown();
        uniTests.tearDownSuite();

        System.out.println("\n✅ Bütün testlər tamamlandı!");
    }
}