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
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package edu.fpms.faltech;

import android.widget.Spinner;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * TeleOp Mode
 * <p>
 * Enables control of the robot via the gamepad
 */
public class FaltechTeleop1v4 extends OpMode {
	int HopperShuffleValue = 1;
	double SpinnersValue = 0;
	DcMotor LiftRight;
	DcMotor LiftLeft;
	DcMotor MtrsLeft;
	DcMotor MtrsRight;
	DcMotor Elevator;
	DcMotor Spinners;

	// Hopper variables
	boolean hopperFlag = false;
	Servo HopperShuffle;
	final static double HOPPER_SPEED  = 0.1;
	final static double HOPPER_MIN_RANGE = 0.0;
	final static double HOPPER_MAX_RANGE = 1.0;
	final static double HOPPER_STOP_RANGE = 0.5;

	// position of the hopper servo.
	double hopperPosition;
	int MAX_ELEVATOR_VALUE= 4000 ;
	int elevatorStartPosition;

	public FaltechTeleop1v4() {


	}

	/*
	 * Code to run when the op mode is first enabled goes here
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
	 */
	@Override
	public void init() {
		LiftRight = hardwareMap.dcMotor.get("LiftRight");
		LiftLeft = hardwareMap.dcMotor.get("LiftLeft");
		MtrsLeft = hardwareMap.dcMotor.get("MtrsLeft");
		MtrsRight = hardwareMap.dcMotor.get("MtrsRight");
		Spinners = hardwareMap.dcMotor.get("Spinners");
		Elevator = hardwareMap.dcMotor.get("Elevator");
		HopperShuffle = hardwareMap.servo.get("HopperShuffle");
		Elevator.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
		elevatorStartPosition = Elevator.getCurrentPosition();
	}

	/*
	 * This method will be called repeatedly in a loop
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()
	 */
	@Override
	public void loop() {
		float MtrsLeftFloat = -gamepad1.left_stick_y;
		float MtrsRightFloat = gamepad1.right_stick_y;
		float LiftRightFloat = gamepad2.right_stick_y;
		float LiftLeftFloat = -gamepad2.right_stick_y;
		float ElevatorFloat = gamepad2.left_stick_y;


		MtrsRight.setPower(MtrsRightFloat);
		MtrsLeft.setPower(MtrsLeftFloat);
		LiftLeft.setPower(LiftLeftFloat);
		LiftRight.setPower(LiftRightFloat);
		Elevator.setPower(ElevatorFloat);
		if (gamepad2.y) {
			Elevator.setTargetPosition(elevatorStartPosition + MAX_ELEVATOR_VALUE);
			Elevator.setPower(1);
		}

		if (gamepad2.left_bumper || gamepad2.right_bumper){
			if (gamepad2.left_bumper) {
				HopperShuffle.setPosition(0);
			} else {//rightbumber
				HopperShuffle.setPosition(1);
			}
		} else if (!gamepad2.left_bumper || !gamepad2.right_bumper){
			HopperShuffle.setPosition(.5);
		}
		if (gamepad1.left_bumper || gamepad1.right_bumper){
			if (gamepad1.left_bumper) {
				Spinners.setPower(1);
			} else {//rightbumber
				Spinners.setPower(-1);

			}
		} else if (!gamepad2.left_bumper || !gamepad2.right_bumper){
			Spinners.setPower(0);
		}
		/*if (gamepad1.left_bumper) {
			double SpinnersValue = -1;
			Spinners.setPower(SpinnersValue);
		}
		//if (gamepad1.right_bumper){
		//	double SpinnersValue = 1;
		//	Spinners.setPower(SpinnersValue);
		//}


		else if (!gamepad1.left_bumper || !gamepad1.right_bumper){
			double SpinnersValue = 0;
			Spinners.setPower(SpinnersValue);
		}
		*/

/*
		//  hopper To the left
		if (gamepad2.left_bumper) {			hopperPosition -= HOPPER_SPEED;
			hopperFlag = true;
			telemetry.addData(" Left  button hopperPosition =  ",  Double.toString(hopperPosition));

		}
		// hopper to the right
		if (gamepad2.right_bumper) {
			hopperPosition += HOPPER_SPEED;
			hopperFlag = true;
			telemetry.addData(" Right button hopperPosition =  ",  Double.toString(hopperPosition) );
		}
		// hopper Stop
		if (gamepad2.b) {
			hopperPosition = HOPPER_STOP_RANGE;
			hopperFlag = true;
			telemetry.addData(" Stop button hopperPosition =  ", Double.toString(hopperPosition));
		}

		// If a hopper key was pressed then send it to the hopper servo
		if ( hopperFlag ){
			// Prevent errors by limiting it to the lower and upper ranges
			hopperPosition = Range.clip(hopperPosition, HOPPER_MIN_RANGE, HOPPER_MAX_RANGE);
			HopperShuffle.setPosition(hopperPosition);
			hopperFlag = false;			// don't come here again until a hopper key is pressed
		}
		else {
			HopperShuffle.setPosition(HOPPER_STOP_RANGE);
		}
	*/
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


