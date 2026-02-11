package subsystems;

import static util.RobotConstants.Outtake.outtakeVelocityLong;
import static util.RobotConstants.Outtake.outtakeVelocityShort;

import com.arcrobotics.ftclib.command.Subsystem;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.LED;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

import subsystems.Outtake;
import util.RobotHardware;

//public class LEDIndicator implements Subsystem{
//    RobotHardware robot;
//    private boolean isOn;


//    public LEDIndicator() {
  //      robot = RobotHardware.getInstance();
//        isOn = false;
 //   }

//    public void setBrightness(double position) {
//        //Brightness between 1.0 and 0.0
//        robot.led.setPosition(position);
//    }
//}