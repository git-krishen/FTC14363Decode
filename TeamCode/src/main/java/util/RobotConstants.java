package util;

public class RobotConstants {
    public static class Drivetrain {
        public static String leftFront = "frontLeftMotor"; // 1
        public static String leftRear = "backLeftMotor"; // 2
        public static String rightFront = "frontRightMotor"; // 0
        public static String rightRear = "backRightMotor"; // 3
    }

    public static class Intake {
        public static String intake = "intakeMotor"; // 0e
        public static double intakeVelocity = Math.PI*2;
    }

    public static class Outtake {
        public static String outtake = "outtakeMotor"; // 2e
        public static String outtakeFollower = "outtakeFollowMotor"; // 3e
        public static String feeder = "feederMotor"; // 1e
        public static double feederVelocity = Math.PI*2;
        //The only one that actually does anything (degree ticks per second)
        public static double outtakeVelocityShort = Math.PI*0.65; // 0.0375 // (500/60.0)*28;
        public static double outtakeVelocityLong = Math.PI*0.75; // 0.0435
    }

    public static class Turret {
//        public static double maxServoPos = Math.PI;
//        public static double minServoPos = 0;
        public static double encoderOffset = 0;
        public static double turretOffsetX = 2.48;
        public static double turretOffsetY = -1.57;
        public static double scoreRedX = 139.0;
        public static double scoreRedY = 141.0;
        public static double scoreBlueX = 0;
        public static double scoreBlueY = 141.0;
    }
}
