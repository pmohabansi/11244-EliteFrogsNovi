package org.firstinspires.ftc.teamcode.programs.sandbox;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.capability.NavigationLayer;
import org.firstinspires.ftc.teamcode.enums.AXIS;
import org.firstinspires.ftc.teamcode.enums.DIRECTION;

@Autonomous(name="Auto Diagonal Test", group = "Concept")
@Disabled
public class AutoDiagonalTest extends NavigationLayer {

    @Override
    public void runOpMode() {
        initialize();

        telemetry.addData(">", "Press Play to start");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            turnRobot(10, AXIS.Z, DIRECTION.DIAGONALLY_LEFT_FORWARD,.5, 500, true);
            sleep(100);
            turnRobot(25, AXIS.Z, DIRECTION.DIAGONALLY_LEFT_BACKWARD,.5, 500, true);
            sleep(100);
            turnRobot(45, AXIS.Z, DIRECTION.DIAGONALLY_RIGHT_FORWARD,.5, 500, true);
            sleep(100);
            turnRobot(75, AXIS.Z, DIRECTION.DIAGONALLY_RIGHT_BACKWARD,.5, 500, true);
            sleep(100);
            break;
        }
        telemetry.update();
        releaseResources();
    }

    @Override
    protected void initialize() {
        super.initialize();
   }

    @Override
    protected void releaseResources() {
        super.releaseResources();
    }


}
