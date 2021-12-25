package org.firstinspires.ftc.teamcode.controllers;

import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.hardware.Robot;

public class IntakeController extends BaseController {

    public IntakeController(Robot robot, Telemetry telemetry, Gamepad gamepad) {
        super(robot, telemetry, gamepad);
    }

    public void controlUsingConsole() {
        telemetry.addLine("Inside drive console\n");

        if (gamepad.x) {
            // Intake
            pullObjectIn();
        } else if (gamepad.y) {
            // Drop
            pushObjectOut();
        } else if (gamepad.b) {
            // Stop
            stop();
        }
        telemetry.update();
    }

    public void pullObjectIn() {
        intakeORDropIntakeMotor(true, 1);
    }

    public void pullObjectIn(double power) {
        intakeORDropIntakeMotor(true, power);
    }

    public void pushObjectOut() {
        intakeORDropIntakeMotor(false, 1);
    }

    public void pushObjectOut(double power) {
        intakeORDropIntakeMotor(false, power);
    }

    private void intakeORDropIntakeMotor(boolean forward, double power) {
        double finalPower = forward==true? power : power * (-1);
        robot.intakeMotor.setPower(finalPower);
    }

    public void stop() {
        robot.intakeMotor.setPower(0.0);
    }
}
