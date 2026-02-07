package pedroPathing;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import commands.CommandSequenceBuilder;
import commands.RunFeederCommand;
import subsystems.Feeder;
import subsystems.Intake;
import subsystems.Outtake;
import util.RobotConstants;
import util.RobotHardware;

@Autonomous(name = "AutonRedCloseGate", group = "Test")
public class AutonRedCloseGate extends AutonTemplate {
    private Pose startPose = new Pose(122,122,Math.toRadians(0));
    // Paths
    private PathChain Path1, Path2, Path3, Path4, Path5, Path6, Path7, Path8;

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
                                new Pose(73.106, 53.261),
                                new Pose(130.000, 58.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))

                .build();

        Path3 = follower.pathBuilder().addPath(
                        new BezierCurve(
                                new Pose(130.000, 58.000),
                                new Pose(89.904, 59.867),
                                new Pose(95.000, 95.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))

                .build();

        Path4 = follower.pathBuilder().addPath(
                        new BezierCurve(
                                new Pose(95.000, 95.000),
                                new Pose(85.005, 47.890),
                                new Pose(130.000, 58.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(30))

                .build();

        Path5 = follower.pathBuilder().addPath(
                        new BezierCurve(
                                new Pose(130.000, 58.000),
                                new Pose(96.000, 60.000),
                                new Pose(95.000, 95.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(30), Math.toRadians(0))

                .build();

        Path6 = follower.pathBuilder().addPath(
                        new BezierCurve(
                                new Pose(95.000, 95.000),
                                new Pose(86.220, 83.060),
                                new Pose(87.826, 87.060),
                                new Pose(108.317, 83.872),
                                new Pose(128.000, 85.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))

                .build();

        Path7 = follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(128.000, 85.000),

                                new Pose(95.000, 95.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))

                .build();

        Path8 = follower.pathBuilder().addPath(
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
                .setTurretAngle(30)
                .moveTo(Path1)
                .shoot(RobotConstants.Outtake.outtakeVelocityShort, 2)
                .parallel((p) -> p.intakeStart().runFeeder().moveTo(Path2))
                .moveTo(Path3)
                .intakeStop()
                .shoot(RobotConstants.Outtake.outtakeVelocityShort, 2)
                .parallel((p) -> p.intakeStart().runFeeder().moveTo(Path4))
                .delay(2)
                .moveTo(Path5)
                .intakeStop()
                .shoot(RobotConstants.Outtake.outtakeVelocityShort, 2)
                .parallel((p) -> p.intakeStart().runFeeder().moveTo(Path6))
                .moveTo(Path7)
                .intakeStop()
                .shoot(RobotConstants.Outtake.outtakeVelocityShort, 2)
                .intakeStop()
                .moveTo(Path8)
                .build();
    }
}
