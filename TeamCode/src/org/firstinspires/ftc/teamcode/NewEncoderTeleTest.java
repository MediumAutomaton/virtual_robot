//Comprehensive motor test suite with multiple safety checks.

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.ArrayList;

@TeleOp(name="EncoderTeleTest Enhanced Edition", group="TeleTest")
public class NewEncoderTeleTest extends OpMode {

    //Configuration values

    final String motorNameInConfig = "motor";
    final String reportedVersion = "2.0.0 (dev A), 8-19-23";

    final int upperLimit = 1000; //in encoder ticks
    final boolean obeyUpperLimit = false;

    final int lowerLimit = 0;
    final boolean obeyLowerLimit = false;

    //Power to run at in modes other than RUN_WITHOUT_ENCODER
    final double defaultPower = 0.2;

    //What the motor should do when no power is applied
    //Only useful in RUN_WITHOUT_ENCODER mode (press B to enter this mode)
    //https://javadoc.io/static/org.firstinspires.ftc/RobotCore/8.2.0/com/qualcomm/robotcore/hardware/DcMotor.ZeroPowerBehavior.html
    final DcMotor.ZeroPowerBehavior zeroPowerAction = DcMotor.ZeroPowerBehavior.FLOAT;

    final DcMotor.RunMode initialRunMode = DcMotor.RunMode.RUN_WITHOUT_ENCODER;
    final DcMotor.Direction motorDirection = DcMotor.Direction.FORWARD;

    //Which gamepad to respond to
    final Gamepad controller = gamepad1;

    //END Configuration


    final ArrayList<DcMotor.RunMode> supportedRunModes = new ArrayList();

    int stagedTarget = 0;
    int currentModeIndex = 0;

    ElapsedTime buttonTimer = new ElapsedTime();

    DcMotor motor;

    @Override
    public void init() {
        supportedRunModes.add(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        supportedRunModes.add(DcMotor.RunMode.RUN_TO_POSITION);
        supportedRunModes.add(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        supportedRunModes.add(DcMotor.RunMode.RUN_USING_ENCODER);

        motor = hardwareMap.get(DcMotor.class, motorNameInConfig);
        motor.setMode(initialRunMode);
        motor.setDirection(motorDirection);
        motor.setTargetPosition(motor.getCurrentPosition());

        telemetry.addData("Size", supportedRunModes.size());
        telemetry.addData("D-Pad up/down", "Change Staged Target by 10").setRetained(true);
        //telemetry.addData() returns a Telemetry.Item which has a .setRetained() function.
        //This way, it will not be cleared automatically on an update.
        telemetry.addData("D-Pad left/right", "Change Staged Target by 100").setRetained(true);
        telemetry.addData("Bumpers", "Change Staged Target by 1000").setRetained(true);
        telemetry.addData("A", "Apply Staged Target").setRetained(true);
        telemetry.addData("B", "Stop Motor").setRetained(true);
//        telemetry.addData("X", "Next Ramping Filter").setRetained(true);
        telemetry.addData("Y", "Next RunMode").setRetained(true);
        telemetry.addData("Back", "Set Staged Target to 0");
        telemetry.addData("Start", "Set Staged Target to Current Position");
        telemetry.addData("Right/Left Trigger", "Analog control of motor. Only useful in RUN_WITHOUT_ENCODER").setRetained(true);
        telemetry.addData("-", "----------------------------------------------------").setRetained(true);
    }

    void checkLimits() {
        if (obeyUpperLimit) {
            if (motor.getCurrentPosition() > upperLimit) {
                stagedTarget = upperLimit;
                motor.setTargetPosition(upperLimit);
            }
        }
        if (obeyLowerLimit) {
            if (motor.getCurrentPosition() < lowerLimit) {
                stagedTarget = lowerLimit;
                motor.setTargetPosition(lowerLimit);
            }
        }
    }

    DcMotor.RunMode getNextRunMode() {
        if (currentModeIndex > (supportedRunModes.size() - 2)) { //One for offset, one for > instead of ==
            currentModeIndex = 0;
        } else {
            currentModeIndex += 1;
        }
        return supportedRunModes.get(currentModeIndex);
    }

    void handleControls() {
        //Change the Staged Target
        if (controller.dpad_up) stagedTarget += 10;
        if (controller.dpad_down) stagedTarget -= 10;

        if (controller.dpad_right) stagedTarget += 100;
        if (controller.dpad_left) stagedTarget -= 100;

        if (controller.right_bumper) stagedTarget += 1000;
        if (controller.left_bumper) stagedTarget -= 1000;

        if (controller.back) stagedTarget = 0;
        if (controller.start) stagedTarget = motor.getCurrentPosition();

        if (controller.y) {
            motor.setMode(getNextRunMode());
            if (motor.getMode() == DcMotor.RunMode.RUN_TO_POSITION) {
                motor.setTargetPosition(motor.getCurrentPosition());
            }
        }

        //TODO: Change the Staged Filter
    }

    void updateTelemetry() {
        telemetry.addData("RunMode", motor.getMode());
        telemetry.addData("Staged Target", stagedTarget);
        telemetry.addData("Encoder Reading", motor.getCurrentPosition());
        telemetry.addData("Power", motor.getPower());
        telemetry.addData("Right Trigger", controller.right_trigger);
        telemetry.addData("Left Trigger", controller.left_trigger);
    }

    @Override
    public void loop() {
        checkLimits();

        if (motor.getMode() == DcMotor.RunMode.RUN_WITHOUT_ENCODER) {
            motor.setPower(controller.right_trigger - controller.left_trigger);
        } else {
            motor.setPower(defaultPower);
        }

        if (buttonTimer.milliseconds() > 100) {
            handleControls();
            buttonTimer.reset();
        }

        updateTelemetry();
    }
}
