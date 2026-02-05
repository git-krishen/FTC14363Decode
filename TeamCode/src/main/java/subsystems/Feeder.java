package subsystems;

import com.arcrobotics.ftclib.command.Subsystem;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

import util.RobotHardware;

public class Feeder implements Subsystem {
    RobotHardware robot;

    public Feeder() {
        robot = RobotHardware.getInstance();
    }

    public double getFeederVelocity() {
        return robot.feederMotor.getVelocity(AngleUnit.RADIANS);
    }

    public void setFeederVelocity(double speed) {
        if (!robot.outtakeMotor.isOverCurrent()) {
            robot.feederMotor.setVelocity(speed, AngleUnit.RADIANS);
        }
    }

    public double getFeederPower() {
        return robot.feederMotor.getPower();
    }

    public void setFeederPower(double power) {
        robot.feederMotor.setPower(power);
    }

    public void stopMotor() {
        robot.feederMotor.setPower(0);
    }
}
