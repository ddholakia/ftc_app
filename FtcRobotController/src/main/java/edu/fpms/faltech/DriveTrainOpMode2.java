/* Copyright (c) 2015 Qualcomm Technologies Inc

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

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class DriveTrainOpMode2 extends LinearOpMode {
  DriveTrain2 driveTrain;
  Lift lift;
  BeaconPush beaconPush;

  @Override
  public void runOpMode() throws InterruptedException {
    // set up the hardware devices we are going to use
    driveTrain = new DriveTrain2(this);
    // lift = new Lift(this);
    // beaconPush = new BeaconPush(this);

    // wait for the start button to be pressed
    waitForStart();
      double driveSpeed = .25;                      // drive slow for now
      driveTrain.GoInches(34, driveSpeed, 30);      // go forward inches, power
      driveTrain.GoLeftSide(2, driveSpeed);         // start turning left and stay away from line
      driveTrain.GoInches(10, driveSpeed, 30);      // move towards the beacons
      driveTrain.GoLeftSide(1, driveSpeed);          // turn left a little more
      driveTrain.GoInches(58, driveSpeed,30);       // now drive all the way to the beacon
      //driveTrain.GoInches(-10,.50,30);      // go backwards by setting inches -negative

    //beaconPush.dropClimbers();
   /*
    lift.GoHigh();
    sleep(3000);
    lift.GoDown();
*/

   // beaconPush.shiftRight();

      // while(opModeIsActive()){
          // beaconPush.leftBeaconIsRed();
      //}

  }
}