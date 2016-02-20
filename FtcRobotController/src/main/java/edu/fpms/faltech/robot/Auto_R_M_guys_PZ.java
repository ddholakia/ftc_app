/*

FALTECH 7079
FOUR POINTS MIDDLE SCHOOL

AUTONOMOUS CLASS NAMING NOMENCLATURE:
    OpMode Type: {Auto|Telop}
    Alliance color: {R|B} // Red or Blue
    Starting Position: {CS|M|MS} // Corner Side or Middle or Mountain Side
    Scoring strategy: {guys|none|beacon}
    End location: {PZ|SZ} // Parking Zone or Safety Zone

DESCRIPTION:
    Autonomous program for Red alliance, starting in Corner Side, scoring the climbers in the box,
    and finishing in the Parking Zone.
TARGET SCORE:
    45

 */

package edu.fpms.faltech.robot;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * A simple example of a linear op mode that will approach an IR beacon
 */
public class Auto_R_M_guys_PZ extends LinearOpMode {

    private Robot robot;

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new Robot(this);
        // wait for the start button to be pressed
        waitForStart();

        //Auto Start
        robot.driveTrain.GoInches(174, -1, 15);
        robot.driveTrain.PivotTurn(-45, .5, 5);
        robot.driveTrain.GoInches(44, -1, 6);
        robot.driveTrain.PivotTurn(-45,.5, 5);
        robot.driveTrain.GoInches(1, -1, 6);
        robot.climberSavers.ClimberReleasePosition();

    }
}
