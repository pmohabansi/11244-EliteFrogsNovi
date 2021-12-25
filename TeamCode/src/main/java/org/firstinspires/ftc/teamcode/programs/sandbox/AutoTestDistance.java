package org.firstinspires.ftc.teamcode.programs.sandbox;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.capability.BaseAutonomous;
import org.firstinspires.ftc.teamcode.enums.DIRECTION;
import org.firstinspires.ftc.teamcode.enums.ROBOT_STATES;

@Autonomous(name="Test Distance", group = "Concept")
@Disabled
public class AutoTestDistance extends BaseAutonomous {

    protected void playGame() {
        telemetry.addData("Status : " , currentState.label);
        telemetry.update();
        switch (currentState) {
            case INITIAL_POSITION:
//                driveRobotRamp(0.1, .5, DIRECTION.FORWARD, 50, 200);
                driveRobot(.5, DIRECTION.FORWARD, 50, 200);

                setCurrentState(ROBOT_STATES.PARKED_IN_BLUE_STORAGE);
                break;

            default:
                stopRobot();

        }
    }



}


