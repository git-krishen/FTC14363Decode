package util;

import com.arcrobotics.ftclib.controller.PIDController;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.Servo;

import com.arcrobotics.ftclib.controller.PIDFController;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.IMU;

import subsystems.MecanumDrive;

public class RobotHardware {
    // Drivetrain
    public DcMotorEx leftFront, leftRear, rightFront, rightRear;
    public IMU imu;

    // Hardware
    private HardwareMap hardwareMap;
    private static RobotHardware instance = null;
    private boolean enabled;

    public GamepadEx driver;

    // Limelight
    public Limelight3A limelight;

    // Color Sensor
//    public ColorSensor colorSensor;

    // Subsystems
    public MecanumDrive drivetrain;

    public TelemetryManager telemetryManager;

    public static RobotHardware getInstance() {
        if (instance == null) {
            instance = new RobotHardware();
        }
        instance.enabled = true;
        return instance;
    }

    public void init(final HardwareMap hardwareMap, GamepadEx driver) {
        this.driver = driver;
        init(hardwareMap);
    }

    public void init(final HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;

        this.telemetryManager = PanelsTelemetry.INSTANCE.getTelemetry();

        // ******************* DRIVETRAIN ******************* //
        leftFront = hardwareMap.get(DcMotorEx.class, RobotConstants.Drivetrain.leftFront);
        leftRear = hardwareMap.get(DcMotorEx.class, RobotConstants.Drivetrain.leftRear);
        rightRear = hardwareMap.get(DcMotorEx.class, RobotConstants.Drivetrain.rightRear);
        rightFront = hardwareMap.get(DcMotorEx.class, RobotConstants.Drivetrain.rightFront);

        leftFront.setDirection(DcMotorEx.Direction.REVERSE); // MAYBE CHANGE
        leftRear.setDirection(DcMotorEx.Direction.REVERSE); // MAYBE CHANGE

        imu = hardwareMap.get(IMU.class, "imu");
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.LEFT, //
                RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD
        ));
        imu.initialize(parameters);
        imu.resetYaw();

        // ******************* LIMELIGHT ******************* //
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.setPollRateHz(100);
        limelight.pipelineSwitch(0);
        limelight.start();

        // ******************* COLOR SENSOR ******************* //
//        colorSensor = hardwareMap.get(ColorSensor.class, "colorSensor");
    }


}