package org.firstinspires.ftc.teamcode.fy23.robot.test;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.fy23.robot.VirtualRobot;
import org.firstinspires.ftc.teamcode.fy23.robot.generators.RudimentaryRampToTarget;
import org.firstinspires.ftc.teamcode.fy23.robot.units.DTS;

@Autonomous
public class RudimentaryRampTest extends LinearOpMode {

    VirtualRobot robot;
    RudimentaryRampToTarget ramper;

    Telemetry.Log log;

    double suggestion;

    public double ticksToCM(double ticks) {
        return (ticks / robot.TPR) * robot.wheelDiameter;
    }

    public double cmToTicks(double cm) {
        return (cm * robot.TPR) / robot.wheelDiameter;
    }

    @Override
    public void runOpMode() {
        robot = new VirtualRobot(hardwareMap);

        robot.drive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        ramper = new RudimentaryRampToTarget();

        log = telemetry.log();
        log.setCapacity(10);

        waitForStart();

        while (opModeIsActive()) {
            ramper.setTargetPos(100);
            ramper.setAcceleration(3);
            ramper.setMaxDeltaVel(.5);
            ramper.setTargetVel(10);
            ramper.startMovement(ticksToCM(robot.drive.getAvgEncoderPos()));

            while (ramper.moving()) {
                suggestion = ramper.getSuggestionAtPos(ticksToCM(robot.drive.getAvgEncoderPos()));
                robot.drive.setVelocity(cmToTicks(suggestion));
                log.add(Double.toString(ramper.getComputeTime()));
                telemetry.addData("currentPos", robot.drive.getAvgEncoderPos());
                telemetry.addData("currentPosCM", ticksToCM(robot.drive.getAvgEncoderPos()));
                telemetry.addData("suggestion", suggestion);
                telemetry.update();
            }

            ramper.setTargetPos(0);
            ramper.setAcceleration(-10);
            ramper.setMaxDeltaVel(-3);
            ramper.setTargetVel(-10);
            ramper.startMovement(ticksToCM(robot.drive.getAvgEncoderPos()));

            while (ramper.moving()) {
                suggestion = ramper.getSuggestionAtPos(ticksToCM(robot.drive.getAvgEncoderPos()));
                robot.drive.setVelocity(cmToTicks(suggestion));
                log.add(Double.toString(ramper.getComputeTime()));
                telemetry.addData("currentPos", robot.drive.getAvgEncoderPos());
                telemetry.addData("currentPosCM", ticksToCM(robot.drive.getAvgEncoderPos()));
                telemetry.addData("suggestion", suggestion);
                telemetry.update();
            }
        }
    }
}
