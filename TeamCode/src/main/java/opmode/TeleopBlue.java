package opmode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import subsystems.Limelight;
import util.RobotHardware;

@TeleOp(name = "TeleopBlue")
public class TeleopBlue extends TeleopBase {
    @Override
    public void initialize() {
        super.initialize();
        RobotHardware.getInstance().limelight.setTargetID(20);
    }
}
