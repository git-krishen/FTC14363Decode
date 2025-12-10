package subsystems;

import com.arcrobotics.ftclib.command.Subsystem;
import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes.*;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.stream.Collectors;

import util.RobotHardware;

public class Limelight implements Subsystem {
    private static RobotHardware robot;
    private static OptionalInt targetID;

    static {
        robot = RobotHardware.getInstance();
        targetID = OptionalInt.empty();
    }

    public static void setTargetID(int id) {
        targetID = OptionalInt.of(id);
    }

    public static void setRobotYaw(double angle) {
        robot.limelight.updateRobotOrientation(angle);
    }

    public static boolean hasTarget() {
        return getTagIDList().contains(targetID);
    }

    public static boolean hasTag(int id) {
        return getTagIDList().contains(id);
    }

    public static OptionalDouble getTargetX() {
        Optional<FiducialResult> targetFiducial = getTargetFiducial();
        if (targetFiducial.isPresent()) {
            return OptionalDouble.of(targetFiducial.get().getTargetXDegrees());
        }
        return OptionalDouble.empty();
    }

    public static OptionalDouble getTargetY() {
        Optional<FiducialResult> targetFiducial = getTargetFiducial();
        if (targetFiducial.isPresent()) {
            return OptionalDouble.of(targetFiducial.get().getTargetYDegrees());
        }
        return OptionalDouble.empty();
    }


    public static OptionalDouble getTargetArea() {
        Optional<FiducialResult> targetFiducial = getTargetFiducial();
        if (targetFiducial.isPresent()) {
            return OptionalDouble.of(targetFiducial.get().getTargetArea());
        }
        return OptionalDouble.empty();
    }

    public static ArrayList<Integer> getTagIDList() {
        ArrayList<Integer> tags = new ArrayList<Integer>();
        List<FiducialResult> fiducials = getFiducialList();
        for (FiducialResult fiducial : fiducials) {
            int id = fiducial.getFiducialId();
            tags.add(id);
        }
        return tags;
    }

    private static List<FiducialResult> getFiducialList() {
        LLResult result = robot.limelight.getLatestResult();
        return result.getFiducialResults();
    }

    private static Optional<FiducialResult> getTargetFiducial() {
        if (targetID.isEmpty()) {
            return Optional.empty();
        }
        List<FiducialResult> fiducials = getFiducialList();
        fiducials = fiducials.stream().filter(
                (f) -> {
                    return f.getFiducialId() == targetID.getAsInt();
                }
        ).collect(Collectors.toList());
        return !fiducials.isEmpty() ? Optional.of(fiducials.get(0)) : Optional.empty();
    }

    @Override
    public void periodic() {
//        LLResult result = robot.limelight.getLatestResult();
//        double robotYaw = robot.imu.getRobotYawPitchRollAngles().getYaw();
//        robot.limelight.updateRobotOrientation(robotYaw);
//        if (result != null && result.isValid()) {
//            Pose3D botPose = result.getBotpose_MT2();
//            if (botPose != null) {
//                double x = botPose.getPosition().x;
//                double y = botPose.getPosition().y;
//                double r = botPose.getOrientation().getYaw();
//                Pose fieldPose = new Pose(x,y,r);
//                robot.drivetrain.setCurrentPose(fieldPose);
//            }
//        }
    }
}
