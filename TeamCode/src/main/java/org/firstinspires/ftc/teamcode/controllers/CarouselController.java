package org.firstinspires.ftc.teamcode.controllers;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.hardware.Robot;

public class CarouselController extends BaseController {

    private boolean stopIssued = false;
    private boolean isRunning = false;

    private double teleOpSpinnerPower = 0.70;

    public CarouselController(Robot robot, Telemetry telemetry, Gamepad gamepad) {
        super(robot, telemetry, gamepad);
    }

    @Override
    public void controlUsingConsole() {
        telemetry.addLine("Inside drive console\n");

        double carouselWheelPower = 0;
        if(gamepad.right_bumper) {
            carouselWheelPower = teleOpSpinnerPower;
        } else if(gamepad.left_bumper) {
            carouselWheelPower = -teleOpSpinnerPower;
        }
        robot.carouselWheelMotor.setPower(carouselWheelPower);

        // Show the elapsed game time and wheel power.
        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.addData("Motors", "Carousel (%.2f)", carouselWheelPower);
        telemetry.update();
    }

    public void spinCarousel(double power, boolean clockWise, double timeoutS) {
        telemetry.addLine("Carousel Spin Initiated");
        telemetry.update();
        runtime.reset();
        if(robot.carouselWheelMotor==null) {
            telemetry.addLine("Carousel Spin Motor Not Found");
            telemetry.update();
            return;
        }
        double finalPower = clockWise? power * (-1) : power;
        robot.carouselWheelMotor.setPower(finalPower);
        do {
            isRunning = true;
            telemetry.addLine("Carousel Spinning.");
            telemetry.addData("Time Left: ",  timeoutS-runtime.seconds());
            telemetry.update();
        } while (runtime.seconds() < timeoutS && !stopIssued);

        robot.carouselWheelMotor.setPower(0);
        isRunning = false;
        telemetry.addLine("Carousel Stopped");
        telemetry.update();
    }

    public void stopCarousel() {
        telemetry.addLine("Stop Carousel Issued");
        stopIssued = true;
        telemetry.update();
    }

    public boolean isRunning() {
        return isRunning;
    }

}
