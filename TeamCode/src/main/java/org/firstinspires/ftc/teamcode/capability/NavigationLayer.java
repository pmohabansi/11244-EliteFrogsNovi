package org.firstinspires.ftc.teamcode.capability;

import static org.firstinspires.ftc.teamcode.hardware.Robot.COUNTS_PER_INCH;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.enums.AXIS;
import org.firstinspires.ftc.teamcode.enums.DIRECTION;

public abstract class NavigationLayer extends VisionLayer {
    protected ElapsedTime runtime = new ElapsedTime();

    protected void initialize(boolean auto) {
        super.initialize(auto);
    }

    protected void initialize() {
        super.initialize(false);
    }

    @Override
    protected void releaseResources() {
        super.releaseResources();
    }

    protected void driveUsingConsole() {
        telemetry.addLine("Inside drive console\n");

        double leftFrontWheelPower = 0;
        double rightFrontWheelPower = 0;
        double leftRearWheelPower = 0;
        double rightRearWheelPower = 0;

        // calculated power to be given to wheels
        // if power value is -ve then robot forward &
        // when power value is +ve then robot backward
        if (gamepad1.right_stick_y != 0) {
            // This is for moving the robot forward and reverse
            telemetry.addLine("forward/back");

            // When Y is moved upward then system receive -ve value
            // & when Y is moved down then system receive +ve value.

            leftFrontWheelPower = Range.clip(gamepad1.right_stick_y, -wheelPowerLimit, wheelPowerLimit);
            rightFrontWheelPower = Range.clip(gamepad1.right_stick_y, -wheelPowerLimit, wheelPowerLimit);
            leftRearWheelPower = Range.clip(gamepad1.right_stick_y, -wheelPowerLimit, wheelPowerLimit);
            rightRearWheelPower = Range.clip(gamepad1.right_stick_y, -wheelPowerLimit, wheelPowerLimit);
        } else if (gamepad1.right_stick_x != 0) {
            // This is for turning the robot right and left
            telemetry.addLine("turning");

            // Similarly when X is moved left then system receive -ve value
            // & when X is moved right then system receive +ve value.

            leftFrontWheelPower = Range.clip(-gamepad1.right_stick_x, -wheelPowerLimit, wheelPowerLimit);
            rightFrontWheelPower = Range.clip(gamepad1.right_stick_x, -wheelPowerLimit, wheelPowerLimit);
            leftRearWheelPower = Range.clip(-gamepad1.right_stick_x, -wheelPowerLimit, wheelPowerLimit);
            rightRearWheelPower = Range.clip(gamepad1.right_stick_x, -wheelPowerLimit, wheelPowerLimit);
        } else if (gamepad1.right_trigger != 0) {
            // This is for shifting the robot to the right
            telemetry.addLine("shifting right");

            leftFrontWheelPower = Range.clip(-gamepad1.right_trigger, -wheelPowerLimit, wheelPowerLimit);
            rightFrontWheelPower = Range.clip(gamepad1.right_trigger, -wheelPowerLimit, wheelPowerLimit);
            leftRearWheelPower = Range.clip(gamepad1.right_trigger, -wheelPowerLimit, wheelPowerLimit);
            rightRearWheelPower = Range.clip(-gamepad1.right_trigger, -wheelPowerLimit, wheelPowerLimit);
        } else if (gamepad1.left_trigger != 0) {
            // This is for shifting the robot to the left
            telemetry.addLine("shifting left");

            leftFrontWheelPower = Range.clip(gamepad1.left_trigger, -wheelPowerLimit, wheelPowerLimit);
            rightFrontWheelPower = Range.clip(-gamepad1.left_trigger, -wheelPowerLimit, wheelPowerLimit);
            leftRearWheelPower = Range.clip(-gamepad1.left_trigger, -wheelPowerLimit, wheelPowerLimit);
            rightRearWheelPower = Range.clip(gamepad1.left_trigger, -wheelPowerLimit, wheelPowerLimit);

        } else if ((gamepad1.left_stick_x > 0) && (gamepad1.left_stick_y < 0)) {
            // This is for moving the robot to the diagonal forward right
            telemetry.addLine("diagonal forward right");

            leftFrontWheelPower = -wheelPowerLimit;
            rightFrontWheelPower = 0;
            leftRearWheelPower = 0;
            rightRearWheelPower = -wheelPowerLimit;
        } else if ((gamepad1.left_stick_x < 0) && (gamepad1.left_stick_y > 0)) {
            // This is for moving the robot to the diagonal backward left
            telemetry.addLine("diagonal backward left");

            leftFrontWheelPower = wheelPowerLimit;
            rightFrontWheelPower = 0;
            leftRearWheelPower = 0;
            rightRearWheelPower = wheelPowerLimit;
        } else if ((gamepad1.left_stick_x < 0) && (gamepad1.left_stick_y < 0)) {
            // This is for moving the robot to the diagonal forward left
            telemetry.addLine("diagonal forward left");

            leftFrontWheelPower = 0;
            rightFrontWheelPower = -wheelPowerLimit;
            leftRearWheelPower = -wheelPowerLimit;
            rightRearWheelPower = 0;
        } else if ((gamepad1.left_stick_x > 0) && (gamepad1.left_stick_y > 0)) {
            // This is for moving the robot to the diagonal backward right
            telemetry.addLine("diagonal backward right");

            leftFrontWheelPower = 0;
            rightFrontWheelPower = wheelPowerLimit;
            leftRearWheelPower = wheelPowerLimit;
            rightRearWheelPower = 0;
        }

        // Send calculated power to wheels
        robot.leftFrontWheelMotor.setPower(leftFrontWheelPower);
        robot.rightFrontWheelMotor.setPower(rightFrontWheelPower);
        robot.leftRearWheelMotor.setPower(leftRearWheelPower);
        robot.rightRearWheelMotor.setPower(rightRearWheelPower);

        // Show the elapsed game time and wheel power.
        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.addData("Motors",
                "front left (%.2f), front right (%.2f), rear left (%.2f), rear right (%.2f).",
                leftFrontWheelPower, rightFrontWheelPower, leftRearWheelPower, rightRearWheelPower);

        telemetry.update();
    }

    private void driveStraight(double leftPower, double rightPower) {
        if (opModeIsActive()) {
            robot.leftFrontWheelMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rightFrontWheelMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.leftRearWheelMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rightRearWheelMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.leftFrontWheelMotor.setPower(leftPower);
            robot.leftRearWheelMotor.setPower(leftPower);
            robot.rightFrontWheelMotor.setPower(rightPower);
            robot.rightRearWheelMotor.setPower(rightPower);
        }
    }

    protected void driveRobot(double power, DIRECTION direction, double inches, double timeoutS) {
        int newFrontLeftTarget = 0;
        int newBackLeftTarget = 0;
        int newFrontRightTarget = 0;
        int newBackRightTarget = 0;

        //TODO - Need to calculate
        int error = 0;
        boolean readyToRun = true;

        stopAndReset();

        // Ensure that the opmode is still active
        if (opModeIsActive()) {
            switch (direction) {
                case FORWARD:
                    newFrontLeftTarget = robot.leftFrontWheelMotor.getCurrentPosition() + (int) (inches * COUNTS_PER_INCH) - error;
                    newFrontRightTarget = robot.rightFrontWheelMotor.getCurrentPosition() + (int) (inches * COUNTS_PER_INCH) - error;
                    newBackLeftTarget = robot.leftRearWheelMotor.getCurrentPosition() + (int) (inches * COUNTS_PER_INCH) - error;
                    newBackRightTarget = robot.rightRearWheelMotor.getCurrentPosition() + (int) (inches * COUNTS_PER_INCH) - error;
                    break;
                case BACKWARD:
                    newFrontLeftTarget = robot.leftFrontWheelMotor.getCurrentPosition() - (int) (inches * COUNTS_PER_INCH) + error;
                    newFrontRightTarget = robot.rightFrontWheelMotor.getCurrentPosition() - (int) (inches * COUNTS_PER_INCH) + error;
                    newBackLeftTarget = robot.leftRearWheelMotor.getCurrentPosition() - (int) (inches * COUNTS_PER_INCH) + error;
                    newBackRightTarget = robot.rightRearWheelMotor.getCurrentPosition() - (int) (inches * COUNTS_PER_INCH) + error;
                    break;
                case RIGHTWARD:
                    newFrontLeftTarget = robot.leftFrontWheelMotor.getCurrentPosition() + (int) (inches * COUNTS_PER_INCH) - error;
                    newFrontRightTarget = robot.rightFrontWheelMotor.getCurrentPosition() - (int) (inches * COUNTS_PER_INCH) + error;
                    newBackLeftTarget = robot.leftRearWheelMotor.getCurrentPosition() - (int) (inches * COUNTS_PER_INCH) + error;
                    newBackRightTarget = robot.rightRearWheelMotor.getCurrentPosition() + (int) (inches * COUNTS_PER_INCH) - error;
                    break;
                case LEFTWARD:
                    newFrontLeftTarget = robot.leftFrontWheelMotor.getCurrentPosition() - (int) (inches * COUNTS_PER_INCH) + error;
                    newFrontRightTarget = robot.rightFrontWheelMotor.getCurrentPosition() + (int) (inches * COUNTS_PER_INCH) - error;
                    newBackLeftTarget = robot.leftRearWheelMotor.getCurrentPosition() + (int) (inches * COUNTS_PER_INCH) - error;
                    newBackRightTarget = robot.rightRearWheelMotor.getCurrentPosition() - (int) (inches * COUNTS_PER_INCH) + error;
                    break;
                case DIAGONALLY_LEFT_FORWARD:   //Yet to be Tested
                    newFrontLeftTarget = 0;
                    newFrontRightTarget = robot.rightFrontWheelMotor.getCurrentPosition() - (int) (inches * COUNTS_PER_INCH) + error;
                    newBackLeftTarget = robot.leftRearWheelMotor.getCurrentPosition() - (int) (inches * COUNTS_PER_INCH) + error;
                    newBackRightTarget = 0;
                    break;
                case DIAGONALLY_LEFT_BACKWARD:  //Yet to be Tested
                    newFrontLeftTarget = robot.leftFrontWheelMotor.getCurrentPosition() + (int) (inches * COUNTS_PER_INCH) - error;
                    newFrontRightTarget = 0;
                    newBackLeftTarget = 0;
                    newBackRightTarget = robot.rightRearWheelMotor.getCurrentPosition() + (int) (inches * COUNTS_PER_INCH) - error;
                    break;
                case DIAGONALLY_RIGHT_FORWARD:  //Yet to be Tested
                    newFrontLeftTarget = robot.leftFrontWheelMotor.getCurrentPosition() - (int) (inches * COUNTS_PER_INCH) + error;
                    newFrontRightTarget = 0;
                    newBackLeftTarget = 0;
                    newBackRightTarget = robot.rightRearWheelMotor.getCurrentPosition() - (int) (inches * COUNTS_PER_INCH) + error;
                    break;
                case DIAGONALLY_RIGHT_BACKWARD: //Yet to be Tested
                    newFrontLeftTarget = 0;
                    newFrontRightTarget = robot.rightFrontWheelMotor.getCurrentPosition() + (int) (inches * COUNTS_PER_INCH) - error;
                    newBackLeftTarget = robot.leftRearWheelMotor.getCurrentPosition() + (int) (inches * COUNTS_PER_INCH) - error;
                    newBackRightTarget = 0;
                    break;
                default:
                    readyToRun = false;
            }

            // Determine new target position, and pass to motor controller
            robot.leftFrontWheelMotor.setTargetPosition(newFrontLeftTarget);
            robot.rightFrontWheelMotor.setTargetPosition(newFrontRightTarget);
            robot.leftRearWheelMotor.setTargetPosition(newBackLeftTarget);
            robot.rightRearWheelMotor.setTargetPosition(newBackRightTarget);

            // Turn On RUN_TO_POSITION
            robot.leftFrontWheelMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.rightFrontWheelMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.leftRearWheelMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.rightRearWheelMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();

            if (readyToRun) {
                robot.leftFrontWheelMotor.setPower(Math.abs(power));
                robot.rightFrontWheelMotor.setPower(Math.abs(power));
                robot.leftRearWheelMotor.setPower(Math.abs(power));
                robot.rightRearWheelMotor.setPower(Math.abs(power));
            }

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() && readyToRun && areWheelsBusy() && (runtime.seconds() < timeoutS)) {

                // Display it for the driver.
                telemetry.addData("Target Positions", "Running to fL: %7d fR: %7d bL: %7d bR: %7d",
                        newFrontLeftTarget, newFrontRightTarget, newBackLeftTarget, newBackRightTarget);
                telemetry.addData("Current Positions", "Running at fL: %7d fR: %7d bL: %7d bR: %7d",
                        robot.leftFrontWheelMotor.getCurrentPosition(), robot.rightFrontWheelMotor.getCurrentPosition(),
                        robot.leftRearWheelMotor.getCurrentPosition(), robot.rightRearWheelMotor.getCurrentPosition());
                telemetry.update();

                if (Math.abs(newFrontLeftTarget - robot.leftFrontWheelMotor.getCurrentPosition()) < 50 ||
                        Math.abs(newFrontRightTarget - robot.rightFrontWheelMotor.getCurrentPosition()) < 50 ||
                        Math.abs(newBackLeftTarget - robot.leftRearWheelMotor.getCurrentPosition()) < 50 ||
                        Math.abs(newBackRightTarget - robot.rightRearWheelMotor.getCurrentPosition()) < 50) {
                    break;
                }
            }

            // Stop all motion;
            robot.leftFrontWheelMotor.setPower(0);
            robot.rightFrontWheelMotor.setPower(0);
            robot.leftRearWheelMotor.setPower(0);
            robot.rightRearWheelMotor.setPower(0);

            robot.leftFrontWheelMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rightFrontWheelMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rightRearWheelMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.leftRearWheelMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            sleep(300);
        }
    }

    protected void driveRobotRamp(double InitPower, double AddPower, DIRECTION direction, double inches, double timeoutS) {
        int newFrontLeftTarget = 0;
        int newBackLeftTarget = 0;
        int newFrontRightTarget = 0;
        int newBackRightTarget = 0;

        //This needs to be variable at the end.
        double power = InitPower;

        //TODO - Need to calculate
        int error = 0;
        boolean readyToRun = true;

        stopAndReset();

        // Ensure that the opmode is still active
        if (opModeIsActive()) {
            switch (direction) {
                case FORWARD:
                    newFrontLeftTarget = robot.leftFrontWheelMotor.getCurrentPosition() + (int) (inches * COUNTS_PER_INCH) - error;
                    newFrontRightTarget = robot.rightFrontWheelMotor.getCurrentPosition() + (int) (inches * COUNTS_PER_INCH) - error;
                    newBackLeftTarget = robot.leftRearWheelMotor.getCurrentPosition() + (int) (inches * COUNTS_PER_INCH) - error;
                    newBackRightTarget = robot.rightRearWheelMotor.getCurrentPosition() + (int) (inches * COUNTS_PER_INCH) - error;
                    break;
                case BACKWARD:
                    newFrontLeftTarget = robot.leftFrontWheelMotor.getCurrentPosition() - (int) (inches * COUNTS_PER_INCH) + error;
                    newFrontRightTarget = robot.rightFrontWheelMotor.getCurrentPosition() - (int) (inches * COUNTS_PER_INCH) + error;
                    newBackLeftTarget = robot.leftRearWheelMotor.getCurrentPosition() - (int) (inches * COUNTS_PER_INCH) + error;
                    newBackRightTarget = robot.rightRearWheelMotor.getCurrentPosition() - (int) (inches * COUNTS_PER_INCH) + error;
                    break;
                case RIGHTWARD:
                    newFrontLeftTarget = robot.leftFrontWheelMotor.getCurrentPosition() + (int) (inches * COUNTS_PER_INCH) - error;
                    newFrontRightTarget = robot.rightFrontWheelMotor.getCurrentPosition() - (int) (inches * COUNTS_PER_INCH) + error;
                    newBackLeftTarget = robot.leftRearWheelMotor.getCurrentPosition() - (int) (inches * COUNTS_PER_INCH) + error;
                    newBackRightTarget = robot.rightRearWheelMotor.getCurrentPosition() + (int) (inches * COUNTS_PER_INCH) - error;
                    break;
                case LEFTWARD:
                    newFrontLeftTarget = robot.leftFrontWheelMotor.getCurrentPosition() - (int) (inches * COUNTS_PER_INCH) + error;
                    newFrontRightTarget = robot.rightFrontWheelMotor.getCurrentPosition() + (int) (inches * COUNTS_PER_INCH) - error;
                    newBackLeftTarget = robot.leftRearWheelMotor.getCurrentPosition() + (int) (inches * COUNTS_PER_INCH) - error;
                    newBackRightTarget = robot.rightRearWheelMotor.getCurrentPosition() - (int) (inches * COUNTS_PER_INCH) + error;
                    break;
                case DIAGONALLY_LEFT_FORWARD:   //Yet to be Tested
                    newFrontLeftTarget = 0;
                    newFrontRightTarget = robot.rightFrontWheelMotor.getCurrentPosition() - (int) (inches * COUNTS_PER_INCH) + error;
                    newBackLeftTarget = robot.leftRearWheelMotor.getCurrentPosition() - (int) (inches * COUNTS_PER_INCH) + error;
                    newBackRightTarget = 0;
                    break;
                case DIAGONALLY_LEFT_BACKWARD:  //Yet to be Tested
                    newFrontLeftTarget = robot.leftFrontWheelMotor.getCurrentPosition() + (int) (inches * COUNTS_PER_INCH) - error;
                    newFrontRightTarget = 0;
                    newBackLeftTarget = 0;
                    newBackRightTarget = robot.rightRearWheelMotor.getCurrentPosition() + (int) (inches * COUNTS_PER_INCH) - error;
                    break;
                case DIAGONALLY_RIGHT_FORWARD:  //Yet to be Tested
                    newFrontLeftTarget = robot.leftFrontWheelMotor.getCurrentPosition() - (int) (inches * COUNTS_PER_INCH) + error;
                    newFrontRightTarget = 0;
                    newBackLeftTarget = 0;
                    newBackRightTarget = robot.rightRearWheelMotor.getCurrentPosition() - (int) (inches * COUNTS_PER_INCH) + error;
                    break;
                case DIAGONALLY_RIGHT_BACKWARD: //Yet to be Tested
                    newFrontLeftTarget = 0;
                    newFrontRightTarget = robot.rightFrontWheelMotor.getCurrentPosition() + (int) (inches * COUNTS_PER_INCH) - error;
                    newBackLeftTarget = robot.leftRearWheelMotor.getCurrentPosition() + (int) (inches * COUNTS_PER_INCH) - error;
                    newBackRightTarget = 0;
                    break;
                default:
                    readyToRun = false;
            }
            // Determine new target position, and pass to motor controller
            robot.leftFrontWheelMotor.setTargetPosition(newFrontLeftTarget);
            robot.rightFrontWheelMotor.setTargetPosition(newFrontRightTarget);
            robot.leftRearWheelMotor.setTargetPosition(newBackLeftTarget);
            robot.rightRearWheelMotor.setTargetPosition(newBackRightTarget);

            // Turn On RUN_TO_POSITION
            robot.leftFrontWheelMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.rightFrontWheelMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.leftRearWheelMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.rightRearWheelMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            robot.leftFrontWheelMotor.setPower(Math.abs(InitPower));
            robot.rightFrontWheelMotor.setPower(Math.abs(InitPower));
            robot.leftRearWheelMotor.setPower(Math.abs(InitPower));
            robot.rightRearWheelMotor.setPower(Math.abs(InitPower));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() && readyToRun && areWheelsBusy() && (runtime.seconds() < timeoutS)) {

                // Calculate power based on ramp up and down sin function
                double powerFL = InitPower + AddPower * Math.sin(3.1415*robot.leftFrontWheelMotor.getCurrentPosition()/newFrontLeftTarget);
                double powerFR = InitPower + AddPower * Math.sin(3.1415*robot.rightFrontWheelMotor.getCurrentPosition()/newFrontRightTarget);
                double powerRL = InitPower + AddPower * Math.sin(3.1415*robot.leftRearWheelMotor.getCurrentPosition()/newBackLeftTarget);
                double powerRR = InitPower + AddPower * Math.sin(3.1415*robot.rightRearWheelMotor.getCurrentPosition()/newBackRightTarget);

                // reset the timeout time and start motion.
                runtime.reset();
                if (readyToRun) {
                    robot.leftFrontWheelMotor.setPower(Math.abs(powerFL));
                    robot.rightFrontWheelMotor.setPower(Math.abs(powerFR));
                    robot.leftRearWheelMotor.setPower(Math.abs(powerRL));
                    robot.rightRearWheelMotor.setPower(Math.abs(powerRR));
                }

                // Display it for the driver.
                telemetry.addData("Target Positions", "Running to fL: %7d fR: %7d bL: %7d bR: %7d",
                        newFrontLeftTarget, newFrontRightTarget, newBackLeftTarget, newBackRightTarget);
                telemetry.addData("Current Positions", "Running at fL: %7d fR: %7d bL: %7d bR: %7d",
                        robot.leftFrontWheelMotor.getCurrentPosition(),
                        robot.rightFrontWheelMotor.getCurrentPosition(),
                        robot.leftRearWheelMotor.getCurrentPosition(),
                        robot.rightRearWheelMotor.getCurrentPosition());
                telemetry.update();

                if (Math.abs(newFrontLeftTarget - robot.leftFrontWheelMotor.getCurrentPosition()) < 50 ||
                        Math.abs(newFrontRightTarget - robot.rightFrontWheelMotor.getCurrentPosition()) < 50 ||
                        Math.abs(newBackLeftTarget - robot.leftRearWheelMotor.getCurrentPosition()) < 50 ||
                        Math.abs(newBackRightTarget - robot.rightRearWheelMotor.getCurrentPosition()) < 50) {
                    break;
                }
            }

            // Stop all motion;
            robot.leftFrontWheelMotor.setPower(0);
            robot.rightFrontWheelMotor.setPower(0);
            robot.leftRearWheelMotor.setPower(0);
            robot.rightRearWheelMotor.setPower(0);

            robot.leftFrontWheelMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rightFrontWheelMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rightRearWheelMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.leftRearWheelMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            sleep(300);
        }
    }

    private boolean areWheelsBusy() {
        return robot.leftFrontWheelMotor.isBusy() && robot.rightFrontWheelMotor.isBusy() && robot.leftRearWheelMotor.isBusy() && robot.rightRearWheelMotor.isBusy();
    }

    private void stopAndReset() {
        robot.leftFrontWheelMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rightFrontWheelMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.leftRearWheelMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rightRearWheelMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.leftFrontWheelMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightFrontWheelMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.leftRearWheelMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightRearWheelMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    protected void turnRobot(double degrees, AXIS axis, DIRECTION spinDir, double topPower, double timeoutS, boolean gyroDrive) {
        stopAndReset();

        //Clockwise is -ve and opposite is +ve
        if(spinDir==DIRECTION.RIGHT_SPIN) {
            degrees = (-1) * degrees;
        }

        double originalAngle = readAngle(axis);
        double currentAngle = originalAngle;
        double target = originalAngle + degrees;
        double error = currentAngle - target;
        double degreesTurned = 0;
        double powerScaled = topPower * Math.abs(error / 90) * pidMultiplierTurning(error);

        runtime.reset();
        do {
            //prevents extreme slowing towards end of turn
            if (-6 < error && error < 0) {
                powerScaled += gyroTurnModLeft;
            } else if (0 < error && error < 6) {
                powerScaled += gyroTurnModRight;
            }

            telemetry.addData("original angle", originalAngle);
            telemetry.addData("current angle", currentAngle);
            telemetry.addData("error", error);
            telemetry.addData("degrees turned", degreesTurned);
            telemetry.addData("target", target);
            telemetry.update();

            //direction handling
            if (error > 0) {
                driveStraight(powerScaled, -powerScaled);
            } else if (error < 0) {
                driveStraight(-powerScaled, powerScaled);
            } else if (error == 0) {
                driveStraight(0, 0);
            }
            fetchIMUAngleInfo();

            currentAngle = readAngle(axis);
            error = currentAngle - target;
            degreesTurned = currentAngle - originalAngle;
            powerScaled = topPower * Math.abs(error / 90) * pidMultiplierTurning(error);

        } while (opModeIsActive()
                && ((Math.abs(error) > gyroTurnThreshold) || (gyroDrive && ((Math.abs(error) >= 1.75) && Math.abs(error) <= 2.25)))
                && (runtime.seconds() < timeoutS));

        stopRobot();
        fetchIMUAngleInfo();
        sleep(100);
    }

    protected void turnRobotRamp(double InitPower, double AddPower, double degrees, AXIS axis, DIRECTION spinDir, double timeoutS, boolean gyroDrive) {
        if(spinDir==DIRECTION.RIGHT_SPIN) {
            degrees = (-1) * degrees;
        }

        //Clockwise is -ve and opposite is +ve
        double originalAngle = readAngle(axis);
        double currentAngle = originalAngle;
        double target = originalAngle + degrees;

        stopAndReset();
        runtime.reset();

        double error = 0;
        double degreesTurned = 0;

        double powerScaled = InitPower;

        do {

            telemetry.addData("original angle", originalAngle);
            telemetry.addData("current angle", currentAngle);
            telemetry.addData("error", error);
            telemetry.addData("degrees turned", degreesTurned);
            telemetry.addData("target", target);
            telemetry.update();

            //direction handling
            if (error > 0) {
                driveStraight(powerScaled, -powerScaled);
            } else if (error < 0) {
                driveStraight(-powerScaled, powerScaled);
            } else if (error == 0) {
                driveStraight(0, 0);
            }
            fetchIMUAngleInfo();

            currentAngle = readAngle(axis);
            error = currentAngle - target;
            degreesTurned = currentAngle - originalAngle;
            powerScaled = InitPower + AddPower * Math.cos((3.1415/2)*(degreesTurned/degrees));

        } while (opModeIsActive()
                && ((Math.abs(error) > gyroTurnThreshold) || (gyroDrive && ((Math.abs(error) >= 1.75) && Math.abs(error) <= 2.25)))
                && (runtime.seconds() < timeoutS));

        stopRobot();
        fetchIMUAngleInfo();
        sleep(100);
    }


    protected void stopRobot() {
        driveStraight(0, 0);
        stopAndReset();
    }

    private double readAngle(AXIS axis) {
        Orientation orientation = robot.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        fetchIMUAngleInfo();

        switch (axis) {
            case X:
                return orientation.thirdAngle;
            case Y:
                return orientation.secondAngle;
            case Z:
                return orientation.firstAngle;
            default:
                return 0;
        }
    }

    private void fetchIMUAngleInfo() {
        try {
            robot.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        } catch (NullPointerException e) {
            //TODO
        }
    }

    private double pidMultiplierTurning(double error) {
        //equation for power multiplier is x/sqrt(x^2 + C)
        double C = .1;
        return Math.abs(error / Math.sqrt((error * error) + C));
    }


}