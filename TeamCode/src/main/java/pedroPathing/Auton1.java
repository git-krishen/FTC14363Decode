package pedroPathing;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import subsystems.Intake;
import subsystems.Outtake;
import util.RobotConstants;

@Autonomous(name = "Auton1", group = "Test")
public class Auton1 extends OpMode {
    private Follower follower;
    private Timer pathTimer, actionTimer, opmodeTimer;
    private int pathState;

    // Poses
    private final Pose startPoseBottom = new Pose(12, 84, Math.toRadians(0));
    private final Pose startPoseTop = new Pose(120,120, Math.toRadians(315));
    private final Pose row1Control = new Pose(24, 72, Math.toRadians(270));
    private final Pose row1Start = new Pose(36, 96, Math.toRadians(270));
    private final Pose row1End = new Pose(36, 120, Math.toRadians(270));
    private final Pose row2Control = new Pose(48, 72, Math.toRadians(270));
    private final Pose row2Start = new Pose(60, 96, Math.toRadians(270));
    private final Pose row2End = new Pose(60, 120, Math.toRadians(270));
    private final Pose row3Control = new Pose(72, 72, Math.toRadians(270));
    private final Pose row3Start = new Pose(84, 96, Math.toRadians(270));
    private final Pose row3End = new Pose(84, 120, Math.toRadians(270));
    private final Pose scorePose = new Pose(84, 72, Math.toRadians(315));

    // Paths
    private PathChain row1, row2, row3, pickup1, pickup2, pickup3, score, score1, score2, score3;

    private Intake intake;
    private Outtake outtake;

    public void buildPaths() {
        score = follower.pathBuilder()
                .addPath(new BezierLine(startPoseTop, scorePose))
                .setLinearHeadingInterpolation(startPoseTop.getHeading(), scorePose.getHeading())
                .build();
        row3 = follower.pathBuilder()
                .addPath(new BezierCurve(new ArrayList<>(List.of(scorePose, row3Control, row3Start))))
                .setLinearHeadingInterpolation(scorePose.getHeading(), row3Start.getHeading())
                .build();
        pickup3 = follower.pathBuilder()
                .addPath(new BezierLine(row3Start, row3End))
                .setLinearHeadingInterpolation(row3Start.getHeading(), row3End.getHeading())
                .addPath(new BezierLine(row3End, row3Start))
                .setLinearHeadingInterpolation(row3End.getHeading(), row3Start.getHeading())
                .build();
        score3 = follower.pathBuilder()
                .addPath(new BezierCurve(new ArrayList<>(List.of(row3Start, row3Control, scorePose))))
                .setLinearHeadingInterpolation(row3Start.getHeading(), scorePose.getHeading())
                .build();

        row2 = follower.pathBuilder()
                .addPath(new BezierCurve(new ArrayList<>(List.of(scorePose, row2Control, row2Start))))
                .setLinearHeadingInterpolation(scorePose.getHeading(), row2Start.getHeading())
                .build();
        pickup2 = follower.pathBuilder()
                .addPath(new BezierLine(row2Start, row2End))
                .setLinearHeadingInterpolation(row2Start.getHeading(), row2End.getHeading())
                .addPath(new BezierLine(row2End, row2Start))
                .setLinearHeadingInterpolation(row2End.getHeading(), row2Start.getHeading())
                .build();
        score2 = follower.pathBuilder()
                .addPath(new BezierCurve(new ArrayList<>(List.of(row2Start, row2Control, scorePose))))
                .setLinearHeadingInterpolation(row2Start.getHeading(), scorePose.getHeading())
                .build();

        row1 = follower.pathBuilder()
                .addPath(new BezierCurve(new ArrayList<>(List.of(scorePose, row1Control, row1Start))))
                .setLinearHeadingInterpolation(scorePose.getHeading(), row1Start.getHeading())
                .build();
        pickup1 = follower.pathBuilder()
                .addPath(new BezierLine(row1Start, row1End))
                .setLinearHeadingInterpolation(row1Start.getHeading(), row1End.getHeading())
                .addPath(new BezierLine(row1End, row1Start))
                .setLinearHeadingInterpolation(row1End.getHeading(), row1Start.getHeading())
                .build();
        score1 = follower.pathBuilder()
                .addPath(new BezierCurve(new ArrayList<>(List.of(row1Start, row1Control, scorePose))))
                .setLinearHeadingInterpolation(row1Start.getHeading(), scorePose.getHeading())
                .build();
    }

    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                follower.followPath(score);
                setPathState(1);
                break;
            case 1:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
                if(!follower.isBusy()) {
                    /* Since this is a pathChain, we can have Pedro hold the end point while we are grabbing the sample */
                    follower.followPath(row3);
                    setPathState(2);
                }
                break;
            case 2:
                if(!follower.isBusy()) {
                    intake.setIntakeMotorVelocity(RobotConstants.Intake.intakeVelocity);
                    follower.followPath(pickup3);
                    setPathState(3);
                }
            case 3:
                if (follower.getPathCompletion() < 0.75) {
                    intake.setIntakeMotorVelocity(RobotConstants.Intake.intakeVelocity);
                } else {
                    intake.stopMotor();
                }
                if(!follower.isBusy()) {
                    intake.stopMotor();
                    follower.followPath(score3, true);
                    setPathState(4);
                }
            case 4:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
                outtake.setFeederVelocity(RobotConstants.Outtake.feederVelocity);
                outtake.setOuttakeVelocity(RobotConstants.Outtake.outtakeVelocity);
                if(!follower.isBusy() && pathTimer.getElapsedTime() > 2000) {
                    /* Since this is a pathChain, we can have Pedro hold the end point while we are grabbing the sample */
                    outtake.stopMotors();
                    follower.followPath(row2);
                    setPathState(5);
                }
                break;
            case 5:
                if(!follower.isBusy()) {
                    intake.setIntakeMotorVelocity(RobotConstants.Intake.intakeVelocity);
                    follower.followPath(pickup2);
                    setPathState(6);
                }
            case 6:
                if (follower.getPathCompletion() < 0.75) {
                    intake.setIntakeMotorVelocity(RobotConstants.Intake.intakeVelocity);
                } else {
                    intake.stopMotor();
                }
                if(!follower.isBusy()) {
                    intake.stopMotor();
                    follower.followPath(score2);
                    setPathState(7);
                }
            case 7:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
                outtake.setFeederVelocity(RobotConstants.Outtake.feederVelocity);
                outtake.setOuttakeVelocity(RobotConstants.Outtake.outtakeVelocity);
                if(!follower.isBusy() && pathTimer.getElapsedTime() > 2000) {
                    /* Since this is a pathChain, we can have Pedro hold the end point while we are grabbing the sample */
                    outtake.stopMotors();
                    follower.followPath(row1);
                    setPathState(8);
                }
                break;
            case 8:
                if(!follower.isBusy()) {
                    intake.setIntakeMotorVelocity(RobotConstants.Intake.intakeVelocity);
                    follower.followPath(pickup1);
                    setPathState(9);
                }
            case 9:
                if (follower.getPathCompletion() < 0.75) {
                    intake.setIntakeMotorVelocity(RobotConstants.Intake.intakeVelocity);
                } else {
                    intake.stopMotor();
                }
                if(!follower.isBusy()) {
                    intake.stopMotor();
                    follower.followPath(score3);
                    setPathState(10);
                }
            case 10:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
                if(!follower.isBusy()) {
                    /* Set the state to a Case we won't use or define, so it just stops running an new paths */
                    setPathState(-1);
                }
                break;
        }
    }

    /** These change the states of the paths and actions. It will also reset the timers of the individual switches **/
    public void setPathState(int pState) {
        pathState = pState;
        pathTimer.resetTimer();
    }

    /** This is the main loop of the OpMode, it will run repeatedly after clicking "Play". **/
    @Override
    public void loop() {

        // These loop the movements of the robot, these must be called continuously in order to work
        follower.update();
        autonomousPathUpdate();

        // Feedback to Driver Hub for debugging
        telemetry.addData("path state", pathState);
        telemetry.addData("x", follower.getPose().getX());
        telemetry.addData("y", follower.getPose().getY());
        telemetry.addData("heading", follower.getPose().getHeading());
        telemetry.update();
    }

    /** This method is called once at the init of the OpMode. **/
    @Override
    public void init() {
        pathTimer = new Timer();
        opmodeTimer = new Timer();
        opmodeTimer.resetTimer();


        follower = Constants.createFollower(hardwareMap);
        buildPaths();
        follower.setStartingPose(startPoseTop);

        intake = new Intake();
        outtake = new Outtake();
    }

    /** This method is called continuously after Init while waiting for "play". **/
    @Override
    public void init_loop() {}

    /** This method is called once at the start of the OpMode.
     * It runs all the setup actions, including building paths and starting the path system **/
    @Override
    public void start() {
        opmodeTimer.resetTimer();
        setPathState(0);
    }

    /** We do not use this because everything should automatically disable **/
    @Override
    public void stop() {}
}
