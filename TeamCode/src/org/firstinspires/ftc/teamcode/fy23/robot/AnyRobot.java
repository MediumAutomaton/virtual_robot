package org.firstinspires.ftc.teamcode.fy23.robot;

import org.firstinspires.ftc.teamcode.fy23.robot.units.PIDconsts;

public class AnyRobot {

    public class Parameters {
        public double TPR;
        public double wheelDiameter;
        public double maxForwardSpeed;
        public PIDconsts pidConsts;
        public String leftFrontMotorName;
        public String rightFrontMotorName;

    }

    public AnyRobot(Parameters parameters) {

    }

}
