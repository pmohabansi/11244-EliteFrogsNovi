package org.firstinspires.ftc.teamcode.hardware;

public interface RobotParameters {

    // Currently: Go-Builda 5203
    public static final double COUNTS_PER_MOTOR_REV = 537.7;
    // There is no gear reduction, we are using 1:1
    public static final double DRIVE_GEAR_REDUCTION = 1;

    // For figuring circumference - Mecanum Wheels - 96mm/3.7785 inches
    public static final double WHEEL_DIAMETER_INCHES = 3.7795;
    public static final double CORRECTION_FACTOR = 1;
    public static final double COUNTS_PER_INCH = CORRECTION_FACTOR * (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * 3.1415);

    static final double ARM_COUNTS_PER_MOTOR_REV_T = 1440;    // eg: TETRIX Motor Encoder
    static final double ARM_COUNTS_PER_MOTOR_REV_G = 2786.0;    // eg: Gobilda Motor Encoder
    static final double ARM_DRIVE_GEAR_REDUCTION = 1;       // This is < 1.0 if geared UP
    static final double ARM_WHEEL_DIAMETER_INCHES = 10.0;   // For figuring circumference
    public static final double ARM_TURN_SPEED = 0.5;


    // Adjust these numbers to suit your robot.
    final double DESIRED_DISTANCE = 2.0; //  this is how close the camera should get to the target (inches)
    //  The GAIN constants set the relationship between the measured position error,
    //  and how much power is applied to the drive motors.  Drive = Error * Gain
    //  Make these values smaller for smoother control.
    final double SPEED_GAIN = 0.02;   //  Speed Control "Gain". eg: Ramp up to 50% power at a 25 inch error.   (0.50 / 25.0)
    final double TURN_GAIN = 0.01;   //  Turn Control "Gain".  eg: Ramp up to 25% power at a 25 degree error. (0.25 / 25.0)
    final double MM_PER_INCH = 25.40;   //  Metric conversion
    static final double wheelPowerLimit     = 0.65;


    public double gyroTurnThreshold = 1.4375;
    public double gyroTurnModLeft = .025;
    public double gyroTurnModRight = -.015;

}
