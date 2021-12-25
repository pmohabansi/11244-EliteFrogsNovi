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
 * far side from Blue Warehouse facing red wall.
 * It will go to Team hub, spin carousel and park in storage.
 * Uses Arm and intake spinner.
 */
@Autonomous(name="Auto Red Storage Parallel", group = "Concept")
@Disabled
public class AutoRedStorageParallel extends BaseAutonomous {

    protected void playGame() {
        telemetry.addData("Status : " , currentState.label);
        telemetry.update();
        switch (currentState) {
            case INITIAL_POSITION:
                Thread armThread = new Thread() {
                    @Override
                    public void run() {
                        armController.moveToHubPosFromRest(ARM_POSITION.TIER_THREE);
                    }
                };
                armThread.run();
                driveRobotRamp(0.2, .5, DIRECTION.FORWARD, 18.5, 10);
                turnRobot(45, AXIS.Z, DIRECTION.LEFT_SPIN, 1,10, true);

                setCurrentState(ROBOT_STATES.REACHED_TEAM_HUB);
                break;

            case REACHED_TEAM_HUB:
                intakeController.pushObjectOut(.6);
                sleep(2000);
                intakeController.stop();

                turnRobot(10, AXIS.Z, DIRECTION.LEFT_SPIN, 1,10, true);
                driveRobotRamp(0.1, .5, DIRECTION.BACKWARD, 30, 10);
                driveRobot(0.1, DIRECTION.BACKWARD, 4.8, 10);

                setCurrentState(ROBOT_STATES.REACHED_CAROUSEL);
                break;

            case REACHED_CAROUSEL:
                spinnerController.spinCarousel(0.5, true, 4);
                while(spinnerController.isRunning()) {
                    //Do Nothing
                }

                setCurrentState(ROBOT_STATES.SPINNED_OFF_CAROUSEL);
                break;

            case SPINNED_OFF_CAROUSEL:
                driveRobot(0.2, DIRECTION.FORWARD, 2, 10);
                turnRobot(65, AXIS.Z, DIRECTION.RIGHT_SPIN, 1,10, true);

                setCurrentState(ROBOT_STATES.READY_TO_PARK);
                break;

            case READY_TO_PARK:
                Thread armThread2 = new Thread() {
                    @Override
                    public void run() {
                        armController.moveToRestPosition();
                    }
                };
                armThread2.run();
                driveRobotRamp(0.1, .4, DIRECTION.FORWARD, 22, 10);

                setCurrentState(ROBOT_STATES.PARKED_IN_BLUE_STORAGE);
                break;

            case PARKED_IN_BLUE_STORAGE:
                break;

            default:
                stopRobot();
        }
    }

}