package subsystems;

import com.arcrobotics.ftclib.command.Subsystem;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import pedroPathing.Constants;
import util.HeadingPID;
import util.RobotHardware;

public class MecanumDrive implements Subsystem {
    private RobotHardware robot;
    private double leftFrontPower, leftRearPower, rightFrontPower, rightRearPower, heading;
    private boolean slowmode;

    private Pose pose;
    private HeadingPID pid;

    public MecanumDrive() {
        this.robot = RobotHardware.getInstance();
        this.pose = new Pose();
        pid = new HeadingPID(0.5,0,0.01,10,10);
    }

    public Pose getCurrentPose() {
        return pose;
    }

    public boolean getSlowMode() {
        return slowmode;
    }

    public void setCurrentPose(Pose pose) {
        this.pose = pose;
    }

    public void setSlowMode(boolean set) {
        slowmode = set;
    }

    public Follower driveToPose(Pose target, HardwareMap hardwareMap) {
        Pose a = new Pose();
        Follower follower = robot.follower;
        PathChain path = follower.pathBuilder()
                .addPath(new BezierLine(getCurrentPose(), target))
                .setLinearHeadingInterpolation(getCurrentPose().getHeading(), target.getHeading())
                .build();
        follower.followPath(path);
        return follower;
    }

    public void stopAll() {
        robot.leftFront.setPower(0);
        robot.leftRear.setPower(0);
        robot.rightFront.setPower(0);
        robot.rightRear.setPower(0);
    }

    public void drive(double ly, double lx, double rx) {
//        robot.telemetryManager.addData("Drive", String.format("driving %f %f %f", ly, lx, rx));
//        robot.telemetryManager.addData("Pose", "x: " + robot.follower.getPose().getX() + " | y: " + robot.follower.getPose().getY() + " | heading: " + robot.follower.getHeading());
//        Limelight.setTargetID(20);

        if (ly != 0 && lx != 0 && robot.leftRear.getZeroPowerBehavior().equals(DcMotor.ZeroPowerBehavior.BRAKE)) {
            robot.leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            robot.leftRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            robot.rightRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            robot.rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        } else if (robot.leftFront.getZeroPowerBehavior().equals(DcMotor.ZeroPowerBehavior.FLOAT)) {
            robot.leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            robot.leftRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            robot.rightRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            robot.rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }

        heading = robot.follower.getHeading();
        double rotX = lx * Math.cos(-heading) - ly * Math.sin(-heading);
        double rotY = lx * Math.sin(-heading) + ly * Math.cos(-heading);

        rotX *= 1.1;

//        if (rx == 0 && (Limelight.hasTag(20))) {
//            rx = Math.clamp(pid.calculate(Math.toRadians(robot.odo.getHeading(AngleUnit.RADIANS)), Math.toRadians(robot.odo.getHeading(AngleUnit.RADIANS)-Limelight.getTargetX().orElse(
//                    0
//            ))), -1, 1);
//        }

        double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);
        leftFrontPower = (rotY + rotX + rx) / denominator;
        leftRearPower = (rotY - rotX + rx) / denominator;
        rightFrontPower = (rotY - rotX - rx) / denominator;
        rightRearPower = (rotY + rotX - rx) / denominator;

        double mult = slowmode ? 0.3 : 1;

        robot.leftFront.setPower(leftFrontPower * mult);
        robot.leftRear.setPower(leftRearPower * mult);
        robot.rightFront.setPower(rightFrontPower * mult);
        robot.rightRear.setPower(rightRearPower * mult);
    }
}