package org.firstinspires.ftc.teamcode.fy23.controls;

import com.qualcomm.robotcore.hardware.Gamepad;

public class GamepadDTS extends GamepadDefault {
    private Gamepad gamepad1;
    private Gamepad gamepad2;
    public GamepadDTS(Gamepad initgamepad1, Gamepad initgamepad2) {
        super(initgamepad1, initgamepad2);
        gamepad1 = initgamepad1;
        gamepad2 = initgamepad2;
    }
    @Override
    public double forwardMovement() {
        return GamepadInputs.rightTriggerLinear(gamepad1, 1) - GamepadInputs.leftTriggerLinear(gamepad1, 1);
    }

    @Override
    public double strafeMovement() {
        return GamepadInputs.rightStickXLinear(gamepad1, 1);
    }

    @Override
    public double rotateMovement() {
        return GamepadInputs.leftStickXLinear(gamepad1, 1);
    }

    @Override
    public double armForward() {
        return 0;
    }

    @Override
    public double armBackward() {
        return 0;
    }

    @Override
    public double armMovement() {
        double net = GamepadInputs.buttonDpadUp(gamepad2) - GamepadInputs.buttonDpadDown(gamepad2);
        return net;
    }

    public double armMediumMovement() {
        double net = GamepadInputs.buttonDpadRight(gamepad2) - GamepadInputs.buttonDpadLeft(gamepad2);
        return net;
    }

    public double armFastMovement() {
        double y = gamepad2.left_stick_y;
        if (y > 0) {
            return -GamepadInputs.leftStickYExponential(gamepad2, 2);
            //negative because the Y-axis is inverted on the gamepad itself
        } else {
            return GamepadInputs.leftStickYExponential(gamepad2, 2);
        }

    }

    @Override
    public double elevatorMovement() {
        double net = GamepadInputs.rightTriggerLinear(gamepad2, 1) - GamepadInputs.leftTriggerLinear(gamepad2, 1);
        return net;
    }

    @Override
    public double clawOpen() {
        return GamepadInputs.buttonX(gamepad2);
    }

    @Override
    public double clawClose() {
        return GamepadInputs.buttonA(gamepad2);
    }
}
