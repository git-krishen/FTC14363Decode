package subsystems;

import com.arcrobotics.ftclib.command.Subsystem;
import com.arcrobotics.ftclib.geometry.Pose2d;
import com.arcrobotics.ftclib.geometry.Rotation2d;
import com.arcrobotics.ftclib.geometry.Translation2d;
import com.qualcomm.hardware.limelightvision.LLResult;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

import util.RobotHardware;

public class Limelight implements Subsystem {
    private RobotHardware robot;

    public Limelight() {
        this.robot = RobotHardware.getInstance();
    }

    @Override
    public void periodic() {
        LLResult result = robot.limelight.getLatestResult();
        double robotYaw = robot.imu.getRobotYawPitchRollAngles().getYaw();
        robot.limelight.updateRobotOrientation(robotYaw);
        if (result != null && result.isValid()) {
            Pose3D botPose = result.getBotpose_MT2();
            if (botPose != null) {
                double x = botPose.getPosition().x;
                double y = botPose.getPosition().y;
                Translation2d fieldTranslation = new Translation2d(x,y);
                double r = botPose.getOrientation().getYaw();
                Rotation2d fieldRotation = new Rotation2d(r);
                Pose2d fieldPose = new Pose2d(fieldTranslation, fieldRotation);
                robot.drivetrain.setCurrentPose(fieldPose);
            }
        }
    }
}
