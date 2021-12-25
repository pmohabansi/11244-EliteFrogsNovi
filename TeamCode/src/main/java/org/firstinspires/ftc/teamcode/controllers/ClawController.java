package org.firstinspires.ftc.teamcode.controllers;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.hardware.Robot;

public class ClawController extends BaseController {

    public ClawController(Robot robot, Telemetry telemetry, Gamepad gamepad) {
        super(robot, telemetry, gamepad);
    }

    private void operateClaw(CLAW_ACTION clawAction) {
        double servoLeftClawPosition = 0;
        double servoRightClawPosition = 0;

        try {
            servoLeftClawPosition = robot.leftClawMotor.getPosition();
            servoRightClawPosition = robot.rightClawMotor.getPosition();
        } catch (Exception ex) {
        }

        switch(clawAction) {
            case OPEN_CLAW:
                telemetry.addLine("Open the Claw");
                servoLeftClawPosition = 0;
                servoRightClawPosition = 0;
                break;
            case CLOSE_CLAW:
                telemetry.addLine("Close the Claw");
                servoLeftClawPosition = 0.75;
                servoRightClawPosition = 0.82;
                break;
        }

        try {
            robot.leftClawMotor.setPosition(servoLeftClawPosition);
            robot.rightClawMotor.setPosition(servoRightClawPosition);
        } catch (Exception ex) {
        }
    }

    public void controlUsingConsole() {
        CLAW_ACTION mySelection = CLAW_ACTION.NOTHING;
        if (gamepad.right_bumper) {
            mySelection = CLAW_ACTION.OPEN_CLAW;
        } else if (gamepad.left_bumper) {
            mySelection = CLAW_ACTION.CLOSE_CLAW;
        }
        operateClaw(mySelection);
    }

    public void grabAndHold() {
        operateClaw(CLAW_ACTION.CLOSE_CLAW);
    }

    public void releaseHold() {
        operateClaw(CLAW_ACTION.OPEN_CLAW);
    }

    public enum CLAW_ACTION {
        NOTHING, OPEN_CLAW, CLOSE_CLAW;
    }

}
