package commands;

import com.arcrobotics.ftclib.command.InstantCommand;

import subsystems.Outtake;

public class OuttakeStartCommand extends InstantCommand {
    public OuttakeStartCommand(Outtake outtake, double vel) {
        super(() -> outtake.setOuttakeVelocity(vel), outtake);
    }
}
