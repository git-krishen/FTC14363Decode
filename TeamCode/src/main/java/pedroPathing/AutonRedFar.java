package pedroPathing;

import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import commands.CommandSequenceBuilder;
import util.RobotConstants;

@Autonomous(name = "AutonRedFar", group = "Test")
public class AutonRedFar extends AutonTemplate {
    private final Pose startPose = new Pose(87,9,Math.toRadians(0));

    // Paths
    private PathChain Path1, Path2, Path3, Path4, Path5;

    // ctrl+f new Pose\((\d+\.?\d+), (\d+\.?\d+\)) replace reflect(new Pose\($1, $2\)
    public void buildPaths() {
        follower.setStartingPose(startPose);
        follower.setPose(startPose);


        Path1 = follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(87.000, 9.000),

                                new Pose(124.000, 9.000) //134
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
                .setVelocityConstraint(10)
                .addPath(
                        new BezierLine(
                                new Pose(124.000, 9.000),

                                new Pose(87.000, 9.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))

                .build();

        Path2 = follower.pathBuilder().addPath(
                        new BezierCurve(
                                new Pose(87.000, 9.000),
                                new Pose(89.500, 22.500),
                                new Pose(87.179, 35.408),
                                new Pose(100.000, 36.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
                .addPath(
                        new BezierLine(
                                new Pose(100.000, 36.000),

                                new Pose(124.000, 36.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
                .addPath(
                        new BezierLine(
                                new Pose(124.000, 36.000),

                                new Pose(87.000, 9.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))

                .build();

        Path3 = follower.pathBuilder().addPath(
                        new BezierCurve(
                                new Pose(87.000, 9.000),
                                new Pose(81.408, 59.161),
                                new Pose(100.000, 58.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
                .addPath(
                        new BezierLine(
                                new Pose(100.000, 58.000),

                                new Pose(120.000, 58.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
                .addPath(
                        new BezierLine(
                                new Pose(120.000, 58.000),

                                new Pose(132.000, 60.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(30))
                .addPath(
                        new BezierCurve(
                                new Pose(132.000, 60.000),
                                new Pose(100.573, 62.794),
                                new Pose(85.000, 85.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(30), Math.toRadians(0))

                .build();

        Path4 = follower.pathBuilder().addPath(
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

        Path5 = follower.pathBuilder().addPath(
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
                .setTurretAngle(68)
                .outtakeStart(RobotConstants.Outtake.outtakeVelocityLong)
                .delay(2)
                .shoot(RobotConstants.Outtake.outtakeVelocityLong, 2)
                .parallel((p) -> p.runFeeder().moveTo(Path1))
                .shoot(RobotConstants.Outtake.outtakeVelocityLong, 2)
                .parallel((p) -> p.runFeeder().moveTo(Path2))
                .shoot(RobotConstants.Outtake.outtakeVelocityLong, 2)
                .parallel((p) -> p.runFeeder().moveTo(Path3))
                .setTurretAngle(45)
                .shoot(RobotConstants.Outtake.outtakeVelocityShort, 2)
                .parallel((p) -> p.runFeeder().moveTo(Path4))
                .shoot(RobotConstants.Outtake.outtakeVelocityShort, 2)
                .outtakeStop()
                .moveTo(Path5)
                .setTurretAngle(0)
                .build();
    }
}
