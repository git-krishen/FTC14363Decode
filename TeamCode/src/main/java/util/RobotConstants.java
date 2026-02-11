package util;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.geometry.Pose;

public class RobotConstants {
    public static class Drivetrain {
        public static String leftFront = "frontLeftMotor"; // 1
        public static String leftRear = "backLeftMotor"; // 2
        public static String rightFront = "frontRightMotor"; // 0
        public static String rightRear = "backRightMotor"; // 3
        public static Pose autonEndPose = new Pose(124,48,Math.toRadians(0));
    }

    public static class Intake {
        public static String intake = "intakeMotor"; // 0e
        public static double intakeVelocity = Math.PI*2;
    }

    @Configurable
    public static class Outtake {
        public static String outtake = "outtakeMotor"; // 2e
        public static String outtakeFollower = "outtakeFollowMotor"; // 3e
        public static String feeder = "feederMotor"; // 1e
        public static String distanceSensorFeed = "distanceSensorFeed"; // 3 i2c
        public static String distanceSensorOut = "distanceSensorOut"; // 2 i2c
        public static double feederSensorThreshold = 100; //mm
        public static double outtakeSensorThreshold = 100;
        public static double feederVelocity = Math.PI*2;
        //The only one that actually does anything (degree ticks per second)
        public static double outtakeVelocityShort = 925; // 2325; // Math.PI*0.85; // 0.0375 // (500/60.0)*28;
//        public static double outtakeVelocityShort = Math.PI*0.85;;
        public static double outtakeVelocityLong = 1075; // 2600;// Math.PI*1.18; // 0.0435
//        public static double outtakeVelocityLong = Math.PI*1.18;;
        public static double shotSpeedSlope = 5.40541;
        public static double shotSpeedIntercept = 675.67568;
        public static double shotSpeedMin = 0;
        public static double shotSpeedMax = 1500;
        public static double kP = 0.005; // 0.007;
        public static double kI = 0.01; // 0.000001;
        public static double kD = 0.00005; // 0
        public static double kS = 0.07; // 0.07;
        public static double kV = 0.00067; // 0.00037; // 1/4760
        public static double kA = 0;
        public static double maxI = 0.3;
        public static double maxAccel = 30000; // 15000; // ticks/s^2
    }

    @Configurable
    public static class Turret {
        // Analog 0, Servo 0
//        public static double maxServoPos = Math.PI;
//        public static double minServoPos = 0;
        public static double encoderOffset = 0;
        public static double turretOffsetX = 2.48;
        public static double turretOffsetY = -1.57;
        public static double scoreRedX = 139.0;
        public static double scoreRedY = 141.0;
        public static double scoreBlueX = 0;
        public static double scoreBlueY = 141.0;
        public static double gearRatio = 29.0/120.0;
        public static double maxAngle = 90;
        public static double minAngle = -225;
        public static double turretSpeed = 15;
        public static double maxPower = 0.5;
        public static double kP = 0.003; // 0.002; // 0.006 // 0.015;
        public static double kI = 0; // 0.01967213;
        public static double kD = 0; // 0.0004575;
        public static double kS = 0.02;
        public static double maxI = 1;
    }

    public static class Limelight {
        public static double axisForward = -1.57; // -40 mm down
        public static double axisRight = 2.48; // 63 mm right
        public static double axisUp = 16.54; // 420 mm up (vertical)
        public static double rotRadius = 0.81; // 20.5 mm radius
    }
}
