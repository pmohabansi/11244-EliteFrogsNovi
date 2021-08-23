package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "TestTeleOp", group = "Concept")
public class TestTeleOp extends LinearOpMode {

    // Define variables for motors which are connected` to the wheels to rotate.
    DcMotor firstMotor  = null;

    /**
     * This function is executed when this Op Mode is selected from the Driver Station.
     */
    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        firstMotor  = hardwareMap.get(DcMotor.class, "first");
        firstMotor.setDirection(DcMotor.Direction.FORWARD);
        firstMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        telemetry.addData(">", "This is a new program");
        telemetry.addData(">", "Developed by 11244: Elite Frogs");

        telemetry.update();

        // Put initialization blocks here.
        waitForStart();
        if (opModeIsActive()) {
            // Put run blocks here.
            firstMotor.setPower(50);
            while (opModeIsActive()) {
                // Put loop blocks here.
                telemetry.update();
            }
        }
    }
}
