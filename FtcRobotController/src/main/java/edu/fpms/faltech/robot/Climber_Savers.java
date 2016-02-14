package edu.fpms.faltech.robot;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by ddhol on 12/1/2015.
 */


public class Climber_Savers {

    private LinearOpMode opMode;

    private Servo climberSaverServo;


    public Climber_Savers(LinearOpMode opMode) throws InterruptedException{
        this.opMode = opMode;
        opMode.telemetry.addData("mtthd: ", "Climber_Savers constructor");
        // get hardware mappings
        climberSaverServo = opMode.hardwareMap.servo.get("ClimberSaverServo");
    }


    //stop
    private void SetClimberSaver(int position){
        climberSaverServo.setPosition(position);
    }
}
