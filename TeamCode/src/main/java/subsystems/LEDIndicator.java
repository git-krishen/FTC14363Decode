package subsystems;

import static util.RobotConstants.Outtake.outtakeVelocityLong;
import static util.RobotConstants.Outtake.outtakeVelocityShort;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.LED;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

import subsystems.Outtake;

public class LEDIndicator {
    private Servo ledServo;
    private static final String LED_NAME = "robot_led";
    private boolean isOn;
    Outtake outtake = new Outtake();

    public void init(HardwareMap hwMap) {
        ledServo = hwMap.get(Servo.class, LED_NAME);
        isOn = false;
    }
    public void setBrightness(double position) { //Brightness between 1.0 and 0.0
        ledServo.setPosition(position);
    }
    public void turnOff() {
        isOn = false;
        setBrightness(0.0);
    }
    public void turnOn() {
        setBrightness(1.0);
        isOn = true;
    }
    public void setColorRed() {
        setBrightness(0.700);
    }

    public void setColorGreen() {
        setBrightness(0.5);
    }
}
