package pedroPathing;

import com.pedropathing.control.FilteredPIDFCoefficients;
import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.ftc.localization.Encoder;
import com.pedropathing.ftc.localization.constants.PinpointConstants;
import com.pedropathing.ftc.localization.constants.TwoWheelConstants;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver.*;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import util.RobotConstants;

public class Constants {
    public static FollowerConstants followerConstants = new FollowerConstants()
            // TODO: Configure robot mass
            .mass(11.34)
            // TODO: Tune these (https://pedropathing.com/docs/pathing/tuning/automatic)
//            // Max robot acceleration in 48 in (Foward/LateralZeroPowerAccelerationTuner)
            .forwardZeroPowerAcceleration(-30.71)
            .lateralZeroPowerAcceleration(-48.15)
            .useSecondaryTranslationalPIDF(true)
            .useSecondaryHeadingPIDF(true)
            .useSecondaryDrivePIDF(true)
            // TODO: Tune these (https://pedropathing.com/docs/pathing/tuning/pids/translational and heading and drive)
            .translationalPIDFCoefficients(new PIDFCoefficients(0.3,0,0.05,0))
            .secondaryTranslationalPIDFCoefficients(new PIDFCoefficients(0.05,0, 0.01,0))
            .headingPIDFCoefficients(new PIDFCoefficients(0.7,0,0.01,0))
            .secondaryHeadingPIDFCoefficients(new PIDFCoefficients(1,0, 0, 0))
            .drivePIDFCoefficients(new FilteredPIDFCoefficients(0.015,0.01, 0.001,0.6,0.01)) //0.015 0.01 0.0001 0.6 0.01
            .secondaryDrivePIDFCoefficients(new FilteredPIDFCoefficients(0.02,0.01, 0.00001,0.6,0.01)) //0.02, 0.01
            .centripetalScaling(0.005);

    public static MecanumConstants driveConstants = new MecanumConstants()
            .maxPower(1)
            .rightFrontMotorName(RobotConstants.Drivetrain.rightFront)
            .rightRearMotorName(RobotConstants.Drivetrain.rightRear)
            .leftRearMotorName(RobotConstants.Drivetrain.leftRear)
            .leftFrontMotorName(RobotConstants.Drivetrain.leftFront)
            // TODO: Check whether these reverses are correct
            .leftFrontMotorDirection(DcMotorSimple.Direction.REVERSE)
            .leftRearMotorDirection(DcMotorSimple.Direction.REVERSE)
            .rightFrontMotorDirection(DcMotorSimple.Direction.FORWARD)
            .rightRearMotorDirection(DcMotorSimple.Direction.FORWARD)
            // TODO: Tune these (https://pedropathing.com/docs/pathing/tuning/automatic)
//            // Max robot velocity in 48 inches (length can be changed in Forward/LateralVelocityTuner but larger numbers are better)
            .xVelocity(60) // 81
            .yVelocity(60); // 71

    public static PinpointConstants localizerConstants = new PinpointConstants()
            // Pod offsets from center
            .forwardPodY(1.5)
            .strafePodX(4.63)
            .distanceUnit(DistanceUnit.INCH)
            .hardwareMapName("pinpoint")
            .encoderResolution(GoBildaOdometryPods.goBILDA_4_BAR_POD)
            // TODO: Tune these (https://pedropathing.com/docs/pathing/tuning/localization/two-wheel)
//            // Encoder directions (reverse if needed)
            .forwardEncoderDirection(EncoderDirection.FORWARD)
            .strafeEncoderDirection(EncoderDirection.FORWARD);
//            // Multipliers (adjust perceived to real units)
//            .forwardTicksToInches(1.0)
//            .strafeTicksToInches(1.0);

    public static PathConstraints pathConstraints = new PathConstraints(0.99, 100, 1, 1);

    public static Follower createFollower(HardwareMap hardwareMap) {
        return new FollowerBuilder(followerConstants, hardwareMap)
                .pinpointLocalizer(localizerConstants)
                .pathConstraints(pathConstraints)
                .mecanumDrivetrain(driveConstants)
                .build();
    }
}