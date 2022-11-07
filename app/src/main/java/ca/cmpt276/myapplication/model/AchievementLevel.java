package ca.cmpt276.myapplication.model;

/**
 * This class contains the details of an achievement level: the name, and minimum score to
 * achieve it.
 */

public class AchievementLevel {
    private String name;
    private String boundary;

    public AchievementLevel(String name) {
        this.name = name;
        this.boundary = "";
    }

    public String getName() {
        return name;
    }

    public String getBoundary() {
        return boundary;
    }

    public void setBoundary(String boundary) {
        this.boundary = boundary;
    }
}
