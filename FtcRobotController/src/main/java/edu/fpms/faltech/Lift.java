package edu.fpms.faltech;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by ddhol on 12/1/2015.
 */


public class Lift {

    final static double liftVerticalValue = 0; // <- fix this
    final static double liftPower = .85; // <- set this

    final static int highValue = 4300; //<- set this

    final static double raisedValue = .35; // <- set this
    final static double loweredValue = .85; // <- set this

    private LinearOpMode opMode;
    private DcMotor liftMotor; // pully motor
    private Servo liftServo; // retracts the lift

    private int startingLiftPos;
    private int currentLiftPos;


    public Lift(LinearOpMode opMode) throws InterruptedException{
        this.opMode = opMode;
        opMode.telemetry.addData("cnstr: ", "Lift constructor");
        // get hardware mappings
        liftMotor = opMode.hardwareMap.dcMotor.get("liftMotor");
        liftServo = opMode.hardwareMap.servo.get("liftServo");

        liftMotor.setDirection(DcMotor.Direction.REVERSE);
        liftMotor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        opMode.waitForNextHardwareCycle();

        startingLiftPos = liftMotor.getCurrentPosition();
        currentLiftPos = startingLiftPos;

        liftServo.setPosition(raisedValue);
    }

    private void stopMotors(){
        liftMotor.setPower(0);
    }

    private boolean GoUp(int targetCount, int timeout) throws InterruptedException {
        ElapsedTime timer = new ElapsedTime();

        opMode.telemetry.addData("Mthd:", "Lift.GoUP");
        liftMotor.setPower(liftPower);
        int targetPosition = startingLiftPos + targetCount;
        while ((currentLiftPos < targetPosition)) {
            currentLiftPos = liftMotor.getCurrentPosition();
            opMode.telemetry.addData("Pos: ", currentLiftPos);
            opMode.telemetry.addData("TPos: ", targetPosition);

            if (timer.time() > timeout) {
                stopMotors();
                return false;
            }
        }
        stopMotors();
        return true;
    }

    public boolean GoDown() throws InterruptedException {
        ElapsedTime timer = new ElapsedTime();

        int timeout = 10;

        opMode.telemetry.addData("Mthd:", "Lift.GoDown");
        liftMotor.setPower(-liftPower);
        int targetPosition = startingLiftPos;
        while ((currentLiftPos > targetPosition)) {
            currentLiftPos = liftMotor.getCurrentPosition();
            opMode.telemetry.addData("Pos: ", currentLiftPos);
            opMode.telemetry.addData("TPos: ", targetPosition);

            if(timer.time() > timeout){
                stopMotors();
                return false;
            }
            stopMotors();
        }
        return true;
    }

    public void ResetFloor(){
        opMode.telemetry.addData("Mthd:", "Lift.ResetFloor");
        startingLiftPos = liftMotor.getCurrentPosition();
    }

    public void Go(){
        opMode.telemetry.addData("Mthd:", "Lift.GoHigh");
        liftMotor.setPower(1);
    }

    public boolean GoHigh() throws InterruptedException{
        opMode.telemetry.addData("Mthd:", "Lift.GoHigh");
        RaiseLift();
        return GoUp(highValue,10);
    }

    public void LowerLift () throws InterruptedException{
        opMode.telemetry.addData("Mthd:", "Lift.LowerLift");
        liftServo.setPosition(loweredValue);
    }

    public void RaiseLift() throws InterruptedException{
        opMode.telemetry.addData("Mthd:", "Lift.RaiseLift");
        liftServo.setPosition(raisedValue);
    }

}
