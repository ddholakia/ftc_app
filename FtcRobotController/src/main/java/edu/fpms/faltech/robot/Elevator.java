package edu.fpms.faltech.robot;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by ddhol on 12/1/2015.
 */


public class Elevator {

    private LinearOpMode opMode;

    private long position;
    private DcMotor elevatorMotor;
    private int rotation;


    public Elevator(LinearOpMode opMode) throws InterruptedException {
        this.opMode = opMode;
        opMode.telemetry.addData("mtthd: ", "Elevator constructor");
        // get hardware mappings

        elevatorMotor = opMode.hardwareMap.dcMotor.get("Elevator");
        elevatorMotor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        opMode.waitForNextHardwareCycle();
        position = elevatorMotor.getCurrentPosition();
    }

    //Elevator Up
    public boolean Up(int inches, int timeout) throws InterruptedException {
        long targetPosition = position + (960 * inches);
        if (inches > 0) {
            elevatorMotor.setPower(1);
            ElapsedTime timer = new ElapsedTime();
            while ((position < targetPosition) && (timer.time() < timeout)) {
                opMode.telemetry.addData("time up", timer.time());
                position = elevatorMotor.getCurrentPosition();
            }
            elevatorMotor.setPower(0);
        }
        if (position >= targetPosition ){
            return true;
        } else{
            return false;
        }
    }
}