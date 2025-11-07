package pedroPathing;

import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import subsystems.MecanumDrive;

@Autonomous(name = "TimeBased")
public class TimeBasedAuto extends OpMode {
    private com.pedropathing.util.Timer timer;
    private MecanumDrive drivetrain;

    @Override
    public void init() {
        drivetrain = new MecanumDrive();
        timer = new Timer();
        timer.resetTimer();
    }

    @Override
    public void loop() {
        if (timer.getElapsedTime() <= 500) {
            drivetrain.drive(1,0,0);
        }
    }
}
