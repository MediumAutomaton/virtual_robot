package org.firstinspires.ftc.teamcode.fy23.robot.subsystems.normalimpl;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.fy23.robot.subsystems.FriendlyIMU;

/** A normal implementation of {@link FriendlyIMU}.
 * <b>This class has an open task:</b> Robot and Subsystems / Remake FriendlyIMUImpl */
public class FriendlyIMUImpl implements FriendlyIMU {

    private double pitch;
    private double roll;
    private double yaw;

    public BNO055IMU imu; //our control hubs should have this type
    private BNO055IMU.Parameters imuParams; //stores configuration stuff for the IMU
    private AngleUnit angleUnit;

    Orientation orientation;

    public FriendlyIMUImpl(Parameters parameters, HardwareMap hardwareMap) {
        imuParams = new BNO055IMU.Parameters();
        imuParams.calibrationDataFile = "BNO055IMUCalibration.json";
        //see "SensorBNO055IMUCalibration" example

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(imuParams);
    }

    private void updateOrientation() {
        orientation = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);
        pitch = orientation.firstAngle;
        roll = orientation.secondAngle;
        yaw = orientation.thirdAngle;
    }

    @Override
    public double pitch() {
        updateOrientation();
        return angleUnit.fromDegrees(pitch);
    }

    @Override
    public double roll() {
        updateOrientation();
        return angleUnit.fromDegrees(roll);
    }

    @Override
    public double yaw() {
        updateOrientation();
        return angleUnit.fromDegrees(yaw);
    }

    @Override
    /** Called by robot.update(). You do not need to call this method. */
    public void update() {

    }

}
