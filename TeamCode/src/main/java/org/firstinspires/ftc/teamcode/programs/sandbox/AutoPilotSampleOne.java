package org.firstinspires.ftc.teamcode.programs.sandbox;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.capability.BaseAutonomous;
import org.firstinspires.ftc.teamcode.capability.NavigationLayer;
import org.firstinspires.ftc.teamcode.enums.ROBOT_STATES;
import org.firstinspires.ftc.teamcode.enums.AXIS;
import org.firstinspires.ftc.teamcode.enums.DIRECTION;

/*
  DO NOT MODIFY THIS CLASS. THIS IS HERE TO SERVE AS A SAMPLE/TEMPLATE.
  MAKE A COPY AND PROVIDE A NEW NAME FOR EACH OF THE PROGRAM YOU WILL CREATE.
  FOR EG.
     - BlueNearToWRHouseAutoOp
     - BlueFarFromWRHouseAutoOp
     - RedNearToWRHouseAutoOp
     - RedFarFromWRHouseAutoOp
 */
@Autonomous(name="Auto Pilot Sample One", group = "Concept")
@Disabled
public class AutoPilotSampleOne extends BaseAutonomous {

    private int barcode = 1;

    private boolean isEndGameOn = false;

    protected ElapsedTime autoElapsedTime = new ElapsedTime();

    @Override
    public void runOpMode() {
        initialize();

        telemetry.addData(">", "Press Play to start");
        telemetry.update();

        waitForStart();

        autoElapsedTime.reset();
//        createTimeKeeper();

        while (opModeIsActive()) {
            if (isStopRequested()) return;

            if (!isEndGameOn) {
                playGame();
            } else {
                playEndGame();
            }

            telemetry.update();
        }
        releaseResources();

    }

    protected void playGame() {
        telemetry.addLine("Status : " + currentState.label);
        switch (currentState) {
            case INITIAL_POSITION:
                int markerPosition = identifyDuckMarkerPosition();
                if (markerPosition > 0) {
                    barcode = markerPosition;
                    telemetry.addData("Identified Duck Marker Position", barcode);
                } else {
                    telemetry.addLine("Did not identify Duck Marker Position");
                }
                goFromInitialPositionToSharedHub();
                currentState = ROBOT_STATES.REACHED_SHARED_HUB;
                break;

            case REACHED_SHARED_HUB:
                dropObjectIntoSharedHub(barcode);
                currentState = ROBOT_STATES.DROPPED_OBJECT_IN_SHARED_HUB;
                break;

            case DROPPED_OBJECT_IN_SHARED_HUB:
                travelFromSharedHubToBlueWallTarget();

                //NOTE: Temporary Code to turn around since, previous no code for other states above
                turnAroundAndGetToTargetWall();

                currentState = ROBOT_STATES.REACHED_BLUE_WALL_TARGET;
                break;

            case REACHED_BLUE_WALL_TARGET:
                goFromWallToWarehouse();
                currentState = ROBOT_STATES.REACHED_BLUE_WAREHOUSE;
                break;

            case REACHED_BLUE_WAREHOUSE:
                grabPayload();
                currentState = ROBOT_STATES.PICKED_UP_PAYLOAD;
                break;

            default:
                stopRobot();
        }
    }

    private void grabPayload() {
    }

    private void goFromWallToWarehouse() {
    }

    private void turnAroundAndGetToTargetWall() {
    }

    private void goFromInitialPositionToSharedHub() {

    }

    private void goFromInitialPositionToBlueWarehouse() {
        driveRobot(10, DIRECTION.FORWARD, 200, 1000);
        turnRobot(90, AXIS.X, DIRECTION.LEFT_SPIN, 10, 1000, true);
        driveRobot(10, DIRECTION.FORWARD, 200, 1000);
        turnRobot(90, AXIS.X, DIRECTION.LEFT_SPIN,10, 1000, true);
        driveRobot(10, DIRECTION.FORWARD, 200, 1000);
        turnRobot(90, AXIS.X, DIRECTION.LEFT_SPIN,10, 1000, true);
        driveRobot(10, DIRECTION.FORWARD, 200, 1000);
        turnRobot(90, AXIS.X, DIRECTION.LEFT_SPIN,10, 1000, true);
    }

    private void dropObjectIntoSharedHub(int barcode) {

    }

    private void travelFromSharedHubToBlueWallTarget() {
        //            NavTargetPosition targetPosition = identifyNavigationTarget();

//            // If we have found a target.
//            if (targetPosition!=null) {
//                telemetry.addData("Target", " %s", targetPosition.getName());
//                telemetry.addData("Range", "%5.1f inches", targetPosition.getRange());
//                telemetry.addData("Bearing", "%3.0f degrees", targetPosition.getBearing());
//            }
    }

    private void playEndGame() {
        switch (currentState) {
            case END_GAME_STARTED:
                //identifySomeReferenceWhereYouAre
                //Say run towards blue wall target
                break;
            case REACHED_BLUE_WALL_TARGET:
                parkInWareHouse();
                currentState = ROBOT_STATES.PARKED_IN_BLUE_WAREHOUSE;
                break;
            default:
        }

    }

    private void parkInWareHouse() {
    }

}


