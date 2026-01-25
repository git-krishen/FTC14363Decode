package subsystems;

import com.arcrobotics.ftclib.command.Subsystem;

import util.HeadingPID;
import util.RobotConstants;
import util.RobotHardware;

public class Turret implements Subsystem {
    private RobotHardware robot;
    private HeadingPID pid;
    private double prevDeg;
    private double angle;

    public Turret() {
        robot = RobotHardware.getInstance();
        pid = new HeadingPID(1,0,0);
        prevDeg = getPositionDegrees();
        angle = getPositionDegrees();
    }

    public double getPositionUnitCircle() {
        return Math.toRadians(getPositionDegrees());
    }

    public double getPositionDegrees() {
        return getPositionProportion()*360;
    }

    public double getPositionProportion() {
        return (robot.turretEncoder.getVoltage()/3.3); //robot.turretServo.getCurrentPosition();
    }

    public void setTargetPositionUnitCircle(double radians) {
        robot.turretServo.setPower(pid.calculate(getPositionUnitCircle(), radians));
    }

    // This operates by unit circle rules, so 90 is forward
    public void setTargetPositionDegrees(double degrees) {
        setTargetPositionUnitCircle(Math.toRadians(degrees));
    }

//    public void setPositionProportion(double amount) {
//    }

    public void setPower(double power) {
        robot.turretServo.setPower(power);
    }

    public void stopTurret() {
        robot.turretServo.setPower(0);
    }

    // TODO: scale speed drop off by square of velocity (kinetic energy)
    public void lockToAprilTag() {
        pid.setGoal(Math.toRadians(Limelight.getTargetX().orElse(0)));
//        double x = robot.follower.getPose().getX();
//        double y = robot.follower.getPose().getY();
//        double botHeading = robot.follower.getPose().getHeading();
////        x += RobotConstants.Turret.turretOffsetX;
////        y += RobotConstants.Turret.turretOffsetY;
////        double distY = RobotConstants.Turret.scoreRedY - y;
////        double distX = RobotConstants.Turret.scoreRedX - x;
////        double targetHeading = Math.atan2(distY, distX)-botHeading;
////        setTargetPositionUnitCircle(targetHeading);
//
//        x += RobotConstants.Turret.turretOffsetX*Math.sin(botHeading) + RobotConstants.Turret.turretOffsetY*Math.cos(botHeading);
//        y += -RobotConstants.Turret.turretOffsetY*Math.cos(botHeading) + RobotConstants.Turret.turretOffsetX*Math.sin(botHeading);
//        double reqAngle = Math.atan2(141-y,139-x);
//        double delta = reqAngle - (botHeading + getPositionUnitCircle());
//        setTargetPositionUnitCircle(Math.atan2(Math.sin(delta),Math.cos(delta)));
    }

    @Override
    public void periodic() {
//        if (Limelight.hasTarget()) {
//            setPower(pid.calculate(0));
//        } else {
//            angle = getPositionDegrees();
//            double delta = angle - prevDeg;
//            Limelight.updateLimelightPose(
//                    RobotConstants.Limelight.axisForward+RobotConstants.Limelight.rotRadius,
//                    RobotConstants.Limelight.axisRight,
//                    RobotConstants.Limelight.axisUp,
//                    180,
//                    18,
//                    0
//            );
//        }
    }
}
