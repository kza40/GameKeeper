package ca.cmpt276.myapplication.model;

public class AchievementLevel {
    private String name;
    private String boundary;

    public AchievementLevel(String name) {
        this.name = name;
        this.boundary = "-";
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
