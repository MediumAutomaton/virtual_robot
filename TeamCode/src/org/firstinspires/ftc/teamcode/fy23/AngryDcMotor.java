package org.firstinspires.ftc.teamcode.fy23;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorImpl;
import com.qualcomm.robotcore.hardware.configuration.MotorType;

public class AngryDcMotor {
    DcMotor motor;
    double offset;

    public AngryDcMotor(DcMotor argMotor, double argOffset) {
        motor = argMotor;
        offset = argOffset;
    }

    public void setPower(double power) {
        motor.setPower((power + offset));
    }

    public double getPower() {
        return motor.getPower();
    }
}
