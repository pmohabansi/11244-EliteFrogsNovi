package org.firstinspires.ftc.teamcode.programs;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.capability.BaseAutonomous;
import org.firstinspires.ftc.teamcode.enums.ARM_POSITION;
import org.firstinspires.ftc.teamcode.enums.AXIS;
import org.firstinspires.ftc.teamcode.enums.DIRECTION;
import org.firstinspires.ftc.teamcode.enums.ROBOT_STATES;

/**
 * This program expects the robot to be parked in Red Alliance,
 * near side from Red Warehouse facing Blue wall.
 * It will go to Team hub, drop and park in Red Warehouse.
 * Uses Arm and intake spinner.
 */
@Autonomous(name="Auto Red WareHouse", group = "Concept")
public class AutoRedWarehouse extends BaseAutonomous {

    protected void playGame() {
        telemetry.addData("Status : " , currentState.label);
        telemetry.update();

        switch (currentState) {
            case INITIAL_POSITION:
                driveRobotRamp(0.2, .5, DIRECTION.FORWARD, 21, 30);
                turnRobot(55, AXIS.Z, DIRECTION.LEFT_SPIN, 1,30, true);

                setCurrentState(ROBOT_STATES.REACHED_TEAM_HUB);
                break;

            case REACHED_TEAM_HUB:
                armController.moveToHubPosFromRest(ARM_POSITION.TIER_THREE);
                intakeController.pushObjectOut(0.5);
                sleep(2000);
                intakeController.stop();

                setCurrentState(ROBOT_STATES.DROPPED_OBJECT_IN_TEAM_HUB);
                break;

            case DROPPED_OBJECT_IN_TEAM_HUB:
                turnRobot(35, AXIS.Z, DIRECTION.LEFT_SPIN, 1,30, true);
                driveRobotRamp(0.1, .6, DIRECTION.LEFTWARD, 22, 30);
                driveRobot(0.3, DIRECTION.LEFTWARD, 9.5, 30);
                driveRobotRamp(0.1, 0.5, DIRECTION.BACKWARD, 40, 30);

                setCurrentState(ROBOT_STATES.REACHED_RED_WAREHOUSE);
                break;

            case REACHED_RED_WAREHOUSE:
                driveRobot(0.3, DIRECTION.RIGHTWARD, 30, 30);
                armController.moveToRestPosition();
                driveRobot(0.3, DIRECTION.BACKWARD, 13, 30);

                setCurrentState(ROBOT_STATES.PARKED_IN_RED_WAREHOUSE);
                break;

            case PARKED_IN_RED_WAREHOUSE:
            default:
                stopRobot();
        }
    }

}