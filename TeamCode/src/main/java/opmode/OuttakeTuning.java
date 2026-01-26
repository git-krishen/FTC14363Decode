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
import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

import java.util.Arrays;
import java.util.Set;

import subsystems.Intake;
import subsystems.Limelight;
import subsystems.MecanumDrive;
import subsystems.Outtake;
import subsystems.Turret;
import util.RobotConstants;
import util.RobotHardware;

@Configurable
@TeleOp(name = "OuttakeTuning", group = "Tuning")
public class OuttakeTuning extends CommandOpMode {
    private final RobotHardware robot = RobotHardware.getInstance();
    private GamepadEx driver;
    private GamepadEx driver2;
    private MecanumDrive drivetrain;
    private Intake intake;
    private Outtake outtake;
    private Turret turret;
    public static double targetPower = 1.0;
    // Maybe I need to set states here???


    @Override
    public void initialize() {
        CommandScheduler.getInstance().reset();
        driver = new GamepadEx(gamepad1);
        driver2 = new GamepadEx(gamepad2);
        robot.init(hardwareMap, driver);

        drivetrain = new MecanumDrive();
        intake = new Intake();
        outtake = new Outtake();
        turret = new Turret();
        // Would add telemetry here

        configureBindings();
    }

    @Override
    public void run() {
        CommandScheduler.getInstance().run();
        robot.follower.update();
        robot.telemetryManager.addData("power", outtake.getOuttakePower());
        robot.telemetryManager.addData("velocity", outtake.getOuttakeVelocity());
        robot.telemetryManager.update();
    }

    private void configureBindings() {
        outtake.setDefaultCommand(
                new RunCommand(() -> outtake.setOuttakePower(targetPower))
        );
    }
}
