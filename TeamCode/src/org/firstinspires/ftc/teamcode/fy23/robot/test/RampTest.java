package org.firstinspires.ftc.teamcode.fy23.robot.test;

import static java.lang.Thread.sleep;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.fy23.robot.VirtualRobot;

@TeleOp
public class RampTest extends OpMode {

    VirtualRobot robot;

    ElapsedTime stopwatch;

    Telemetry.Log log = telemetry.log();

    double deltaVel;
    double accel;
    double loopTime;
    double lastTime;
    double currentTime;
    double currentVel;
    double newVel;
    double remainingDistance;
    double totalDistance;
    double timeToDecel;
    double accelToStop;
    double currentPos;

    double maxAccel = 1;
    double targetVel = 3;
    double maxDeltaVel = 0.01;
    double minVel = 0.1;
    double targetPos = 100;

    double maxVel; // Ask me about this!

    boolean debugFlag = false;
    int debugCounter = 0;
    boolean flagUsed = false;

    double ticksToCM(double ticks) {
        return (ticks / robot.TPR) * robot.wheelDiameter;
    }

    double cmToTicks(double cm) {
        return (cm * robot.TPR) / robot.wheelDiameter;
    }

    @Override
    public void init() {
        robot = new VirtualRobot(hardwareMap);
        robot.drive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        stopwatch = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);

        currentPos = 0;
        currentTime = 0;
        lastTime = 0;
        currentVel = 0;

        totalDistance = targetPos - currentPos;

        maxVel = targetVel; // Ask me about this!
    }

    @Override
    public void start() {
        stopwatch.reset();
    }

    @Override
    public void loop() throws InterruptedException {
        if (currentPos < targetPos) {
            if (targetVel >= currentVel) {
                accel = maxAccel;
            } else {
                accel = -maxAccel;
            }

            currentPos = ticksToCM(robot.drive.getAvgEncoderPos());
            remainingDistance = totalDistance - currentPos;

            timeToDecel = remainingDistance / currentVel;
            accelToStop = currentVel / timeToDecel;
            if (accelToStop >= Math.abs(accel) - 0.1) { targetVel = 0; }
//                if (accelToStop >= Math.abs(accel) - 0.1) {
//                    System.out.println("loopTime");
//                    System.out.println(loopTime);
//                    System.out.println("accel");
//                    System.out.println(accel);
//                    System.out.println("deltaVel");
//                    System.out.println(String.valueOf(deltaVel));
//                    System.out.println("currentVel");
//                    System.out.println(String.valueOf(currentVel));
//                    System.out.println("currentVel + deltaVel");
//                    System.out.println(String.valueOf(currentVel + deltaVel));
//                    System.out.println("newVel");
//                    System.out.println(String.valueOf(newVel));
//                    System.out.println("");
//                }

                currentTime = stopwatch.milliseconds();
                loopTime = currentTime - lastTime;
                loopTime = loopTime / 1000; // convert to seconds to be consistent with accel

                deltaVel = accel * loopTime;
                deltaVel = Range.clip(deltaVel, -maxDeltaVel, maxDeltaVel);
//                if (Math.abs(deltaVel) > maxDeltaVel) {
//                    log.add("Range.clip() not working as advertised (deltaVel)!");
//                    log.add(String.valueOf(deltaVel));
//                }
                newVel = currentVel + deltaVel;
                newVel = Range.clip(newVel, minVel, maxVel);
                /* So, took me an hour and all this commented-out debug stuff you see around here to
                * figure out that the upper bound actually does need to be maxVel, not targetVel. So
                * the paper was right the first time. Because targetVel becomes 0 once it's time to
                * decelerate, forcing newVel to instantly become 0. So I made a variable called
                * maxVel and just set it to targetVel in init(). Yay! It all works now! */
//                if (Math.abs(newVel) < minVel) {
//                    log.add("Range.clip() not working as advertised (newVel)!");
//                    log.add(String.valueOf(newVel));
//                    log.add("minVel");
//                    log.add(String.valueOf(minVel));
//                }

                robot.drive.setVelocity(cmToTicks(newVel));

                lastTime = currentTime;
//                if (accelToStop >= Math.abs(accel) - 0.1) {
//                    System.out.println("Just flipped, printing currentVel then newVel");
//                    System.out.println(currentVel);
//                    System.out.println(newVel);
//                }
                currentVel = newVel; // newVel gets corrupted first, here's where it spreads to currentVel

//                if (accelToStop >= Math.abs(accel) - 0.1 || debugFlag) {
//                    if (debugCounter == 2) {
//                        debugFlag = false;
//                        flagUsed = true;
//                    }
//                    System.out.println("loopTime");
//                    System.out.println(loopTime);
//                    System.out.println("accel");
//                    System.out.println(accel);
//                    System.out.println("deltaVel");
//                    System.out.println(String.valueOf(deltaVel));
//                    System.out.println("currentVel");
//                    System.out.println(String.valueOf(currentVel));
//                    System.out.println("currentVel + deltaVel");
//                    System.out.println(String.valueOf(currentVel + deltaVel));
//                    System.out.println("newVel");
//                    System.out.println(String.valueOf(newVel));
//                    System.out.println("");
//                    if (!flagUsed) {
//                        debugFlag = true;
//                    }
//                    debugCounter += 1;
//                }
            } else {
                robot.drive.setVelocity(0);
            }

            telemetry.addData("currentPos", currentPos);
            telemetry.addData("currentVel", currentVel);
            telemetry.addData("deltaVel", deltaVel);
            telemetry.addData("leftFront velocity", robot.drive.leftFront.getVelocity());
            telemetry.addData("currentTime", currentTime);
            telemetry.addData("accel", accel);
            telemetry.addData("loopTime", loopTime);
            telemetry.addData("remainingDistance", remainingDistance);
            telemetry.addData("timeToDecel", timeToDecel);
            telemetry.addData("accelToStop", accelToStop);

//            sleep(50);
    }
}
