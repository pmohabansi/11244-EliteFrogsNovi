package org.firstinspires.ftc.teamcode.capability;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.controllers.AutoArmController;
import org.firstinspires.ftc.teamcode.controllers.CarouselController;
import org.firstinspires.ftc.teamcode.controllers.ClawController;
import org.firstinspires.ftc.teamcode.controllers.IntakeController;
import org.firstinspires.ftc.teamcode.enums.ROBOT_STATES;

public abstract class BaseAutonomous extends NavigationLayer {

    protected AutoArmController armController;
    protected ClawController clawController;
    protected CarouselController spinnerController;
    protected IntakeController intakeController;

    protected boolean isEndGameOn = false;

    protected ElapsedTime autoElapsedTime = new ElapsedTime();

    protected ROBOT_STATES currentState = ROBOT_STATES.INITIAL_POSITION;

    @Override
    protected void initialize() {
        super.initialize(true);
        armController = new AutoArmController(robot, telemetry, this);
        clawController = new ClawController(robot, telemetry, gamepad2);
        spinnerController = new CarouselController(robot, telemetry, gamepad1);
        intakeController = new IntakeController(robot, telemetry, gamepad2);
    }

    @Override
    protected void releaseResources() {
        super.releaseResources();
    }

    protected void createTimeKeeper() {
        Thread timeKeeper = new Thread() {
            @Override
            public void run() {
                double timeInSeconds = autoElapsedTime.seconds();

                while(timeInSeconds<=20) {
                    if(timeInSeconds==20) {
                        isEndGameOn = true;
                        setCurrentState(ROBOT_STATES.END_GAME_STARTED);
                    }
                }
            }
        };
        timeKeeper.start();
    }

    @Override
    public void runOpMode() {
        initialize();

        telemetry.addData(">", "Press Play to start");
        telemetry.update();

        performPrePlayActivity();

        waitForStart();

        autoElapsedTime.reset();
//        createTimeKeeper();

        while (opModeIsActive()) {
            if (isStopRequested()) break;
            playGame();
            telemetry.update();
        }
        releaseResources();
    }

    protected void performPrePlayActivity() {
        //Do Nothing
    }

    protected abstract void playGame();

    protected void setCurrentState(ROBOT_STATES reachedTeamHub) {
        currentState = reachedTeamHub;
    }

}
