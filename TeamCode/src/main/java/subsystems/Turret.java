package subsystems;

import com.arcrobotics.ftclib.command.Subsystem;

import util.HeadingPID;
import util.RobotConstants;
import util.RobotHardware;

public class Turret implements Subsystem {
    private RobotHardware robot;
    private HeadingPID pid;

    public Turret() {
        robot = RobotHardware.getInstance();
        pid = new HeadingPID(0.1,0,0);
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
        robot.turretServo.setPower(pid.calculate(getPositionUnitCircle(), Math.clamp(radians, -Math.PI/2, Math.PI/2)));
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

    public void lockToAprilTag() {
        double x = robot.follower.getPose().getX();
        double y = robot.follower.getPose().getY();
        double heading = robot.follower.getPose().getHeading() + getPositionUnitCircle();
        x += RobotConstants.Turret.turretOffsetX;
        y += RobotConstants.Turret.turretOffsetY;
        double distY = RobotConstants.Turret.scoreRedY - y;
        double distX = RobotConstants.Turret.scoreRedX - x;
        double targetHeading = (Math.PI/2)-Math.atan2(distY, distX);
        setTargetPositionUnitCircle(targetHeading);
    }
}
