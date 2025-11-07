package subsystems;

import com.arcrobotics.ftclib.command.Subsystem;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

import util.RobotHardware;

public class Outtake implements Subsystem {
    RobotHardware robot;

    public Outtake() {
        robot = RobotHardware.getInstance();
    }

    public double getOuttakeVelocity() {
        return robot.outtakeMotor.getVelocity(AngleUnit.RADIANS);
    }

    public void setOuttakeVelocity(double speed) {
        robot.outtakeMotor.setVelocity(speed, AngleUnit.RADIANS);
    }

    public double getOuttakePower() {
        return robot.outtakeMotor.getPower();
    }

    public void setOuttakePower(double power) {
        robot.outtakeMotor.setPower(power);
    }

    public double getFeederVelocity() {
        return robot.feederMotor.getVelocity(AngleUnit.RADIANS);
    }

    public void setFeederVelocity(double speed) {
        robot.feederMotor.setVelocity(speed, AngleUnit.RADIANS);
    }

    public double getFeederPower() {
        return robot.feederMotor.getPower();
    }

    public void setFeederPower(double power) {
        robot.feederMotor.setPower(power);
    }

    public void stopOuttakeMotor() {
        robot.outtakeMotor.setVelocity(0);
    }

    public void stopFeederMotor() {
        robot.feederMotor.setVelocity(0);
    }

    public void stopMotors() {
        robot.outtakeMotor.setVelocity(0);
        robot.feederMotor.setVelocity(0);
    }
}
