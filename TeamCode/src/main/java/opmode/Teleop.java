package opmode;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.RunCommand;
import com.arcrobotics.ftclib.command.StartEndCommand;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import subsystems.MecanumDrive;
import util.RobotHardware;

@TeleOp
public class Teleop extends CommandOpMode {
    private final RobotHardware robot = RobotHardware.getInstance();
    private GamepadEx driver;
    // Maybe I need to set states here???
    private MecanumDrive drivetrain;


    @Override
    public void initialize() {
        CommandScheduler.getInstance().reset();
        driver = new GamepadEx(gamepad1);
        robot.init(hardwareMap, driver);
        // Would add telemetry here

        drivetrain = new MecanumDrive();
    }

    @Override
    public void run() {
        CommandScheduler.getInstance().run();
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
                new InstantCommand(() -> robot.imu.resetYaw())
        );

//        driver.getGamepadButton(GamepadKeys.Button.B).whileHeld(
//                new StartEndCommand(
//                        () -> drivetrain.setSlowMode(true),
//                        () -> drivetrain.setSlowMode(false)
//                )
//        );
        driver.getGamepadButton(GamepadKeys.Button.B).whenPressed(
                new InstantCommand(() -> drivetrain.setSlowMode(!drivetrain.getSlowMode()))
        );

        //        driver.getGamepadButton(GamepadKeys.Button.DPAD_DOWN).whenPressed(
//                new Command() {
//                    DoubleSupplier result;
//
//                    @Override
//                    public void initialize() {
//                        result = robot.drivetrain.driveToPose(
//                                new Pose(1,1,1),
//                                hardwareMap
//                        );
//                    }
//
//                    @Override
//                    public boolean isFinished() {
//                        return result.getAsDouble() == 1.0;
//                    }
//
//                    @Override
//                    public Set<Subsystem> getRequirements() {
//                        return Set.of(robot.drivetrain);
//                    }
//                }, true
//        );
    }
}
