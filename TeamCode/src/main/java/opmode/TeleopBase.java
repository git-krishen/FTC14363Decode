package opmode;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.RunCommand;
import com.arcrobotics.ftclib.command.StartEndCommand;
import com.arcrobotics.ftclib.command.Subsystem;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.pedropathing.geometry.Pose;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import subsystems.Intake;
import subsystems.Limelight;
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
    private Turret turret;
    private Limelight limelight;
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
        this.turret = robot.turret;
        this.limelight = robot.limelight;
        // Would add telemetry here
//        Pose llPose = robot.limelight.getRobotPose().orElse(new Pose());
//        robot.telemetryManager.debug("x" + " " + robot.follower.getPose().getX() + " | " + llPose.getX());
//        robot.telemetryManager.debug("y" + " " + robot.follower.getPose().getY() + " | " + llPose.getY());
//        robot.telemetryManager.debug("heading" + " " + robot.follower.getPose().getHeading() + " | " + llPose.getHeading());
//        robot.telemetryManager.debug("llRobotPose" + " " + Arrays.toString(limelight.getOrientationArrayString()));
//        robot.telemetryManager.debug("turret" + " " + turret.getDebugInfo());
//        robot.telemetryManager.debug("outtakeVel" + " " + outtake.getOuttakeVelocity());
//        robot.telemetryManager.debug("maxRPMFrac" + " " + robot.outtakeMotor.getMotorType().getAchieveableMaxRPMFraction());
//        robot.telemetryManager.update();

        configureBindings();
    }

    @Override
    public void run() {
//        Pose llPose = robot.limelight.getRobotPose().orElse(new Pose());
//        robot.telemetryManager.addData("x", robot.follower.getPose().getX() + " | " + llPose.getX());
//        robot.telemetryManager.addData("y", robot.follower.getPose().getY() + " | " + llPose.getY());
//        robot.telemetryManager.addData("heading", robot.follower.getPose().getHeading() + " | " + llPose.getHeading());
//        robot.telemetryManager.addData("llRobotPose", Arrays.toString(limelight.getOrientationArrayString()));
        robot.telemetryManager.addData("turret", turret.getDebugInfo());
        robot.telemetryManager.addData("outtakeVel", outtake.getOuttakeVelocity());
        robot.telemetryManager.addData("maxRPMFrac", robot.outtakeMotor.getMotorType().getAchieveableMaxRPMFraction());
//        robot.telemetryManager.debug("x" + " " + robot.follower.getPose().getX() + " | " + llPose.getX());
//        robot.telemetryManager.debug("y" + " " + robot.follower.getPose().getY() + " | " + llPose.getY());
//        robot.telemetryManager.debug("heading" + " " + robot.follower.getPose().getHeading() + " | " + llPose.getHeading());
//        robot.telemetryManager.debug("llRobotPose" + " " + Arrays.toString(limelight.getOrientationArrayString()));
        robot.telemetryManager.debug("turret" + " " + turret.getDebugInfo());
        robot.telemetryManager.debug("outtakeVel" + " " + outtake.getOuttakeVelocity());
        robot.telemetryManager.debug("maxRPMFrac" + " " + robot.outtakeMotor.getMotorType().getAchieveableMaxRPMFraction());
        robot.telemetryManager.update();
        CommandScheduler.getInstance().run();
        robot.follower.update();
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

//        turret.setDefaultCommand(
//                new RunCommand(() -> {
//                    double angle = turret.getTotalRotationTurret();
//                    double forward = RobotConstants.Limelight.axisForward + Math.cos(Math.toRadians(angle))*RobotConstants.Limelight.rotRadius;
//                    double right = RobotConstants.Limelight.axisRight + Math.sin(Math.toRadians(angle))*RobotConstants.Limelight.rotRadius;
//                    double up = RobotConstants.Limelight.axisUp;
//                    limelight.updateLimelightPose(
//                            forward,
//                            right,
//                            up,
//                            angle,
//                            15,
//                            0
//                    );
////                    turret.lockToAprilTag();
//                }, turret)
//        );

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

        driver2.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).whileHeld(
                new InstantCommand(() -> turret.changeTargetRotation(RobotConstants.Turret.turretSpeed), turret)
        );
        driver2.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).whileHeld(
                new InstantCommand(() -> turret.changeTargetRotation(-RobotConstants.Turret.turretSpeed), turret)
        );

        driver.getGamepadButton(GamepadKeys.Button.DPAD_UP).whenPressed(
                new InstantCommand(
                        () -> {
                            outtake.setOuttakePower(-1);
                        },
                        outtake
                )
        );
        driver.getGamepadButton(GamepadKeys.Button.DPAD_UP).whenReleased(
                new InstantCommand(
                        () -> {
                            outtake.stopOuttakeMotor();
                        },
                        outtake
                )
        );

        driver.getGamepadButton(GamepadKeys.Button.DPAD_DOWN).whenPressed(
                new InstantCommand(
                        () -> {
                            outtake.setFeederPower(-1);
                            intake.setIntakePower(-0.5);
                        },
                        outtake, intake
                )
        );
        driver.getGamepadButton(GamepadKeys.Button.DPAD_DOWN).whenReleased(
                new InstantCommand(
                        () -> {
                            outtake.stopFeederMotor();
                            intake.stopMotor();
                        },
                        outtake, intake
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
                    intake.setIntakePower(curr>0.1 ? 0 : 0.8);
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

        driver.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).whenHeld(
                new Command() {
                    @Override
                    public void execute() {
                        if (driver.getGamepadButton(GamepadKeys.Button.A).get()) {
                            outtake.setFeederPower(0.8);
                        } else {
                            outtake.setFeederPower(1);
                        }
                    }

                    @Override
                    public void end(boolean interrupted) {
                        outtake.stopFeederMotor();
                    }

                    @Override
                    public Set<Subsystem> getRequirements() {
                        return new HashSet<>();
                    }
                }
        );

        driver.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).whenHeld(
                new Command() {
                    @Override
                    public void execute() {
                        if (driver.getGamepadButton(GamepadKeys.Button.A).get()) {
                            outtake.setOuttakeVelocity(RobotConstants.Outtake.outtakeVelocityLong);
                        } else {
                            outtake.setOuttakeVelocity(RobotConstants.Outtake.outtakeVelocityShort);
                        }
                    }

                    @Override
                    public void end(boolean interrupted) {
                        outtake.stopOuttakeMotor();
                    }

                    @Override
                    public Set<Subsystem> getRequirements() {
                        return Set.of(outtake);
                    }
                }
        );

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
}
