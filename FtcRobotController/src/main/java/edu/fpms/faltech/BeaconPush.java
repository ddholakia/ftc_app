package edu.fpms.faltech;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by ddhol on 12/1/2015.
 */


public class BeaconPush {

    final static double rightButtonValue = 0; // <- set this
    final static double leftButtonValue = 1; // <- set this

    final static double holdClimbersValue = 0; // <- set this
    final static double releaseClimbersValue = 1; // <- set this

    private LinearOpMode opMode;
    private Servo buttonServo; // pully motor
    private Servo climberServo; // retracts the lift
    private ColorSensor rearColorSensor;

    public BeaconPush(LinearOpMode opMode) throws InterruptedException{
        this.opMode = opMode;
        opMode.telemetry.addData("cnstr: ", "BeaconPush constructor");
        // get hardware mappings
        buttonServo = opMode.hardwareMap.servo.get("buttonServo");
        climberServo = opMode.hardwareMap.servo.get("climberServo");

        buttonServo.setPosition(leftButtonValue);
        climberServo.setPosition(holdClimbersValue);

        rearColorSensor = opMode.hardwareMap.colorSensor.get("rearColorSensor");
    }

    public void shiftRight(){
        buttonServo.setPosition(rightButtonValue);
    }

    public void shiftLeft(){
        buttonServo.setPosition(leftButtonValue);
    }

    public void dropClimbers(){
        climberServo.setPosition(releaseClimbersValue);
    }

    public boolean leftBeaconIsRed(){
        opMode.telemetry.addData("red: ", rearColorSensor.red());
        opMode.telemetry.addData("green: ", rearColorSensor.green());
        opMode.telemetry.addData("blue: ", rearColorSensor.blue());

        return rearColorSensor.red() > rearColorSensor.blue();
    }
}
