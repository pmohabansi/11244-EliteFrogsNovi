package org.firstinspires.ftc.teamcode.programs;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.capability.BaseAutonomous;
import org.firstinspires.ftc.teamcode.enums.ARM_POSITION;
import org.firstinspires.ftc.teamcode.enums.AXIS;
import org.firstinspires.ftc.teamcode.enums.DIRECTION;
import org.firstinspires.ftc.teamcode.enums.ROBOT_STATES;

/**
 * This program expects the robot to be parked in Red Alliance,
 * far side from Red Warehouse facing blue wall.
 * It will go to Team hub, drop load and park in Storage.
 * Uses Arm and intake spinner.
 */
@Autonomous(name="Auto Red Storage", group = "Concept")
public class AutoRedStorage extends BaseAutonomous {

    protected void playGame() {
        telemetry.addData("Status : " , currentState.label);
        telemetry.update();

        switch (currentState) {
            case INITIAL_POSITION:
                driveRobotRamp(0.2, .5, DIRECTION.FORWARD, 18.5, 10);
                turnRobot(45, AXIS.Z, DIRECTION.RIGHT_SPIN, 1,10, true);

                setCurrentState(ROBOT_STATES.REACHED_TEAM_HUB);
                break;

            case REACHED_TEAM_HUB:
                armController.moveToHubPosFromRest(ARM_POSITION.TIER_THREE);
                sleep(500);
                intakeController.pushObjectOut(.5);
                sleep(2000);
                intakeController.stop();

                setCurrentState(ROBOT_STATES.DROPPED_OBJECT_IN_TEAM_HUB);
                break;

            case DROPPED_OBJECT_IN_TEAM_HUB:
                turnRobot(15, AXIS.Z, DIRECTION.RIGHT_SPIN, 1,10, true);
                driveRobotRamp(0.1, .5, DIRECTION.BACKWARD, 30, 10);
                driveRobot(0.1, DIRECTION.BACKWARD, 4.8, 10);

                setCurrentState(ROBOT_STATES.REACHED_CAROUSEL);
                break;

            case REACHED_CAROUSEL:
                spinnerController.spinCarousel(0.5, false, 4);
                while(spinnerController.isRunning()) {
                    //Do Nothing
                }

                setCurrentState(ROBOT_STATES.SPINNED_OFF_CAROUSEL);
                break;

            case SPINNED_OFF_CAROUSEL:
                driveRobot(0.2, DIRECTION.FORWARD, 2, 10);
                turnRobot(60, AXIS.Z, DIRECTION.LEFT_SPIN, 1,10, true);

                setCurrentState(ROBOT_STATES.READY_TO_PARK);
                break;

            case READY_TO_PARK:
                driveRobotRamp(0.1, .4, DIRECTION.FORWARD, 20, 10);
                armController.moveToRestPosition();

                setCurrentState(ROBOT_STATES.PARKED_IN_RED_STORAGE);
                break;

            case PARKED_IN_RED_STORAGE:
            default:
                stopRobot();
        }
    }

}