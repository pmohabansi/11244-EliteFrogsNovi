package org.firstinspires.ftc.teamcode.enums;

public enum FREIGHT_OBJECT {
    DUCK("Duck"), CUBE("Cube"), BALL("Ball"), MARKER("Marker"), CUSTOM_OBJECT("Custom Object");

    private String label;

    private FREIGHT_OBJECT(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
