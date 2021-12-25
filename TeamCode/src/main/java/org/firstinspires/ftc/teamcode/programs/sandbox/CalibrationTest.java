package org.firstinspires.ftc.teamcode.programs.sandbox;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.capability.NavigationLayer;
import org.firstinspires.ftc.teamcode.enums.DIRECTION;

@TeleOp(name="Calibration Test", group = "Concept")
@Disabled
public class CalibrationTest extends NavigationLayer {

    @Override
    public void runOpMode() {
        initialize();
       driveRobot(.1, DIRECTION.FORWARD, 10, 30);
        releaseResources();
    }

    @Override
    protected void initialize() {
        super.initialize();
   }

    @Override
    protected void releaseResources() {
        super.releaseResources();
    }


}
