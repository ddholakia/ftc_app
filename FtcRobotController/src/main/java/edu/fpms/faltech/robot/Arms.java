package edu.fpms.faltech.robot;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by ddhol on 12/1/2015.
 */


public class Arms {

    private LinearOpMode opMode;
    public Hopper hopper;

    private DcMotor ArmRight;
    private DcMotor ArmLeft;
    private DcMotor Elevator;

    private void MoveArms(double power){
        ArmRight.setPower(power);
        ArmLeft.setPower(power);
    }

    public Arms(LinearOpMode opMode) throws InterruptedException{
        this.opMode = opMode;
        opMode.telemetry.addData("mtthd: ", "Arms constructor");
        // get hardware mappings
        ArmRight = opMode.hardwareMap.dcMotor.get("ArmRight");
        ArmLeft = opMode.hardwareMap.dcMotor.get("ArmLeft");
        Elevator = opMode.hardwareMap.dcMotor.get("Elevator");
        hopper = new Hopper(opMode);
    }


    public void Extend(int seconds)throws InterruptedException{
        MoveArms(.5);
        wait(seconds * 1000);
        MoveArms(0);
    }
    public void Retract(int seconds)throws InterruptedException {
        MoveArms(-.25);
        wait(seconds * 1000);
        MoveArms(0);
    }


    public void Up(int seconds)throws InterruptedException{
        Elevator.setPower(.5);
        wait(seconds * 1000);
        Elevator.setPower(0);
    }

    //Elevator down
    public void Down(int seconds)throws InterruptedException {
        Elevator.setPower(-.25);
        wait(seconds * 1000);
        Elevator.setPower(0);
    }
}

