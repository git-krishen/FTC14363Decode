package pedroPathing;

import com.pedropathing.control.FilteredPIDFCoefficients;
import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.ftc.localization.Encoder;
import com.pedropathing.ftc.localization.constants.TwoWheelConstants;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Constants {
    public static FollowerConstants followerConstants = new FollowerConstants()
            // TODO: Configure robot mass
//            .mass(//Put robot mass in kg here)
            // TODO: Tune these (https://pedropathing.com/docs/pathing/tuning/automatic)
//            // Max robot acceleration in 48 in (Foward/LateralZeroPowerAccelerationTuner)
//            .forwardZeroPowerAcceleration(0.0)
//            .lateralZeroPowerAcceleration(0.0)
            .useSecondaryTranslationalPIDF(true)
            .useSecondaryHeadingPIDF(true)
            .useSecondaryDrivePIDF(true);
            // TODO: Tune these (https://pedropathing.com/docs/pathing/tuning/pids/translational and heading and drive)
//            .translationalPIDFCoefficients(new PIDFCoefficients(0.1,0,0.01,0))
//            .secondaryDrivePIDFCoefficients(new FilteredPIDFCoefficients(0.1,0, 0.01,0.6,0.01))
//            .headingPIDFCoefficients(new PIDFCoefficients(0.1,0,0.01,0))
//            .secondaryHeadingPIDFCoefficients(new PIDFCoefficients(0.1,0, 0.01, 0))
//            .drivePIDFCoefficients(new FilteredPIDFCoefficients(0.1,0, 0.01,0.6,0.01))
//            .secondaryDrivePIDFCoefficients(new FilteredPIDFCoefficients(0.1,0, 0.01,0.6,0.01));

    public static MecanumConstants driveConstants = new MecanumConstants()
            .maxPower(1)
            .rightFrontMotorName("frontRightMotor")
            .rightRearMotorName("backRightMotor")
            .leftRearMotorName("backLeftMotor")
            .leftFrontMotorName("frontLeftMotor")
            // TODO: Check whether these reverses are correct
            .leftFrontMotorDirection(DcMotorSimple.Direction.REVERSE)
            .leftRearMotorDirection(DcMotorSimple.Direction.REVERSE)
            .rightFrontMotorDirection(DcMotorSimple.Direction.FORWARD)
            .rightRearMotorDirection(DcMotorSimple.Direction.FORWARD);
            // TODO: Tune these (https://pedropathing.com/docs/pathing/tuning/automatic)
//            // Max robot velocity in 48 inches (length can be changed in Forward/LateralVelocityTuner but larger numbers are better)
//            .xVelocity(1.0)
//            .yVelocity(1.0)

    public static TwoWheelConstants localizerConstants = new TwoWheelConstants()
            // TODO: Change this to be correct odo wheels
            .forwardEncoder_HardwareMapName("leftFront")
            .strafeEncoder_HardwareMapName("rightRear")
            .IMU_HardwareMapName("imu")
            // TODO: Change this to be correct RevHub Orientation
            .IMU_Orientation(
                    new RevHubOrientationOnRobot(
                            RevHubOrientationOnRobot.LogoFacingDirection.UP,
                            RevHubOrientationOnRobot.UsbFacingDirection.LEFT
                    )
            );
            // TODO: Tune these (https://pedropathing.com/docs/pathing/tuning/localization/two-wheel)
//            // Pod offsets from center
//            .forwardPodY(0.0)
//            .strafePodX(0.0)
//            // Encoder directions (reverse if needed)
//            .forwardEncoderDirection(Encoder.REVERSE)
//            .strafeEncoderDirection(Encoder.FORWARD)
//            // Multipliers (adjust perceived to real units)
//            .forwardTicksToInches(1.0)
//            .strafeTicksToInches(1.0);

    public static PathConstraints pathConstraints = new PathConstraints(0.99, 100, 1, 1);

    public static Follower createFollower(HardwareMap hardwareMap) {
        return new FollowerBuilder(followerConstants, hardwareMap)
                .twoWheelLocalizer(localizerConstants)
                .pathConstraints(pathConstraints)
                .mecanumDrivetrain(driveConstants)
                .build();
    }
}