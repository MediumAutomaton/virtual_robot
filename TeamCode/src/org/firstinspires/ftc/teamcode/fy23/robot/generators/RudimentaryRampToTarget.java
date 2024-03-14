package org.firstinspires.ftc.teamcode.fy23.robot.generators;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.fy23.robot.VirtualRobot;

/** Ramp acceleration and deceleration while moving to a target position. Acts on a single axis and
 * works with doubles. */
public class RudimentaryRampToTarget {

    private double targetPos = 0;
    private double currentPos = 0;

    private double targetVel = 0;
    private double currentVel = 0;

    /** Acceleration in (math was made with centimeters per second in mind, but it shouldn't matter) */
    private double accel = 50;

    private double maxDeltaVel = 3;

    // Used to know how much time passes between getCurrentSuggestion() calls
    // because the timing of OpModes is inconsistent
    private ElapsedTime stopwatch = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
    private double lastTime;
    private double currentTime;
    private double lastPos;
    private double loopTime;
    private double loopMove;
    private double totalDistance;
    private double distanceRemaining;
    private double timeToDecelerate;
    private double decelRateNeeded;
    double deltaVel;
    private double newVel;

    private double computeTime;

    private boolean moveInProgress = false;
    //    private AnyRobot robot; // Using the interface has not gone well.
//    private VirtualRobot robot;

//    public RudimentaryRampToTarget(VirtualRobot argRobot) {
//        robot = argRobot;
//    }

    public void setTargetPos(double argPos) {
        targetPos = argPos;
    }

    public double getTargetPos() {
        return targetPos;
    }

    public void setTargetVel(double argTargetVel) { targetVel = argTargetVel; }

    public double getTargetVel() { return targetVel; }

    public void setAcceleration(double argAccel) {
        accel = argAccel;
    }

    public double getAcceleration() {
        return accel;
    }

    public void setMaxDeltaVel(double argMaxDeltaVel) { maxDeltaVel = argMaxDeltaVel; }

    public double getMaxDeltaVel() { return maxDeltaVel; }

    public boolean moving() {
        return moveInProgress;
    }


//    private double ticksToCM(double ticks) {
//        return (ticks / robot.TPR) * robot.wheelDiameter;
//    }

    /**
     * Start moving towards the currently set target.
     */
    public void startMovement(double argCurrentPos) {
        stopwatch.reset();
        moveInProgress = true;
        lastTime = 0;
        lastPos = argCurrentPos;
        currentVel = 0;
//        currentPos = ticksToCM(robot.drive.getAvgEncoderPos());
        totalDistance = targetPos - argCurrentPos;
    }

    /**
     * Cancel the in-progress move. (This automatically happens when a move completes, so this is
     * only needed when the robot is actively moving.)
     */
    public void cancelMovement() {
        moveInProgress = false;
    }

    /**
     * Get the velocity suggested by the ramping algorithm. (It's a "suggestion" because you could
     * run it through processors.) */
    public double getSuggestionAtPos(double argCurrentPos) {
        if (moving()) {
            currentTime = stopwatch.milliseconds();
            loopTime = currentTime - lastTime; // How much time has passed
//            currentPos = ticksToCM(robot.drive.getAvgEncoderPos());
            currentPos = argCurrentPos;
            loopMove = currentPos - lastPos; // how much we have moved this loop

            distanceRemaining = totalDistance - currentPos;
            timeToDecelerate = distanceRemaining / currentVel; // At this speed, how long do we have before reaching the target?
            decelRateNeeded = currentVel / timeToDecelerate; // How fast would we need to decelerate to hit the target exactly, if we started now?
            // Once this equals our desired deceleration rate, we'll start decelerating.

            if (currentPos < targetPos) { // Are we still going...

                deltaVel = (accel * loopTime)/1000; // Dimensional analysis!
                // (3 cm / 1 sec²) * (1 sec / 1000 ms) * (50 ms / 1 loop) = (0.150 cm / 1 sec / 1 loop)
                // (acceleration)  * (conv. sec to ms - loopTime is measured in ms) * (how long the loop took)
                // Basically, how much faster should we be going now than last time given our
                // acceleration rate?

                if (decelRateNeeded <= accel - 0.1) { // Are we still accelerating...

                    newVel = currentVel + Math.min(deltaVel, maxDeltaVel);
                    // Apply the acceleration (change in speed), but don't change too much!
                    currentVel = Math.max(newVel, targetVel); // Don't go too fast!

                } else { // ...or do we need to start decelerating now?

                    // same as above, but subtract speed (basically flip all the signs and min()/max())
                    newVel = currentVel - Math.max(deltaVel, maxDeltaVel);
                    currentVel = Math.min(newVel, targetVel);

                }

                lastTime = currentTime;
                lastPos = currentPos;
                computeTime = stopwatch.milliseconds() - currentTime;
                return currentVel;
            } else {
                cancelMovement();
                return 0;
                // If we have reached our target, cancel the move.
            }
        } else {
            return 0;
            // If the move is finished or cancelled, do nothing.
        }
    }

    public double getComputeTime() {
        return computeTime;
    }
}
