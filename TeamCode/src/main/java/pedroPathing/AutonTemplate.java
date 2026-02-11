package pedroPathing;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.RunCommand;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathBuilder;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import subsystems.Feeder;
import subsystems.Intake;
import subsystems.Outtake;
import subsystems.Turret;
import util.FieldDrawing;
import util.RobotConstants;
import util.RobotHardware;

public abstract class AutonTemplate extends OpMode {
    protected RobotHardware robot;
    protected Follower follower;
    protected Timer pathTimer, actionTimer, opmodeTimer;
    protected int pathState;
    protected Intake intake;
    protected Outtake outtake;
    protected Feeder feeder;
    protected Turret turret;

    protected Command autonomousCommand;

    public abstract void buildPaths();

    /** This is the main loop of the OpMode, it will run repeatedly after clicking "Play". **/
    @Override
    public void loop() {
        follower.update();
        CommandScheduler.getInstance().run();

        telemetry.addData("path state", pathState);
        telemetry.addData("x", follower.getPose().getX() + " | " + follower.getPose().getX());
        telemetry.addData("y", follower.getPose().getY() + " | " + follower.getPose().getY());
        telemetry.addData("heading", follower.getPose().getHeading());
        telemetry.addData("outtakeSpeed", outtake.getOuttakeVelocity() + " " + outtake.getTargetVelocity());
        FieldDrawing.drawFollowerDebug(follower);
        telemetry.update();
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

        robot.ll.start();
        intake = robot.intake;
        outtake = robot.outtake;
        feeder = robot.feeder;
        turret = robot.turret;

        FieldDrawing.init();

        CommandScheduler.getInstance().registerSubsystem(outtake, turret);
    }

    /** These change the states of the paths and actions. It will also reset the timers of the individual switches **/
    public void setPathState(int pState) {
        pathState = pState;
        pathTimer.resetTimer();
    }

    public Pose reflect(Pose p) {
        return p.mirror();
    }

    /** This method is called continuously after Init while waiting for "play". **/
    @Override
    public void init_loop() {
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

        follower.activateAllPIDFs();

        if (autonomousCommand != null) {
            CommandScheduler.getInstance().schedule(
                    autonomousCommand
                    .alongWith(new RunCommand(() -> intake.setIntakePower(1)))
            );
        }
    }

    @Override
    public void stop() {
        RobotConstants.Drivetrain.autonEndPose = follower.getPose();
        CommandScheduler.getInstance().reset();
        outtake.stopOuttakeMotor();
        intake.stopMotor();
        feeder.stopMotor();
    }
}
