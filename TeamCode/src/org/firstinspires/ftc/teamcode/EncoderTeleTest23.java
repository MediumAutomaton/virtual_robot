//Michael's test program for running a motor to encoder positions
//Now with safety! (nevermind that it's not working yet :( )

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.ArrayList;

@TeleOp(name="EncoderTeleTest23", group="TeleTest")
public class EncoderTeleTest23 extends OpMode {

    //Configure the Safety Check
    ElapsedTime safetyCheckClock;
    int safetyCheckInterval = 100;
    int requiredDistance = 50;

    int currentPos = 0;
    boolean safetyCheck() { //returns true if continued operation is safe, false if unsafe
        int currentMotorPos = motor.getCurrentPosition();
        if ((lastMotorPos > Math.abs(currentMotorPos - requiredDistance)) && (Math.abs(lastMotorPos - motor.getTargetPosition()) > requiredDistance)) {
            //if we haven't moved requiredDistance ticks since last we checked (since the safetyCheckInterval last came around)
            //and we actually have somewhere to go
            return false;
        } else {
            lastMotorPos = currentMotorPos;
            return true;
        }
    }

    //Declare variables first because we have to
    ElapsedTime upDeb;
    ElapsedTime downDeb;
    ElapsedTime otherDeb;
    int upDebTime = 200; //waiting time in milliseconds
    int downDebTime = 200;
    int otherDebTime = 200;

    int targetPosA = 0;//Stage target here - we'll send it to the motor later
    int targetPosB = 0;
    boolean aActive = true;
    boolean bActive = false;

    DcMotor motor;
    double motorPower = 0.4;
    int lastMotorPos = 0;

    Telemetry.Log log = telemetry.log();
    //Get the log. Append a message to the bottom of Telemetry with log.add("msg");
    Telemetry.Item telStagedA;
    Telemetry.Item telStagedB;
    Telemetry.Item telActiveTarget;
    Telemetry.Item telActualPosition;
    Telemetry.Item telActiveMotor;
    Telemetry.Item telRunmode;
    Telemetry.Item telSetPower;
    Telemetry.Item telActualPower;

//    ArrayList<String> motorList = new ArrayList<>(6);
    ArrayList<String> motorList = new ArrayList<>(1);
    int listIdx = 0;

    void initMotor(int idx) {
        String motorString = motorList.get(idx);
        motor = hardwareMap.get(DcMotor.class, motorString);

        //Put the motor into a known configuration
        motor.setDirection(DcMotor.Direction.REVERSE);
        motor.setPower(0);
        motor.setTargetPosition(0);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);//Set our current position as 0
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        log.add("Initialized motor " + motorList.get(idx));
    }

    void changeStagedTarget(int change) {
        if (aActive) {
            targetPosA += change;
        } else {
            targetPosB += change;
        }
    }

    void setStagedTarget(int value) {
        if (aActive) {
            targetPosA = value;
        } else {
            targetPosB = value;
        }
    }

    int getStagedTarget() {
        if (aActive) {
            return targetPosA;
        } else {
            return targetPosB;
        }
    }

    @Override
    public void init() {
        //Initialize Telemetry
        log.setCapacity(15);
        telemetry.setAutoClear(false);

        //Add entries to motorList
//        motorList.add("leftFront");
//        motorList.add("leftBack");
//        motorList.add("rightFront");
//        motorList.add("rightBack");
//        motorList.add("armPivot");
//        motorList.add("armExtend");
        motorList.add("motor");

        //List the contents of motorList (motors available to test) in the log
        log.add("motorList contains the following:");
        for (int i=0; i<motorList.size(); i++) { //first int i=0, then while i<6, loop then i++
            log.add(motorList.get(i));
        }

        //Initialize the first motor
        initMotor(listIdx); //listIdx should be 0 at this time

        //Populate our variables for ElapsedTimes
        upDeb = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        downDeb = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        otherDeb = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        safetyCheckClock = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);

        //Send initial telemetry
        telStagedA = telemetry.addData("->>>Staged Target A", targetPosA);
        //telStagedA is now a Telemetry.Item that we can call functions on!
        //Hover over the text "Telemetry.Item" in Studio.
        //At the bottom of the resulting dialog should be "'Item in Telemetry' on localhost
        //Click this to see these functions in your browser.
        telStagedB = telemetry.addData("----Staged Target B", targetPosB);
        telActiveTarget = telemetry.addData("Active Target", motor.getTargetPosition());
        telActualPosition = telemetry.addData("Actual Position", motor.getCurrentPosition());
        telActiveMotor = telemetry.addData("Active Motor", motorList.get(listIdx));
        telRunmode = telemetry.addData("Motor runmode", motor.getMode());
        telSetPower = telemetry.addData("Set Motor Power", motorPower);
        telActualPower = telemetry.addData("Actual Motor power", motor.getPower());
        telemetry.addLine("------------------------------------------");
        telemetry.addLine("D-Pad: Up/Down 10, Left/Right 100");
        telemetry.addLine("Bumpers: 1000");
        telemetry.addLine("A/B set Active Target to respective Staged Target");
        telemetry.addLine("X stops motor");
        telemetry.addLine("Y sets current position as 0");
        telemetry.addLine("Right trigger toggles selected Staged Target");
        telemetry.addLine("Left trigger sets selected Staged Target to current position");
        telemetry.addLine("Start/Back cycle through motors");
        telemetry.addLine("Left/Right stick click change motor power");
        telemetry.addLine("Extra stuff on Gamepad 2:");
        telemetry.addLine("A sets RUN_WITHOUT_ENCODER");
        telemetry.addLine("B sets RUN_TO_POSITION");
        telemetry.addLine("Triggers do analog control when in RUN_WITHOUT_ENCODER");
        telemetry.addLine("------------------------------------------");
        //Log will be displayed here

        log.add("Initialized");
    }

    @Override
    public void loop() {
        if (aActive) {
//            telemetry.addData("->>>Staged Target A", targetPosA);
            telStagedA.setCaption("->>>Staged Target A");
        } else {
//            telemetry.addData("----Staged Target A", targetPosA);
            telStagedA.setCaption("----Staged Target A");
        }
        if (bActive) {
//            telemetry.addData("->>>Staged Target B", targetPosB);
            telStagedB.setCaption("->>>Staged Target B");
        } else {
//            telemetry.addData("----Staged Target B", targetPosB);
            telStagedB.setCaption("----Staged Target B");
        }
        telStagedA.setValue(targetPosA);
        telStagedB.setValue(targetPosB);
        telActiveTarget.setValue(motor.getTargetPosition());
        telActualPosition.setValue(motor.getCurrentPosition());
        telRunmode.setValue(motor.getMode());
        telSetPower.setValue(motorPower);
        telActualPower.setValue(motor.getPower());

        //Safety Check
        if (safetyCheckClock.milliseconds() > safetyCheckInterval) {
            safetyCheckClock.reset();
//            if (!safetyCheck()) {
            if (false) { //disabled feature :(
                motor.setPower(0);
                motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                telemetry.clearAll();
                telemetry.addLine("||||||||||||||||||||||");
                telemetry.addLine("||| Safety Lockout |||");
                telemetry.addLine("||||||||||||||||||||||");
                telemetry.update();
//                while (true) {} //Yes, this is intentional.
            }
        }

        //Change Staged Target
        if (gamepad1.right_trigger > 0.5 && otherDeb.milliseconds() > otherDebTime) {
            aActive = !aActive;
            bActive = !bActive;
            otherDeb.reset();
        }

        //Set Staged Target to Current Position
        else if (gamepad1.left_trigger > 0.5 && otherDeb.milliseconds() > otherDebTime) {
            setStagedTarget(motor.getCurrentPosition());
            otherDeb.reset();
        }

        //Motor power adjustment
        else if (gamepad1.right_stick_button && motor.getPower() < 1.0 && otherDeb.milliseconds() > otherDebTime) {
            motorPower = (motorPower+0.1);
            otherDeb.reset();
        } else if (gamepad1.left_stick_button && motor.getPower() > 0 && otherDeb.milliseconds() > otherDebTime) {
            motorPower = (motorPower-0.1);
            otherDeb.reset();
        }

        //Cycle selected motor
        else if (gamepad1.start && otherDeb.milliseconds() > otherDebTime) {
            if (listIdx == (motorList.size() - 1)) {
                listIdx = 0;
            } else {
                listIdx += 1;
            }
            initMotor(listIdx);
            otherDeb.reset();
        } else if (gamepad1.back && otherDeb.milliseconds() > otherDebTime) {
            if (listIdx == 0) {
                listIdx = (motorList.size() - 1);
            } else {
                listIdx -= 1;
            }
            initMotor(listIdx);
            otherDeb.reset();
        }

        //10-tick adjustment
        else if (gamepad1.dpad_up && upDeb.milliseconds() > upDebTime) {
            changeStagedTarget(10);
            upDeb.reset();
        } else if (gamepad1.dpad_down && downDeb.milliseconds() > downDebTime) {
            changeStagedTarget(-10);
            downDeb.reset();

            //100-tick adjustment
        } else if (gamepad1.dpad_right && upDeb.milliseconds() > upDebTime) {
            changeStagedTarget(100);
            upDeb.reset();
        } else if (gamepad1.dpad_left && downDeb.milliseconds() > downDebTime) {
            changeStagedTarget(-100);
            downDeb.reset();

            //1000-tick adjustment
        } else if (gamepad1.right_bumper && upDeb.milliseconds() > upDebTime) {
            changeStagedTarget(1000);
            upDeb.reset();
        } else if (gamepad1.left_bumper && downDeb.milliseconds() > downDebTime) {
            changeStagedTarget(-1000);
            downDeb.reset();
        }

        //Set the Active Target to Staged Target A
        else if (gamepad1.a && otherDeb.milliseconds() > otherDebTime) {
            motor.setPower(motorPower);
            motor.setTargetPosition(targetPosA);
            otherDeb.reset();

            //Set the Active Target to Staged Target B
        } else if (gamepad1.b && otherDeb.milliseconds() > otherDebTime) {
            motor.setPower(motorPower);
            motor.setTargetPosition(targetPosB);
            otherDeb.reset();

            //Stop Motor
        } else if (gamepad1.x && otherDeb.milliseconds() > otherDebTime) {
            motor.setPower(0);
            otherDeb.reset();

            //Reset encoder position (set current position as 0)
        } else if (gamepad1.y && otherDeb.milliseconds() > otherDebTime) {
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motor.setTargetPosition(0);
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            otherDeb.reset();

            //Bonus feature: analog control with gamepad2
        } else if (gamepad2.a && otherDeb.milliseconds() > otherDebTime) {
            motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            otherDeb.reset();
        } else if (gamepad2.b && otherDeb.milliseconds() > otherDebTime) {
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            otherDeb.reset();
        }

        if (motor.getMode() == DcMotor.RunMode.RUN_WITHOUT_ENCODER) {
            motor.setPower(gamepad2.right_trigger - gamepad2.left_trigger);
        }

    }
}
