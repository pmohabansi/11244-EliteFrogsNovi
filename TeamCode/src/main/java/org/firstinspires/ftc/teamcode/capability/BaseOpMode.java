package org.firstinspires.ftc.teamcode.capability;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.enums.ROBOT_STATES;
import org.firstinspires.ftc.teamcode.hardware.Robot;
import org.firstinspires.ftc.teamcode.hardware.RobotParameters;

public abstract class BaseOpMode extends LinearOpMode implements RobotParameters {

    protected Robot robot;

    protected void initialize(boolean auto) {
        initHardware(auto);
    }

    protected void initHardware(boolean auto) {
        robot = new Robot();
        robot.initialize(hardwareMap, auto);
    }

    protected void releaseResources() {
        //Do nothing
    }

}
