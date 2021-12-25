package org.firstinspires.ftc.teamcode.controllers;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.capability.BaseAutonomous;
import org.firstinspires.ftc.teamcode.enums.ARM_POSITION;
import org.firstinspires.ftc.teamcode.hardware.Robot;

public class AutoArmController extends BaseArmController {

    private static final double tierDiff = 2;
    private static final double tierOneToGround = 0;
    private static final double tierTwoToGround = tierOneToGround + tierDiff;
    private static final double tierThreeToGround = tierTwoToGround + tierDiff;

    private static final double restToTierThree = 14.5;
    private static final double restToTierTwo = restToTierThree + tierDiff;
    private static final double restToTierOne = restToTierTwo + tierDiff;
    private static final double groundToRest = restToTierOne + tierDiff;

    private static final double power_value = 0.4;

    private static boolean rampPower = false;

    private BaseAutonomous opMode;

    private ARM_POSITION currentPosition = ARM_POSITION.REST;

    public AutoArmController(Robot robot, Telemetry telemetry, BaseAutonomous opMode) {
        super(robot, telemetry, null);
        this.opMode = opMode;
    }

    public void moveToRestPosition() {
        double distanceToMove = 0;
        switch (currentPosition) {
            case GROUND:
                distanceToMove = groundToRest;
                break;
            case TIER_ONE:
                distanceToMove = restToTierOne;
                break;
            case TIER_TWO:
                distanceToMove = restToTierTwo;
                break;
            case TIER_THREE:
                distanceToMove = restToTierThree;
                break;
        }
        telemetry.addLine("Moved From " + currentPosition.toString());
        telemetry.addLine("Moved To " + ARM_POSITION.REST.toString());
        telemetry.update();

        lift(distanceToMove, rampPower, 2);
        currentPosition = ARM_POSITION.REST;
        stopAndReset();
    }

    public void moveToGround() {
        double distanceToMove = 0;
        switch (currentPosition) {
            case TIER_ONE:
                distanceToMove = tierOneToGround;
                break;
            case TIER_TWO:
                distanceToMove = tierTwoToGround;
                break;
            case TIER_THREE:
                distanceToMove = tierThreeToGround;
                break;
            case REST:
                distanceToMove = groundToRest;
                break;
        }
        telemetry.addLine("Moved From " + currentPosition.toString());
        telemetry.addLine("Moved To " + ARM_POSITION.GROUND.toString());
        telemetry.update();

        drop(distanceToMove, rampPower, 2);
        currentPosition = ARM_POSITION.GROUND;
    }

    public void moveToHubPosFromRest(ARM_POSITION targetPos) {
        double distanceToMove = 0;
        switch (targetPos) {
            case TIER_ONE:
                distanceToMove = restToTierOne;
                break;
            case TIER_TWO:
                distanceToMove = restToTierTwo;
                break;
            case TIER_THREE:
                distanceToMove = restToTierThree;
                break;
        }
        telemetry.addLine("Moved From " + ARM_POSITION.REST.toString());
        telemetry.addLine("Moved To " + targetPos.toString());
        telemetry.update();

        drop(distanceToMove, rampPower, 10);
        currentPosition = targetPos;
    }

    public void moveToHubPosFromGround(ARM_POSITION targetPos) {
        double distanceToMove = 0;
        switch (targetPos) {
            case TIER_ONE:
                distanceToMove = tierOneToGround;
                break;
            case TIER_TWO:
                distanceToMove = tierTwoToGround;
                break;
            case TIER_THREE:
                distanceToMove = tierThreeToGround;
                break;
        }
        telemetry.addLine("Moved From " + ARM_POSITION.GROUND.toString());
        telemetry.addLine("Moved To " + targetPos.toString());
        telemetry.update();

        lift(distanceToMove, rampPower, 10);
        currentPosition = targetPos;
    }

    private void lift(double newPosition, boolean rampUpPower, int timeoutS) {
        newPosition *= (-1);
        if (rampUpPower) {
            operateArmMovementRamp(.1, power_value, newPosition, timeoutS);
        } else {
            operateArmMovement(power_value, newPosition, timeoutS);
        }
    }

    private void drop(double newPosition, boolean rampUpPower, int timeoutS) {
        if(rampUpPower) {
            operateArmMovementRamp(.1, power_value, newPosition, timeoutS);
        } else {
            operateArmMovement( power_value, newPosition, timeoutS);
        }
    }

    public void stopAndReset() {
        getMotor().setPower(0);
        getMotor().setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    private void operateArmMovement(double power, double armShiftInInches, int timeoutS) {
//        stopAndReset();
        DcMotor motor = getMotor();

        int startPosition = motor.getCurrentPosition();
        int newTarget = (int) (startPosition + Math.round(armShiftInInches * ARM_COUNTS_PER_INCH));
        int remDist = newTarget - motor.getCurrentPosition();

        runtime.reset();
        while (opMode.opModeIsActive()
                && (runtime.seconds() < timeoutS)
                && ((armShiftInInches >0 && remDist>15) || (armShiftInInches <0 && remDist < 15) )) {
            motor.setPower(power);

            motor.setTargetPosition(newTarget);
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // Display it for the driver.
            telemetry.addData("Target Position", "Running to pos: %7d", startPosition);
            telemetry.addData("Current Position", "Running at pos: %7d", motor.getCurrentPosition());
            telemetry.addData("Remaining Distance", "Remaining pos: %7d", remDist);
            telemetry.update();

            remDist = newTarget - motor.getCurrentPosition();
        }
        applyParkingBrakes();
    }

    private void operateArmMovementRamp(double initPower, double addPower, double armShiftInInches, int timeoutS) {
        stopAndReset();
        DcMotor motor = getMotor();

        int startPosition = motor.getCurrentPosition();
        int newTarget = (int) (startPosition + Math.round(armShiftInInches * ARM_COUNTS_PER_INCH));
        int remDist = newTarget - motor.getCurrentPosition();

        runtime.reset();
        while (opMode.opModeIsActive()
                && (runtime.seconds() < timeoutS)
                && ((armShiftInInches >0 && remDist>5) || (armShiftInInches <0 && remDist < 5) )) {
            double power = initPower + addPower * Math.sin(3.1415*motor.getCurrentPosition()/newTarget);

            motor.setPower(power);
            motor.setTargetPosition(newTarget);
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // Display it for the driver.
            telemetry.addData("Target Position", "Running to pos: %7d", startPosition);
            telemetry.addData("Current Position", "Running at pos: %7d", motor.getCurrentPosition());
            telemetry.update();

            remDist = newTarget - motor.getCurrentPosition();
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