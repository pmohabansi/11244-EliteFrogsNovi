package org.firstinspires.ftc.sandbox;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.enums.NAVTARGET;
import org.firstinspires.ftc.teamcode.models.NavTargetPosition;

@TeleOp(name="Drive around", group = "Concept")
@Disabled
public class Octupus extends LinearOpMode {
    // Adjust these numbers to suit your robot.
    final double DESIRED_DISTANCE = 2.0; //  this is how close the camera should get to the target (inches)
    //  The GAIN constants set the relationship between the measured position error,
    //  and how much power is applied to the drive motors.  Drive = Error * Gain
    //  Make these values smaller for smoother control.
    final double SPEED_GAIN = 0.02;   //  Speed Control "Gain". eg: Ramp up to 50% power at a 25 inch error.   (0.50 / 25.0)
    final double TURN_GAIN = 0.01;   //  Turn Control "Gain".  eg: Ramp up to 25% power at a 25 degree error. (0.25 / 25.0)
    final double MM_PER_INCH = 25.40;   //  Metric conversion
    private static final double wheelPowerLimit     = 0.75;

    private static final String VUFORIA_KEY =
            "AYnwvy//////AAABmb7CbfH0f03Nrl90EkgFFxgQsKYg5wdzlq6MU2uXenx7dK7DLZ5swuvzjKpg6mFH5UzKbHF93+2abr54O4XGMTcis75+VPt/xxGL2uG/z++P9JswAn6dkdCGpSjCDGoIVwn/zBGvodhSu6zBLgDRntTnMFVcEDeylhnzRQsafWJz8JG+XXgQNNIqO6je6ED9HfwNVzpXq5axiVC60bR//chpWSOC3NDV7gdcpGfZo55Hie4dL6EkNLEXKEDKiCTaFQ0SMvXr+NgYoaBvCfoYwh4oUFnoV1ky3p5r4oLLPrHcxfBhLa18qyTz7ZXPEZUf+fJ1Q9cKWI54oNKT1WmEA0uqf5bTlntFjFzYrmlhK4QX";

    private VuforiaLocalizer vuforia = null;

    private ElapsedTime runtime = new ElapsedTime();

    private DcMotor leftFrontWheelMotor  = null;
    private DcMotor rightFrontWheelMotor = null;
    private DcMotor leftRearWheelMotor   = null;
    private DcMotor rightRearWheelMotor  = null;

    private double leftFrontWheelPower = 0;
    private double rightFrontWheelPower = 0;
    private double leftRearWheelPower = 0;
    private double rightRearWheelPower = 0;

    private VuforiaTrackables targetsFreightFrenzy;

    @Override
    public void runOpMode() {
        initializeVuforia();
        targetsFreightFrenzy = getVuforiaTrackables();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).

        initializeWheelMotors();

        telemetry.addData(">", "Press Play to start");
        telemetry.update();

        waitForStart();

        if(targetsFreightFrenzy!=null) {
            targetsFreightFrenzy.activate();
        }


        if (isStopRequested()) return;

        double drive = 0;        // Desired forward power (-1 to +1)
        double turn = 0;        // Desired turning power (-1 to +1)

        while (opModeIsActive()) {
            // Initialize power to zero so in case user is not pressing any keys then
            // robot should remain in same position.
            double leftFrontWheelPower = 0;
            double rightFrontWheelPower = 0;
            double leftRearWheelPower = 0;
            double rightRearWheelPower = 0;

            NavTargetPosition targetPosition = identifyNavigationTarget(targetsFreightFrenzy);

            // If we have found a target.
            if (targetPosition!=null) {
                telemetry.addData("Target", " %s", targetPosition.getName());
                telemetry.addData("Range", "%5.1f inches", targetPosition.getRange());
                telemetry.addData("Bearing", "%3.0f degrees", targetPosition.getBearing());



            } else {
                telemetry.addData(">", "Drive using joystick to find target\n");

                driveManually();
            }
            telemetry.update();
        }
    }

    private void initializeWheelMotors() {
        leftFrontWheelMotor  = hardwareMap.get(DcMotor.class, "FL");
        rightFrontWheelMotor = hardwareMap.get(DcMotor.class, "FR");
        leftRearWheelMotor   = hardwareMap.get(DcMotor.class, "RL");
        rightRearWheelMotor  = hardwareMap.get(DcMotor.class, "RR");

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        leftFrontWheelMotor.setDirection(DcMotor.Direction.FORWARD);
        rightFrontWheelMotor.setDirection(DcMotor.Direction.REVERSE);
        leftRearWheelMotor.setDirection(DcMotor.Direction.FORWARD);
        rightRearWheelMotor.setDirection(DcMotor.Direction.REVERSE);

        leftRearWheelMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightRearWheelMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftFrontWheelMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightFrontWheelMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    @NonNull
    private VuforiaTrackables getVuforiaTrackables() {
        // Load the trackable objects from the Assets file, and give them meaningful names
        VuforiaTrackables targetsFreightFrenzy = this.vuforia.loadTrackablesFromAsset("FreightFrenzy");
        targetsFreightFrenzy.get(0).setName(NAVTARGET.BlueStorage.getName());
        targetsFreightFrenzy.get(1).setName(NAVTARGET.BlueAllianceWall.getName());
        targetsFreightFrenzy.get(2).setName(NAVTARGET.RedStorage.getName());
        targetsFreightFrenzy.get(3).setName(NAVTARGET.RedAllianceWall.getName());

        // Start tracking targets in the background
        targetsFreightFrenzy.activate();
        return targetsFreightFrenzy;
    }

    private void initializeVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         * To get an on-phone camera preview, use the code below.
         * If no camera preview is desired, use the parameter-less constructor instead (commented out below).
         */
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        // VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;

        // Turn off Extended tracking.  Set this true if you want Vuforia to track beyond the target.
        parameters.useExtendedTracking = false;

        // Connect to the camera we are to use.  This name must match what is set up in Robot Configuration
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");
        this.vuforia = ClassFactory.getInstance().createVuforia(parameters);
    }

    private NavTargetPosition identifyNavigationTarget(VuforiaTrackables targetsFreightFrenzy) {
        if(targetsFreightFrenzy==null) {
            return null;
        }

        OpenGLMatrix targetPose = null;
        NavTargetPosition targetPos = null;
        for (VuforiaTrackable trackable : targetsFreightFrenzy) {
            if (((VuforiaTrackableDefaultListener) trackable.getListener()).isVisible()) {
                targetPose = ((VuforiaTrackableDefaultListener) trackable.getListener()).getVuforiaCameraFromTarget();

                // if we have a target, process the "pose" to determine the position of the target relative to the robot.
                if (targetPose != null) {
                    String targetName = trackable.getName();
                    VectorF trans = targetPose.getTranslation();

                    // Extract the X & Y components of the offset of the target relative to the robot
                    double targetX = trans.get(0) / MM_PER_INCH; // Image X axis
                    double targetY = trans.get(2) / MM_PER_INCH; // Image Z axis

                    // target range is based on distance from robot position to origin (right triangle).
                    double targetRange = Math.hypot(targetX, targetY);

                    // target bearing is based on angle formed between the X axis and the target range line
                    double targetBearing = Math.toDegrees(Math.asin(targetX / targetRange));

                    targetPos = new NavTargetPosition(targetName, targetRange, targetBearing);

                    break;  // jump out of target tracking loop if we find a target.

                }
            }
        }

        return targetPos;
    }

    private void driveManually() {
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

        telemetry.addLine("");
        // Send calculated power to wheels
        leftFrontWheelMotor.setPower(leftFrontWheelPower);
        rightFrontWheelMotor.setPower(rightFrontWheelPower);
        leftRearWheelMotor.setPower(leftRearWheelPower);
        rightRearWheelMotor.setPower(rightRearWheelPower);

        // Show the elapsed game time and wheel power.
        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.addData("Motors", "front left (%.2f), front right (%.2f), rear left (%.2f)" +
                        ", rear right (%.2f).", leftFrontWheelPower, rightFrontWheelPower,
                leftRearWheelPower, rightRearWheelPower);

        telemetry.update();
    }

    private void driveTowardsTarget(NavTargetPosition targetPosition) {
        double turn = 0;
        double drive =0;
        // Determine heading and range error so we can use them to control the robot automatically.
        double rangeError = (targetPosition.getRange() - DESIRED_DISTANCE);
        double headingError = targetPosition.getBearing();

        // Use the speed and turn "gains" to calculate how we want the robot to move.
        drive = rangeError * SPEED_GAIN;
        turn = headingError * TURN_GAIN;

        telemetry.addData("Auto", "Drive %5.2f, Turn %5.2f", drive, turn);

        // Calculate left and right wheel powers and send to them to the motors.
//        double leftPower = Range.clip(drive - turn, -wheelPowerLimit, wheelPowerLimit);
//        double rightPower = Range.clip(drive + turn, -wheelPowerLimit, wheelPowerLimit);

//        //Diagonal Forward Right
//        leftFrontWheelPower = -wheelPowerLimit;
//        rightFrontWheelPower = 0;
//        leftRearWheelPower = 0;
//        rightRearWheelPower = -wheelPowerLimit;
//
//        //Diagonal Forward Left
//
//        leftFrontWheelPower = 0;
//        rightFrontWheelPower = -wheelPowerLimit;
//        leftRearWheelPower = -wheelPowerLimit;
//        rightRearWheelPower = 0;

        leftFrontWheelPower = drive;
        rightFrontWheelPower = drive;
        leftRearWheelPower = drive;
        rightRearWheelPower = drive;

        leftFrontWheelMotor.setPower(leftFrontWheelPower);
        rightFrontWheelMotor.setPower(rightFrontWheelPower);
        leftRearWheelMotor.setPower(leftRearWheelPower);
        rightRearWheelMotor.setPower(rightRearWheelPower);

        do {
            targetPosition = identifyNavigationTarget(targetsFreightFrenzy);
        } while (targetPosition.getRange()>DESIRED_DISTANCE);
    }




    private NAVTARGET getMeNextTarget(NAVTARGET target) {
        switch(target) {
            case BlueStorage:
                return NAVTARGET.BlueAllianceWall;
            case BlueAllianceWall:
                return NAVTARGET.RedAllianceWall;
            case RedAllianceWall:
                return NAVTARGET.RedStorage;
            case RedStorage:
            default:
                return NAVTARGET.BlueStorage;
        }
    }

    private void aboutTurn() {
        double turnSlowLimit = 0.25;
        leftFrontWheelPower = Range.clip(-2, -turnSlowLimit, turnSlowLimit);
        rightFrontWheelPower = Range.clip(2, -turnSlowLimit, turnSlowLimit);
        leftRearWheelPower = Range.clip(-2, -turnSlowLimit, turnSlowLimit);
        rightRearWheelPower = Range.clip(2, -turnSlowLimit, turnSlowLimit);
    }


    /*
     *  Method to perform a relative move, based on encoder counts.
     *  Encoders are not reset as the move is based on the current position.
     *  Move will stop if any of three conditions occur:
     *  1) Move gets to the desired position
     *  2) Move runs out of time
     *  3) Driver stops the opmode running.

    public void encoderDrive(double speed,
                             double leftInches, double rightInches,
                             double timeoutS) {
        int newLeftTarget;
        int newRightTarget;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newLeftTarget = robot.leftDrive.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
            newRightTarget = robot.rightDrive.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);
            robot.leftDrive.setTargetPosition(newLeftTarget);
            robot.rightDrive.setTargetPosition(newRightTarget);

            // Turn On RUN_TO_POSITION
            robot.leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            robot.leftDrive.setPower(Math.abs(speed));
            robot.rightDrive.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (robot.leftDrive.isBusy() && robot.rightDrive.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1",  "Running to %7d :%7d", newLeftTarget,  newRightTarget);
                telemetry.addData("Path2",  "Running at %7d :%7d",
                        robot.leftDrive.getCurrentPosition(),
                        robot.rightDrive.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            robot.leftDrive.setPower(0);
            robot.rightDrive.setPower(0);

            // Turn off RUN_TO_POSITION
            robot.leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            //  sleep(250);   // optional pause after each move
        }
    }
     */

}
