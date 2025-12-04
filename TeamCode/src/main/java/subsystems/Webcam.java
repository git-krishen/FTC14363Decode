//package subsystems;
//
//import android.util.Size;
//
//import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
//import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
//import org.firstinspires.ftc.vision.VisionPortal;
//import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
//import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import util.RobotHardware;
//
//public class Webcam {
//    private RobotHardware robot;
//    private AprilTagProcessor aprilTagProcessor;
//    private VisionPortal visionPortal;
//    private List<AprilTagDetection> detectedTags = new ArrayList<>();
//
//    public Webcam() {
//        robot = RobotHardware.getInstance();
//
//        aprilTagProcessor = new AprilTagProcessor.Builder()
//                .setDrawTagID(true)
//                .setDrawTagOutline(true)
//                .setDrawAxes(true)
//                .setDrawCubeProjection(true)
//                .setOutputUnits(DistanceUnit.CM, AngleUnit.DEGREES)
//                .build();
//
//        visionPortal = new VisionPortal.Builder()
//                .setCamera(robot.webcam)
//                .setCameraResolution(new Size(640,480))
//                .addProcessor(aprilTagProcessor)
//                .build();
//    }
//
//    public void update() {
//        detectedTags = aprilTagProcessor.getDetections();
//    }
//
//    public List<AprilTagDetection> getDetectedTags() {
//        return detectedTags;
//    }
//
//    public AprilTagDetection getTagDetection(int id) {
//        for (AprilTagDetection detection : detectedTags) {
//            if (detection.id == id) {
//                return detection;
//            }
//        }
//        return null;
//    }
//
//    public void showDetectionTelemetry(AprilTagDetection detection) {
//        if (detection == null) {return;}
//        if (detection.metadata != null) {
//            robot.telemetryManager.debug(String.format("\n==== (ID %d) %s", detection.id, detection.metadata.name));
//            robot.telemetryManager.debug(String.format("XYZ %6.1f %6.1f %6.1f  (inch)", detection.ftcPose.x, detection.ftcPose.y, detection.ftcPose.z));
//            robot.telemetryManager.debug(String.format("PRY %6.1f %6.1f %6.1f  (deg)", detection.ftcPose.pitch, detection.ftcPose.roll, detection.ftcPose.yaw));
//            robot.telemetryManager.debug(String.format("RBE %6.1f %6.1f %6.1f  (inch, deg, deg)", detection.ftcPose.range, detection.ftcPose.bearing, detection.ftcPose.elevation));
//        } else {
//            robot.telemetryManager.debug(String.format("\n==== (ID %d) Unknown", detection.id));
//            robot.telemetryManager.debug(String.format("Center %6.0f %6.0f   (pixels)", detection.center.x, detection.center.y));
//        }
//    }
//
//    public void stopStreaming() {
//        visionPortal.stopStreaming();
//    }
//
//    public void resumeStreaming() {
//        visionPortal.resumeStreaming();
//    }
//
//    public void stop() {
//        if (visionPortal != null) {
//            visionPortal.close();
//        }
//    }
//
//}
