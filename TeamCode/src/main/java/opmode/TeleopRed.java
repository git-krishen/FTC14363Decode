package opmode;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import subsystems.Limelight;
import util.RobotConstants;
import util.RobotHardware;

@TeleOp(name = "TeleopRed")
public class TeleopRed extends TeleopBase {
    @Override
    public void initialize() {
        super.initialize();
        RobotHardware.getInstance().limelight.setTargetID(24);
        setScorePose(new Pose(RobotConstants.Turret.scoreRedX, RobotConstants.Turret.scoreRedY));
    }
}
