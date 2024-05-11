package org.firstinspires.ftc.teamcode.fy23.robot.teletest;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.fy23.processors.AccelLimiter;
import org.firstinspires.ftc.teamcode.fy23.robot.subsystems.blank.BlankMotor;
import org.firstinspires.ftc.teamcode.fy23.robot.subsystems.digitaldevice.DigitalDeviceBlank;
import org.firstinspires.ftc.teamcode.fy23.robot.subsystems.normalimpl.PixelArmImpl;
import org.firstinspires.ftc.teamcode.fy23.units.SimplePowerTpSConverter;

@TeleOp
public class ProgrammingBoard extends OpMode {

    PixelArmImpl.Parameters armParams = new PixelArmImpl.Parameters();
    PixelArmImpl pixelArm;

    @Override
    public void init() {
        armParams.present = true;
        armParams.pivotMotor = hardwareMap.get(DcMotorEx.class, "motor");
//        armParams.elevatorMotor = hardwareMap.get(DcMotorEx.class, "armExtend");
        armParams.elevatorMotor = new BlankMotor();
        armParams.pivotAccelLimiter = new AccelLimiter(1.0, 0.1); // TODO: not tuned!!
        armParams.pivotPowerTpSConverter = new SimplePowerTpSConverter(2499, 1249); // TODO: not measured on real hardware!!
        armParams.pivotTicksPerDegree = 10; // TODO: not measured!!
        armParams.pivotUpperLimit = 2000; // TODO: not measured on real hardware!!
        armParams.pivotLowerLimit = 0; // TODO: not measured on real hardware!!
        armParams.pivotUpperLimitSwitch = new DigitalDeviceBlank(); // not installed
        armParams.pivotLowerLimitSwitch = new DigitalDeviceBlank(); // not installed
        armParams.maxPivotRecoveryPower = 0.2;
        armParams.elevatorAccelLimiter = new AccelLimiter(1.0, 0.1); // TODO: not tuned!!
        armParams.elevatorPowerTpSConverter = new SimplePowerTpSConverter(2499, 1249); // TODO: not measured on real hardware!!
        armParams.elevatorTicksPerMillimeter = 10; // TODO: not measured!!
        armParams.elevatorUpperLimit = 2500;
        armParams.elevatorLowerLimit = 0;
        armParams.elevatorUpperLimitSwitch = new DigitalDeviceBlank(); // not installed
        armParams.elevatorLowerLimitSwitch = new DigitalDeviceBlank(); // not installed
        armParams.maxElevatorRecoveryPower = 0.2;

        pixelArm = new PixelArmImpl(armParams);
    }

    @Override
    public void loop() {
        pixelArm.setPivotPower(gamepad1.right_trigger - gamepad1.left_trigger);
        telemetry.addData("position", pixelArm.getPivotPosition());
        telemetry.addData("power", pixelArm.getPivotPower());
        pixelArm.update();
    }
}
