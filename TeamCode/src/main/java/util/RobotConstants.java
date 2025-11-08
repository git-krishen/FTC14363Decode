package util;

public class RobotConstants {
    public static class Drivetrain {
        public static String leftFront = "frontLeftMotor";
        public static String leftRear = "backLeftMotor";
        public static String rightFront = "frontRightMotor";
        public static String rightRear = "backRightMotor";
    }

    public static class Intake {
        public static String intake = "intakeMotor";
        public static double intakeVelocity = Math.PI*2;
    }

    public static class Outtake {
        public static String outtake = "outtakeMotor";
        public static String feeder = "feederMotor";
        public static double feederVelocity = Math.PI*2;
        public static double outtakeVelocity = Math.PI*1;
    }
}
