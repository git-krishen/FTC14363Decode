package commands;

import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.command.RunCommand;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import subsystems.Feeder;
import util.RobotConstants;
import util.RobotHardware;

public class RunFeederCommand extends CommandBase {
    private Feeder feeder;
    public RunFeederCommand(Feeder feeder) {
        this.feeder = feeder;
    }

    @Override
    public void initialize() {
        super.initialize();
    }

    @Override
    public void execute() {
        feeder.setFeederPower(1);
    }

    @Override
    public boolean isFinished() {
        return RobotHardware.getInstance().outtakeDistanceSensor.getDistance(DistanceUnit.MM) > RobotConstants.Outtake.outtakeSensorThreshold;
    }

    @Override
    public void end(boolean interrupted) {
        feeder.stopMotor();
    }
}
