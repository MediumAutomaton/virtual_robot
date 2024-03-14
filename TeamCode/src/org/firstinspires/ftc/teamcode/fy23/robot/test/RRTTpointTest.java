package org.firstinspires.ftc.teamcode.fy23.robot.test;

import static java.lang.Thread.sleep;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.fy23.robot.generators.RudimentaryRampToTarget;

import java.util.ArrayList;

@Autonomous
public class RRTTpointTest extends OpMode {

    RudimentaryRampToTarget ramper;

    double suggestion;

    ArrayList<Integer> expectedOutputList = new ArrayList<>(12);

    @Override
    public void init() {
//        expectedOutputList.add(1);
//        expectedOutputList.add(2);
//        expectedOutputList.add(3);
//        expectedOutputList.add(4);
//        expectedOutputList.add(5);
//

        ramper = new RudimentaryRampToTarget();
        ramper.setAcceleration(1);
        ramper.setTargetVel(3);
        ramper.setTargetPos(10);
        ramper.setMaxDeltaVel(2);

        System.out.println("RRTTpointTest output can be viewed on this console.");
        System.out.println("Test parameters:");
        System.out.println("Acceleration is 1cm/sec");
        System.out.println("Target velocity is 3cm/sec");
        System.out.println("Target position is 10cm from start");
        System.out.println("maxDeltaVel is 2cm (making it irrelevant here)");
        System.out.println("Roughly 1000ms between each iteration");
    }

    public void start() throws InterruptedException {
        System.out.println();
        System.out.print("Input | Expected Output | Actual Output");
        ramper.startMovement(0);
        for(int i=0; i<12; i++) {
            suggestion = ramper.getSuggestionAtPos(i);
            System.out.println(String.format("  %d   | Expected Output | %f", i, suggestion));
            sleep(950);
        }
    }

    @Override
    public void loop() {

    }
}
