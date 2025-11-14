package config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TestConfig {
    private static Properties properties = new Properties();
    private static final String CONFIG_FILE = "src/test/resources/config.properties";

    static {
        try (InputStream input = new FileInputStream(CONFIG_FILE)) {
            properties.load(input);
        } catch (IOException e) {
            System.err.println("⚠️ Config faylı yüklənmədi, default dəyərlər istifadə olunur");
            loadDefaults();
        }
    }

    private static void loadDefaults() {
        properties.setProperty("base.url", "https://studyleo.com/en/universities");
        properties.setProperty("timeout.explicit", "15");
        properties.setProperty("timeout.page.load", "45");
        properties.setProperty("timeout.implicit", "3");
        properties.setProperty("headless.mode", "false");
        properties.setProperty("retry.count", "2");
        properties.setProperty("wait.short", "500");
        properties.setProperty("wait.medium", "1000");
        properties.setProperty("wait.long", "2000");
    }

    public static String getBaseUrl() {
        return properties.getProperty("base.url");
    }

    public static int getExplicitTimeout() {
        return Integer.parseInt(properties.getProperty("timeout.explicit", "15"));
    }

    public static int getPageLoadTimeout() {
        return Integer.parseInt(properties.getProperty("timeout.page.load", "45"));
    }

    public static int getImplicitTimeout() {
        return Integer.parseInt(properties.getProperty("timeout.implicit", "3"));
    }

    public static boolean isHeadless() {
        String headless = System.getProperty("headless", properties.getProperty("headless.mode", "false"));
        return Boolean.parseBoolean(headless);
    }

    public static int getRetryCount() {
        return Integer.parseInt(properties.getProperty("retry.count", "2"));
    }

    public static int getShortWait() {
        return Integer.parseInt(properties.getProperty("wait.short", "500"));
    }

    public static int getMediumWait() {
        return Integer.parseInt(properties.getProperty("wait.medium", "1000"));
    }

    public static int getLongWait() {
        return Integer.parseInt(properties.getProperty("wait.long", "2000"));
    }
}