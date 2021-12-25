package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;

/**
 * This class would be used to define and initialize all
 * hardware components of the Robot
 */
public class Robot implements RobotParameters {

    public DcMotor rightRearWheelMotor;
    public DcMotor rightFrontWheelMotor;
    public DcMotor leftRearWheelMotor;
    public DcMotor leftFrontWheelMotor;

    public DcMotor carouselWheelMotor;
    public DcMotor armWheelMotor_G;
    public DcMotor armWheelMotor_T;
    public Servo leftClawMotor;
    public Servo rightClawMotor;

    public CRServo intakeMotor;

    public BNO055IMU imu;
    public BNO055IMU.Parameters imuPRM;

    public WebcamName webcamName = null;
    public int cameraMonitorViewId = -1;

    private HardwareMap hardwareMap;

    private boolean autonomous = false;

    public Robot() {
        armWheelMotor_G = null;
        armWheelMotor_T = null;
    }

    /* Initialize standard Hardware interfaces */
    public void initialize(HardwareMap hardwareMap, boolean autonomous) {
        // save reference to HW Map
        this.hardwareMap = hardwareMap;
        this.autonomous = autonomous;

        initIMU();
        initCarousel();
        initWheelMotors(true);
        initClaw();
        initIntakeMotor();
        initCamera();
        //Register and init other hardware components here in similar fashion
    }

    private void initWheelMotors(boolean useEncoders) {
        rightRearWheelMotor = hardwareMap.get(DcMotor.class, "RR");
        rightFrontWheelMotor = hardwareMap.get(DcMotor.class, "FR");
        leftRearWheelMotor = hardwareMap.get(DcMotor.class, "RL");
        leftFrontWheelMotor = hardwareMap.get(DcMotor.class, "FL");

        DcMotor.RunMode mode = useEncoders ? DcMotor.RunMode.RUN_USING_ENCODER : DcMotor.RunMode.RUN_WITHOUT_ENCODER;

        rightRearWheelMotor.setMode(mode);
        rightFrontWheelMotor.setMode(mode);
        leftRearWheelMotor.setMode(mode);
        leftFrontWheelMotor.setMode(mode);

        rightRearWheelMotor.setPower(0);
        rightFrontWheelMotor.setPower(0);
        leftRearWheelMotor.setPower(0);
        leftFrontWheelMotor.setPower(0);

        if(autonomous) {
            leftFrontWheelMotor.setDirection(DcMotor.Direction.REVERSE);
            rightFrontWheelMotor.setDirection(DcMotor.Direction.FORWARD);
            leftRearWheelMotor.setDirection(DcMotor.Direction.REVERSE);
            rightRearWheelMotor.setDirection(DcMotor.Direction.FORWARD);
        } else {
            leftFrontWheelMotor.setDirection(DcMotor.Direction.FORWARD);
            rightFrontWheelMotor.setDirection(DcMotor.Direction.REVERSE);
            leftRearWheelMotor.setDirection(DcMotor.Direction.FORWARD);
            rightRearWheelMotor.setDirection(DcMotor.Direction.REVERSE);
        }

        rightRearWheelMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFrontWheelMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftRearWheelMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftFrontWheelMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void initIMU() {
        imu = hardwareMap.get(BNO055IMU.class, "imu");

        if(imu!=null) {
            imuPRM = new BNO055IMU.Parameters();
            imuPRM.angleUnit = BNO055IMU.AngleUnit.DEGREES;
            imuPRM.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
            imuPRM.calibrationDataFile = "BNO055IMUCalibration.json";
            imuPRM.loggingEnabled = true;
            imuPRM.loggingTag = "IMU";
            imuPRM.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

            imu.initialize(imuPRM);
        }
    }

    private void initClaw() {
        try {
            armWheelMotor_G = hardwareMap.get(DcMotor.class, "arm_G");
        } catch(Exception ex) {
            //TODO
        }

        try {
            armWheelMotor_T = hardwareMap.get(DcMotor.class, "arm_T");
        } catch(Exception ex) {
            //TODO
        }

        try {
            leftClawMotor = hardwareMap.servo.get("las");
        } catch(Exception ex) {
            //TODO
        }

        try {
            rightClawMotor = hardwareMap.servo.get("ras");
        } catch(Exception ex) {
            //TODO
        }

        if(armWheelMotor_G !=null) {
            armWheelMotor_G.setDirection(DcMotor.Direction.FORWARD);
            armWheelMotor_G.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            armWheelMotor_G.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        if(armWheelMotor_T !=null) {
            armWheelMotor_T.setDirection(DcMotor.Direction.FORWARD);
            armWheelMotor_T.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            armWheelMotor_T.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        if(leftClawMotor!=null && rightClawMotor!=null) {
            leftClawMotor.setDirection(Servo.Direction.REVERSE);
            rightClawMotor.setDirection(Servo.Direction.FORWARD);
        }
    }

    private void initIntakeMotor() {
        try {
            intakeMotor = hardwareMap.crservo.get("intake");
            intakeMotor.resetDeviceConfigurationForOpMode();
        } catch(Exception ex) {
            //TODO
        }
        if(intakeMotor!=null) {
            intakeMotor.setDirection(CRServo.Direction.FORWARD);
        }
    }

    private void initCarousel() {
        try {
            carouselWheelMotor = hardwareMap.get(DcMotor.class, "CS");
            carouselWheelMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            carouselWheelMotor.setDirection(DcMotor.Direction.REVERSE);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    private void initCamera() {
        try {
            webcamName = hardwareMap.get(WebcamName.class, "Webcam 1");
            cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        } catch(Exception ex){
            //TODO
        }
    }

}