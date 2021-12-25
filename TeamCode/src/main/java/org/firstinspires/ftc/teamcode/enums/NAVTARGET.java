package org.firstinspires.ftc.teamcode.enums;

public enum NAVTARGET {

    BlueStorage("Blue Storage"), BlueAllianceWall("Blue Alliance Wall"), RedStorage("Red Storage"), RedAllianceWall("Red Alliance Wall");

    private String name;

    NAVTARGET(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
