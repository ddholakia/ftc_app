package edu.fpms.faltech;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by MAN on 1/29/2016.
 */
public class MoveElevator{
    private DcMotor Elevator;
    int elevatorCurrentPosition;
    int elevatorPosition;
    boolean debug = true;
    private LinearOpMode opMode;

    public MoveElevator(LinearOpMode opMode) throws InterruptedException {
        this.opMode = opMode;
        // get hardware mappings
        if (debug) DbgLog.msg("**DEBUG** entering MoveElevator");
        Elevator = opMode.hardwareMap.dcMotor.get("Elevator");
        opMode.telemetry.addData("mtthd: ", " Elevator mapped");
        if (debug) DbgLog.msg("**DEBUG** Elevator sucessfully mapped");
    }

        public void moveit(int inches, double power, int seconds) throws InterruptedException{

            ElapsedTime timer = new ElapsedTime();

            if (debug) DbgLog.msg("**DEBUG** entering moveit" +
                    String.format(" inches d% power %s seconds %d",inches,seconds,
                            Double.toString(power)));

            if (inches >= 0) {
                //telemetry.addData("Ele: ", "move up");
                // go forward since inches is positive
                Elevator.setDirection(DcMotor.Direction.FORWARD);

            } else {
                Elevator.setDirection(DcMotor.Direction.REVERSE);
                power = -power; // set the power negative
            }

            // Now move the motor for the elevator
            if(power>0) {
                if (debug) DbgLog.msg("**DEBUG** entering moving power positive");

                int targetPosition = elevatorCurrentPosition + elevatorPosition;
                while ((elevatorCurrentPosition < targetPosition) && (timer.time() < seconds)) {
                    elevatorCurrentPosition = Elevator.getCurrentPosition();
                    Elevator.setPower(power);
                }
            } else if (power<0) {
                if (debug) DbgLog.msg("**DEBUG** entering moving power negative");
                int targetPosition = elevatorCurrentPosition - elevatorPosition;
                while ((elevatorCurrentPosition > targetPosition) && (timer.time() < seconds)) {
                    elevatorCurrentPosition = Elevator.getCurrentPosition();
                    //telemetry.addData("Pos: ", elevatorCurrentPosition);
                    Elevator.setPower(power);
            }
        }
    }
}