package org.firstinspires.ftc.teamcode.controllers;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.hardware.Robot;
import org.firstinspires.ftc.teamcode.hardware.RobotParameters;

public abstract class BaseArmController extends BaseController{

    protected boolean isGobildaMotorInstalled = false;
    protected boolean isTetrixMotorInstalled = false;

    protected static final double FINAL_REST_POSITION_POWER_G = 0.001;   // For the gobilda motor
    protected static final double FINAL_REST_POSITION_POWER_T = 0.0001; // For the Tetrix motor

    public double ARM_COUNTS_PER_INCH = 0;

    public BaseArmController(Robot robot, Telemetry telemetry, Gamepad gamepad) {
        super(robot, telemetry, gamepad);

        if (robot.armWheelMotor_G != null && robot.armWheelMotor_G.getMotorType().getTicksPerRev() == RobotParameters.ARM_COUNTS_PER_MOTOR_REV_G) {
            isGobildaMotorInstalled = true;
        } else if (robot.armWheelMotor_T != null && robot.armWheelMotor_T.getMotorType().getTicksPerRev() == RobotParameters.ARM_COUNTS_PER_MOTOR_REV_T) {
            isTetrixMotorInstalled = true;
        }
        double ARM_COUNTS_PER_MOTOR_REV = isGobildaMotorInstalled? RobotParameters.ARM_COUNTS_PER_MOTOR_REV_G : RobotParameters.ARM_COUNTS_PER_MOTOR_REV_T;
        ARM_COUNTS_PER_INCH = (ARM_COUNTS_PER_MOTOR_REV * RobotParameters.ARM_DRIVE_GEAR_REDUCTION) / (RobotParameters.ARM_WHEEL_DIAMETER_INCHES * 3.1415);
    }

    protected DcMotor getMotor() {
        if (isGobildaMotorInstalled) {
            return robot.armWheelMotor_G;
        } else if (isTetrixMotorInstalled) {
            return robot.armWheelMotor_T;
        }
        return null;
    }

    protected double getBrakingPower() {
        return isGobildaMotorInstalled? FINAL_REST_POSITION_POWER_G : (FINAL_REST_POSITION_POWER_T + 0.0008);
    }


}
