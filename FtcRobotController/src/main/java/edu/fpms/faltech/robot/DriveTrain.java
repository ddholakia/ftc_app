package edu.fpms.faltech.robot;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.InputMismatchException;

/**
 * Created by ddhol on 12/1/2015.
 */


public class DriveTrain {

    final static int pulsesPerRevolution = 1680 / 2;
    final static double tireCircumference = 12.56; //inches

    private LinearOpMode opMode;
    private DcMotor leftMotor;
    private DcMotor rightMotor;

    private GyroSensor gyroSensor;
   // private int heading;
   public Churro_Grabber churro_grabber ;


    public DriveTrain(LinearOpMode opMode) throws InterruptedException{
        this.opMode = opMode;
        opMode.telemetry.addData("mtthd: ", "DriveTrain constructor");
        // get hardware mappings
        leftMotor = opMode.hardwareMap.dcMotor.get("leftMotor");
        rightMotor = opMode.hardwareMap.dcMotor.get("rightMotor");
        gyroSensor = opMode.hardwareMap.gyroSensor.get("gyroSensor");

        churro_grabber = new Churro_Grabber(opMode);

        leftMotor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        rightMotor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        rightMotor.setDirection(DcMotor.Direction.REVERSE);

        // calibrate the gyro.
        gyroSensor.calibrate();
        // make sure the gyro is calibrated.
        while (gyroSensor.isCalibrating()) {
            Thread.sleep(50);
        }
        gyroSensor.resetZAxisIntegrator();
        opMode.waitForNextHardwareCycle();
    }

    //stopMotors
    private void stopMotors(){
        leftMotor.setPower(0);
        rightMotor.setPower(0);
    }

    // getEncoderAverage
    private int getEncoderAverage(){
        return (leftMotor.getCurrentPosition() + rightMotor.getCurrentPosition()) / 2;
    }

    private boolean Go(int distance, double power, int timeout) throws InterruptedException {
        ElapsedTime timer = new ElapsedTime();

       // resetEncoders();

        leftMotor.setPower(power);
        rightMotor.setPower(power);

        int currentPosition = getEncoderAverage();

        if(power>0) { //go forward
            int targetPosition = currentPosition + distance;
            while ((currentPosition < targetPosition) && (timer.time() < timeout)) {
                currentPosition = getEncoderAverage();
                opMode.telemetry.addData("Pos: ", currentPosition);
                opMode.telemetry.addData("TPos: ", targetPosition);
            }
        } else if (power<0) { //go backward
            int targetPosition = currentPosition - distance;
            while ((currentPosition > targetPosition) && (timer.time() < timeout)) {
                currentPosition = getEncoderAverage();
                opMode.telemetry.addData("Pos: ", currentPosition);
                opMode.telemetry.addData("TPos: ", targetPosition);
            }
        }
        stopMotors();

        if(timer.time() > timeout){
            return false;
        }
        return true;
    }
    //PivotRight
    private void PivotRight(double power){
        opMode.telemetry.addData("Pivot Right Power", power);
        leftMotor.setPower(power);
        rightMotor.setPower(-power);
    }
    //PivotLeft
    private void PivotLeft(double power){
        opMode.telemetry.addData("Pivot Left Power", power);
        leftMotor.setPower(-power);
        rightMotor.setPower(power);
    }
    //GoInches
    public boolean GoInches(double inches, double power, int seconds) throws InterruptedException {
        opMode.telemetry.addData("Cmd: ", "GoInches");
        int distance = (int)(((Math.abs(inches) / tireCircumference) * pulsesPerRevolution));
        return Go(distance,power,seconds);
    }

    //Pivot Turn
    public boolean PivotTurn(int degrees, double power, int seconds) throws InterruptedException {
        opMode.telemetry.addData("Mthd: ", "PivotTurn");
        ElapsedTime timer = new ElapsedTime();
        if((degrees > 359) || (degrees < -359)){
            opMode.telemetry.addData("Error", "Incorrect Degree Value" + degrees);
            return false;
        }
        Thread.sleep(500);
        gyroSensor.resetZAxisIntegrator();// set heading to zero
        while (gyroSensor.isCalibrating()) {
            Thread.sleep(50);
        }
        opMode.waitForNextHardwareCycle();

        if (degrees > 0) { // right turn
            opMode.telemetry.addData(" Right Turn ", degrees);
            PivotRight(power);
            int heading = 0;
            while (heading < degrees) {
                if (timer.time() > seconds) {
                    stopMotors();
                    return false;
                }
                heading = gyroSensor.getHeading();
                opMode.telemetry.addData("  Heading: ", heading);

            }
            stopMotors();
        } else if (degrees < 0) { // left turn
            opMode.telemetry.addData(" Left Turn ", degrees);
            PivotLeft(power);
            int heading = 359;
            while ((heading > (360 + degrees)) || (heading == 0)) {
                if (timer.time() > seconds) {
                    stopMotors();
                    return false;
                }

                heading = gyroSensor.getHeading();
                opMode.telemetry.addData("  Heading: ", heading);
            }
            stopMotors();

        }
        return true;
    }

    //Gyro Test
    public void GyroTest(){
        while(true) {
            opMode.telemetry.addData("Heading", gyroSensor.getHeading());
        }
    }
}
