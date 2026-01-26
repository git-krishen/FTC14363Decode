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

@Autonomous(name = "AutonRedFar", group = "Test")
public class AutonRedFar extends OpMode {
    RobotHardware robot;
    private Follower follower;
    private Timer pathTimer, actionTimer, opmodeTimer;
    private int pathState;
    private Pose startPose = new Pose(87,9,0);

    // Paths
    private PathChain Path1, Path2, Path3, Path4, Path5, Path6, Path7, Path8;

    private Intake intake;
    private Outtake outtake;

    // ctrl+f new Pose\((\d+\.?\d+), (\d+\.?\d+\)) replace reflect(new Pose\($1, $2\)
    public void buildPaths() {
        Path1 = follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(87.000, 9.000),

                                new Pose(134.000, 9.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))

                .build();

        Path2 = follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(134.000, 9.000),

                                new Pose(87.000, 9.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))

                .build();

        Path3 = follower.pathBuilder().addPath(
                        new BezierCurve(
                                new Pose(87.000, 9.000),
                                new Pose(61.009, 56.018),
                                new Pose(131.835, 23.128),
                                new Pose(200.000, 34.743),
                                new Pose(74.972, 45.972),
                                new Pose(87.000, 9.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))

                .build();

        Path4 = follower.pathBuilder().addPath(
                        new BezierCurve(
                                new Pose(87.000, 9.000),
                                new Pose(49.741, 101.622),
                                new Pose(210.000, 38.000),
                                new Pose(137.922, 55.755),
                                new Pose(75.096, 61.479),
                                new Pose(136.904, 73.030),
                                new Pose(128.000, 69.078)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))

                .build();

        Path5 = follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(128.000, 69.078),

                                new Pose(85.000, 85.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))

                .build();

        Path6 = follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(85.000, 85.000),

                                new Pose(128.000, 85.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))

                .build();

        Path7 = follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(128.000, 85.000),

                                new Pose(85.000, 85.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))

                .build();

        Path8 = follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(85.000, 85.000),

                                new Pose(85.000, 60.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))

                .build();
    }

    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                if (!follower.isBusy() && pathTimer.getElapsedTime() < 250) {
                    outtake.setOuttakeVelocity(RobotConstants.Outtake.outtakeVelocityLong);
                } else if (pathTimer.getElapsedTime() < 2750) {

                } else if (pathTimer.getElapsedTime() < 2750+1500) {
                    intake.setIntakeMotorVelocity(RobotConstants.Intake.intakeVelocity);
                    outtake.setFeederVelocity(RobotConstants.Outtake.feederVelocity);
                } else {
                    outtake.stopMotors();
                    setPathState(1);
                }
                break;
            case 1:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
                if(!follower.isBusy()) {
                    intake.setIntakePower(1);
                    /* Since this is a pathChain, we can have Pedro hold the end point while we are grabbing the sample */
                    follower.followPath(Path1);
                    setPathState(2);
                }
                break;
            case 2:
                if(!follower.isBusy()) {
                    follower.followPath(Path2);
                    setPathState(3);
                }
                break;
            case 3:
                if (follower.isBusy()) {
                    outtake.setOuttakeVelocity(RobotConstants.Outtake.outtakeVelocityLong);
                } else if (pathTimer.getElapsedTime() < 4000) {

                } else if (pathTimer.getElapsedTime() < 4000+1500) {
                    intake.setIntakeMotorVelocity(RobotConstants.Intake.intakeVelocity);
                    outtake.setFeederVelocity(RobotConstants.Outtake.feederVelocity);
                } else {
                    outtake.stopMotors();
                    setPathState(4);
                }
                break;
            case 4:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
                if(!follower.isBusy()) {
                    intake.setIntakePower(1);
                    /* Since this is a pathChain, we can have Pedro hold the end point while we are grabbing the sample */
                    follower.followPath(Path3);
                    setPathState(5);
                }
                break;
            case 5:
                if (follower.isBusy()) {
                    outtake.setOuttakeVelocity(RobotConstants.Outtake.outtakeVelocityLong*1.05);
                } else if (pathTimer.getElapsedTime() < 4000) {

                } else if (pathTimer.getElapsedTime() < 4000+2000) {
                    intake.setIntakeMotorVelocity(RobotConstants.Intake.intakeVelocity);
                    outtake.setFeederVelocity(RobotConstants.Outtake.feederVelocity);
                } else {
                    outtake.stopMotors();
                    setPathState(6);
                }
                break;
            case 6:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
                if(!follower.isBusy()) {
                    intake.setIntakePower(1);
                    /* Since this is a pathChain, we can have Pedro hold the end point while we are grabbing the sample */
                    follower.followPath(Path4);
                    setPathState(7);
                }
                break;
            case 7:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
                if(!follower.isBusy()) {
                    intake.setIntakePower(1);
                    /* Since this is a pathChain, we can have Pedro hold the end point while we are grabbing the sample */
                    follower.followPath(Path5);
                    setPathState(8);
                }
                break;
            case 8:
                if (follower.isBusy()) {
                    outtake.setOuttakeVelocity(RobotConstants.Outtake.outtakeVelocityShort*1.05);
                } else if (pathTimer.getElapsedTime() < 7000) {

                } else if (pathTimer.getElapsedTime() < 7000+2000) {
                    intake.setIntakeMotorVelocity(RobotConstants.Intake.intakeVelocity);
                    outtake.setFeederVelocity(RobotConstants.Outtake.feederVelocity);
                } else {
                    outtake.stopMotors();
                    setPathState(9);
                }
                break;
            case 9:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
                if(!follower.isBusy()) {
                    intake.setIntakePower(1);
                    /* Since this is a pathChain, we can have Pedro hold the end point while we are grabbing the sample */
                    follower.followPath(Path6);
                    setPathState(10);
                }
                break;
            case 10:
                if(!follower.isBusy()) {
                    follower.followPath(Path7);
                    setPathState(11);
                }
                break;
            case 11:
                if (follower.isBusy()) {
                    outtake.setOuttakeVelocity(RobotConstants.Outtake.outtakeVelocityShort*1.05);
                } else if (pathTimer.getElapsedTime() < 4000) {

                } else if (pathTimer.getElapsedTime() < 4000+2000) {
                    intake.setIntakeMotorVelocity(RobotConstants.Intake.intakeVelocity);
                    outtake.setFeederVelocity(RobotConstants.Outtake.feederVelocity);
                } else {
                    outtake.stopMotors();
                    follower.followPath(Path8);
                    setPathState(12);
                }
                break;
            case 12:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
                if(!follower.isBusy()) {
                    intake.stopMotor();
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
