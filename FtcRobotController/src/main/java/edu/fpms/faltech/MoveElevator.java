package edu.fpms.faltech;

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
    private LinearOpMode opMode;

    public MoveElevator(LinearOpMode opmode) throws InterruptedException {
        this.opMode = opMode;
        // get hardware mappings
        Elevator = opMode.hardwareMap.dcMotor.get("Elevator");
        opMode.telemetry.addData("mtthd: ", " Elevator mapped");
    }

        public void moveit(int inches, double power, int seconds) throws InterruptedException{

            ElapsedTime timer = new ElapsedTime();

            if (inches >= 0) {
                //telemetry.addData("Ele: ", "move up");
                // go forward since inches is positive
                Elevator.setDirection(DcMotor.Direction.FORWARD);

            } else {
                //telemetry.addData("Elv: ", "move down");
                // inches is negative then reverse the direction
                Elevator.setDirection(DcMotor.Direction.REVERSE);
                power = -power; // set the power negative
            }

            // Now move the motor for the elevator
            if(power>0) {
                int targetPosition = elevatorCurrentPosition + elevatorPosition;
                while ((elevatorCurrentPosition < targetPosition) && (timer.time() < seconds)) {
                    elevatorCurrentPosition = Elevator.getCurrentPosition();
                    //telemetry.addData("Elv_Pos: ", elevatorCurrentPosition);
                    //telemetry.addData("El_TPos: ", targetPosition);
                }
            } else if (power<0) {
                int targetPosition = elevatorCurrentPosition - elevatorPosition;
                while ((elevatorCurrentPosition > targetPosition) && (timer.time() < seconds)) {
                    elevatorCurrentPosition = Elevator.getCurrentPosition();
                    //telemetry.addData("Pos: ", elevatorCurrentPosition);
                    Elevator.setPower(power);
            }
        }
    }
}