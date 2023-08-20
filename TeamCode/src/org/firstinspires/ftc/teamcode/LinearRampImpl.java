//Ramp a motor so it takes 1 second to reach 1 power.

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="LinearRampImpl", group="TeleTest")
public class LinearRampImpl extends OpMode {

    ElapsedTime stopwatch = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
    ElapsedTime chronometer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
    DcMotor motor;

    double powerTarget;
    double secondPowerTarget;
    double lastTime = 0;
    double timeDiff;
    double newPower;
    double initialPower;
    double secondInitialPower;
    boolean ranResetPulse = false;

    @Override
    public void init() {
        motor = hardwareMap.get(DcMotor.class, "motor");
        motor.setDirection(DcMotor.Direction.FORWARD);
        powerTarget = 1;
        initialPower = 0;
        telemetry.addData("Motor Power", motor.getPower());
    }

    public void start() {
        chronometer.reset();
        lastTime = 0;
        stopwatch.reset();
    }

    @Override
    public void loop() {
        if (chronometer.milliseconds() < 1200) {
            if (motor.getPower() < powerTarget) {
//                timeDiff = stopwatch.milliseconds() - lastTime;
                newPower = (1 / 1) * stopwatch.seconds() + initialPower;
                //(1 power / 1 second - the line's slope. Think y=mx+b.)
                motor.setPower(newPower);
//                lastTime = stopwatch.milliseconds();
            }
        }

        if (chronometer.milliseconds() > 1950 && !ranResetPulse) {
            initialPower = 1;
            powerTarget = 0;
            lastTime = 0;
            ranResetPulse = true;
            stopwatch.reset();
        }

        if (chronometer.milliseconds() > 2000 && chronometer.milliseconds() < 3200) {
            if (motor.getPower() > powerTarget) {
//                timeDiff = stopwatch.milliseconds() - lastTime;
                newPower = (-1 / 1) * stopwatch.seconds() + initialPower;
                motor.setPower(newPower);
//                lastTime = stopwatch.milliseconds();
            }
        }

        if (chronometer.milliseconds() > 4000) {
            initialPower = 0;
            powerTarget = 1;
            lastTime = 0;
            ranResetPulse = false;
            chronometer.reset();
            stopwatch.reset();
        }

        telemetry.addData("Motor Power", motor.getPower());
        telemetry.addData("Chronometer", chronometer.seconds());
        telemetry.addData("Stopwatch", stopwatch.seconds());
    }
}
