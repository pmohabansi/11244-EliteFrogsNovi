package org.firstinspires.ftc.teamcode.enums;

public enum ROBOT_STATES {
    INITIAL_POSITION ("At Initial Position"),
    DROPPED_OBJECT_IN_TEAM_HUB("Dropped Object in Team Hub"),
    REACHED_BLUE_WAREHOUSE("Reached Blue Warehouse"),
    REACHED_RED_WAREHOUSE("Reached Red Warehouse"),
    REACHED_BLUE_WALL_TARGET("Reached Blue Wall Target"),
    REACHED_RED_WALL_TARGET("Reached Red Wall Target"),
    OBJECT_PICKED_UP("Object Picked up from Warehouse"),
    REACHED_TEAM_HUB("Reached Team Hub"),
    REACHED_SHARED_HUB("Reached Shared Hub"),
    DROPPED_OBJECT_IN_SHARED_HUB("Object Dropped in Shared Hub"),
    REACHED_CAROUSEL("Reached Carousel"),
    SPINNED_OFF_CAROUSEL("Spinned Off Carousel"),
    END_GAME_STARTED("End Game Started"),
    PARKED_IN_BLUE_WAREHOUSE("Parked in Blue WareHouse Storage"),
    PARKED_IN_RED_WAREHOUSE("Parked in Red WareHouse Storage"),
    PICKED_UP_PAYLOAD("Picked up Payload from Warehouse"),
    READY_TO_PARK("Ready to Park"),
    PARKED_IN_BLUE_STORAGE("Parked in Blue Storage"),
    PARKED_IN_RED_STORAGE("Parked in Red Storage");

    public String label;

    private ROBOT_STATES(String label) {
        this.label = label;
    }
}
