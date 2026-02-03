//package subsystems;
//
//import com.arcrobotics.ftclib.command.Subsystem;
//import com.pedropathing.geometry.Pose;
//import com.qualcomm.hardware.limelightvision.LLResult;
//import com.qualcomm.hardware.limelightvision.LLResultTypes.FiducialResult;
//
//import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
//import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.nio.charset.StandardCharsets;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Optional;
//import java.util.OptionalDouble;
//import java.util.OptionalInt;
//import java.util.stream.Collectors;
//
//import util.RobotConstants;
//import util.RobotHardware;
//
//public class LimelightAsync implements Subsystem {
//    private RobotHardware robot;
//    private OptionalInt targetID;
//
//    public LimelightAsync() {
//        robot = RobotHardware.getInstance();
//        targetID = OptionalInt.empty();
//    }
//
//    public void setTargetID(int id) {
//        targetID = OptionalInt.of(id);
//    }
//
//    public void setRobotYaw(double angle) {
//        robot.ll.updateRobotOrientation(angle);
//    }
//
//    public boolean hasTarget() {
//        return getTagIDList().contains(targetID.orElse(-1));
//    }
//
//    public boolean hasTag(int id) {
//        return getTagIDList().contains(id);
//    }
//
//    public OptionalDouble getTargetX() {
//        Optional<FiducialResult> targetFiducial = getTargetFiducial();
//        if (targetFiducial.isPresent()) {
//            return OptionalDouble.of(targetFiducial.get().getTargetXDegrees());
//        }
//        return OptionalDouble.empty();
//    }
//
//    public OptionalDouble getTargetY() {
//        Optional<FiducialResult> targetFiducial = getTargetFiducial();
//        if (targetFiducial.isPresent()) {
//            return OptionalDouble.of(targetFiducial.get().getTargetYDegrees());
//        }
//        return OptionalDouble.empty();
//    }
//
//
//    public OptionalDouble getTargetArea() {
//        Optional<FiducialResult> targetFiducial = getTargetFiducial();
//        if (targetFiducial.isPresent()) {
//            return OptionalDouble.of(targetFiducial.get().getTargetArea());
//        }
//        return OptionalDouble.empty();
//    }
//
//    public ArrayList<Integer> getTagIDList() {
//        ArrayList<Integer> tags = new ArrayList<Integer>();
//        List<FiducialResult> fiducials = getFiducialList();
//        for (FiducialResult fiducial : fiducials) {
//            int id = fiducial.getFiducialId();
//            tags.add(id);
//        }
//        return tags;
//    }
//
//    public Optional<Pose> getRobotPose() {
//        List<FiducialResult> list = getFiducialList();
//        if (list == null || list.isEmpty()) return Optional.empty();
//        Pose3D rawPose = list.get(0).getRobotPoseFieldSpace();
//        Pose pose = new Pose(rawPose.getPosition().x, rawPose.getPosition().y, rawPose.getOrientation().getYaw(AngleUnit.DEGREES));
//        return Optional.of(pose);
//    }
//
//    private List<FiducialResult> getFiducialList() {
//        LLResult result = robot.ll.getLatestResult();
//        return result.getFiducialResults();
//    }
//
//    private Optional<FiducialResult> getTargetFiducial() {
//        if (targetID.isEmpty()) {
//            return Optional.empty();
//        }
//        List<FiducialResult> fiducials = getFiducialList();
//        fiducials = fiducials.stream().filter(
//                (f) -> {
//                    return f.getFiducialId() == targetID.getAsInt();
//                }
//        ).collect(Collectors.toList());
//        return !fiducials.isEmpty() ? Optional.of(fiducials.get(0)) : Optional.empty();
//    }
//
//    // Meters, degrees
//    public boolean updateLimelightPose(double forward, double side, double up, double yaw, double pitch, double roll) {
//        try {
//            JSONObject pipelineUpdate = new JSONObject();
//            double[] cameraPose = {forward, side, up, roll, pitch, yaw};
//            // x
//            pipelineUpdate.put("rsf", forward);
//            // y
//            pipelineUpdate.put("rss", side);
//            // z
//            pipelineUpdate.put("rsu", up);
//            // yaw
//            pipelineUpdate.put("rsyaw", yaw);
//            // pitch
//            pipelineUpdate.put("rspitch", pitch);
//            // roll
//            pipelineUpdate.put("rsroll", roll);
//            return sendPostRequest("/update-pipeline", pipelineUpdate.toString());
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//    public String[] getOrientationArrayString() {
//        JSONObject statusJson = sendGetRequest("/results");
//        if(statusJson == null)
//        {
//            return new String[]{"null statusjson"};
//        }
//        JSONArray finalimuArray = statusJson.optJSONArray("t6c_rs");
//        if (finalimuArray == null) {
//            String ret = "";
//            Iterator i = statusJson.keys();
//            while (i.hasNext()) {
//                ret += i.next() + "\n";
//            }
//            return new String[]{"null finalimuArray" + "\n" + ret};
//        }
//        try {
//            return new String[]{
//                    finalimuArray.getString(0),
//                    finalimuArray.getString(1),
//                    finalimuArray.getString(2),
//                    finalimuArray.getString(3),
//                    finalimuArray.getString(4),
//                    finalimuArray.getString(5)
//            };
//        } catch(Exception e) {
//            return new String[]{e.toString()};
//        }
//    }
//
//    private boolean sendPostRequest(String endpoint, String data) {
//        String baseUrl = "http://" + "172.29.0.1" + ":5807";;
//        HttpURLConnection connection = null;
//        try {
//            String urlString = baseUrl + endpoint;
//            URL url = new URL(urlString);
//            connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("POST");
//            connection.setDoOutput(true);
//            connection.setRequestProperty("Content-Type", "application/json");
//            connection.setReadTimeout(50);
//            connection.setConnectTimeout(50);
//
//            if (data != null) {
//                try (OutputStream os = connection.getOutputStream()) {
//                    byte[] input = data.getBytes(StandardCharsets.UTF_8);
//                    os.write(input, 0, input.length);
//                }
//            }
//
//            int responseCode = connection.getResponseCode();
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//                return true;
//            } else {
//                //System.out.println("HTTP POST Error: " + responseCode);
//            }
//        } catch (Exception e) {
//            robot.telemetryManager.addData("error", e.toString());
//        } finally {
//            if (connection != null) {
//                connection.disconnect();
//            }
//        }
//        return false;
//    }
//
//    private JSONObject sendGetRequest(String endpoint) {
//        String baseUrl = "http://" + "172.29.0.1" + ":5807";;
//        HttpURLConnection connection = null;
//        try {
//            String urlString = baseUrl + endpoint;
//            URL url = new URL(urlString);
//            connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("GET");
//            connection.setReadTimeout(50);
//            connection.setConnectTimeout(50);
//
//            int responseCode = connection.getResponseCode();
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//                String response = readResponse(connection);
//                return new JSONObject(response);
//            } else {
//                //System.out.println("HTTP GET Error: " + responseCode);
//            }
//        } catch (Exception e) {
//        } finally {
//            if (connection != null) {
//                connection.disconnect();
//            }
//        }
//        return null;
//    }
//
//    private String readResponse(HttpURLConnection connection) throws IOException {
//        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//        StringBuilder response = new StringBuilder();
//        String line;
//
//        while ((line = reader.readLine()) != null) {
//            response.append(line);
//        }
//        reader.close();
//
//        return response.toString();
//    }
//
//    @Override
//    public void periodic() {
//        double angle = robot.turret.getTotalRotationTurret();
//        double forward = RobotConstants.Limelight.axisForward + Math.cos(Math.toRadians(angle))*RobotConstants.Limelight.rotRadius;
//        double right = RobotConstants.Limelight.axisRight + Math.sin(Math.toRadians(angle))*RobotConstants.Limelight.rotRadius;
//        double up = RobotConstants.Limelight.axisUp;
//        updateLimelightPose(
//                forward,
//                right,
//                up,
//                angle,
//                15,
//                0
//        );
//        if (hasTarget()) {
//            robot.turret.setTargetRotationTurret(getTotalRotationTurret()-(getTargetX().orElse(0)));
//        }
//    }
//}
