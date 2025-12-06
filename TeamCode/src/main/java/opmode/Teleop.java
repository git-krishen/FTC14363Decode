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
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.Set;

import subsystems.Intake;
import subsystems.MecanumDrive;
import subsystems.Outtake;
import util.RobotConstants;
import util.RobotHardware;

@TeleOp
public class Teleop extends CommandOpMode {
    private final RobotHardware robot = RobotHardware.getInstance();
    private GamepadEx driver;
    private MecanumDrive drivetrain;
    private Intake intake;
    private Outtake outtake;
    // Maybe I need to set states here???


    @Override
    public void initialize() {
        CommandScheduler.getInstance().reset();
        driver = new GamepadEx(gamepad1);
        robot.init(hardwareMap, driver);

        drivetrain = new MecanumDrive();
        intake = new Intake();
        outtake = new Outtake();
        // Would add telemetry here

        configureBindings();
    }

    private void configureBindings() {
        robot.telemetryManager.debug(String.format("%f %f %f", driver.getLeftY(), driver.getLeftX(), driver.getRightX()));
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

        driver.getGamepadButton(GamepadKeys.Button.START).whenPressed(
                new InstantCommand(() -> {
                    robot.imu.resetYaw();

                })
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
                    intake.setIntakeMotorVelocity(curr>0.1 ? 0 : RobotConstants.Intake.intakeVelocity);
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
                new InstantCommand(() -> outtake.setFeederVelocity(RobotConstants.Outtake.feederVelocity))
        );
        driver.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).whenHeld(
                new StartEndCommand(
                        () -> outtake.setFeederVelocity(RobotConstants.Outtake.feederVelocity),
                        () -> outtake.stopFeederMotor(),
                        intake
                )
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
