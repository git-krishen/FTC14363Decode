package commands;

import com.arcrobotics.ftclib.command.CommandBase;
import com.qualcomm.robotcore.util.ElapsedTime;

import subsystems.Feeder;
import subsystems.Intake;
import subsystems.Outtake;
import util.RobotHardware;

public class ShootCommand extends CommandBase {
    private final Outtake outtake;
    private final Feeder feeder;
    private final Intake intake;
    private final double targetVel;
    private final double timeout;
    private ElapsedTime timer;

    public ShootCommand(Outtake outtake, Feeder feeder, Intake intake, double targetVel, double timeOutSeconds) {
        this.outtake = outtake;
        this.feeder = feeder;
        this.intake = intake;
        this.targetVel = targetVel;
        this.timeout = timeOutSeconds;
        this.addRequirements(outtake, feeder, intake);
    }

    @Override
    public void initialize() {
        timer = new ElapsedTime();
    }

    @Override
    public void execute() {
        outtake.setOuttakeVelocity(targetVel);
        if (outtake.atTargetSpeed() || timer.seconds() >= timeout-500) {
            feeder.setFeederPower(1);
//            intake.setIntakePower(1);
        }
    }

    @Override
    public boolean isFinished() {
        return timer.seconds() >= timeout;
    }

    @Override
    public void end(boolean interrupted) {
//        outtake.setOuttakeVelocity(0);
//        intake.stopMotor();
    }
}
