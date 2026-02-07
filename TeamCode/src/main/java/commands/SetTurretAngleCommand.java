package commands;

import com.arcrobotics.ftclib.command.InstantCommand;
import subsystems.Turret;
import util.RobotConstants;

/**
 * Command to set the turret to a specific angle.
 * Useful for pre-aiming the turret before arriving at a shooting position.
 */
public class SetTurretAngleCommand extends InstantCommand {

    /**
     * Set the turret to a specific angle in degrees.
     *
     * @param turret the turret subsystem
     * @param degrees the target angle in turret degrees
     */
    public SetTurretAngleCommand(Turret turret, double degrees) {
        super(() -> turret.setTargetRotationTurret(degrees), turret);
    }

    /**
     * Pre-aim the turret to the goal as if the robot were at the specified position.
     *
     * @param turret the turret subsystem
     * @param robotX hypothetical robot X position (inches)
     * @param robotY hypothetical robot Y position (inches)
     * @param robotHeadingDeg hypothetical robot heading (degrees)
     */
    public SetTurretAngleCommand(Turret turret, double robotX, double robotY, double robotHeadingDeg) {
        super(() -> {
            double x = robotX;
            double y = robotY;
            double botHeading = robotHeadingDeg;
            x += RobotConstants.Turret.turretOffsetX*Math.cos(botHeading) - RobotConstants.Turret.turretOffsetY*Math.sin(botHeading);
            y += RobotConstants.Turret.turretOffsetX*Math.sin(botHeading) + RobotConstants.Turret.turretOffsetY*Math.cos(botHeading);
            double reqAngle = Math.atan2(RobotConstants.Turret.scoreRedY-y,RobotConstants.Turret.scoreRedY-x);
            double delta = reqAngle - botHeading;
            delta -= Math.PI/2;
            double angle = Math.toDegrees(Math.atan2(Math.sin(delta),Math.cos(delta)));
            angle += Math.PI/2;
            turret.setTargetRotationTurret(angle);
        }, turret);
    }
}