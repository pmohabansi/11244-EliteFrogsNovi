package org.firstinspires.ftc.teamcode.controllers;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.hardware.RobotParameters;
import org.firstinspires.ftc.teamcode.hardware.Robot;

public class ArmController extends BaseArmController {
    protected ElapsedTime armTime;
    protected double armMoveTimeOut;
    protected int newArmTarget;
    protected boolean isAtTheNewPosition;
    protected boolean isNewPositionTrigger;

    protected ARM_ACTION innerState;
    protected int newPosition;
    protected double timeOutNewState;

    protected boolean callRestPostionAfterFreeForm;

    public ArmController(Robot robot, Telemetry telemetry, Gamepad gamepad) {
        super(robot, telemetry, gamepad);
        armTime = new ElapsedTime();
        this.armMoveTimeOut = 0.0;
        this.newArmTarget = 0;
        this.isAtTheNewPosition = false;
        this.isNewPositionTrigger = false;

        this.callRestPostionAfterFreeForm = false;

        innerState = ARM_ACTION.NOTHING;
        armTime.reset();
    }

    @Override
    public void controlUsingConsole() {
        ARM_ACTION mySelection = ARM_ACTION.NOTHING;
        if (gamepad.right_trigger != 0) {
            freeFormRotationUP(gamepad.right_trigger);
            return;
        } else if (gamepad.left_trigger != 0) {
            freeFormRotationDown(gamepad.left_trigger);
            return;
        } else if (gamepad.dpad_up) {
            mySelection = ARM_ACTION.ARM_LIFT_UP;
        } else if (gamepad.dpad_down) {
            mySelection = ARM_ACTION.ARM_LIFT_DOWN;
        }

        if ((gamepad.left_trigger == 0) || (gamepad.left_trigger == 0)) {
                executeRestAfterFreeForm(getMotor());
        }

        switch (mySelection) {
            case ARM_LIFT_UP:
                // This is to lift the arm Up
                telemetry.addLine("arm lift Up");
                timeOutNewState = 0.15;
                liftArm(5);
                break;
            case ARM_LIFT_DOWN:
                // This is to lift the arm Down
                telemetry.addLine("arm lift Down");
                newPosition = -5;
                timeOutNewState = 0.1;
                dropArm(-5);
                break;
        }
        encoderDrive(robot.ARM_TURN_SPEED, newPosition, timeOutNewState);
    }

    private void freeFormRotationUP(float trigger) {
        double armPower = 0;
        armPower = Range.clip(trigger, -RobotParameters.wheelPowerLimit, RobotParameters.wheelPowerLimit);
        freeFormRotationAction(getMotor(), armPower);
        callRestPostionAfterFreeForm = true;
    }

    private void freeFormRotationDown(float trigger) {
        double armPower = 0;
        armPower = Range.clip(-trigger, -RobotParameters.wheelPowerLimit, RobotParameters.wheelPowerLimit);
        freeFormRotationAction(getMotor(), armPower);
        callRestPostionAfterFreeForm = true;
    }

    private void freeFormRotationAction(DcMotor motor, double power) {
        if (motor != null && motor.getMode() == DcMotor.RunMode.RUN_USING_ENCODER) {
            motor.setPower(power);
        }
    }

    private void executeRestAfterFreeForm(DcMotor motor) {
        if (motor != null) {
            if (callRestPostionAfterFreeForm) {
                getMotor().setPower(getBrakingPower());
                motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

                isAtTheNewPosition = false;
                isNewPositionTrigger = false;
                callRestPostionAfterFreeForm = false;
            }
        }
    }

    /*
     *  Method to perform a relative move, based on encoder counts.
     *  Encoders are not reset as the move is based on the current position.
     *  Move will stop if any of three conditions occur:
     *  1) Move gets to the desired position
     *  2) Move runs out of time
     *  3) Driver stops the opmode running.
     */
    private void encoderDrive(double speed,
                              double armShiftInInches,
                              double timeoutS) {
        if (!isAtTheNewPosition && isNewPositionTrigger) {
            executeNewPosition(getMotor(), speed, armShiftInInches, timeoutS);
            armTime.reset();

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            //while ((armTime.seconds() < timeoutS) &&
            //        robot.armWheelMotor.isBusy()) {
            //
            //    // Display it for the driver.
            //    telemetry.addData("Path1", "Running to %7d", newArmTarget);
            //    telemetry.addData("Path2", "Running at %7d", robot.armWheelMotor.getCurrentPosition());
            //    telemetry.update();
            //}
            telemetry.addData("Path1", "armTime (%.8f), timeOut (%.8f)",
                    armTime.seconds(), armMoveTimeOut);
        }
        if (executeRestPosition(getMotor())) {
            telemetry.addData("Path2", "armTime (%.8f), timeOut (%.8f)",
                    armTime.seconds(), armMoveTimeOut);
        }
        telemetry.addData("Path3", "armTime (%.8f), timeOut (%.8f)",
                armTime.seconds(), armMoveTimeOut);
    }

    private void executeNewPosition(DcMotor motor, double speed, double armShiftInInches, double timeoutS) {
        if (motor != null) {
            this.armMoveTimeOut = timeoutS;

            // Determine new target position, and pass to motor controller
            newArmTarget = motor.getCurrentPosition() + (int) (armShiftInInches * ARM_COUNTS_PER_INCH);
            motor.setTargetPosition(newArmTarget);

            // Turn On RUN_TO_POSITION
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            motor.setPower(Math.abs(speed));

            isAtTheNewPosition = true;
        }
    }

    private boolean executeRestPosition(DcMotor motor) {
        try {
            if (motor != null) {
                if (motor.getMode() != DcMotor.RunMode.RUN_USING_ENCODER) {
                    if (((armTime.seconds() > armMoveTimeOut) &&
                            (isAtTheNewPosition && isNewPositionTrigger)) ||
                            !motor.isBusy()) {

                        getMotor().setPower(getBrakingPower());
                        getMotor().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                        return true;
                    }
                    return false;
                }
                return false;
            }
            return false;
        } catch (Exception ex) {
            telemetry.addData(motor.getDeviceName(), ex.toString());
        }
        return false;
    }

    public void setNewPosition() {
        isNewPositionTrigger = true;
    }

    private void liftArm(int newPosition) {
        innerState = ARM_ACTION.ARM_LIFT_UP;
        this.newPosition = newPosition;
        setNewPosition();
    }

    private void dropArm(int newPosition) {
        innerState = ARM_ACTION.ARM_LIFT_DOWN;
        this.newPosition = newPosition;
        setNewPosition();
    }

    public ARM_ACTION getInnerState() {
        return innerState;
    }

    public enum ARM_ACTION {
        NOTHING, ARM_LIFT_UP, ARM_LIFT_DOWN;
    }

}