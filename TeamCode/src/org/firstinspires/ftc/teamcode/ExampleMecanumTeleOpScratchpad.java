//MI-FTC 2023 - https://mi-ftc.github.io
//If this is hard to read, break it up with some extra newlines or change your text size.
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.fy23.controls.GamepadInterface;
import org.firstinspires.ftc.teamcode.fy23.controls.GamepadLinear;
import org.firstinspires.ftc.teamcode.fy23.controls.GamepadThreeAxis;

import java.util.ArrayList;

@TeleOp(group="Tutorial")
public class ExampleMecanumTeleOpScratchpad extends OpMode {
    // https://mi-ftc.github.io/tutorial/o/opmode/index.html

    DcMotor leftFront;
    DcMotor rightFront;
    DcMotor leftBack;
    DcMotor rightBack;

    double drive;
    double turn;
    double strafe;
    double GCD;

    ElapsedTime buttonDeb = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);

    int currentGamepadIdx;
    GamepadInterface gamepad;

    ArrayList<GamepadInterface> gamepadList = new ArrayList<>(2);
    ArrayList<String> gamepadNameList = new ArrayList<>(2);

    void setGamepad(int idx) {
        gamepad = gamepadList.get(idx);
    }

    @Override
    public void init() {

        //Make sure these names match the Robot Controller configuration
        //You can change either this or the RC config to match if necessary
        leftFront = hardwareMap.get(DcMotor.class, "front_left_motor");
        rightFront = hardwareMap.get(DcMotor.class, "front_right_motor");
        leftBack = hardwareMap.get(DcMotor.class, "back_left_motor");
        rightBack = hardwareMap.get(DcMotor.class, "back_right_motor");

        //Your robot may need these inverted
        leftFront.setDirection(DcMotor.Direction.REVERSE);
        rightFront.setDirection(DcMotor.Direction.FORWARD); //State FORWARD too for clarity
        leftBack.setDirection(DcMotor.Direction.REVERSE);
        rightBack.setDirection(DcMotor.Direction.FORWARD);

        gamepadList.add(new GamepadThreeAxis(gamepad1, gamepad2));
        gamepadList.add(new GamepadLinear(gamepad1, gamepad2));

        gamepadNameList.add("GamepadThreeAxis");
        gamepadNameList.add("GamepadLinear");

        currentGamepadIdx = 0;
        setGamepad(currentGamepadIdx);
    }

//    @Override
//    public void init_loop() {
//
//        //Put any code here which should loop until Start is pressed
//
//    }

//    @Override
//    public void start() {
//
//        //Put any code here which should run only once after Start is pressed
//
//    }

    @Override
    public void loop() {
        if (buttonDeb.milliseconds() > 500) {
            if (gamepad1.dpad_right) {
                currentGamepadIdx = 1;
                setGamepad(1);
                buttonDeb.reset();
            } else if (gamepad1.dpad_left) {
                currentGamepadIdx = 0;
                setGamepad(0);
                buttonDeb.reset();
            }
        }
        //Put any code here which should loop until Stop is pressed

        // https://gm0.org/en/latest/docs/software/tutorials/mecanum-drive.html

        drive = gamepad.forwardMovement();
        turn = gamepad.rotateMovement(); //Adjust multiplier to change sensitivity
        //The driver I worked with preferred slower turning
        strafe = gamepad.strafeMovement();
        GCD = Math.max(Math.abs(drive) + Math.abs(turn) + Math.abs(strafe), 1);

        leftFront.setPower(drive + turn + strafe);
        //Think this way: Must add power for positive (right) turn, so add turn. Same for strafe.
        rightFront.setPower(drive - turn - strafe);
        //Must remove power for positive (right) turn, so subtract turn.
        leftBack.setPower(drive + turn - strafe);
        rightBack.setPower(drive - turn + strafe);

        //Telemetry, to isolate problems with code or controller
        telemetry.addData("rightTrigger", gamepad1.right_trigger);
        telemetry.addData("leftTrigger", gamepad1.left_trigger);
        telemetry.addData("leftStickX", gamepad1.left_stick_x);
        telemetry.addData("rightStickX", gamepad1.right_stick_x);

        telemetry.addData("Current Gamepad", gamepadNameList.get(currentGamepadIdx));
        telemetry.addData("gamepad.forwardMovement()", gamepad.forwardMovement());
        telemetry.addData("gamepad.rotateMovement()", gamepad.rotateMovement());
        telemetry.addData("gamepad.strafeMovement()", gamepad.strafeMovement());

        telemetry.addData("drive", drive);
        telemetry.addData("turn", turn);
        telemetry.addData("strafe", strafe);

    }

//    @Override
//    public void stop() {
//
//        //Put anything here that needs to run once after Stop is pressed
//
//    }
}
