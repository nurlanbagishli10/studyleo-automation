package model;

import java.util.ArrayList;
import java.util.List;

public class ButtonConfig {
    private String name;
    private String xpath;
    private String description;
    private boolean isDynamic;
    private List<String> alternativeXPaths;

    public ButtonConfig(String name, String xpath, String description, boolean isDynamic, String... alternatives) {
        this.name = name;
        this.xpath = xpath;
        this.description = description;
        this.isDynamic = isDynamic;
        this.alternativeXPaths = new ArrayList<>();
        for (String alt : alternatives) {
            this.alternativeXPaths.add(alt);
        }
    }

    // Getters
    public String getName() { return name; }
    public String getXpath() { return xpath; }
    public String getDescription() { return description; }
    public boolean isDynamic() { return isDynamic; }
    public List<String> getAlternativeXPaths() { return alternativeXPaths; }

    @Override
    public String toString() {
        return "ButtonConfig{name='" + name + "', description='" + description + "'}";
    }
}