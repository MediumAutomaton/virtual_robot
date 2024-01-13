package org.firstinspires.ftc.teamcode.fy23;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.ArrayList;

@TeleOp(name="Encoder Timer", group="TeleTest")
public class EncoderTimer extends OpMode {
    DcMotor motor;

    Telemetry.Log log = telemetry.log();

//    ArrayList<String> motorList = new ArrayList<>(6);
    ArrayList<String> motorList = new ArrayList<>(1);
    int listIdx = 0;

    ArrayList<Double> timesList = new ArrayList<>();

    void initMotor(int idx) {
        String motorString = motorList.get(idx);
        motor = hardwareMap.get(DcMotor.class, motorString);

        //Put the motor into a known configuration
        motor.setDirection(DcMotor.Direction.REVERSE);
        motor.setPower(0);
        motor.setTargetPosition(0);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);//Set our current position as 0
//        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        log.add("Initialized motor " + motorList.get(idx));
    }

    int trials = 0;
    int trialQty = 10;
    boolean trialRunning = false;
    double trialTime;

    double runningTotal;
    double peak;
    double item;

    int lastEnc;
    int currentEnc;

    ElapsedTime trialMeasure = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);

    ElapsedTime otherDeb = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
    int otherDebTime = 500;

    @Override
    public void init() {
        log.setCapacity(30);

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
    }

    @Override
    public void loop() {
        telemetry.addData("Trial Running", trialRunning);
        telemetry.addData("Motor Power", motor.getPower());
//        telemetry.addData("Current trial", trials);
        telemetry.addData("Current Position", motor.getCurrentPosition());
        telemetry.addData("Trials to run", trialQty);
        telemetry.addLine("Press start/back to cycle through motors");
        telemetry.addLine("Hold down B to make sure the motor spins");
        telemetry.addLine("Use the bumpers to set the number of trials");
        telemetry.addLine("Press A to begin trial on selected motor");
        telemetry.addLine("||| Motor will spin while trial is running! |||");
        telemetry.addLine("View the following after running some trials:");
        telemetry.addData("Average time between changes", runningTotal);
        telemetry.addData("Peak time between changes", peak);

        if (trialRunning) {
            if (trials < trialQty) {
//            if (true) {
//                log.add("Hi!");
                currentEnc = motor.getCurrentPosition();
                if (currentEnc != lastEnc) {
                    trialTime = trialMeasure.milliseconds();
                    log.add("Trial " + trials + ": New reading is previous + " + (currentEnc-lastEnc) + " ticks.");
                    log.add("Changed after " + trialTime + " milliseconds.");
                    timesList.add(trialTime);
                    trialMeasure.reset();
                    trials++;
                }
            } else {
                motor.setPower(0);
                trialRunning = false;
                log.add("Trial completed.");
                runningTotal = 0;
                peak = 0;
                for (int i=0; i<timesList.size(); i++) {
                    item = timesList.get(i);
                    runningTotal += item;
                    if (item > peak) {
                        peak = item;
                    }
                }
                log.add("Sum: " + runningTotal);
                runningTotal = runningTotal / timesList.size(); //I'm just putting the final result there too
                log.add("Average time between changes was " + runningTotal + " milliseconds.");
            }
        } else {
            if (gamepad1.a) {
                trialRunning = true;
                trials = 0;
                telemetry.clearAll();
                lastEnc = motor.getCurrentPosition();
                timesList.clear();
                trialMeasure.reset();
                motor.setPower(0.5);
                log.add("Trial started.");
            } else if (gamepad1.start && otherDeb.milliseconds() > otherDebTime) {
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
            } else if (gamepad1.right_bumper && otherDeb.milliseconds() > otherDebTime) {
                trialQty += 10;
            } else if (gamepad1.left_bumper && otherDeb.milliseconds() > otherDebTime) {
                trialQty -= 10;
            }
//            if (gamepad1.b) {
//                motor.setPower(0.5);
//            } else {
//                motor.setPower(0);
//            }
        }
    }
}
