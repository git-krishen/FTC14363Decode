package subsystems;

import com.arcrobotics.ftclib.command.Subsystem;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

import util.RobotConstants;
import util.RobotHardware;

public class Outtake implements Subsystem {
    RobotHardware robot;
    double targetVelocity;
    ElapsedTime loopTimer;
    double lastVelocity;
    double integralSum;
    double filteredAcceleration;
    double alpha;
    double lastVelocityError;

    public Outtake() {
        robot = RobotHardware.getInstance();
        targetVelocity = 0;
        lastVelocity = 0;
        integralSum = 0;
        lastVelocityError = 0;
        filteredAcceleration = 0;
        alpha = 1;
        loopTimer = new ElapsedTime();
    }

    public double getOuttakeVelocity() {
        return robot.outtakeMotor.getVelocity(AngleUnit.RADIANS);
    }

    public void setOuttakeVelocity(double speed) {
        targetVelocity = speed;
    }

    public double getOuttakePower() {
        return robot.outtakeMotor.getPower();
    }

    public void setOuttakePower(double power) {
        if (!robot.outtakeMotor.isOverCurrent()) {
            robot.outtakeMotor.setPower(power);
            robot.outtakeFollower.setPower(power);
        }
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

    public void stopOuttakeMotor() {
        robot.outtakeMotor.setVelocity(0);
        robot.outtakeFollower.setVelocity(0);
    }

    public void stopFeederMotor() {
        robot.feederMotor.setVelocity(0);
    }

    public void stopMotors() {
        robot.outtakeMotor.setVelocity(0);
        robot.outtakeFollower.setVelocity(0);
        robot.feederMotor.setVelocity(0);
    }

    @Override
    public void periodic() {
        // Get loop time for derivative and integral calculations
        double dt = loopTimer.seconds();
        loopTimer.reset();
        if (dt <= 0) dt = 0.02;

        // Get current velocity (average of both motors for accuracy)
        double currentVelocity = (robot.outtakeMotor.getVelocity() + robot.outtakeFollower.getVelocity()) / 2.0;

        // Calculate velocity error
        double velocityError = targetVelocity - currentVelocity;

        // Measure actual acceleration from velocity change
        double measuredAcceleration = (currentVelocity - lastVelocity) / dt;
        lastVelocity = currentVelocity;

        // Apply exponential moving average filter to offset sensor noise
        filteredAcceleration = (alpha * measuredAcceleration) + (1.0 - alpha) * filteredAcceleration;

        // Calculate desired acceleration (aggressive: close the gap as fast as possible)
        // Clamp to physical limits
        double desiredAcceleration = velocityError / dt;
        desiredAcceleration = Math.clamp(desiredAcceleration,
                -RobotConstants.Outtake.maxAccel,
                RobotConstants.Outtake.maxAccel);

        // ==================== FEEDFORWARD ====================
        // Full equation: kS * sign(v) + kV * targetVel + kA * acceleration
        double ffOutput = RobotConstants.Outtake.kS * Math.signum(targetVelocity)
                + RobotConstants.Outtake.kV * targetVelocity
                + RobotConstants.Outtake.kA * desiredAcceleration;

        // ==================== PID ====================
        // Proportional
        double pOutput = RobotConstants.Outtake.kP * velocityError;

        // Integral with anti-windup
        integralSum += velocityError * dt;
        integralSum = Math.clamp(integralSum, -RobotConstants.Outtake.maxI, RobotConstants.Outtake.maxI);

        // Zero-crossing reset prevents overshoot
        if (lastVelocityError != 0 && Math.signum(velocityError) != Math.signum(lastVelocityError)) {
            integralSum = 0;
        }
        double iOutput = RobotConstants.Outtake.kI * integralSum;

        // Derivative (on measurement to avoid setpoint kick)
        double dOutput = RobotConstants.Outtake.kD * -filteredAcceleration;

        lastVelocityError = velocityError;

        // Combine PID terms
        double pidOutput = pOutput + iOutput + dOutput;

        // ==================== TOTAL OUTPUT ====================
        double totalPower = ffOutput + pidOutput;
        totalPower = Math.clamp(totalPower, 0, 1.0);  // Motor power range (flywheel only spins one direction)

        // Apply power to both motors
        robot.outtakeMotor.setPower(totalPower);
        robot.outtakeFollower.setPower(totalPower);
    }
}
