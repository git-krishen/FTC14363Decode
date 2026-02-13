package pedroPathing;

import com.arcrobotics.ftclib.command.RunCommand;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.util.ArrayList;
import java.util.List;

import commands.CommandSequenceBuilder;
import commands.RunFeederCommand;
import subsystems.Feeder;
import subsystems.Intake;
import subsystems.Outtake;
import util.RobotConstants;
import util.RobotHardware;

@Autonomous(name = "AutonRedCloseGate", group = "Red")
public class AutonRedCloseGate extends AutonTemplate {
    private final Pose startPose = new Pose(122,122,Math.toRadians(0));

    // Paths
    private PathChain Path1, Path2, Path3, Path4, Path5, Path6;

    // ctrl+f new Pose\((\d+\.?\d+), (\d+\.?\d+\)) replace reflect(new Pose\($1, $2\)
    public void buildPaths() {
        follower.setStartingPose(startPose);
        follower.setPose(startPose);


        Path1 = follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(122.000, 122.000),

                                new Pose(95.000, 95.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))

                .build();

        Path2 = follower.pathBuilder().addPath(
                        new BezierCurve(
                                new Pose(95.000, 95.000),
                                new Pose(84.115, 56.665),
                                new Pose(100.000, 60.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
                .addPath(
                        new BezierLine(
                                new Pose(100.000, 60.000),

                                new Pose(118.000, 60.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
                .addPath(
                        new BezierCurve(
                                new Pose(118.000, 58.000),
                                new Pose(89.904, 59.867),
                                new Pose(95.000, 95.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))

                .build();

        Path3 = follower.pathBuilder().addPath(
                        new BezierCurve(
                                new Pose(95.000, 95.000),
                                new Pose(85.005, 47.890),
                                new Pose(134.000, 56.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(35))

                .build();

        Path4 = follower.pathBuilder().addPath(
                        new BezierCurve(
                                new Pose(134.000, 56.000),
                                new Pose(96.000, 60.000),
                                new Pose(95.000, 95.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(35), Math.toRadians(0))

                .build();

        Path5 = follower.pathBuilder().addPath(
                        new BezierCurve(
                                new Pose(95.000, 95.000),
                                new Pose(94.367, 84.381),
                                new Pose(102.000, 85.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
                .addPath(
                        new BezierLine(
                                new Pose(102.000, 85.000),

                                new Pose(116.000, 85.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
                .addPath(
                        new BezierLine(
                                new Pose(116.000, 85.000),

                                new Pose(95.000, 95.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))

                .build();

        Path6 = follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(95.000, 95.000),

                                new Pose(90.000, 60.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))

                .build();
    }

    @Override
    public void init() {
        super.init();
        autonomousCommand = new CommandSequenceBuilder()
                .setTurretAngle(45)
                .outtakeStart(RobotConstants.Outtake.outtakeVelocityShort*1.02)
                .moveTo(Path1)
                .shoot(RobotConstants.Outtake.outtakeVelocityShort*1.02, 2)
                .parallel((p) -> p.runFeeder().moveTo(Path2))
                .shoot(RobotConstants.Outtake.outtakeVelocityShort*1.02, 2)
                .parallel((p) -> p.runFeeder().moveTo(Path3))
                .parallel((p) -> p.delay(2))
                .moveTo(Path4)
                .shoot(RobotConstants.Outtake.outtakeVelocityShort*1.02, 2)
                .parallel((p) -> p.runFeeder().moveTo(Path5))
                .shoot(RobotConstants.Outtake.outtakeVelocityShort, 2)
                .moveTo(Path6)
                .setTurretAngle(0)
                .build();
    }
}
