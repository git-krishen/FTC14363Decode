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
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.Collections;
import java.util.Set;
import java.util.function.DoubleSupplier;

import subsystems.ColorSensorSubsystem;
import subsystems.Limelight;
import subsystems.MecanumDrive;
import util.RobotHardware;

@TeleOp
public class Teleop extends CommandOpMode {
    private final RobotHardware robot = RobotHardware.getInstance();
    private GamepadEx driver;
    // Maybe I need to set states here???

    private final Limelight limelight = new Limelight();
    private final ColorSensorSubsystem colorSensor = new ColorSensorSubsystem();


    @Override
    public void initialize() {
        CommandScheduler.getInstance().reset();
        driver = new GamepadEx(gamepad1);
        robot.init(hardwareMap, driver);
        limelight.register();
        // Would add telemetry here
    }

    @Override
    public void run() {
        CommandScheduler.getInstance().run();
        robot.drivetrain.setDefaultCommand(new RunCommand(() -> {
            System.out.println(robot.drivetrain.getCurrentPose());
            double ly = Math.abs(driver.getLeftY()) > 0.15 ? driver.getLeftY() : 0;
            double lx = Math.abs(driver.getLeftX()) > 0.15 ? driver.getLeftX() : 0;
            double rx = Math.abs(driver.getRightX()) > 0.15 ? driver.getRightX() : 0;
            robot.drivetrain.drive(
                    ly,
                    lx,
                    rx
            );
        }, robot.drivetrain));

        driver.getGamepadButton(GamepadKeys.Button.A).whenPressed(
                new InstantCommand(() -> robot.imu.resetYaw())
        );

        driver.getGamepadButton(GamepadKeys.Button.B).whileHeld(
                new StartEndCommand(
                        () -> robot.drivetrain.setSlowMode(true),
                        () -> robot.drivetrain.setSlowMode(false)
                )
        );

        driver.getGamepadButton(GamepadKeys.Button.DPAD_DOWN).whenPressed(
                new Command() {
                    DoubleSupplier result;

                    @Override
                    public void initialize() {
                        result = robot.drivetrain.driveToPose(
                                new Pose(1,1,1),
                                hardwareMap
                        );
                    }

                    @Override
                    public boolean isFinished() {
                        return result.getAsDouble() == 1.0;
                    }

                    @Override
                    public Set<Subsystem> getRequirements() {
                        return Set.of(robot.drivetrain);
                    }
                }, true
        );

    }
}
