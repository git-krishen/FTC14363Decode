package commands;

import com.arcrobotics.ftclib.command.InstantCommand;

import subsystems.Outtake;

public class OuttakeStopCommand extends InstantCommand {
    public OuttakeStopCommand(Outtake outtake) {
        super(outtake::stopOuttakeMotor, outtake);
    }
}
