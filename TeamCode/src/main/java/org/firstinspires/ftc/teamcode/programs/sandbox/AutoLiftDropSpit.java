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
 */
@Autonomous(name="AutoLiftDropSpit", group = "Concept")
@Disabled
public class AutoLiftDropSpit extends BaseAutonomous {

    protected void playGame() {
        telemetry.addData("Status : " , currentState.label);
        telemetry.update();
        switch (currentState) {
            case INITIAL_POSITION:
                armController.moveToHubPosFromRest(ARM_POSITION.TIER_THREE);
                sleep(1000);

                intakeController.pushObjectOut();
                sleep(3000);
                intakeController.stop();

                armController.moveToRestPosition();
                sleep(1000);

//                armController.moveToHubPosFromRest(ARM_POSITION.TIER_TWO);
//                sleep(1000);
//                armController.moveToRestPosition();
//                sleep(1000);
//
//                armController.moveToHubPosFromRest(ARM_POSITION.TIER_ONE);
//                sleep(1000);
//                armController.moveToRestPosition();
//                sleep(1000);

//                armController.moveToGround();
//                sleep(5000);

//                armController.moveToHubPosFromGround(ARM_POSITION.TIER_ONE);
//                sleep(2000);
//
//                armController.moveToRestPosition();
//                sleep(2000);

                setCurrentState(ROBOT_STATES.REACHED_CAROUSEL);
                break;

            case REACHED_CAROUSEL:
                break;

            default:
                stopRobot();

        }
    }

    protected void playGame3() {
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