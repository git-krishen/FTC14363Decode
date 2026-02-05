package subsystems;

import com.arcrobotics.ftclib.command.Subsystem;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.util.ElapsedTime;

import util.RobotConstants;
import util.RobotHardware;

public class Turret implements Subsystem {
    private RobotHardware robot;
    // Encoder for servo position feedback
    private final AnalogInput servoEncoder;
    // Continuous rotation servo
    private final CRServo servo;
    // Run-to-position mode flag
    private boolean rtp;
    // Current power applied to servo
    private double power;
    // Maximum allowed power
    private double maxPower;
    // Direction of servo movement
    private Direction direction;
    // Last measured angle
    private double previousAngle;
    // Accumulated rotation in degrees
    private double totalRotation;
    // Target rotation in degrees
    private double targetRotation;

    // PID controller coefficients and state
    private double kP;
    private double kI;
    private double kD;
    private double integralSum;
    private double lastError;
    private double maxIntegralSum;
    private ElapsedTime pidTimer;

    // Initialization and debug fields
    public double STARTPOS;
    public int ntry = 0;
    public int cliffs = 0;
    public double homeAngle;
    private boolean reset = false;

    // Direction enum for servo
    public enum Direction {
        FORWARD,
        REVERSE
    }

    // region constructors

    // Basic constructor, defaults to FORWARD direction
    public Turret() {
        robot = RobotHardware.getInstance();
        rtp = true;
        this.servo = robot.turretServo;
        servoEncoder = robot.turretEncoder;
        direction = Direction.FORWARD;
        initialize();
    }

    // Constructor with explicit direction
    public Turret(Direction direction) {
        robot = RobotHardware.getInstance();
        rtp = true;
        this.servo = robot.turretServo;
        servoEncoder = robot.turretEncoder;
        this.direction = direction;
        initialize();
    }

    // Initialization logic for servo and encoder
    private void initialize() {
        servo.setPower(0);

        // Try to get a valid starting position

        // Default PID coefficients
        kP = RobotConstants.Turret.kP;
        kI = RobotConstants.Turret.kI;
        kD = RobotConstants.Turret.kD;
        integralSum = 0.0;
        lastError = 0.0;
        maxIntegralSum = RobotConstants.Turret.maxI;
        pidTimer = new ElapsedTime();
        pidTimer.reset();

        maxPower = 0.5;
        cliffs = 0;

        forceResetTotalRotation();
    }
    // endregion

    // Set servo direction
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    // Set power to servo, respecting direction and maxPower
    public void setPower(double power) {
        this.power = Math.max(-maxPower, Math.min(maxPower, power));
        servo.setPower(this.power * (direction == Direction.REVERSE ? -1 : 1));
    }

    // Get current power
    public double getPower() {
        return power;
    }

    // Set maximum allowed power
    public void setMaxPower(double maxPower) {
        this.maxPower = maxPower;
    }

    // Get maximum allowed power
    public double getMaxPower() {
        return maxPower;
    }

    // Enable or disable run-to-position mode
    public void setRtp(boolean rtp) {
        this.rtp = rtp;
        if (rtp) {
            resetPID();
        }
    }

    // Get run-to-position mode state
    public boolean getRtp() {
        return rtp;
    }

    // Set PID P coefficient
    public void setKP(double kP) {
        this.kP = kP;
    }

    // Set PID I coefficient and reset integral
    public void setKI(double kI) {
        this.kI = kI;
        resetIntegral();
    }

    // Set PID D coefficient
    public void setKD(double kD) {
        this.kD = kD;
    }

    // Set all PID coefficients
    public void setPidCoeffs(double kP, double kI, double kD){
        setKP(kP);
        setKI(kI);
        setKD(kD);
    }

    // Get PID P coefficient
    public double getKP() {
        return kP;
    }

    // Get PID I coefficient
    public double getKI() {
        return kI;
    }

    // Get PID D coefficient
    public double getKD() {
        return kD;
    }

    // Set only P coefficient (alias)
    public void setK(double k) {
        setKP(k);
    }

    // Get only P coefficient (alias)
    public double getK() {
        return getKP();
    }

    // Set maximum allowed integral sum
    public void setMaxIntegralSum(double maxIntegralSum) {
        this.maxIntegralSum = maxIntegralSum;
    }

    // Get maximum allowed integral sum
    public double getMaxIntegralSum() {
        return maxIntegralSum;
    }

    // Get total rotation since initialization
    public double getTotalRotation() {
        return totalRotation;
    }

    public double getTotalRotationTurret() {
        return totalRotation*RobotConstants.Turret.gearRatio;
    }

    // Get current target rotation
    public double getTargetRotation() {
        return targetRotation;
    }

    // Increment target rotation by a value
    public void changeTargetRotation(double change) {
        if (((getTotalRotationTurret() < RobotConstants.Turret.maxAngle || targetRotation < RobotConstants.Turret.maxAngle) || change < 0) && ((getTotalRotationTurret() > RobotConstants.Turret.minAngle || targetRotation > RobotConstants.Turret.minAngle) || change > 0)) targetRotation += change;
    }

    // Set target rotation and reset PID
    public void setTargetRotation(double target) {
        if (target < RobotConstants.Turret.maxAngle && target > RobotConstants.Turret.minAngle) {
            targetRotation = target;
            resetPID();
        }
    }

    public void setTargetRotationTurret(double target) {
        if (target < RobotConstants.Turret.maxAngle && target > RobotConstants.Turret.minAngle) {
            targetRotation = target/RobotConstants.Turret.gearRatio;
            resetPID();
        }
    }



    // Get current angle from encoder (in degrees)
    public double getCurrentAngle() {
        if (servoEncoder == null) return 0;
        return (servoEncoder.getVoltage() / 3.3) * 360;
    }

    // Check if servo is at target (default tolerance)
    public boolean isAtTarget() {
        return isAtTarget(5);
    }

    // Check if servo is at target (custom tolerance)
    public boolean isAtTarget(double tolerance) {
        return Math.abs(targetRotation - totalRotation) < tolerance;
    }

    // Force reset total rotation and PID state
    public void forceResetTotalRotation() {
        totalRotation = 0;
        previousAngle = getCurrentAngle();
        homeAngle = previousAngle;
        resetPID();
    }

    // Reset PID controller state
    public void resetPID() {
        resetIntegral();
        lastError = 0;
        pidTimer.reset();
    }

    // Reset integral sum
    public void resetIntegral() {
        integralSum = 0;
    }

    @Override
    public void periodic() {
        if (!reset) {
            forceResetTotalRotation();
            reset = true;
        }
        setMaxPower(RobotConstants.Turret.maxPower);

        double currentAngle = getCurrentAngle();
        double angleDifference = currentAngle - previousAngle;

        // Handle wraparound at 0/360 degrees
        if (angleDifference > 180) {
            angleDifference -= 360;
            cliffs--;
        } else if (angleDifference < -180) {
            angleDifference += 360;
            cliffs++;
        }

        // Update total rotation with wraparound correction
        totalRotation = (currentAngle - homeAngle + cliffs * 360);
        previousAngle = currentAngle;

        if (!rtp) return;

        double dt = pidTimer.seconds();
        pidTimer.reset();

        double error = targetRotation - totalRotation;

        // PID integral calculation with clamping
        integralSum += error * dt;
        integralSum = Math.clamp(integralSum, -maxIntegralSum, maxIntegralSum);

        // Integral wind-down in deadzone
        final double INTEGRAL_DEADZONE = 2.0;
        if (Math.abs(error) < INTEGRAL_DEADZONE) {
            integralSum *= 0.95;
        }

        // PID derivative calculation
        double derivative = (error - lastError) / dt;
        lastError = error;

        // PID output calculation
        kP = RobotConstants.Turret.kP;
        kI = RobotConstants.Turret.kI;
        kD = RobotConstants.Turret.kD;
        double pTerm = kP * error;
        double iTerm = kI * integralSum;
        double dTerm = kD * derivative;

        double output = pTerm + iTerm + dTerm;

        // Deadzone for output
        final double DEADZONE = 0.5;
        if (Math.abs(error) > DEADZONE) {
            power = Math.min(maxPower, Math.abs(output)+RobotConstants.Turret.kS) * Math.signum(output);
            setPower(power);
        } else {
            setPower(0);
        }
    }

    // Log current state for telemetry/debug
    public String getDebugInfo() {
        return String.format(
                "Current Volts: %.3f\n" +
                        "Current Angle: %.2f\n" +
                        "Total Rotation: %.2f\n" +
                        "Tot Rot Turret: %.2f\n" +
                        "Target Rotation: %.2f\n" +
                        "Current Power: %.3f\n" +
                        "PID Values: P=%.3f I=%.3f D=%.3f\n" +
                        "PID Terms: Error=%.2f Integral=%.2f",
                servoEncoder.getVoltage(),
                getCurrentAngle(),
                totalRotation,
                getTotalRotationTurret(),
                targetRotation,
                power,
                kP, kI, kD,
                targetRotation - totalRotation,
                integralSum
        );
    }

    public void lockToAprilTag() {
        if (robot.limelight.hasTarget()) {
            setTargetRotationTurret(getTotalRotationTurret()-(robot.limelight.getTargetX().orElse(0)));
        }
    }

//
//    // TODO: scale speed drop off by square of velocity (kinetic energy)
//    public void lockToAprilTag() {
//        pid.setGoal(Math.toRadians(Limelight.getTargetX().orElse(0)));
////        double x = robot.follower.getPose().getX();
////        double y = robot.follower.getPose().getY();
////        double botHeading = robot.follower.getPose().getHeading();
//////        x += RobotConstants.Turret.turretOffsetX;
//////        y += RobotConstants.Turret.turretOffsetY;
//////        double distY = RobotConstants.Turret.scoreRedY - y;
//////        double distX = RobotConstants.Turret.scoreRedX - x;
//////        double targetHeading = Math.atan2(distY, distX)-botHeading;
//////        setTargetPositionUnitCircle(targetHeading);
////
////        x += RobotConstants.Turret.turretOffsetX*Math.sin(botHeading) + RobotConstants.Turret.turretOffsetY*Math.cos(botHeading);
////        y += -RobotConstants.Turret.turretOffsetY*Math.cos(botHeading) + RobotConstants.Turret.turretOffsetX*Math.sin(botHeading);
////        double reqAngle = Math.atan2(141-y,139-x);
////        double delta = reqAngle - (botHeading + getPositionUnitCircle());
////        setTargetPositionUnitCircle(Math.atan2(Math.sin(delta),Math.cos(delta)));
//    }
//
//    @Override
//    public void periodic() {
////        if (Limelight.hasTarget()) {
////            setPower(pid.calculate(0));
////        } else {
////            angle = getPositionDegrees();
////            double delta = angle - prevDeg;
////            Limelight.updateLimelightPose(
////                    RobotConstants.Limelight.axisForward+RobotConstants.Limelight.rotRadius,
////                    RobotConstants.Limelight.axisRight,
////                    RobotConstants.Limelight.axisUp,
////                    180,
////                    18,
////                    0
////            );
////        }
//    }
}