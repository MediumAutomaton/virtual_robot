package org.firstinspires.ftc.teamcode.fy23.robot;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.fy23.robot.units.PIDconsts;

public class RobotB {

    public MecanumDrive drive;
    public PIDconsts pidConsts;

    public RobotB(HardwareMap hardwareMap) {
        drive = new MecanumDrive(hardwareMap, "front_left_motor", "front_right_motor", "back_left_motor", "back_right_motor");
        pidConsts = new PIDconsts(0.05, 0.01, 0.50);
        // TunablePID tuning for this robot
    }
}
