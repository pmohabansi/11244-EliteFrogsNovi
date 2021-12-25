package org.firstinspires.ftc.teamcode.programs.sandbox;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.capability.BaseAutonomous;
import org.firstinspires.ftc.teamcode.enums.ARM_POSITION;
import org.firstinspires.ftc.teamcode.enums.AXIS;
import org.firstinspires.ftc.teamcode.enums.DIRECTION;
import org.firstinspires.ftc.teamcode.enums.ROBOT_STATES;

/**
 * This program expects the robot to be parked in Blue Alliance,
 * near side from Blue Warehouse facing red wall.
 * It will go to Team hub, lift one more item and drop and park in storage.
 * Uses Arm and intake spinner.
 */
@Autonomous(name="Auto Blue WareHouse Full Blown", group = "Concept")
@Disabled
public class AutoBlueWarehouseFullBlown extends BaseAutonomous {

    private int pickDropCycleCount = 0;
    private int pickDropMaxCycleCount = 1;

    protected void playGame() {
        telemetry.addData("Status : " , currentState.label);
        telemetry.update();

        switch (currentState) {
            case INITIAL_POSITION:
                driveRobotRamp(0.2, .5, DIRECTION.FORWARD, 21, 30);
                turnRobot(55, AXIS.Z, DIRECTION.RIGHT_SPIN, 1,30, true);

                armController.moveToHubPosFromRest(ARM_POSITION.TIER_THREE);
                sleep(500);

                setCurrentState(ROBOT_STATES.REACHED_TEAM_HUB);
                break;

            case REACHED_TEAM_HUB:
                intakeController.pushObjectOut(.6);
                sleep(2000);
                intakeController.stop();

                if(pickDropCycleCount==pickDropMaxCycleCount) {
                    setCurrentState(ROBOT_STATES.READY_TO_PARK);
                } else {
                    setCurrentState(ROBOT_STATES.DROPPED_OBJECT_IN_TEAM_HUB);
                }
                break;

            case DROPPED_OBJECT_IN_TEAM_HUB:
                turnRobot(55, AXIS.Z, DIRECTION.LEFT_SPIN, 1,30, true);
                driveRobotRamp(0.1, .5, DIRECTION.BACKWARD, 18, 30);
                turnRobot(85, AXIS.Z, DIRECTION.LEFT_SPIN, 1,30, true);
//                turnRobot(45, AXIS.Z, DIRECTION.LEFT_SPIN, 1,30, true);
                driveRobot(0.3, DIRECTION.LEFTWARD, 7, 30);
                armController.moveToGround();
                driveRobotRamp(0.1, 0.5, DIRECTION.FORWARD, 40, 30);

                setCurrentState(ROBOT_STATES.REACHED_BLUE_WAREHOUSE);
                break;

            case REACHED_BLUE_WAREHOUSE:
                sleep(100);
                driveRobot(0.3, DIRECTION.FORWARD, 3, 30);
                intakeController.pullObjectIn(1);
                sleep(2000);
                intakeController.stop();

                pickDropCycleCount++;

                setCurrentState(ROBOT_STATES.OBJECT_PICKED_UP);
                break;

            case OBJECT_PICKED_UP:
                driveRobotRamp(0.1, 0.4, DIRECTION.BACKWARD, 40, 30);
                turnRobot(90, AXIS.Z, DIRECTION.RIGHT_SPIN, 1,30, true);
                driveRobotRamp(0.1, 0.4, DIRECTION.FORWARD, 18.5, 30);
                turnRobot(55, AXIS.Z, DIRECTION.RIGHT_SPIN, 1,30, true);

                armController.moveToHubPosFromGround(ARM_POSITION.TIER_THREE);
                sleep(500);

                setCurrentState(ROBOT_STATES.REACHED_TEAM_HUB);
                break;

            case READY_TO_PARK:
                driveRobotRamp(0.1, .5, DIRECTION.BACKWARD, 5, 30);
                turnRobot(45, AXIS.Z, DIRECTION.RIGHT_SPIN, 1,30, true);
                driveRobot(1, DIRECTION.BACKWARD, 40, 10);
                armController.moveToRestPosition();

                setCurrentState(ROBOT_STATES.PARKED_IN_BLUE_STORAGE);
                break;

            case PARKED_IN_BLUE_STORAGE:
            default:
                stopRobot();
        }
    }

}