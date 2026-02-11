package opmode;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.RunCommand;
import com.arcrobotics.ftclib.command.Subsystem;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.pedropathing.geometry.Pose;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.util.HashSet;
import java.util.Set;

import subsystems.Feeder;
import subsystems.Intake;
import subsystems.MecanumDrive;
import subsystems.Outtake;
import subsystems.Turret;
import util.RobotConstants;
import util.RobotHardware;

public class TeleopBase extends CommandOpMode {
    private final RobotHardware robot = RobotHardware.getInstance();
    private GamepadEx driver;
    private GamepadEx driver2;
    private MecanumDrive drivetrain;
    private Intake intake;
    private Outtake outtake;
    private Feeder feeder;
    private Turret turret;
    private Pose scorePose;
    // Maybe I need to set states here???


    @Override
    public void initialize() {
        CommandScheduler.getInstance().reset();
        driver = new GamepadEx(gamepad1);
        driver2 = new GamepadEx(gamepad2);
        robot.init(hardwareMap, driver);

        this.drivetrain = robot.drivetrain;
        this.intake = robot.intake;
        this.outtake = robot.outtake;
        this.feeder = robot.feeder;
        this.turret = robot.turret;
        robot.ll.start();

        scorePose = new Pose(RobotConstants.Turret.scoreRedX, RobotConstants.Turret.scoreRedY);

        CommandScheduler.getInstance().registerSubsystem(outtake, turret);

        robot.follower.setPose(RobotConstants.Drivetrain.autonEndPose);

        configureBindings();
    }

    @Override
    public void run() {
        CommandScheduler.getInstance().run();
        robot.follower.update();

        Pose llPoseEstimate = robot.limelight.getRobotPose().orElse(new Pose(-67,-67,0));
        robot.telemetryManager.addData("x", robot.follower.getPose().getX() + " | " + llPoseEstimate.getX());
        robot.telemetryManager.addData("y", robot.follower.getPose().getY() + " | " + llPoseEstimate.getY());
        robot.telemetryManager.addData("heading", robot.follower.getPose().getHeading() + " | " + llPoseEstimate.getHeading());
        robot.telemetryManager.addData("turret", turret.getDebugInfo());
        robot.telemetryManager.addData("outtakeVel", outtake.getOuttakeVelocity());
        robot.telemetryManager.addData("maxRPMFrac", robot.outtakeMotor.getMotorType().getAchieveableMaxRPMFraction());
        robot.telemetryManager.addData("limelight", robot.limelight.getOrientationArrayString());
        robot.telemetryManager.addData("distance (intake|outtake)", robot.intakeDistanceSensor.getDistance(DistanceUnit.MM) + " | " + robot.outtakeDistanceSensor.getDistance(DistanceUnit.MM));
        robot.telemetryManager.update();
    }



    private void configureBindings() {
        drivetrain.setDefaultCommand(new RunCommand(() -> {
            double ly = Math.abs(driver.getLeftY()) > 0.15 ? driver.getLeftY() : 0;
            double lx = Math.abs(driver.getLeftX()) > 0.15 ? driver.getLeftX() : 0;
            double rx = Math.abs(driver.getRightX()) > 0.15 ? driver.getRightX() : 0;
            drivetrain.drive(
                    ly,
                    lx,
                    rx
            );
            }, drivetrain));

//        turret.setDefaultCommand(new RunCommand(() -> {
//            double angle = turret.getTotalRotationTurret();
//            double forward = RobotConstants.Limelight.axisForward + Math.cos(Math.toRadians(angle))*RobotConstants.Limelight.rotRadius;
//            double right = RobotConstants.Limelight.axisRight + Math.sin(Math.toRadians(angle))*RobotConstants.Limelight.rotRadius;
//            double up = RobotConstants.Limelight.axisUp;
//            robot.limelight.updateLimelightPose(forward, right, up, angle, 15.0, 0.0);
//            if (robot.limelight.hasTarget()) {
//                turret.lockToAprilTag();
//            } else {
////                turret.setTargetRotationTurret(0);
//
//                double x = robot.follower.getPose().getX();
//                double y = robot.follower.getPose().getY();
//                double botHeading = robot.follower.getPose().getHeading();
//                x += RobotConstants.Turret.turretOffsetX*Math.cos(botHeading) - RobotConstants.Turret.turretOffsetY*Math.sin(botHeading);
//                y += RobotConstants.Turret.turretOffsetX*Math.sin(botHeading) + RobotConstants.Turret.turretOffsetY*Math.cos(botHeading);
//                double reqAngle = Math.atan2(RobotConstants.Turret.scoreRedY-y,RobotConstants.Turret.scoreRedY-x);
//                double delta = reqAngle - botHeading;
//                delta -= Math.PI/2;
//                double finalAngle = Math.toDegrees(Math.atan2(Math.sin(delta),Math.cos(delta)));
//                finalAngle += Math.PI/2;
//                robot.telemetryManager.debug("odoShoot", finalAngle);
////                turret.setTargetRotationTurret(Math.toDegrees(Math.atan2(Math.sin(delta),Math.cos(delta))));
//            }
//            }, turret));
        driver2.getGamepadButton(GamepadKeys.Button.A).whileHeld(
                new InstantCommand(() -> {
                    double angle = turret.getTotalRotationTurret();
                    double forward = RobotConstants.Limelight.axisForward + Math.cos(Math.toRadians(angle))*RobotConstants.Limelight.rotRadius;
                    double right = RobotConstants.Limelight.axisRight + Math.sin(Math.toRadians(angle))*RobotConstants.Limelight.rotRadius;
                    double up = RobotConstants.Limelight.axisUp;
                    robot.limelight.updateLimelightPose(forward, right, up, angle, 15.0, 0.0);
                    if (robot.limelight.hasTarget()) {
                        turret.lockToAprilTag();
                    }
                }, turret)
        );

        driver.getGamepadButton(GamepadKeys.Button.START).whenPressed(
                new InstantCommand(() -> {
                    robot.follower.setPose(robot.follower.getPose().setHeading(0));
                })
        );

//        driver.getGamepadButton(GamepadKeys.Button.LEFT_STICK_BUTTON).whenPressed(
//                new InstantCommand(() -> {
//                    robot.odo.recalibrateIMU();
//                })
//        );driver.getGamepadButton(GamepadKeys.Button.DPAD_LEFT).whileHeld(
//                new InstantCommand(() -> turret.setPosition(turret.getPosition()+0.01))
//        );

//        driver2.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).whileHeld(
//                new StartEndCommand(
//                        () -> turret.setPower(-1),
//                        () -> turret.stopTurret()
//                )
//        );
//        driver2.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).whileHeld(
//                new StartEndCommand(
//                        () -> turret.setPower(1),
//                        () -> turret.stopTurret()
//                )
//        );
        driver2.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).whileHeld(
                new InstantCommand(() -> turret.changeTargetRotation(RobotConstants.Turret.turretSpeed), turret)
        );
        driver2.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).whileHeld(
                new InstantCommand(() -> turret.changeTargetRotation(-RobotConstants.Turret.turretSpeed), turret)
        );
//        driver2.getGamepadButton(GamepadKeys.Button.A).whenPressed(
//                new InstantCommand(() -> turret.forceResetTotalRotation())
//        );


//        driver.getGamepadButton(GamepadKeys.Button.DPAD_UP).whenPressed(
//                new InstantCommand(
//                        () -> {
//                            outtake.setOuttakePower(-1);
//                        },
//                        outtake
//                )
//        );
//        driver.getGamepadButton(GamepadKeys.Button.DPAD_UP).whenReleased(
//                new InstantCommand(
//                        () -> {
//                            outtake.stopOuttakeMotor();
//                        },
//                        outtake
//                )
//        );

        driver.getGamepadButton(GamepadKeys.Button.DPAD_DOWN).whenPressed(
                new InstantCommand(
                        () -> {
                            feeder.setFeederPower(-1);
                            intake.setIntakePower(-1);
                        },
                        feeder, intake
                )
        );
        driver.getGamepadButton(GamepadKeys.Button.DPAD_DOWN).whenReleased(
                new InstantCommand(
                        () -> {
                            feeder.stopMotor();
                            intake.stopMotor();
                        },
                        feeder, intake
                )
        );

        RunCommand slowModeCommand = new RunCommand(() -> {
            drivetrain.setSlowMode(driver.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0.3);
        });
        CommandScheduler.getInstance().schedule(false, slowModeCommand);

        Command triggerCommand = new Command() {
            boolean triggered = false;

            @Override
            public void execute() {
                double triggerVal = driver.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER);
                if (!triggered && triggerVal>0.3) {
                    double curr = intake.getIntakePower();
                    intake.setIntakePower(curr>0.1 ? 0 : 1);
                    triggered = true;
                } else if (triggered && triggerVal<0.3) {
                    triggered = false;
                }
            }

            @Override
            public void end(boolean interrupted) {
                intake.stopMotor();
            }

            @Override
            public Set<Subsystem> getRequirements() {
                return Set.of(intake);
            }
        };
        intake.setDefaultCommand(triggerCommand);

        Command triggerCommandFeeder = new Command() {
            boolean triggered = false;

            @Override
            public void execute() {
                double triggerVal = driver.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER);
                if (!triggered && triggerVal>0.3) {
                    double curr = intake.getIntakePower();
                    feeder.setFeederPower(curr>0.1 ? 1 : 0);
                    triggered = true;
                } else if (triggered && triggerVal<0.3) {
                    triggered = false;
                }
                if (robot.outtakeDistanceSensor.getDistance(DistanceUnit.MM)<=RobotConstants.Outtake.outtakeSensorThreshold) {
                    feeder.stopMotor();
                }
            }

            @Override
            public void end(boolean interrupted) {
                feeder.stopMotor();
            }

            @Override
            public Set<Subsystem> getRequirements() {
                return Set.of(feeder);
            }
        };
        feeder.setDefaultCommand(triggerCommandFeeder);

        driver.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).whenHeld(
                new Command() {
                    @Override
                    public void execute() {
                        feeder.setFeederPower(1);
                    }

                    @Override
                    public void end(boolean interrupted) {
                        feeder.stopMotor();
                    }

                    @Override
                    public Set<Subsystem> getRequirements() {
                        return Set.of(feeder);
                    }
                }
        );

//        driver.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).whenHeld(
//                new Command() {
//                    @Override
//                    public void execute() {
////                        double lerp = RobotConstants.Outtake.shotSpeedSlope*drivetrain.getDistanceToGoal() + RobotConstants.Outtake.shotSpeedIntercept;
////                        outtake.setOuttakeVelocity(Math.clamp(lerp, RobotConstants.Outtake.shotSpeedMin, RobotConstants.Outtake.shotSpeedMax));
//                        if (driver.getGamepadButton(GamepadKeys.Button.A).get()) {
//                            outtake.setOuttakeVelocity(RobotConstants.Outtake.outtakeVelocityLong);
//                        } else {
//                            outtake.setOuttakeVelocity(RobotConstants.Outtake.outtakeVelocityShort);
//                        }
//                    }
//
//                    @Override
//                    public void end(boolean interrupted) {
//                        outtake.stopOuttakeMotor();
//                    }
//
//                    @Override
//                    public Set<Subsystem> getRequirements() {
//                        return Set.of(outtake);
//                    }
//                }
//        );
        outtake.setDefaultCommand(new RunCommand(() -> {
//            double lerp = RobotConstants.Outtake.shotSpeedSlope*drivetrain.getDistanceToGoal(scorePose) + RobotConstants.Outtake.shotSpeedIntercept;
//            outtake.setOuttakeVelocity(Math.clamp(lerp, RobotConstants.Outtake.shotSpeedMin, RobotConstants.Outtake.shotSpeedMax));
            if (driver2.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0.3) {
                outtake.setOuttakeVelocity(RobotConstants.Outtake.outtakeVelocityLong);
            } else {
                outtake.setOuttakeVelocity(RobotConstants.Outtake.outtakeVelocityShort);
            }
        }, outtake));

//        driver.getGamepadButton(GamepadKeys.Button.DPAD_DOWN).whenPressed(
//                new Command() {
//                    Follower follower;
//
//                    @Override
//                    public void initialize() {
//                        follower = drivetrain.driveToPose(
//                                new Pose(72,24,90),
//                                hardwareMap
//                        );
//                    }
//
//                    @Override
//                    public void execute() {
//                        follower.update();
//                        System.out.println(follower.getPathCompletion());
//                    }
//
//                    @Override
//                    public boolean isFinished() {
//                        return follower.isBusy();
//                    }
//
//                    @Override
//                    public void end(boolean interrupted) {
//                        follower.pausePathFollowing();
//                    }
//
//                    @Override
//                    public Set<Subsystem> getRequirements() {
//                        return Set.of(drivetrain);
//                    }
//                }, false
//        );
    }

    public void setScorePose(Pose p) {
        this.scorePose = p;
    }
}
