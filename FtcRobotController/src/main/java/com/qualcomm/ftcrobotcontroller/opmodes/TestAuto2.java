package com.qualcomm.ftcrobotcontroller.opmodes;
import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.TouchSensor;

import static java.lang.Math.abs;

/**
 * Created by Jordan Burklund on 7/30/2015.
 * Modified by Marcus and Dad on 1/25/2016 to learn how program autonomous
 */
public class TestAuto2 extends LinearOpMode {
    DcMotor Motor_left;
    DcMotor Motor_right;
    int inches_to_travel= 52;
    int value_per_inch = 88;
    int step1 = inches_to_travel * value_per_inch; // distance to travel in the first step
    int loopVar;
    int i = 0;
    int Motor_right_position = 0, Motor_right_target_position = 0;
    int Motor_left_position = 0, Motor_left_target_position = 0;

    double Motor_left_power, Motor_right_power;
    boolean debug = true;

    // Set without_encoder to false to force it to use encoders
    boolean without_encoders = true;
    String device_name = "*none*";

    @Override
    public void runOpMode() throws InterruptedException {

        if (debug) DbgLog.msg("**DEBUG** entering runOpMode");

        // Get references to the motors from the hardware map
        Motor_left = hardwareMap.dcMotor.get("MtrsLeft");
        Motor_right = hardwareMap.dcMotor.get("MtrsRight");

        //if (motor1.getMode() == DcMotorController.RunMode.RESET_ENCODERS) {
        //if (debug) DbgLog.msg("**DEBUG** Setting to RUN_USING_ENCODERS");

        // Reset the encoders
        Motor_left.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        Motor_right.setMode(DcMotorController.RunMode.RESET_ENCODERS);


        // Tell the motors that we are using encoders
        Motor_left.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        Motor_right.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        if (debug) DbgLog.msg("**DEBUG** Setting Motor_left" + String.format("%d", step1));
        if (debug) DbgLog.msg("**DEBUG** Setting Motor_right" + String.format("%d", -step1));

        Motor_left.setTargetPosition(-step1);
        Motor_right.setTargetPosition(step1);

        // Now tell the motor to run to that position

        Motor_right.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        Motor_left.setMode(DcMotorController.RunMode.RUN_TO_POSITION);


        // If without encodes just set it to let the motor run forever to see if this also works
        //if (without_encoders) motor1.setMode(Dc,MotorController.RunMode.RUN_WITHOUT_ENCODERS );

        // Set the power that we want run at
        Motor_left.setPower(1.0);
        Motor_right.setPower(1.0);

        // Run while the average of the left and the right are less than the target position
        // Note that we convert to absolute since one wheel turn backwards

        while (( (Motor_right_position + Motor_left_position) / 2) <= step1) {

            // Get info from the left motor
            Motor_left_position = Motor_left.getCurrentPosition();
            Motor_left_target_position = Motor_left.getTargetPosition();
            Motor_left_power = Motor_left.getPower();

            // Get info from the right motor

            Motor_right_position = Motor_right.getCurrentPosition();
            Motor_right_target_position = Motor_right.getTargetPosition();
            Motor_right_power = Motor_right.getPower();
            //waitOneFullHardwareCycle();

            // if (debug) DbgLog.msg("**DEBUG**" +String.format("Loop % device_name %s motor1_position %d target_position %d motor_power %s",i,device_name, motor_position,
        //target_position,Double.toString(motor_power)) );

            telemetry.addData("Motor_left  ", String.format("loop=%d position %d target %d power %s ", i, Motor_left_position, Motor_left_target_position,Double.toString(Motor_left_power) ));
            telemetry.addData("Motor_right ", String.format("loop=%d position %d target %d power %s ", i, Motor_right_position, Motor_right_target_position,Double.toString(Motor_right_power) ));

            // Sleep for x milliseconds to give the motor a chance to move
            sleep(50);

            // Convert them to absolute values for the while loop check
            Motor_right_position = abs(Motor_right_position);
            Motor_left_position = abs(Motor_right_position);
            // Simulate position if we are running without encoders
            //if (without_encoders) motor_position++;
            i++;
        }
        Motor_left.setPower(0);
        Motor_right.setPower(0);
    }

}