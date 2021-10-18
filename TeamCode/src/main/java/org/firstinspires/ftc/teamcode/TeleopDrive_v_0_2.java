/*
 * Developer: Apar Mohabansi
 * Date: 10/10/2017
 */
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@SuppressWarnings({"WeakerAccess", "FieldCanBeLocal"})

@TeleOp(name = "Concept: TeleopDrive_v_0_2", group = "Concept")
public class TeleopDrive_v_0_2 extends LinearOpMode {

    // Define variables for power to be given to the motors.
    double carouselWheelPower;
    double wheelPowerLimit     = 0.75;

    // Define variables for motors which are connected` to the wheels to rotate.
    DcMotor carouselWheelMotor  = null;

    // Declare LinearOpMode members.
    ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        carouselWheelMotor  = hardwareMap.get(DcMotor.class, "CS");

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        carouselWheelMotor.setDirection(DcMotor.Direction.FORWARD);

        carouselWheelMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");

        waitForStart();

        while (opModeIsActive()) {

            // Initialize power to zero so in case user is not pressing any keys then
            // robot should remain in same position.
            carouselWheelPower = 0;

            // calculated power to be given to wheels
            // if power value is -ve then robot forward &
            // when power value is +ve then robot backward
             if (gamepad1.right_trigger != 0) {
                // This is for shifting the robot to the right
                telemetry.addLine("carouselWheelMotor");

                carouselWheelPower = Range.clip(-gamepad1.right_trigger, -wheelPowerLimit, wheelPowerLimit);
            }

            telemetry.addLine("");
            // Send calculated power to wheels
            carouselWheelMotor.setPower(carouselWheelPower);

            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Motors", "carouselWheelPower (%.2f).", carouselWheelPower);

            telemetry.update();
        }
    }
}
