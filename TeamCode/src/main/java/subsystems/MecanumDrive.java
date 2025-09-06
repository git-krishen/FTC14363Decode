package subsystems;

import com.arcrobotics.ftclib.command.Subsystem;
import com.arcrobotics.ftclib.geometry.Pose2d;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import util.RobotHardware;

public class MecanumDrive implements Subsystem {
    private RobotHardware robot;
    private double leftFrontPower, leftRearPower, rightFrontPower, rightRearPower, heading;
    private boolean slowmode;

    private Pose2d pose;

    public MecanumDrive() {
        this.robot = RobotHardware.getInstance();
        this.pose = new Pose2d();
    }

    public Pose2d getCurrentPose() {
        return pose;
    }

    public void setCurrentPose(Pose2d pose) {
        this.pose = pose;
    }

    public void setSlowMode(boolean set) {
        slowmode = set;
    }

    public void drive(double ly, double lx, double rx) {
        System.out.println("driving");

        lx *= 1.1;
        rx *= 1.1;

        heading = robot.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
        double rotX = lx * Math.cos(-heading) - ly * Math.sin(-heading);
        double rotY = lx * Math.sin(-heading) + ly * Math.cos(-heading);

        leftFrontPower = (rotY + rotX + rx);
        leftRearPower = (rotY - rotX + rx);
        rightFrontPower = (rotY - rotX - rx);
        rightRearPower = (rotY + rotX - rx);

        double mult = slowmode ? 0.3 : 1;

        robot.leftFront.setPower(leftFrontPower * mult);
        robot.leftRear.setPower(leftRearPower * mult);
        robot.rightFront.setPower(rightFrontPower * mult);
        robot.rightRear.setPower(rightRearPower * mult);
    }
}