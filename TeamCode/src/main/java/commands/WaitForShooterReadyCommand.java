package commands;

import com.arcrobotics.ftclib.command.CommandBase;
import com.qualcomm.robotcore.util.ElapsedTime;

import subsystems.Outtake;

/**
 * Waits for shooter flywheel to reach target velocity.
 * Completes when velocity is within tolerance OR timeout expires.
 */
public class WaitForShooterReadyCommand extends CommandBase {
    private final Outtake outtake;
    private final double timeout;
    private ElapsedTime timer;

    public WaitForShooterReadyCommand(Outtake outtake, double timeoutSeconds) {
        this.outtake = outtake;
        this.timeout = timeoutSeconds;
        // Note: No addRequirements - we're just observing, not controlling
    }

    @Override
    public void initialize() {
        timer = new ElapsedTime();
    }

    @Override
    public void execute() {
        // Nothing to do - just waiting lmao
    }

    @Override
    public boolean isFinished() {
        return outtake.atTargetSpeed() || timer.seconds() >= timeout;
    }

    @Override
    public void end(boolean interrupted) {
        // Nothing to clean up
    }
}