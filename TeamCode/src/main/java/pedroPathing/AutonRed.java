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
import java.util.List;

import subsystems.Intake;
import subsystems.Outtake;
import util.RobotConstants;
import util.RobotHardware;

@Autonomous(name = "AutonRed", group = "Test")
public class AutonRed extends OpMode {
    private Follower follower;
    private Timer pathTimer, actionTimer, opmodeTimer;
    private int pathState;

    // Poses
    private final Pose startPoseBottom = new Pose(87, 8.5, Math.toRadians(270));
    private final Pose startPoseTop = new Pose(120,120, Math.toRadians(315));
    private final Pose row1Control = new Pose(94, 48-18-20, Math.toRadians(30));
    private final Pose row1Start = new Pose(108-18, 36+8-20, Math.toRadians(0));
    private final Pose row1End = new Pose(132-20, 36+8-20, Math.toRadians(0));
//    private final Pose row2Control = new Pose(72-8, 72-24, Math.toRadians(30));
//    private final Pose row2Start = new Pose(108-18, 60+8, Math.toRadians(0));
//    private final Pose row2End = new Pose(132-14, 60+8, Math.toRadians(0));
    private final Pose row2Control = new Pose(72-8, 48-24, Math.toRadians(30));
    private final Pose row2Start = new Pose(108-18, 36+8, Math.toRadians(0));
    private final Pose row2End = new Pose(132-16, 36+8, Math.toRadians(0));
    private final Pose row3Control = new Pose(72-8, 96-24, Math.toRadians(30));
    private final Pose row3Start = new Pose(108-18, 84+8, Math.toRadians(0));
    private final Pose row3End = new Pose(132-24, 84+8, Math.toRadians(0));
    private final Pose scorePoseTop = new Pose(84, 60, Math.toRadians(135));
    private final Pose scorePoseBottom = new Pose(90, 12, Math.toRadians(245)); //y=18 // 240

    // Paths
    private PathChain row1, row2, row3, pickup1, pickup2, pickup3, score, score1, score2, score3;

    private Intake intake;
    private Outtake outtake;

    public void buildPaths() {
        score = follower.pathBuilder()
                .addPath(new BezierLine(startPoseBottom, scorePoseBottom))
                .setLinearHeadingInterpolation(startPoseBottom.getHeading(), scorePoseBottom.getHeading())
//                .setVelocityConstraint(5)
                .build();
        row3 = follower.pathBuilder()
                .addPath(new BezierCurve(new ArrayList<>(List.of(scorePoseBottom, row3Control, row3Start))))
                .setLinearHeadingInterpolation(scorePoseBottom.getHeading(), row3Start.getHeading())
                .build();
        pickup3 = follower.pathBuilder()
                .addPath(new BezierLine(row3Start, row3End))
                .setLinearHeadingInterpolation(row3Start.getHeading(), row3End.getHeading())
                .setVelocityConstraint(20)
                .addPath(new BezierLine(row3End, row3Start))
                .setLinearHeadingInterpolation(row3End.getHeading(), row3Start.getHeading())
                .setVelocityConstraint(20)
                .build();
        score3 = follower.pathBuilder()
                .addPath(new BezierCurve(new ArrayList<>(List.of(row3Start, row3Control, scorePoseBottom))))
                .setLinearHeadingInterpolation(row3Start.getHeading(), scorePoseBottom.getHeading())
                .build();

        row2 = follower.pathBuilder()
                .addPath(new BezierCurve(new ArrayList<>(List.of(scorePoseBottom, row2Control, row2Start))))
                .setLinearHeadingInterpolation(scorePoseBottom.getHeading(), row2Start.getHeading())
                .build();
        pickup2 = follower.pathBuilder()
                .addPath(new BezierLine(row2Start, row2End))
                .setLinearHeadingInterpolation(row2Start.getHeading(), row2End.getHeading())
                .setVelocityConstraint(20)
                .addPath(new BezierLine(row2End, row2Start))
                .setLinearHeadingInterpolation(row2End.getHeading(), row2Start.getHeading())
                .setVelocityConstraint(20)
                .build();
        score2 = follower.pathBuilder()
                .addPath(new BezierCurve(new ArrayList<>(List.of(row2Start, row2Control, scorePoseBottom))))
                .setLinearHeadingInterpolation(row2Start.getHeading(), scorePoseBottom.getHeading())
                .build();

        row1 = follower.pathBuilder()
                .addPath(new BezierCurve(new ArrayList<>(List.of(scorePoseBottom, row1Control, row1Start))))
                .setLinearHeadingInterpolation(scorePoseBottom.getHeading(), row1Start.getHeading())
                .build();
        pickup1 = follower.pathBuilder()
                .addPath(new BezierLine(row1Start, row1End))
                .setLinearHeadingInterpolation(row1Start.getHeading(), row1End.getHeading())
                .setVelocityConstraint(20)
                .addPath(new BezierLine(row1End, row1Start))
                .setLinearHeadingInterpolation(row1End.getHeading(), row1Start.getHeading())
                .setVelocityConstraint(20)
                .build();
        score1 = follower.pathBuilder()
                .addPath(new BezierCurve(new ArrayList<>(List.of(row1Start, row1Control, scorePoseBottom))))
                .setLinearHeadingInterpolation(row1Start.getHeading(), scorePoseBottom.getHeading())
                .build();
    }

    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                if (!follower.isBusy() && pathTimer.getElapsedTime() < 250) {
                    follower.followPath(score, true);
                    outtake.setOuttakeVelocity(RobotConstants.Outtake.outtakeVelocityLong);
//                    outtake.setOuttakePower(1);
                }
                if (pathTimer.getElapsedTime() < 2500) {

                } else if (pathTimer.getElapsedTime() < 2750) {
                    outtake.setFeederVelocity(RobotConstants.Outtake.feederVelocity);
                } else if (pathTimer.getElapsedTime() < 4750) {
                    outtake.stopFeederMotor();
                } else if (pathTimer.getElapsedTime() < 5000) {
                    outtake.setFeederVelocity(RobotConstants.Outtake.feederVelocity);
                    intake.setIntakeMotorVelocity(RobotConstants.Intake.intakeVelocity);
                } else if (pathTimer.getElapsedTime() < 7000) {
                    intake.stopMotor();
                    outtake.stopFeederMotor();
                } else if (pathTimer.getElapsedTime() < 7500) {
                    outtake.setFeederVelocity(RobotConstants.Outtake.feederVelocity);
                    intake.setIntakeMotorVelocity(RobotConstants.Intake.intakeVelocity);
                } else {
                    outtake.stopOuttakeMotor();
                    outtake.stopFeederMotor();
                    intake.stopMotor();
                    setPathState(1);
                }
                break;
            case 1:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
                if(!follower.isBusy()) {
                    /* Since this is a pathChain, we can have Pedro hold the end point while we are grabbing the sample */
                    follower.followPath(row1);
                    setPathState(2);
                }
                break;
            case 2:
                if(!follower.isBusy()) {
                    follower.followPath(pickup1);
                    setPathState(3);
                }
                break;
            case 3:
                if (pathTimer.getElapsedTime() < 500) {
                    intake.setIntakePower(1);
                    outtake.setFeederVelocity(RobotConstants.Outtake.feederVelocity);
                } else if (pathTimer.getElapsedTime() < 700) {
                    outtake.setFeederVelocity(-RobotConstants.Outtake.feederVelocity);
                } else if (follower.getPathCompletion() < 1) {
                    outtake.stopFeederMotor();
                } else {
                    intake.stopMotor();
                }
                if(!follower.isBusy()) {
                    intake.stopMotor();
                    follower.followPath(score2, true);
                    setPathState(4);
                }
                break;
            case 4:
                if (follower.isBusy()) {
                    outtake.setOuttakeVelocity(RobotConstants.Outtake.outtakeVelocityLong);
                }
                if (pathTimer.getElapsedTime() < 2500) {

                } else if (pathTimer.getElapsedTime() < 2750) {
                    outtake.setFeederVelocity(RobotConstants.Outtake.feederVelocity);
                } else if (pathTimer.getElapsedTime() < 4750) {
                    outtake.stopFeederMotor();
                } else if (pathTimer.getElapsedTime() < 5000) {
                    outtake.setFeederVelocity(RobotConstants.Outtake.feederVelocity);
                    intake.setIntakeMotorVelocity(RobotConstants.Intake.intakeVelocity);
                } else if (pathTimer.getElapsedTime() < 7000) {
                    intake.stopMotor();
                    outtake.stopFeederMotor();
                } else if (pathTimer.getElapsedTime() < 7500) {
                    outtake.setFeederVelocity(RobotConstants.Outtake.feederVelocity);
                    intake.setIntakeMotorVelocity(RobotConstants.Intake.intakeVelocity);
                } else {
                    outtake.stopOuttakeMotor();
                    outtake.stopFeederMotor();
                    intake.stopMotor();
                    setPathState(5);
                }
                break;
            case 5:
                if(!follower.isBusy()) {
                    follower.followPath(row1);
                    setPathState(6);
                }
                break;
            case 6:
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

        drawCurrentAndHistory();

        // Feedback to Driver Hub for debugging
        telemetry.addData("path state", pathState);
        telemetry.addData("x", follower.getPose().getX());
        telemetry.addData("y", follower.getPose().getY());
        telemetry.addData("heading", follower.getPose().getHeading());
        telemetry.update();

        follower.update();
        autonomousPathUpdate();
    }

    /** This method is called once at the init of the OpMode. **/
    @Override
    public void init() {
        RobotHardware.getInstance().init(hardwareMap);
        pathTimer = new Timer();
        opmodeTimer = new Timer();
        opmodeTimer.resetTimer();


        follower = Constants.createFollower(hardwareMap);
        buildPaths();
        follower.setStartingPose(startPoseBottom);

        intake = new Intake();
        outtake = new Outtake();

        drawCurrent();
    }

    public void drawCurrent() {
        try {
            Drawing.drawRobot(follower.getPose());
            Drawing.sendPacket();
        } catch (Exception e) {
            throw new RuntimeException("Drawing failed " + e);
        }
    }

    public void drawCurrentAndHistory() {
        Drawing.drawPoseHistory(follower.getPoseHistory());
        drawCurrent();
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
