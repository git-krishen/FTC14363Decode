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

@Autonomous(name = "AutonRed2Row", group = "Test")
public class AutonRed2Row extends OpMode {
    RobotHardware robot;
    private Follower follower;
    private Timer pathTimer, actionTimer, opmodeTimer;
    private int pathState;

    // Poses
    private final Pose startPose = new Pose(87, 8.5, Math.toRadians(270));
    private final Pose row1Control = new Pose(94, 10, Math.toRadians(30));
    private final Pose row1Start = new Pose(90, 24+2, Math.toRadians(0));
    private final Pose row1End = new Pose(120, 24+2, Math.toRadians(0));
    private final Pose row2Control = new Pose(76, 10+22, Math.toRadians(30));
    private final Pose row2Start = new Pose(90, 24+24, Math.toRadians(0));
    private final Pose row2End = new Pose(122, 24+22, Math.toRadians(0));
    private final Pose row3Control = new Pose(86, 10+48, Math.toRadians(30));
    private final Pose row3Start = new Pose(90, 24+48, Math.toRadians(0));
    private final Pose row3End = new Pose(112, 24+48, Math.toRadians(0));
    private final Pose scorePose = new Pose(90, 12, Math.toRadians(245)); //y=18 // 240
    private final Pose scorePose2 = new Pose(90, 12, Math.toRadians(240));

    // Paths
    private PathChain row1, row2, row3, pickup1, pickup2, pickup3, score, score1, score2, score3;

    private Intake intake;
    private Outtake outtake;

    public void buildPaths() {
        score = createLine(startPose, scorePose);
//        row3 = createCurve(scorePose, row3Control, row3Start);
        row3 = createLine(scorePose, row3Start);
        pickup3 = createPickupPath(row3Start, row3End, 20);
//        score3 = createCurve(row3Start, row3Control, scorePose);
        score3 = createLine(row3Start, scorePose2);

//        row2 = createCurve(scorePose, row2Control, row2Start);
        row2 = createLine(scorePose, row2Start);
        pickup2 = createPickupPath(row2Start, row2End, 20);
//        score2 = createCurve(row2Start, row2Control, scorePose);
        score2 = createLine(row2Start, scorePose2);

//        row1 = createCurve(scorePose, row1Control, row1Start);
        row1 = createLine(scorePose, row1Start);
        pickup1 = createPickupPath(row1Start, row1End, 20);
//        score1 = createCurve(row1Start, row1Control, scorePose);
        score1 = createLine(row1Start, scorePose);
    }

    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                if (!follower.isBusy() && pathTimer.getElapsedTime() < 250) {
                    follower.followPath(score, true);
                    outtake.setOuttakeVelocity(Math.PI*1.6);
                } else {
                    handleShooting(
                            2500,
                            500,
                            1000,
                            750,
                            1000,
                            750,
                            1);
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
                handleIntake(750,0);
                if(!follower.isBusy()) {
                    intake.stopMotor();
                    follower.followPath(score1, true);
                    setPathState(4);
                }
                break;
            case 4:
                if (follower.isBusy()) {
                    outtake.setOuttakeVelocity(Math.PI*1.7);
                } else {
                    handleShooting(
                            3000,
                            750,
                            1000,
                            1250,
                            1000,
                            750,
                            5);
                }
                break;
            case 5:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
                if(!follower.isBusy()) {
                    /* Since this is a pathChain, we can have Pedro hold the end point while we are grabbing the sample */
                    follower.followPath(row2);
                    setPathState(6);
                }
                break;
            case 6:
                if(!follower.isBusy()) {
                    follower.followPath(pickup2);
                    setPathState(7);
                }
                break;
            case 7:
                handleIntake(750,0);
                if(!follower.isBusy()) {
                    intake.stopMotor();
                    follower.followPath(score2, true);
                    setPathState(8);
                }
                break;
            case 8:
                if (follower.isBusy()) {
                    outtake.setOuttakeVelocity(Math.PI*1.7);
                } else {
                    handleShooting(
                            4000,
                            750,
                            1000,
                            1250,
                            1000,
                            750,
                            9);
                }
                break;
            case 9:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
                if(!follower.isBusy()) {
                    /* Since this is a pathChain, we can have Pedro hold the end point while we are grabbing the sample */
                    follower.followPath(row1);
                    setPathState(10);
                }
                break;
            case 10:
//                if(!follower.isBusy()) {
//                    follower.followPath(pickup3);
//                    setPathState(11);
//                }
//                break;
//            case 11:
//                handleIntake(750,0);
//                if(!follower.isBusy()) {
////                    intake.stopMotor();
////                    follower.followPath(score3, true);
//                    setPathState(12);
//                }
//                break;
//            case 12:
////                if (follower.isBusy()) {
////                    outtake.setOuttakeVelocity(Math.PI*1.7);
////                } else {
////                    handleShooting(
////                            5000,
////                            750,
////                            1000,
////                            1000,
////                            1000,
////                            750,
////                            13);
////                }
////                break;
////            case 13:
////                if(!follower.isBusy()) {
////                    follower.followPath(row1);
////                    setPathState(14);
////                }
////                break;
////            case 14:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
                if(!follower.isBusy()) {
                    /* Set the state to a Case we won't use or define, so it just stops running an new paths */
                    setPathState(-1);
                }
                break;
        }
    }

    private PathChain createLine(Pose startPose, Pose endPose) {
        return follower.pathBuilder()
                .addPath(new BezierLine(startPose, endPose))
                .setLinearHeadingInterpolation(startPose.getHeading(), endPose.getHeading())
                .build();
    }

    private PathChain createCurve(Pose startPose, Pose controlPose, Pose endPose) {
        return follower.pathBuilder()
                .addPath(new BezierCurve(new ArrayList<>(List.of(startPose, controlPose, endPose))))
                .setLinearHeadingInterpolation(startPose.getHeading(), endPose.getHeading())
                .build();
    }

    private PathChain createPickupPath(Pose startPose, Pose endPose, int maxVel) {
        return follower.pathBuilder()
                .addPath(new BezierLine(startPose, endPose))
                .setLinearHeadingInterpolation(startPose.getHeading(), endPose.getHeading())
                .setVelocityConstraint(maxVel)
                .addPath(new BezierLine(startPose, endPose))
                .setLinearHeadingInterpolation(startPose.getHeading(), endPose.getHeading())
                .setVelocityConstraint(maxVel)
                .build();
    }

    private void handleShooting(int startDelay, int shotDelay, int shotTiming, int nextPathState) {
        handleShooting(startDelay, shotDelay, shotDelay, shotTiming, nextPathState);
    }

    private void handleShooting(int startDelay, int shotTwoDelay, int shotThreeDelay, int shotTiming, int nextPathState) {
        handleShooting(startDelay, shotTiming, shotTwoDelay, shotTiming, shotThreeDelay, shotTiming, nextPathState);
    }

    private void handleShooting(int startDelay, int shotOneTiming, int shotTwoDelay, int shotTwoTiming, int shotThreeDelay, int shotThreeTiming, int nextPathState) {
        if (pathTimer.getElapsedTime() < startDelay) {

        } else if (pathTimer.getElapsedTime() < startDelay+shotOneTiming) {
            outtake.setFeederVelocity(RobotConstants.Outtake.feederVelocity);
        } else if (pathTimer.getElapsedTime() < startDelay+shotOneTiming+shotTwoDelay) {
            outtake.stopFeederMotor();
        } else if (pathTimer.getElapsedTime() < startDelay+shotOneTiming+shotTwoDelay+shotTwoTiming) {
            outtake.setFeederVelocity(RobotConstants.Outtake.feederVelocity);
            intake.setIntakeMotorVelocity(RobotConstants.Intake.intakeVelocity);
        } else if (pathTimer.getElapsedTime() < startDelay+shotOneTiming+shotTwoDelay+shotTwoTiming+shotThreeDelay) {
            intake.stopMotor();
            outtake.stopFeederMotor();
        } else if (pathTimer.getElapsedTime() < startDelay+shotOneTiming+shotTwoDelay+shotTwoTiming+shotThreeDelay+shotThreeTiming) {
            outtake.setFeederVelocity(RobotConstants.Outtake.feederVelocity);
            intake.setIntakeMotorVelocity(RobotConstants.Intake.intakeVelocity);
        } else {
            outtake.stopOuttakeMotor();
            outtake.stopFeederMotor();
            intake.stopMotor();
            setPathState(nextPathState);
        }
    }

    private void handleIntake(int intakeTime, int reverseTime) {
        if (pathTimer.getElapsedTime() < intakeTime) {
            intake.setIntakePower(1);
            outtake.setFeederVelocity(RobotConstants.Outtake.feederVelocity);
        } else if (pathTimer.getElapsedTime() < intakeTime+reverseTime) {
            outtake.setFeederVelocity(-RobotConstants.Outtake.feederVelocity);
        } else if (follower.getPathCompletion() < 1) {
            outtake.stopFeederMotor();
        } else {
            intake.stopMotor();
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
        telemetry.addData("x", follower.getPose().getX() + " | " + follower.getPose().getX());
        telemetry.addData("y", follower.getPose().getY() + " | " + follower.getPose().getY());
        telemetry.addData("heading", follower.getPose().getHeading());
        telemetry.update();

        follower.update();
        autonomousPathUpdate();
    }

    /** This method is called once at the init of the OpMode. **/
    @Override
    public void init() {
        robot = RobotHardware.getInstance();
        robot.init(hardwareMap);
        pathTimer = new Timer();
        opmodeTimer = new Timer();
        opmodeTimer.resetTimer();


        follower = robot.follower;
        buildPaths();

        intake = robot.intake;
        outtake = robot.outtake;

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
    public void init_loop() {
        follower.setStartingPose(startPose);
        follower.setPose(startPose);
        follower.updatePose();
        telemetry.addData("odo", follower.getPose().getX() + " | " + follower.getPose().getY());
        telemetry.addData("follower", follower.getPose().getX() + " | " + follower.getPose().getY());
        telemetry.update();
    }

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
