package subsystems;

import com.arcrobotics.ftclib.command.Subsystem;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

import util.RobotHardware;

public class Intake implements Subsystem {
    RobotHardware robot;

    public Intake() {
        robot = RobotHardware.getInstance();
    }

    public double getIntakeMotorVelocity() {
        return robot.intakeMotor.getVelocity(AngleUnit.RADIANS);
    }

    public void setIntakeMotorVelocity(double speed) {
        robot.intakeMotor.setVelocity(speed, AngleUnit.RADIANS);
    }

    public double getIntakePower() {
        return robot.intakeMotor.getPower();
    }

    public void setIntakePower(double power) {
        robot.intakeMotor.setPower(power);
    }

    public void stopMotor() {
        robot.intakeMotor.setPower(0);
    }
}
