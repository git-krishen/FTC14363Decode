package pedroPathing;

import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import commands.CommandSequenceBuilder;
import util.RobotConstants;

@Autonomous(name = "AutonBlueFar", group = "Blue")
public class AutonBlueFar extends AutonTemplate {
    private final Pose startPose = new Pose(57,9,Math.toRadians(180));

    // Paths
    private PathChain Path1, Path2, Path3, Path4, Path5;

    // ctrl+f new Pose\((\d+\.?\d+), (\d+\.?\d+\)) replace reflect(new Pose\($1, $2\)
    public void buildPaths() {
        follower.setStartingPose(startPose);
        follower.setPose(startPose);


        Path1 = follower.pathBuilder().addPath(
                        new BezierLine(
                                reflect(new Pose(87.000, 9.000)),

                                reflect(new Pose(120.000, 9.000)) //134
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
                .setVelocityConstraint(10)
                .addPath(
                        new BezierLine(
                                reflect(new Pose(120.000, 9.000)),

                                reflect(new Pose(87.000, 9.000))
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))

                .build();

        Path2 = follower.pathBuilder().addPath(
                        new BezierCurve(
                                reflect(new Pose(87.000, 9.000)),
                                reflect(new Pose(89.500, 22.500)),
                                reflect(new Pose(87.179, 35.408)),
                                reflect(new Pose(100.000, 36.000))
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
                .addPath(
                        new BezierLine(
                                reflect(new Pose(100.000, 36.000)),

                                reflect(new Pose(124.000, 36.000))
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
                .addPath(
                        new BezierLine(
                                reflect(new Pose(124.000, 36.000)),

                                reflect(new Pose(87.000, 9.000))
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))

                .build();

        Path3 = follower.pathBuilder().addPath(
                        new BezierCurve(
                                reflect(new Pose(87.000, 9.000)),
                                reflect(new Pose(81.408, 59.161)),
                                reflect(new Pose(100.000, 58.000))
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
                .addPath(
                        new BezierLine(
                                reflect(new Pose(100.000, 58.000)),

                                reflect(new Pose(120.000, 58.000))
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
                .addPath(
                        new BezierLine(
                                reflect(new Pose(120.000, 58.000)),

                                reflect(new Pose(132.000, 60.000))
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180-30))
                .addPath(
                        new BezierCurve(
                                reflect(new Pose(132.000, 60.000)),
                                reflect(new Pose(100.573, 62.794)),
                                reflect(new Pose(85.000, 85.000))
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(180-30), Math.toRadians(180))

                .build();

        Path4 = follower.pathBuilder().addPath(
                        new BezierLine(
                                reflect(new Pose(85.000, 85.000)),

                                reflect(new Pose(116.000, 85.000))
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
                .addPath(
                        new BezierLine(
                                reflect(new Pose(116.000, 85.000)),

                                reflect(new Pose(85.000, 85.000))
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))

                .build();

        Path5 = follower.pathBuilder().addPath(
                        new BezierLine(
                                reflect(new Pose(85.000, 85.000)),

                                reflect(new Pose(90.000, 60.000))
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))

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
