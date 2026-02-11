package commands;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import subsystems.Feeder;
import subsystems.Intake;
import subsystems.Limelight;
import subsystems.Outtake;
import subsystems.Turret;
import util.RobotHardware;


/**
 * Fluent builder API for constructing autonomous command sequences using FTCLib.
 * Provides a declarative way to sequence commands without manual state machine management.
 *
 * Example usage:
 * <pre>
 * autonomousCommand = new CommandSequenceBuilder(follower, intake, spindexer, limelight, shooter, turret)
 *     .parallel(p -> p.limelightScan().catalog())
 *     .waitForShooterReady(2.0)
 *     .waitForTurretAligned(1.0)
 *     .shoot()
 *     .parallel(p -> p.moveTo(path).intakeStart())
 *     .intakeStop()
 *     .build();
 * </pre>
 */
public class CommandSequenceBuilder {
    RobotHardware robot;
    protected final Follower follower;
    protected final Intake intake;
    protected final Limelight limelight;
    protected final Outtake outtake;
    protected final Turret turret;
    protected final Feeder feeder;

    private final List<Command> commands = new ArrayList<>();

    /**
     * Creates a new command sequence builder.
     */
    public CommandSequenceBuilder() {
        this.robot = RobotHardware.getInstance();
        this.follower = robot.follower;
        this.intake = robot.intake;
        this.limelight = robot.limelight;
        this.outtake = robot.outtake;
        this.turret = robot.turret;
        this.feeder = robot.feeder;
    }

    // ==================== Move-To Path Methods ====================

    /**
     * Adds a move-to-path command.
     *
     * @param path the path to follow
     * @return this builder for chaining
     */
    public CommandSequenceBuilder moveTo(Path path) {
        commands.add(new FollowPathCommand(follower, path, true));
        return this;
    }

    /**
     * Adds a move-to-path command with custom hold end setting.
     *
     * @param path the path to follow
     * @param holdEnd whether to hold position at the end
     * @return this builder for chaining
     */
    public CommandSequenceBuilder moveTo(Path path, boolean holdEnd) {
        commands.add(new FollowPathCommand(follower, path, holdEnd));
        return this;
    }

    /**
     * Adds a move-to-path-chain command.
     *
     * @param pathChain the path chain to follow
     * @return this builder for chaining
     */
    public CommandSequenceBuilder moveTo(PathChain pathChain) {
        commands.add(new FollowPathCommand(follower, pathChain, true));
        return this;
    }

    /**
     * Adds a move-to-path-chain command with custom hold end setting.
     *
     * @param pathChain the path chain to follow
     * @param holdEnd whether to hold position at the end
     * @return this builder for chaining
     */
    public CommandSequenceBuilder moveTo(PathChain pathChain, boolean holdEnd) {
        commands.add(new FollowPathCommand(follower, pathChain, holdEnd));
        return this;
    }

    /**
     * Adds a move-to-path command with custom speed.
     *
     * @param path the path to follow
     * @param maxPower the maximum power/speed (0.0-1.0)
     * @return this builder for chaining
     */
    public CommandSequenceBuilder moveTo(Path path, double maxPower) {
        commands.add(new FollowPathCommand(follower, path, maxPower, true));
        return this;
    }

    /**
     * Adds a move-to-path command with custom speed and hold end setting.
     *
     * @param path the path to follow
     * @param maxPower the maximum power/speed (0.0-1.0)
     * @param holdEnd whether to hold position at the end
     * @return this builder for chaining
     */
    public CommandSequenceBuilder moveTo(Path path, double maxPower, boolean holdEnd) {
        commands.add(new FollowPathCommand(follower, path, maxPower, holdEnd));
        return this;
    }

    /**
     * Adds a move-to-path-chain command with custom speed.
     *
     * @param pathChain the path chain to follow
     * @param maxPower the maximum power/speed (0.0-1.0)
     * @return this builder for chaining
     */
    public CommandSequenceBuilder moveTo(PathChain pathChain, double maxPower) {
        commands.add(new FollowPathCommand(follower, pathChain, maxPower, true));
        return this;
    }

    /**
     * Adds a move-to-path-chain command with custom speed and hold end setting.
     *
     * @param pathChain the path chain to follow
     * @param maxPower the maximum power/speed (0.0-1.0)
     * @param holdEnd whether to hold position at the end
     * @return this builder for chaining
     */
    public CommandSequenceBuilder moveTo(PathChain pathChain, double maxPower, boolean holdEnd) {
        commands.add(new FollowPathCommand(follower, pathChain, maxPower, holdEnd));
        return this;
    }

    // ==================== Inline Coordinate Path Methods ====================

    /**
     * Creates and follows a straight-line path using inline coordinates with linear heading interpolation.
     *
     * @param x1 starting x coordinate (inches)
     * @param y1 starting y coordinate (inches)
     * @param h1 starting heading (degrees)
     * @param x2 ending x coordinate (inches)
     * @param y2 ending y coordinate (inches)
     * @param h2 ending heading (degrees)
     * @return this builder for chaining
     */
    public CommandSequenceBuilder moveTo(double x1, double y1, double h1, double x2, double y2, double h2) {
        PathChain path = follower.pathBuilder()
                .addPath(new com.pedropathing.geometry.BezierLine(
                        new com.pedropathing.geometry.Pose(x1, y1),
                        new com.pedropathing.geometry.Pose(x2, y2)))
                .setLinearHeadingInterpolation(Math.toRadians(h1), Math.toRadians(h2))
                .build();
        commands.add(new FollowPathCommand(follower, path, true));
        return this;
    }

    /**
     * Creates and follows a straight-line path using inline coordinates with tangent heading.
     *
     * @param x1 starting x coordinate (inches)
     * @param y1 starting y coordinate (inches)
     * @param x2 ending x coordinate (inches)
     * @param y2 ending y coordinate (inches)
     * @return this builder for chaining
     */
    public CommandSequenceBuilder moveToTangent(double x1, double y1, double x2, double y2) {
        PathChain path = follower.pathBuilder()
                .addPath(new com.pedropathing.geometry.BezierLine(
                        new com.pedropathing.geometry.Pose(x1, y1),
                        new com.pedropathing.geometry.Pose(x2, y2)))
                .setTangentHeadingInterpolation()
                .build();
        commands.add(new FollowPathCommand(follower, path, true));
        return this;
    }

    /**
     * Creates and follows a straight-line path using inline coordinates with constant heading.
     *
     * @param x1 starting x coordinate (inches)
     * @param y1 starting y coordinate (inches)
     * @param x2 ending x coordinate (inches)
     * @param y2 ending y coordinate (inches)
     * @param heading constant heading to maintain (degrees)
     * @return this builder for chaining
     */
    public CommandSequenceBuilder moveTo(double x1, double y1, double x2, double y2, double heading) {
        PathChain path = follower.pathBuilder()
                .addPath(new com.pedropathing.geometry.BezierLine(
                        new com.pedropathing.geometry.Pose(x1, y1),
                        new com.pedropathing.geometry.Pose(x2, y2)))
                .setConstantHeadingInterpolation(Math.toRadians(heading))
                .build();
        commands.add(new FollowPathCommand(follower, path, true));
        return this;
    }

    /**
     * Creates and follows a curved Bezier path using inline coordinates with linear heading interpolation.
     *
     * @param x1 starting x coordinate (inches)
     * @param y1 starting y coordinate (inches)
     * @param h1 starting heading (degrees)
     * @param cx control point x coordinate (inches)
     * @param cy control point y coordinate (inches)
     * @param x2 ending x coordinate (inches)
     * @param y2 ending y coordinate (inches)
     * @param h2 ending heading (degrees)
     * @return this builder for chaining
     */
    public CommandSequenceBuilder moveToViaCurve(double x1, double y1, double h1, double cx, double cy,
                                                 double x2, double y2, double h2) {
        PathChain path = follower.pathBuilder()
                .addPath(new com.pedropathing.geometry.BezierCurve(
                        new com.pedropathing.geometry.Pose(x1, y1),
                        new com.pedropathing.geometry.Pose(cx, cy),
                        new com.pedropathing.geometry.Pose(x2, y2)))
                .setLinearHeadingInterpolation(Math.toRadians(h1), Math.toRadians(h2))
                .build();
        commands.add(new FollowPathCommand(follower, path, true));
        return this;
    }

    // ==================== Action Methods ====================

    /**
     * Adds a shooting command that shoots all balls at the given shoot speed.
     *
     * @return this builder for chaining
     */
    public CommandSequenceBuilder shoot(double vel, double timeOutSeconds) {
        commands.add(new ShootCommand(outtake, feeder, intake, vel, timeOutSeconds));
        return this;
    }

    /**
     * Adds an outtake start command.
     *
     * @return this builder for chaining
     */
    public CommandSequenceBuilder outtakeStart(double vel) {
        commands.add(new OuttakeStartCommand(outtake, vel));
        return this;
    }

    /**
     * Adds an outtake stop command.
     *
     * @return this builder for chaining
     */
    public CommandSequenceBuilder outtakeStop() {
        commands.add(new OuttakeStopCommand(outtake));
        return this;
    }

    /**
     * Adds an intake start command.
     *
     * @return this builder for chaining
     */
    public CommandSequenceBuilder intakeStart() {
        commands.add(new IntakeStartCommand(intake));
        return this;
    }

    /**
     * Adds an intake stop command.
     *
     * @return this builder for chaining
     */
    public CommandSequenceBuilder intakeStop() {
        commands.add(new IntakeStopCommand(intake));
        return this;
    }

    /**
     * Adds an feeder run command, will run feeder unless distance sensor registers a ball or interrupted.
     *
     * @return this builder for chaining
     */
    public CommandSequenceBuilder runFeeder() {
        commands.add(new RunFeederCommand(feeder));
        return this;
    }

//    /**
//     * Adds a limelight scan command.
//     *
//     * @return this builder for chaining
//     */
//    public CommandSequenceBuilder limelightScan() {
//        commands.add(new LimelightScanCommand(limelight));
//        return this;
//    }
//
//    /**
//     * Adds a limelight scan command with custom timeout.
//     *
//     * @param timeout timeout in seconds
//     * @return this builder for chaining
//     */
//    public CommandSequenceBuilder limelightScan(double timeout) {
//        commands.add(new LimelightScanCommand(limelight, timeout));
//        return this;
//    }

    /**
     * Adds a delay command.
     *
     * @param seconds the delay duration in seconds
     * @return this builder for chaining
     */
    public CommandSequenceBuilder delay(double seconds) {
        commands.add(new WaitCommand((long)(seconds * 1000)));
        return this;
    }

    /**
     * Waits for shooter to reach target velocity.
     *
     * @param timeout max seconds to wait
     * @return this builder for chaining
     */
    public CommandSequenceBuilder waitForShooterReady(double timeout) {
        commands.add(new WaitForShooterReadyCommand(outtake, timeout));
        return this;
    }

    /**
     * Waits for turret to be aligned (not out of range).
     *
     * @param timeout max seconds to wait
     * @return this builder for chaining
     */
    public CommandSequenceBuilder waitForTurretAligned(double timeout) {
        commands.add(new WaitForTurretAlignedCommand(turret, timeout));
        return this;
    }

    // ==================== Turret Pre-Aim Methods ====================

    /**
     * Sets the turret to a specific angle in degrees.
     * Useful for manually positioning the turret.
     *
     * @param degrees the target angle in turret degrees
     * @return this builder for chaining
     */
    public CommandSequenceBuilder setTurretAngle(double degrees) {
        commands.add(new SetTurretAngleCommand(turret, degrees));
        return this;
    }

    /**
     * Pre-aims the turret to the goal as if the robot were at the specified position.
     * Useful for aiming the turret before arriving at a shooting position.
     *
     * @param robotX hypothetical robot X position (inches)
     * @param robotY hypothetical robot Y position (inches)
     * @param robotHeadingDeg hypothetical robot heading (degrees)
     * @return this builder for chaining
     */
    public CommandSequenceBuilder preAimTurret(double robotX, double robotY, double robotHeadingDeg) {
        commands.add(new SetTurretAngleCommand(turret, robotX, robotY, robotHeadingDeg));
        return this;
    }

    /**
     * Pre-aims the turret to the goal based on the end position of a path chain.
     * Calculates where the robot will be at the end of the path and aims accordingly.
     *
     * @param pathChain the path chain to get the end position from
     * @return this builder for chaining
     */
    public CommandSequenceBuilder preAimToPathEnd(PathChain pathChain) {
        Pose endPose = pathChain.getPath(pathChain.size() - 1).getLastControlPoint();
        commands.add(new SetTurretAngleCommand(turret, endPose.getX(), endPose.getY(), Math.toDegrees(endPose.getHeading())));
        return this;
    }

    /**
     * Adds a custom command directly.
     *
     * @param command the command to add
     * @return this builder for chaining
     */
    public CommandSequenceBuilder addCommand(Command command) {
        commands.add(command);
        return this;
    }

    // ==================== Parallel Groups ====================

    /**
     * Adds a parallel command group.
     * All commands in the group will run simultaneously until all complete.
     *
     * Example:
     * <pre>
     * .parallel(p -> p.moveTo(path).catalog())
     * </pre>
     *
     * @param builder a consumer that adds commands to the parallel group
     * @return this builder for chaining
     */
    public CommandSequenceBuilder parallel(Consumer<ParallelBuilder> builder) {
        ParallelBuilder parallelBuilder = new ParallelBuilder();
        builder.accept(parallelBuilder);
        commands.add(parallelBuilder.build());
        return this;
    }

    // ==================== Build ====================

    /**
     * Builds and returns the configured command sequence.
     * Call this at the end of your sequence definition.
     *
     * @return the configured SequentialCommandGroup
     */
    public Command build() {
        return new SequentialCommandGroup(commands.toArray(new Command[0]));
    }

    // ==================== Parallel Builder Inner Class ====================

    /**
     * Builder for parallel command groups.
     * Provides the same fluent methods but adds to a parallel group instead.
     */
    public static class ParallelBuilder {
        RobotHardware robot;
        private final Follower follower;
        private final Intake intake;
        private final Limelight limelight;
        private final Outtake outtake;
        private final Turret turret;
        private final Feeder feeder;
        private final List<Command> parallelCommands = new ArrayList<>();

        private ParallelBuilder() {
            this.robot = RobotHardware.getInstance();
            this.follower = robot.follower;
            this.intake = robot.intake;
            this.limelight = robot.limelight;
            this.outtake = robot.outtake;
            this.turret = robot.turret;
            this.feeder = robot.feeder;
        }

        // Path methods
        public ParallelBuilder moveTo(Path path) {
            parallelCommands.add(new FollowPathCommand(follower, path, true));
            return this;
        }

        public ParallelBuilder moveTo(Path path, double maxPower) {
            parallelCommands.add(new FollowPathCommand(follower, path, maxPower, true));
            return this;
        }

        public ParallelBuilder moveTo(Path path, boolean holdEnd) {
            parallelCommands.add(new FollowPathCommand(follower, path, holdEnd));
            return this;
        }

        public ParallelBuilder moveTo(Path path, double maxPower, boolean holdEnd) {
            parallelCommands.add(new FollowPathCommand(follower, path, maxPower, holdEnd));
            return this;
        }

        public ParallelBuilder moveTo(PathChain pathChain) {
            parallelCommands.add(new FollowPathCommand(follower, pathChain, true));
            return this;
        }

        public ParallelBuilder moveTo(PathChain pathChain, double maxPower) {
            parallelCommands.add(new FollowPathCommand(follower, pathChain, maxPower, true));
            return this;
        }

        public ParallelBuilder moveTo(PathChain pathChain, boolean holdEnd) {
            parallelCommands.add(new FollowPathCommand(follower, pathChain, holdEnd));
            return this;
        }

        public ParallelBuilder moveTo(PathChain pathChain, double maxPower, boolean holdEnd) {
            parallelCommands.add(new FollowPathCommand(follower, pathChain, maxPower, holdEnd));
            return this;
        }

        // Action methods
        public ParallelBuilder shoot(double vel, double timeOutSeconds) {
            parallelCommands.add(new ShootCommand(outtake, feeder, intake, vel, timeOutSeconds));
            return this;
        }

        public ParallelBuilder outtakeStart(double vel) {
            parallelCommands.add(new OuttakeStartCommand(outtake, vel));
            return this;
        }

        public ParallelBuilder outtakeStop() {
            parallelCommands.add(new OuttakeStopCommand(outtake));
            return this;
        }

        public ParallelBuilder intakeStart() {
            parallelCommands.add(new IntakeStartCommand(intake));
            return this;
        }

        public ParallelBuilder intakeStop() {
            parallelCommands.add(new IntakeStopCommand(intake));
            return this;
        }

        public ParallelBuilder runFeeder() {
            parallelCommands.add(new RunFeederCommand(feeder));
            return this;
        }

//        public ParallelBuilder limelightScan() {
//            parallelCommands.add(new LimelightScanCommand(limelight));
//            return this;
//        }

        public ParallelBuilder delay(double seconds) {
            parallelCommands.add(new WaitCommand((long)(seconds * 1000)));
            return this;
        }

        public ParallelBuilder waitForShooterReady(double timeout) {
            parallelCommands.add(new WaitForShooterReadyCommand(outtake, timeout));
            return this;
        }

        public ParallelBuilder waitForTurretAligned(double timeout) {
            parallelCommands.add(new WaitForTurretAlignedCommand(turret, timeout));
            return this;
        }

        // Turret pre-aim methods
        public ParallelBuilder setTurretAngle(double degrees) {
            parallelCommands.add(new SetTurretAngleCommand(turret, degrees));
            return this;
        }

        public ParallelBuilder preAimTurret(double robotX, double robotY, double robotHeadingDeg) {
            parallelCommands.add(new SetTurretAngleCommand(turret, robotX, robotY, robotHeadingDeg));
            return this;
        }

        public ParallelBuilder preAimToPathEnd(PathChain pathChain) {
            Pose endPose = pathChain.getPath(pathChain.size() - 1).getLastControlPoint();
            parallelCommands.add(new SetTurretAngleCommand(turret, endPose.getX(), endPose.getY(), Math.toDegrees(endPose.getHeading())));
            return this;
        }

        public ParallelBuilder addCommand(Command command) {
            parallelCommands.add(command);
            return this;
        }

        /**
         * Builds the parallel command group.
         */
        ParallelCommandGroup build() {
            return new ParallelCommandGroup(parallelCommands.toArray(new Command[0]));
        }
    }
}