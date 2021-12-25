package org.firstinspires.ftc.teamcode.controllers;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.hardware.Robot;

public abstract class BaseController {

    protected Robot robot;
    protected Telemetry telemetry;
    protected ElapsedTime runtime;
    protected Gamepad gamepad;

    public BaseController(Robot robot, Telemetry telemetry, Gamepad gamepad) {
        this.robot = robot;
        this.telemetry = telemetry;
        this.gamepad = gamepad;
        runtime = new ElapsedTime();
    }

    public abstract void controlUsingConsole();

}
