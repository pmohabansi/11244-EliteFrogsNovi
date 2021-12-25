package org.firstinspires.ftc.teamcode.programs.sandbox;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.capability.BaseAutonomous;
import org.firstinspires.ftc.teamcode.enums.AXIS;
import org.firstinspires.ftc.teamcode.enums.DIRECTION;
import org.firstinspires.ftc.teamcode.enums.ROBOT_STATES;

/**
 * This program expects the robot to be parked in Blue Alliance,
 * far side from Blue Warehouse facing red wall.
 * It will go to Team hub, spin carousel and park in storage.
 */
@Autonomous(name="Drop Spin Park Far Blue WH Beta", group = "Concept")
@Disabled
public class AutoDropSpinParkFBW_BETA extends BaseAutonomous {

    @Override
    protected void performPrePlayActivity() {
//        armController.liftArmAuto(30);
   }


    protected void playGame1() {
        telemetry.addData("Status : " , currentState.label);
        telemetry.update();
        switch (currentState) {
            case INITIAL_POSITION:
                driveRobotRamp(0.2, .5, DIRECTION.FORWARD, 23.5, 200);
                turnRobot(55, AXIS.Z, DIRECTION.LEFT_SPIN, 1,500, true);

                setCurrentState(ROBOT_STATES.REACHED_TEAM_HUB);
                break;

            case REACHED_TEAM_HUB:
                intakeController.pushObjectOut();
                sleep(1000);
                driveRobotRamp(0.1, .5, DIRECTION.BACKWARD, 30, 200);
                turnRobot(25, AXIS.Z, DIRECTION.RIGHT_SPIN, 1,200, true);
//                turnRobotRamp(0.2, 1 , 15, AXIS.Z, DIRECTION.RIGHT_SPIN,500, true);
                driveRobot(0.1, DIRECTION.BACKWARD, 4, 10);

                setCurrentState(ROBOT_STATES.REACHED_CAROUSEL);
                break;

            case REACHED_CAROUSEL:
                spinnerController.spinCarousel(0.4, true, 3);
                while(spinnerController.isRunning()) {
                    //Do Nothing
                }

                setCurrentState(ROBOT_STATES.SPINNED_OFF_CAROUSEL);
                break;

            case SPINNED_OFF_CAROUSEL:
                driveRobot(0.2, DIRECTION.FORWARD, 3, 50);
                turnRobot(35, AXIS.Z, DIRECTION.RIGHT_SPIN, 1,500, true);

                setCurrentState(ROBOT_STATES.READY_TO_PARK);
                break;

            case READY_TO_PARK:
                driveRobotRamp(0.1, .4, DIRECTION.FORWARD, 13, 50);

                setCurrentState(ROBOT_STATES.PARKED_IN_BLUE_STORAGE);
                break;

            case PARKED_IN_BLUE_STORAGE:
                break;

            default:
                stopRobot();

        }
    }

    protected void playGame() {
        identifyLameDuck();
    }

    protected void playGame2() {
        telemetry.addData("Status : " , currentState.label);
        telemetry.update();
        switch (currentState) {
            case INITIAL_POSITION:
                driveRobot(0.5, DIRECTION.FORWARD, 23.5, 200);
                turnRobot(55, AXIS.Z, DIRECTION.LEFT_SPIN,1, 500, true);

                setCurrentState(ROBOT_STATES.REACHED_TEAM_HUB);
                break;

            case REACHED_TEAM_HUB:
                clawController.releaseHold();
                sleep(200);

                driveRobot(0.5, DIRECTION.BACKWARD, 31, 200);
                turnRobot(15, AXIS.Z, DIRECTION.RIGHT_SPIN,1, 500, true);
                driveRobot(0.2, DIRECTION.BACKWARD, 4.5, 10);

                setCurrentState(ROBOT_STATES.REACHED_CAROUSEL);
                break;

            case REACHED_CAROUSEL:
                spinnerController.spinCarousel(0.75, false, 5);
                while(spinnerController.isRunning()) {
                    //Do Nothing
                }

                setCurrentState(ROBOT_STATES.SPINNED_OFF_CAROUSEL);
                break;

            case SPINNED_OFF_CAROUSEL:
                turnRobot(40, AXIS.Z, DIRECTION.RIGHT_SPIN,1, 200, true);

                setCurrentState(ROBOT_STATES.READY_TO_PARK);
                break;

            case READY_TO_PARK:
                driveRobotRamp(0.2, .6, DIRECTION.FORWARD, 17, 50);

                setCurrentState(ROBOT_STATES.PARKED_IN_BLUE_STORAGE);
                break;

            case PARKED_IN_BLUE_STORAGE:
                break;

            default:
                stopRobot();

        }
    }

}


