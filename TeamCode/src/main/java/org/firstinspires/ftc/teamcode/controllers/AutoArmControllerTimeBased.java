package org.firstinspires.ftc.teamcode.controllers;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.capability.BaseAutonomous;
import org.firstinspires.ftc.teamcode.enums.ARM_POSITION;
import org.firstinspires.ftc.teamcode.hardware.Robot;

/*
    This does not work the way we want it to be. But we will leave it here,
    just in case we want to revisit this time based strategy for arm movement.
 */
public class AutoArmControllerTimeBased extends BaseArmController {

    private static final int restToTierThree = 2000;
    private static final int restToTierTwo = 1250;
    private static final int restToTierOne = 4000;
    private static final int groundToRest = 5000;
    private static final int tierOneToGround = 1000;
    private static final int tierTwoToGround = 2000;
    private static final int tierThreeToGround = 3000;

    private static final double power_value = 0.6;

    private ARM_POSITION currentPosition = ARM_POSITION.REST;

    private BaseAutonomous opMode;

    private AutoArmControllerTimeBased(Robot robot, Telemetry telemetry, BaseAutonomous opMode) {
        super(robot, telemetry, null);
        this.opMode = opMode;
    }

    private void moveToRestPosition() {
        int duration = 0;
        switch (currentPosition) {
            case GROUND:
                duration = groundToRest;
                break;
            case TIER_ONE:
                duration = restToTierOne;
                break;
            case TIER_TWO:
                duration = restToTierTwo;
                break;
            case TIER_THREE:
                duration = restToTierThree;
                break;
        }
        telemetry.addLine("Moved From " + currentPosition.toString());
        telemetry.addLine("Moved To " + ARM_POSITION.REST.toString());
        telemetry.update();

        lift(duration);
        currentPosition = ARM_POSITION.REST;
    }

    private void moveToGround() {
        int duration = 0;
        switch (currentPosition) {
            case TIER_ONE:
                duration = tierOneToGround;
                break;
            case TIER_TWO:
                duration = tierTwoToGround;
                break;
            case TIER_THREE:
                duration = tierThreeToGround;
                break;
            case REST:
                duration = groundToRest;
                break;
        }
        telemetry.addLine("Moved From " + currentPosition.toString());
        telemetry.addLine("Moved To " + ARM_POSITION.GROUND.toString());
        telemetry.update();

        drop(duration);
        currentPosition = ARM_POSITION.GROUND;
    }

    private void moveToHubPosFromRest(ARM_POSITION targetPos) {
        int duration = 0;
        switch (targetPos) {
            case TIER_ONE:
                duration = restToTierOne;
                break;
            case TIER_TWO:
                duration = restToTierTwo;
                break;
            case TIER_THREE:
                duration = restToTierThree;
                break;
        }
        telemetry.addLine("Moved From " + ARM_POSITION.REST.toString());
        telemetry.addLine("Moved To " + targetPos.toString());
        telemetry.update();

        drop(duration);
        currentPosition = targetPos;
    }

    private void moveToHubPosFromGround(ARM_POSITION targetPos) {
        int duration = 0;
        switch (targetPos) {
            case TIER_ONE:
                duration = tierOneToGround;
                break;
            case TIER_TWO:
                duration = tierTwoToGround;
                break;
            case TIER_THREE:
                duration = tierThreeToGround;
                break;
        }
        telemetry.addLine("Moved From " + ARM_POSITION.GROUND.toString());
        telemetry.addLine("Moved To " + targetPos.toString());
        telemetry.update();

        lift(duration);
        currentPosition = targetPos;
    }

    private void lift(double duration) {
        operateArmMovementRamp((-1) * power_value, duration);
    }

    private void drop(double duration) {
        operateArmMovementRamp(power_value, duration);
    }

    private void stopAndReset() {
        getMotor().setPower(0);
        getMotor().setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    private void operateArmMovement(double power, double duration) {
        DcMotor motor = getMotor();

        runtime.reset();

        motor.setPower(getBrakingPower());
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        while (opMode.opModeIsActive()
                && (runtime.milliseconds() < duration) ) {
            motor.setPower(power);

            // Display it for the driver.
            telemetry.addData("Time Left",  duration - runtime.milliseconds());
            telemetry.update();
        }
        applyParkingBrakes();
    }

    private void operateArmMovementRamp(double oPower, double duration) {
        DcMotor motor = getMotor();

        runtime.reset();
        double timeLeft = duration - runtime.milliseconds();

        motor.setPower(getBrakingPower());
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        while (opMode.opModeIsActive() && timeLeft>5) {
            double power = getBrakingPower() + (oPower * Math.sin((3.1415)*(runtime.milliseconds()/duration)));

            motor.setPower(power);

            // Display it for the driver.
            telemetry.addData("Time Left",  timeLeft);
            telemetry.addData("Power",  power);
            telemetry.update();

            timeLeft = duration - runtime.milliseconds();
        }
        applyParkingBrakes();
    }

    private void applyParkingBrakes() {
        telemetry.addLine("Applied Brake");
        telemetry.update();

        getMotor().setPower(getBrakingPower());
        getMotor().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    @Override
    public void controlUsingConsole() {
        //Will not be implemented
    }
}