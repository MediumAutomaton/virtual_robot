package org.firstinspires.ftc.teamcode.fy23.robot.subsystems.normalimpl;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.fy23.robot.subsystems.Claw;

/** A normal implementation of {@link Claw}. */
public class ClawImpl implements Claw {

    private Servo servo;
    private double openPosition;
    private double closePosition;
    private State state = State.NONE;

    public ClawImpl(Parameters parameters) {
        servo = parameters.clawServo;
        openPosition = parameters.openPosition;
        closePosition = parameters.closePosition;
    }

    @Override
    public void setState(State state) {
        this.state = state;
        if (state == State.OPEN){
            servo.setPosition(openPosition);
        }
        else if (state == State.CLOSED){
            servo.setPosition(closePosition);
        }
    }

    @Override
    public State getState() {
        return state;
    }

    @Override
    /** Called by robot.update(). You do not need to call this method. */
    public void update() {

    }

}
