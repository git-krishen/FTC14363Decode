package util;

import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;

import pedroPathing.Constants;
import subsystems.Limelight;

public class RobotHardware {
    // Drivetrain
    public DcMotorEx leftFront, leftRear, rightFront, rightRear;
//    public IMU imu;
    public Follower follower;

    // Hardware
    private HardwareMap hardwareMap;
    private static RobotHardware instance = null;
    private boolean enabled;

    // Intake
    public DcMotorEx intakeMotor;

    // Outtake
    public DcMotorEx outtakeMotor;
    public DcMotorEx outtakeFollower;
    public DcMotorEx feederMotor;

    public GamepadEx driver;

    // Turret
    public CRServo turretServo;
    public AnalogInput turretEncoder;

    // Limelight
    public Limelight3A limelight;

    // Camera
    public WebcamName webcam;

    // Color Sensor
//    public ColorSensor colorSensor;

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

        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        leftFront.setDirection(DcMotorEx.Direction.REVERSE); // MAYBE CHANGE
//        rightRear.setDirection(DcMotorEx.Direction.REVERSE); // MAYBE CHANGE
        leftRear.setDirection(DcMotorEx.Direction.REVERSE); // MAYBE CHANGE
//        rightFront.setDirection(DcMotorEx.Direction.REVERSE); // MAYBE CHANGE

//        imu = hardwareMap.get(IMU.class, "imu");
////        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
////                RevHubOrientationOnRobot.LogoFacingDirection.UP, //
////                RevHubOrientationOnRobot.UsbFacingDirection.RIGHT
////        ));
//        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
//                RevHubOrientationOnRobot.LogoFacingDirection.RIGHT, //
//                RevHubOrientationOnRobot.UsbFacingDirection.UP
//        ));
//
//        imu.initialize(parameters);
//        imu.resetYaw();

        follower = Constants.createFollower(hardwareMap);
        follower.activateAllPIDFs();

        // ******************* INTAKE ******************* //
        intakeMotor = hardwareMap.get(DcMotorEx.class, RobotConstants.Intake.intake);
        intakeMotor.setDirection(DcMotorEx.Direction.FORWARD);

        // ******************* OUTTAKE ******************* //
        outtakeMotor = hardwareMap.get(DcMotorEx.class, RobotConstants.Outtake.outtake);
        outtakeMotor.setDirection(DcMotorEx.Direction.REVERSE);
//        outtakeMotor.setPIDFCoefficients();
        outtakeMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        outtakeMotor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(50,0,0,0));
        outtakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        outtakeFollower = hardwareMap.get(DcMotorEx.class, RobotConstants.Outtake.outtakeFollower);
        outtakeFollower.setMode(outtakeMotor.getMode());
        outtakeFollower.setPIDFCoefficients(outtakeMotor.getMode(), outtakeMotor.getPIDFCoefficients(outtakeMotor.getMode()));
        outtakeFollower.setZeroPowerBehavior(outtakeMotor.getZeroPowerBehavior());
        outtakeFollower.setDirection(outtakeMotor.getDirection().inverted());
        feederMotor = hardwareMap.get(DcMotorEx.class, RobotConstants.Outtake.feeder);
        feederMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // ******************* TURRET ******************* //
        turretServo = hardwareMap.get(CRServo.class, "turretServo");
        turretServo.resetDeviceConfigurationForOpMode();
        turretEncoder = hardwareMap.get(AnalogInput.class, "turretEncoder");

        // ******************* LIMELIGHT ******************* //
        Limelight3A limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.setPollRateHz(100);
        limelight.pipelineSwitch(9);
        limelight.updateRobotOrientation(0);
        limelight.start();
        Limelight.setTargetID(24);
        Limelight.updateLimelightPose(
                RobotConstants.Limelight.axisForward+RobotConstants.Limelight.rotRadius,
                RobotConstants.Limelight.axisRight,
                RobotConstants.Limelight.axisUp,
                180,
                18,
                0
                );

        // ******************* CAMERA ******************* //
//        WebcamName webcam = hardwareMap.get(WebcamName.class, "webcam1");

        // ******************* COLOR SENSOR ******************* //
//        colorSensor = hardwareMap.get(ColorSensor.class, "colorSensor");
    }


}