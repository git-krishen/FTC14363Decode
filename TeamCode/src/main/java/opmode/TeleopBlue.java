package opmode;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import subsystems.Limelight;
import util.RobotConstants;
import util.RobotHardware;

@TeleOp(name = "TeleopBlue")
public class TeleopBlue extends TeleopBase {
    @Override
    public void initialize() {
        super.initialize();
        RobotHardware.getInstance().limelight.setTargetID(20);
        setScorePose(new Pose(RobotConstants.Turret.scoreBlueX, RobotConstants.Turret.scoreBlueY));
    }
}
