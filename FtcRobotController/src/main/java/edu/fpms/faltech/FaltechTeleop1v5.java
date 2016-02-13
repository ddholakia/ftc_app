/* Copyright (c) 2014 Qualcomm Technologies Inc

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Qualcomm Technologies Inc nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.


Thanks to Brendan Hollaway, from 6209 Venom


*/

package edu.fpms.faltech;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * TeleOp Mode
 * <p>
 * Enables control of the robot via the gamepad
 *
 */
public class FaltechTeleop1v5 extends OpMode {
	int HopperShuffleValue = 1;
	double SpinnersValue = 0;
    boolean isReversed = false;
    boolean btn_pressed = false;
    long timer = 0;
    
    //Motors
	DcMotor ArmRight;
	DcMotor ArmLeft;
	DcMotor MtrsLeft;
	DcMotor MtrsRight;
	DcMotor Elevator;
	DcMotor Spinners;
    
    //Servos
    Servo ChurroGrab1;
    Servo ChurroGrab2;
    Servo HopperSrv;
    
    //Motor Power Settings
    float MtrsLeftPower;
    float MtrsRightPower;
    float ArmRightPower;
    float ArmLeftPower;
	

	public FaltechTeleop1v5() {


	}

	/*
	 * Code to run when the op mode is first enabled goes here
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
	 */
	@Override
	public void init() {
		ArmRight = hardwareMap.dcMotor.get("ArmRight");
		ArmLeft = hardwareMap.dcMotor.get("ArmLeft");
		MtrsLeft = hardwareMap.dcMotor.get("MtrsLeft");
		MtrsRight = hardwareMap.dcMotor.get("MtrsRight");
        MtrsRight.setDirection(DcMotor.Direction.REVERSE);
		Spinners = hardwareMap.dcMotor.get("Spinners");
		Elevator = hardwareMap.dcMotor.get("Elevator");
		HopperSrv = hardwareMap.servo.get("HopperSrv");
        ChurroGrab1 = hardwareMap.servo.get("ChurroGrab1");
        ChurroGrab2 = hardwareMap.servo.get("ChurroGrab2");


        //Set Churro Grabber's Position
        ChurroGrab1.setPosition(1);
        ChurroGrab2.setPosition(0);
    }


	/*
	 * This method will be called repeatedly in a loop
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()
	 */
	@Override
	public void loop() {

        //Drive Train
        this.telemetry.addData("isReversed",isReversed);
        if(System.nanoTime() > timer)
            btn_pressed = false;
        if (!btn_pressed && gamepad1.b) { // Toggle Backwards Driving
            isReversed = !isReversed;
            timer = System.nanoTime() + (long)(0.5 * Math.pow(10, 9));
            btn_pressed = true;
        }
        if (isReversed) {
            MtrsLeftPower = -gamepad1.left_stick_y;
            MtrsRightPower = -gamepad1.right_stick_y;
        }
        else {
            MtrsLeftPower = gamepad1.left_stick_y;
            MtrsRightPower = gamepad1.right_stick_y;
        }

        //Arms
        if (gamepad2.right_trigger > 50) { //If RT is pressed, full-power mode
            ArmRightPower = gamepad2.right_stick_y;
            ArmLeftPower = gamepad2.right_stick_y;
        }
		else if (gamepad2.right_trigger <= 50 ) { //IF RT is not pressed, half-power mode
            ArmRightPower = gamepad2.right_stick_y / 2;
            ArmLeftPower = gamepad2.right_stick_y / 2;
        }

        //Elevator
		float ElevatorFloat = gamepad2.left_stick_y;

        //setting motors power
		MtrsRight.setPower(MtrsRightPower);
		MtrsLeft.setPower(MtrsLeftPower);
		ArmLeft.setPower(ArmLeftPower);
		ArmRight.setPower(ArmRightPower);
		Elevator.setPower(ElevatorFloat);

        //Hoppers
		if (gamepad2.left_bumper || gamepad2.right_bumper){
			if (gamepad2.left_bumper) {
				HopperSrv.setPosition(0);
			} else {//rightbumper
				HopperSrv.setPosition(1);
			}
		} else if (!gamepad2.left_bumper || !gamepad2.right_bumper){
			HopperSrv.setPosition(.5);
		}

        //Churro Grabbers
        if (gamepad1.left_bumper || gamepad1.right_bumper){  //Churro Grabbers
            if (gamepad1.right_bumper) { //disengadge Churro Grabbers
                ChurroGrab1.setPosition(0);
                ChurroGrab2.setPosition(1);
                this.telemetry.addData("ChurroGrabbers"," Right");
            } else if (gamepad1.left_bumper){ //engadge Churro Grabbers
                ChurroGrab1.setPosition(1);
                ChurroGrab2.setPosition(0);
                this.telemetry.addData("ChurroGrabbers"," Left");
            }
        }

        //Spinners
        if ((gamepad1.right_trigger > .50)||(gamepad1.left_trigger > .50)) {
            if (gamepad1.right_trigger > .50) { //If Right trigger then collect
                Spinners.setPower(-1.0);
                this.telemetry.addData("Spinners","In");
        }
            else if (gamepad1.left_trigger > .50) { //if left trigger then flush
                Spinners.setPower(1.0);
                this.telemetry.addData("Spinners","Out");
            }

        } else if (!(gamepad1.right_trigger > .50)|| !(gamepad1.left_trigger > 50)){
            Spinners.setPower(0);
        }
		telemetry.addData("Right Speed", -gamepad1.right_stick_y);
		telemetry.addData("Left Speed", -gamepad1.left_stick_y);

	}

	/*
	 * Code to run when the op mode is first disabled goes here
	 *
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
	 */
	@Override
	public void stop() {

	}

}


