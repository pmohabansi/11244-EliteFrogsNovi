package org.firstinspires.ftc.teamcode.models;

public class NavTargetPosition {

    private String name;
    private double range = 0;        // Distance from camera to target in Inches
    private double bearing = 0;      // Robot Heading, relative to target.  Positive degrees means target is to the right.

    public NavTargetPosition(String name, double range, double bearing) {
        this.name = name;
        this.range = range;
        this.bearing = bearing;
    }

    public NavTargetPosition(double range, double bearing) {
        this(null, range, bearing);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRange() {
        return range;
    }

    public void setRange(double range) {
        this.range = range;
    }

    public double getBearing() {
        return bearing;
    }

    public void setBearing(double bearing) {
        this.bearing = bearing;
    }
}
