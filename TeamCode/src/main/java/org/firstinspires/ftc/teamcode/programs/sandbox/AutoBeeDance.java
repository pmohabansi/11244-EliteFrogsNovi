package org.firstinspires.ftc.teamcode.programs.sandbox;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.capability.NavigationLayer;
import org.firstinspires.ftc.teamcode.controllers.CarouselController;
import org.firstinspires.ftc.teamcode.controllers.ClawController;
import org.firstinspires.ftc.teamcode.enums.DIRECTION;
import org.firstinspires.ftc.teamcode.models.NavTargetPosition;

@Autonomous(name="Auto Bee Dance", group = "Concept")
@Disabled
public class AutoBeeDance extends NavigationLayer {

    private ClawController clawController;
    private CarouselController spinnerController;

    @Override
    public void runOpMode() {
        initialize();

        telemetry.addData(">", "Press Play to start");
        telemetry.update();

        waitForStart();
        if (isStopRequested()) return;

        while (opModeIsActive()) {
            NavTargetPosition targetPosition = identifyNavigationTarget();

            // If we have found a target.
            if (targetPosition!=null) {
                telemetry.addData("Target", " %s", targetPosition.getName());
                telemetry.addData("Range", "%5.1f inches", targetPosition.getRange());
                telemetry.addData("Bearing", "%3.0f degrees", targetPosition.getBearing());

                doTheBeeDance(targetPosition);

            } else {
                telemetry.addData(">", "Drive using joystick to find target\n");
                driveUsingConsole();
            }
            telemetry.update();
        }
        releaseResources();
    }

    @Override
    protected void initialize() {
        super.initialize();

        clawController = new ClawController(robot, telemetry, null);
        spinnerController = new CarouselController(robot, telemetry, null);
    }

    @Override
    protected void releaseResources() {
        super.releaseResources();
    }

    private void doTheBeeDance(NavTargetPosition targetPosition) {
        driveRobot(10, DIRECTION.FORWARD, targetPosition.getRange()-1, 1000 );
        driveRobot(10, DIRECTION.LEFTWARD, targetPosition.getRange()-1, 1000 );
        driveRobot(10, DIRECTION.BACKWARD, targetPosition.getRange()-1, 1000 );
        driveRobot(10, DIRECTION.RIGHTWARD, targetPosition.getRange()-1, 1000 );
    }


}
