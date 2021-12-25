package org.firstinspires.ftc.teamcode.programs.sandbox;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.capability.NavigationLayer;
import org.firstinspires.ftc.teamcode.enums.AXIS;
import org.firstinspires.ftc.teamcode.enums.DIRECTION;

@Autonomous(name="Auto Spin Around", group = "Concept")
@Disabled
public class AutoSpinAround extends NavigationLayer {

    @Override
    public void runOpMode() {
        initialize();

        telemetry.addData(">", "Press Play to start");
        telemetry.update();

//        waitForStart();

        while (opModeIsActive()) {
            turnRobot(10, AXIS.Z, DIRECTION.LEFT_SPIN,2, 500, true);
            sleep(1000);
            turnRobot(25, AXIS.Z, DIRECTION.LEFT_SPIN,2, 500, true);
            sleep(1000);
            turnRobot(45, AXIS.Z, DIRECTION.LEFT_SPIN,2, 500, true);
            sleep(1000);
            turnRobot(75, AXIS.Z, DIRECTION.LEFT_SPIN,2, 500, true);
            sleep(1000);
            turnRobot(75, AXIS.Z, DIRECTION.RIGHT_SPIN,2, 500, true);
            sleep(1000);
            turnRobot(45, AXIS.Z, DIRECTION.RIGHT_SPIN,2, 500, true);
            sleep(1000);
            turnRobot(25, AXIS.Z, DIRECTION.RIGHT_SPIN,2, 500, true);
            sleep(1000);
            turnRobot(10, AXIS.Z, DIRECTION.RIGHT_SPIN,2, 500, true);

//            turnRobot(90, AXIS.Z, 2, 500, true);
//            turnRobot(-90, AXIS.Z, 2, 500, true);
//            turnRobot(90, AXIS.Z, 2, 500, true);
//            turnRobot(-90, AXIS.Z, 2, 500, true);

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
