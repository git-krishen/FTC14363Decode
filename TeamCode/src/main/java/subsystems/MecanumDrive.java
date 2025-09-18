package subsystems;

import com.arcrobotics.ftclib.command.Subsystem;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

import java.util.function.DoubleSupplier;

import pedroPathing.Constants;
import util.RobotHardware;

public class MecanumDrive implements Subsystem {
    private RobotHardware robot;
    private double leftFrontPower, leftRearPower, rightFrontPower, rightRearPower, heading;
    private boolean slowmode;

    private Pose pose;

    public MecanumDrive() {
        this.robot = RobotHardware.getInstance();
        this.pose = new Pose();
    }

    public Pose getCurrentPose() {
        return pose;
    }

    public void setCurrentPose(Pose pose) {
        this.pose = pose;
    }

    public void setSlowMode(boolean set) {
        slowmode = set;
    }

    public DoubleSupplier driveToPose(Pose target, HardwareMap hardwareMap) {
        Pose a = new Pose();
        Follower follower = Constants.createFollower(hardwareMap);
        PathChain path = follower.pathBuilder()
                .addPath(new BezierLine(getCurrentPose(), target))
                .setLinearHeadingInterpolation(getCurrentPose().getHeading(), target.getHeading())
                .build();
        follower.followPath(path);
        return follower::getPathCompletion;
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