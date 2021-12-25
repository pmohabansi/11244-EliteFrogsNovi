package org.firstinspires.ftc.teamcode.programs.sandbox;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.capability.BaseAutonomous;
import org.firstinspires.ftc.teamcode.capability.NavigationLayer;
import org.firstinspires.ftc.teamcode.controllers.CarouselController;
import org.firstinspires.ftc.teamcode.controllers.ClawController;
import org.firstinspires.ftc.teamcode.enums.AXIS;
import org.firstinspires.ftc.teamcode.enums.DIRECTION;
import org.firstinspires.ftc.teamcode.enums.ROBOT_STATES;

@Autonomous(name="Just Park in WH - Near Blue WH", group = "Concept")
@Disabled
public class AutoJustParkNearBW extends BaseAutonomous {

    private ClawController clawController;
    private CarouselController spinnerController;

    private ROBOT_STATES currentState = ROBOT_STATES.INITIAL_POSITION;

    private int barcode = 1;

    private boolean isEndGameOn = false;

    protected ElapsedTime autoElapsedTime = new ElapsedTime();

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

    @Override
    public void runOpMode() {
        initialize();

        telemetry.addData(">", "Press Play to start");
        telemetry.update();

        waitForStart();

        autoElapsedTime.reset();
        createTimeKeeper();

        while (opModeIsActive()) {
            if (isStopRequested()) return;

//            if (!isEndGameOn) {
                playInitialGame();
//            }

            telemetry.update();
        }
        releaseResources();

    }

    @Override
    protected void playGame() {

    }

    private void playInitialGame() {
        telemetry.addData("Status : " , currentState.label);
        telemetry.update();
        switch (currentState) {
            case INITIAL_POSITION:
                turnRobot(45, AXIS.Z, DIRECTION.LEFT_SPIN, 1, 50, true);
                sleep(10);
                turnRobot(45, AXIS.Z, DIRECTION.LEFT_SPIN,1, 50, true);
                sleep(10);

                setCurrentState(ROBOT_STATES.REACHED_BLUE_WALL_TARGET);
                break;

            case REACHED_BLUE_WALL_TARGET:
                driveRobot(1, DIRECTION.FORWARD, 60, 100);
                turnRobot(45, AXIS.Z, DIRECTION.RIGHT_SPIN,1, 50, true);
                sleep(10);
                turnRobot(45, AXIS.Z, DIRECTION.RIGHT_SPIN,1, 50, true);
                sleep(10);
                driveRobot(1, DIRECTION.FORWARD, 40, 1000);

                setCurrentState(ROBOT_STATES.REACHED_BLUE_WAREHOUSE);
                break;

            case REACHED_BLUE_WAREHOUSE:
                turnRobot(45, AXIS.Z, DIRECTION.LEFT_SPIN, 1, 50, true);
                sleep(10);
                turnRobot(45, AXIS.Z, DIRECTION.LEFT_SPIN,1, 50, true);
                sleep(10);
                driveRobot(1, DIRECTION.FORWARD, 10, 100);
                turnRobot(45, AXIS.Z, DIRECTION.LEFT_SPIN,1, 50, true);
                sleep(10);

                setCurrentState(ROBOT_STATES.PARKED_IN_BLUE_WAREHOUSE);
                break;

            default:
                stopRobot();

        }
    }


}


