package org.firstinspires.ftc.teamcode.programs;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.capability.NavigationLayer;
import org.firstinspires.ftc.teamcode.controllers.ArmController;
import org.firstinspires.ftc.teamcode.controllers.ClawController;
import org.firstinspires.ftc.teamcode.controllers.CarouselController;
import org.firstinspires.ftc.teamcode.controllers.IntakeController;
import org.firstinspires.ftc.teamcode.enums.ROBOT_STATES;

@TeleOp(name = "TeleOpDrive", group = "Concept")
public class TeleOpDrive extends NavigationLayer {

    private ArmController armController;
    private ClawController clawController;
    private CarouselController carouselController;
    private IntakeController intakeController;

    protected ElapsedTime autoElapsedTime = new ElapsedTime();

    @Override
    protected void initialize() {
        super.initialize();
        armController = new ArmController(robot, telemetry, gamepad2);
        clawController = new ClawController(robot, telemetry, gamepad2);
        carouselController = new CarouselController(robot, telemetry, gamepad1);
        intakeController = new IntakeController(robot, telemetry, gamepad2);
    }

    @Override
    protected void releaseResources() {
        super.releaseResources();
    }

    @Override
    public void runOpMode() {
        initialize();

        telemetry.addData(">", "Press Play to start");
        telemetry.update();

        waitForStart();

        autoElapsedTime.reset();
        while (opModeIsActive()) {
            if (isStopRequested()) break;

            driveUsingConsole();
            armController.controlUsingConsole();
            clawController.controlUsingConsole();
            carouselController.controlUsingConsole();
            intakeController.controlUsingConsole();
            telemetry.update();
        }

        releaseResources();
    }
}
