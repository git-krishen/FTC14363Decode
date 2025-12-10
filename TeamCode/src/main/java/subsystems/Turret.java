//package subsystems;
//
//import util.RobotConstants;
//import util.RobotHardware;
//
//public class Turret {
//    private RobotHardware robot;
//
//    public Turret() {
//        robot = RobotHardware.getInstance();
//    }
//
//    public double getPositionUnitCircle() {
//        double proportion = robot.turretServo.getPosition();
//        return ((RobotConstants.Turret.maxServoPos-RobotConstants.Turret.minServoPos) * proportion) + RobotConstants.Turret.minServoPos;
//    }
//
//    public double getPositionDegrees() {
//        return Math.toDegrees(getPositionUnitCircle());
//    }
//
//    public void setPositionUnitCircle(double radians) {
//        double clamped = Math.clamp(radians, RobotConstants.Turret.minServoPos, RobotConstants.Turret.maxServoPos);
//        double proportion = (clamped-RobotConstants.Turret.minServoPos)/(RobotConstants.Turret.maxServoPos-RobotConstants.Turret.minServoPos);
//        setPosition(proportion);
//    }
//
//    // This operates by unit circle rules, so 90 is forward
//    public void setPositionDegrees(double degrees) {
//        setPositionUnitCircle(Math.toRadians(degrees));
//    }
//
//    public void setPosition(double proportion) {
//        robot.turretServo.setPosition(proportion);
//    }
//
//    public void reset() {
//        setPositionUnitCircle(Math.PI/2);
//    }
//
//    public void lockToAprilTag() {
//        setPositionDegrees(getPositionDegrees() + Limelight.getTargetX().orElse(0));
//    }
//}
