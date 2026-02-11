package pedroPathing;

import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import commands.CommandSequenceBuilder;
import util.RobotConstants;

@Autonomous(name = "AutonRedCloseGateAlt", group = "Test")
public class AutonRedCloseGateAlt extends AutonTemplate {
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

                                new Pose(85.000, 85.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))

                .build();

        Path2 = follower.pathBuilder().addPath(
                        new BezierCurve(
                                new Pose(85.000, 85.000),
                                new Pose(82.573, 58.206),
                                new Pose(100.000, 60.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
                .addPath(
                        new BezierLine(
                                new Pose(100.000, 60.000),

                                new Pose(124.000, 60.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
                .addPath(
                        new BezierLine(
                                new Pose(124.000, 60.000),

                                new Pose(85.000, 85.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))

                .build();

        Path3 = follower.pathBuilder().addPath(
                        new BezierCurve(
                                new Pose(85.000, 85.000),
                                new Pose(85.005, 47.890),
                                new Pose(122.000, 56.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(25))

                .build();

        Path4 = follower.pathBuilder().addPath(
                        new BezierCurve(
                                new Pose(122.000, 56.000),
                                new Pose(96.000, 60.000),
                                new Pose(85.000, 85.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(35), Math.toRadians(0))

                .build();

        Path5 = follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(85.000, 85.000),

                                new Pose(116.000, 85.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
                .addPath(
                        new BezierLine(
                                new Pose(116.000, 85.000),

                                new Pose(85.000, 85.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))

                .build();

        Path6 = follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(85.000, 85.000),

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
