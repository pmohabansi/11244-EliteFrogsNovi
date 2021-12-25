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

@Autonomous(name="Spin And Park Near Blue WH", group = "Concept")
@Disabled
public class AutoSpinAndParkNearBW extends BaseAutonomous {

    private ClawController clawController;
    private CarouselController spinnerController;

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
                turnRobot(45, AXIS.Z, DIRECTION.RIGHT_SPIN,1, 500, true);
                sleep(10);
                turnRobot(45, AXIS.Z, DIRECTION.RIGHT_SPIN,1, 500, true);
                sleep(10);

                driveRobot(0.5, DIRECTION.FORWARD, 150, 200);
                turnRobot(45, AXIS.Z, DIRECTION.RIGHT_SPIN,1, 500, true);
                driveRobot(0.1, DIRECTION.FORWARD, 5, 10);

                setCurrentState(ROBOT_STATES.REACHED_CAROUSEL);
                break;

            case REACHED_CAROUSEL:
                spinnerController.spinCarousel(1, true, 15);
                while(spinnerController.isRunning()) {
                    //Do Nothing
                }
                setCurrentState(ROBOT_STATES.SPINNED_OFF_CAROUSEL);
                break;
            case SPINNED_OFF_CAROUSEL:

                driveRobot(.1, DIRECTION.BACKWARD, 5, 20);
                turnRobot(45, AXIS.Z, DIRECTION.LEFT_SPIN,1, 200, true);
                sleep(10);
                driveRobot(.1, DIRECTION.LEFTWARD, 10, 50);

                setCurrentState(ROBOT_STATES.READY_TO_PARK);
                break;

            case READY_TO_PARK:
                driveRobot(1, DIRECTION.BACKWARD, 250, 1000);
                turnRobot(45, AXIS.Z, DIRECTION.LEFT_SPIN,1, 500, true);
                sleep(10);
                turnRobot(45, AXIS.Z, DIRECTION.LEFT_SPIN,1, 500, true);
                sleep(10);
                driveRobot(1, DIRECTION.FORWARD, 20, 100);

                setCurrentState(ROBOT_STATES.PARKED_IN_BLUE_WAREHOUSE);
                break;

            case REACHED_BLUE_WAREHOUSE:
                break;

            default:
                stopRobot();

        }
    }


}


