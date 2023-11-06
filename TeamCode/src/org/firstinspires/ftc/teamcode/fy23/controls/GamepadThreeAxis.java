package org.firstinspires.ftc.teamcode.fy23.controls;

import com.qualcomm.robotcore.hardware.Gamepad;

public class GamepadThreeAxis implements GamepadInterface {
    private Gamepad gamepad1;
    private Gamepad gamepad2;
    public GamepadThreeAxis(Gamepad initgamepad1, Gamepad initgamepad2) {
        gamepad1 = initgamepad1;
        gamepad2 = initgamepad2;
    }
    @Override
    public double forwardMovement() {
        return GamepadInputs.rightTriggerLinear(gamepad1, 1) - GamepadInputs.leftTriggerLinear(gamepad1, 1);
    }

    @Override
    public double strafeMovement() {
        return GamepadInputs.rightStickXExponential(gamepad1, 1);
    }

    @Override
    public double rotateMovement() {
        return GamepadInputs.leftStickXExponential(gamepad1, 1);
    }

    @Override
    public double armMovement() {
        return 0;
    }

    @Override
    public double elevatorMovement() {
        return 0;
    }

    @Override
    public double clawOpen() {
        return 0;
    }

    @Override
    public double clawClose() {
        return 0;
    }
}
