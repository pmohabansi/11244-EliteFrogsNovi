/*
 * Developer: Apar Mohabansi
 * Date: 10/10/2017
 */
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@SuppressWarnings({"WeakerAccess", "FieldCanBeLocal"})

@TeleOp(name = "Concept: TeleopDrive_v_0_1", group = "Concept")
public class TeleopDrive_v_0_1 extends LinearOpMode {

    // Define variables for power to be given to the motors.
    double leftFrontWheelPower;
    double rightFrontWheelPower;
    double leftRearWheelPower;
    double rightRearWheelPower;
    double carouselWheelPower;
    double armWheelPower;
    double servoLeftClawPosition;
    double servoRightClawPosition;
    double wheelPowerLimit = 0.75;
    int armNewPosition = 0;

    // Define variables for motors which are connected` to the wheels to rotate.
    DcMotor leftFrontWheelMotor = null;
    DcMotor rightFrontWheelMotor = null;
    DcMotor leftRearWheelMotor = null;
    DcMotor rightRearWheelMotor = null;
    DcMotor carouselWheelMotor = null;
    DcMotor armWheelMotor = null;
    Servo leftClawMotor = null;
    Servo rightClawMotor = null;

    // Declare LinearOpMode members.
    ElapsedTime runtime = new ElapsedTime();

    static final double COUNTS_PER_MOTOR_REV = 1440;    // eg: TETRIX Motor Encoder
    static final double DRIVE_GEAR_REDUCTION = 0.5;     // This is < 1.0 if geared UP
    static final double WHEEL_DIAMETER_INCHES = 20.0;     // For figuring circumference
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
                                          (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double TURN_SPEED = 0.5;


    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        rightRearWheelMotor = hardwareMap.get(DcMotor.class, "RR");
        rightFrontWheelMotor = hardwareMap.get(DcMotor.class, "FR");
        leftRearWheelMotor = hardwareMap.get(DcMotor.class, "RL");
        leftFrontWheelMotor = hardwareMap.get(DcMotor.class, "FL");
        carouselWheelMotor = hardwareMap.get(DcMotor.class, "CS");
        armWheelMotor = hardwareMap.get(DcMotor.class, "arm");
        leftClawMotor = hardwareMap.servo.get("las");
        rightClawMotor = hardwareMap.servo.get("ras");

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        leftFrontWheelMotor.setDirection(DcMotor.Direction.FORWARD);
        rightFrontWheelMotor.setDirection(DcMotor.Direction.REVERSE);
        leftRearWheelMotor.setDirection(DcMotor.Direction.FORWARD);
        rightRearWheelMotor.setDirection(DcMotor.Direction.REVERSE);
        carouselWheelMotor.setDirection(DcMotor.Direction.REVERSE);
        armWheelMotor.setDirection(DcMotor.Direction.FORWARD);
        leftClawMotor.setDirection(Servo.Direction.REVERSE);
        rightClawMotor.setDirection(Servo.Direction.FORWARD);

        leftRearWheelMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightRearWheelMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftFrontWheelMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightFrontWheelMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        carouselWheelMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        armWheelMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armWheelMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");

        waitForStart();

        while (opModeIsActive()) {

            // Initialize power to zero so in case user is not pressing any keys then
            // robot should remain in same position.
            leftFrontWheelPower = 0;
            rightFrontWheelPower = 0;
            leftRearWheelPower = 0;
            rightRearWheelPower = 0;
            carouselWheelPower = 0;
            armWheelPower = 0;
            servoLeftClawPosition = leftClawMotor.getPosition();
            servoRightClawPosition = rightClawMotor.getPosition();


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
            } else if (gamepad1.right_bumper) {
                carouselWheelPower = 0.45;
            } else if (!gamepad1.right_bumper) {
                carouselWheelPower = 0;
            }

            if (gamepad2.right_trigger != 0) {
                // This is to lift the arm Up
                telemetry.addLine("arm lift Up");

                encoderDrive(TURN_SPEED, 5, 4.0);
            } else if (gamepad2.left_trigger != 0) {
                // This is to lift the arm Down
                telemetry.addLine("arm lift Down");

                encoderDrive(TURN_SPEED, -5, 4.0);
            } else if (gamepad2.right_bumper) {
                telemetry.addLine("Open the Claw");
                servoLeftClawPosition = 0;
                servoRightClawPosition = 0;
            } else if (gamepad2.left_bumper) {
                telemetry.addLine("Close the Claw");
                servoLeftClawPosition = 0.75;
                servoRightClawPosition = 0.82;
            }

            telemetry.addLine("");
            // Send calculated power to wheels
            leftFrontWheelMotor.setPower(leftFrontWheelPower);
            rightFrontWheelMotor.setPower(rightFrontWheelPower);
            leftRearWheelMotor.setPower(leftRearWheelPower);
            rightRearWheelMotor.setPower(rightRearWheelPower);
            carouselWheelMotor.setPower(carouselWheelPower);
            leftClawMotor.setPosition(servoLeftClawPosition);
            rightClawMotor.setPosition(servoRightClawPosition);

            telemetry.addData("Motors", "front left (%.2f), front right (%.2f), rear left (%.2f)" +
                            ", rear right (%.2f), carosel (%.2f), arm (%.2f), newPosition (%d), arm position (%d)," +
                            " LeftClaw (%.2f), RightClaw (%.2f).",
                    leftFrontWheelPower, rightFrontWheelPower, leftRearWheelPower,
                    rightRearWheelPower, carouselWheelPower, armWheelPower, armNewPosition, armWheelMotor.getCurrentPosition(),
                    servoLeftClawPosition, servoRightClawPosition);

            telemetry.update();
        }
    }

    /*
     *  Method to perfmorm a relative move, based on encoder counts.
     *  Encoders are not reset as the move is based on the current position.
     *  Move will stop if any of three conditions occur:
     *  1) Move gets to the desired position
     *  2) Move runs out of time
     *  3) Driver stops the opmode running.
     */
    public void encoderDrive(double speed,
                             double armShiftInInches,
                             double timeoutS) {
        int newArmTarget;


        // Determine new target position, and pass to motor controller
        newArmTarget = armWheelMotor.getCurrentPosition() + (int) (armShiftInInches * COUNTS_PER_INCH);
        armWheelMotor.setTargetPosition(newArmTarget);

        // Turn On RUN_TO_POSITION
        armWheelMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // reset the timeout time and start motion.
        runtime.reset();
        armWheelMotor.setPower(Math.abs(speed));

        // keep looping while we are still active, and there is time left, and both motors are running.
        // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
        // its target position, the motion will stop.  This is "safer" in the event that the robot will
        // always end the motion as soon as possible.
        // However, if you require that BOTH motors have finished their moves before the robot continues
        // onto the next step, use (isBusy() || isBusy()) in the loop test.
        while (opModeIsActive() &&
                (runtime.seconds() < timeoutS) &&
                armWheelMotor.isBusy()) {

            // Display it for the driver.
            telemetry.addData("Path1", "Running to %7d", newArmTarget);
            telemetry.addData("Path2", "Running at %7d", armWheelMotor.getCurrentPosition());
            telemetry.update();
        }

        // Stop all motion;
        armWheelMotor.setPower(0);

        // Turn off RUN_TO_POSITION
        armWheelMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
}
