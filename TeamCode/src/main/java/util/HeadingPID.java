package util;

import com.arcrobotics.ftclib.controller.wpilibcontroller.ProfiledPIDController;
import com.arcrobotics.ftclib.trajectory.TrapezoidProfile;

public class HeadingPID {
    private double p;
    private double i;
    private double d;
    private ProfiledPIDController pid;

    public HeadingPID(double p, double i, double d) {
        this.p = p;
        this.i = i;
        this.d = d;
        pid = new ProfiledPIDController(p,i,d,new TrapezoidProfile.Constraints(60,60));
    }

    public HeadingPID(double p, double i, double d, double maxVel, double maxAccel) {
        this.p = p;
        this.i = i;
        this.d = d;
        pid = new ProfiledPIDController(p,i,d,new TrapezoidProfile.Constraints(maxVel,maxAccel));
    }

    public ProfiledPIDController getPid() {
        return pid;
    }

    public double getGoal() {
        return pid.getGoal().position;
    }

    public void setGoal(double goalRadians) {
        goalRadians = angleWrap(goalRadians);
        pid.setGoal(goalRadians);
    }

    public boolean atGoal() {
        return pid.atGoal();
    }

    public double calculate(double currRadians) {
        currRadians = angleWrap(currRadians);
        return pid.calculate(currRadians);
    }

    public double calculate(double currRadians, double goalRadians) {
        currRadians = angleWrap(currRadians);
        goalRadians = angleWrap(goalRadians);
        return pid.calculate(currRadians, goalRadians);
    }

    public double angleWrap(double radians) {
        while (radians > Math.PI) {
            radians -= 2 * Math.PI;
        }
        while (radians < -Math.PI) {
            radians += 2 * Math.PI;
        }
        return radians;
    }
}
