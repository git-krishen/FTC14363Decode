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
        pid = new HeadingPID(1, 0, 0);
        prevDeg = getPositionDegrees();
        angle = getPositionDegrees();
    }

    public double getPositionUnitCircle() {
        return Math.toRadians(getPositionDegrees());
    }

    public double getPositionDegrees() {
        return getPositionProportion() * 360;
    }

    public double getPositionProportion() {
        return (robot.turretEncoder.getVoltage() / 3.3); //robot.turretServo.getCurrentPosition();
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
}