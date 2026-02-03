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

@Autonomous(name = "ShootAndGTFORed", group = "Test")
public class ShootAndGTFORed extends OpMode {
    RobotHardware robot;
    private Follower follower;
    private Timer pathTimer, actionTimer, opmodeTimer;
    private int pathState;
    private Pose startPose = new Pose(87,9, Math.toRadians(90));

    // Paths
    private PathChain Path1, Path2;

    private Intake intake;
    private Outtake outtake;

    // ctrl+f new Pose\((\d+\.?\d+), (\d+\.?\d+\)) replace reflect(new Pose\($1, $2\)
    public void buildPaths() {
        Path1 = follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(87.000, 9.000),

                                new Pose(87.000, 12.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(65))

                .build();

        Path2 = follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(87.000, 12.000),

                                new Pose(120.000, 12.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(65), Math.toRadians(0))

                .build();
    }

    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                if (!follower.isBusy() && pathTimer.getElapsedTime() < 250) {
                    follower.followPath(Path1);
                    outtake.setOuttakeVelocity(RobotConstants.Outtake.outtakeVelocityLong);
                } else if (pathTimer.getElapsedTime() < 1500) {

                } else if (pathTimer.getElapsedTime() < 1500+2000) {
                    intake.setIntakePower(0.8);
                    outtake.setFeederPower(0.8);
                } else {
                    outtake.stopMotors();
                    setPathState(1);
                }
                break;
            case 1:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
                if(!follower.isBusy()) {
                    /* Since this is a pathChain, we can have Pedro hold the end point while we are grabbing the sample */
                    follower.followPath(Path2);
                    setPathState(2);
                }
                break;
            case 2:
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
            outtake.setFeederPower(0.8);
        } else if (pathTimer.getElapsedTime() < startDelay+shotOneTiming+shotTwoDelay) {
            outtake.stopFeederMotor();
        } else if (pathTimer.getElapsedTime() < startDelay+shotOneTiming+shotTwoDelay+shotTwoTiming) {
            outtake.setFeederPower(0.8);
            intake.setIntakePower(0.8);
        } else if (pathTimer.getElapsedTime() < startDelay+shotOneTiming+shotTwoDelay+shotTwoTiming+shotThreeDelay) {
            intake.stopMotor();
            outtake.stopFeederMotor();
        } else if (pathTimer.getElapsedTime() < startDelay+shotOneTiming+shotTwoDelay+shotTwoTiming+shotThreeDelay+shotThreeTiming) {
            outtake.setFeederPower(0.8);
            intake.setIntakePower(0.8);
        } else {
            outtake.stopOuttakeMotor();
            outtake.stopFeederMotor();
            intake.stopMotor();
            setPathState(nextPathState);
        }
    }

    private void handleIntake(int intakeTime, int feederTime) {
        if (pathTimer.getElapsedTime() < intakeTime) {
            intake.setIntakePower(1);
        } else if (pathTimer.getElapsedTime() < intakeTime+feederTime) {
            outtake.setFeederVelocity(0.8);
        } else if (follower.getPathCompletion() < 1) {
            outtake.stopFeederMotor();
        } else {
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
        telemetry.addData("outtakeSpeed", outtake.getOuttakeVelocity() + " " + outtake.getTargetVelocity());
        telemetry.update();

        follower.update();
        outtake.periodic();
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

    public Pose reflect(Pose p) {
        return p.mirror();
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
