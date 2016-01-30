package edu.fpms.faltech;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by ddhol on 12/1/2015.
 */


public class DriveTrain2 {

    final static int pulsesPerRevolution = 1680;
    final static double tireCircumference = 15.7; //inches
    final static int distancePerInch = 86;          // As measured by the experiment

    private LinearOpMode opMode;
    private HardwareMap hardwareMap = new HardwareMap();
    private DcMotorController driveTrainMotorController;
    private DcMotor leftMotors; // two motors wired together
    private DcMotor rightMotors; // two motors wired together

    private GyroSensor gyroSensor;
    private int heading;

    public DriveTrain2(LinearOpMode opMode) throws InterruptedException{
        this.opMode = opMode;
        opMode.telemetry.addData("mtthd: ", " ** The Falcon is ready ** ");
        // get hardware mappings
        leftMotors = opMode.hardwareMap.dcMotor.get("MtrsLeft");
        rightMotors = opMode.hardwareMap.dcMotor.get("MtrsRight");

        leftMotors.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        rightMotors.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);


        rightMotors.setDirection(DcMotor.Direction.REVERSE);
        gyroSensor = opMode.hardwareMap.gyroSensor.get("gyroSensor");

        //calibrate the gyro.
        gyroSensor.calibrate();
        //make sure the gyro is calibrated.
        while (gyroSensor.isCalibrating()) {
            Thread.sleep(50);
        }

        opMode.waitForNextHardwareCycle();
    }

    private void resetEncoders()throws InterruptedException {

        leftMotors.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        rightMotors.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        opMode.waitForNextHardwareCycle();
    }

    private void stopMotors(){
        leftMotors.setPower(0);
        rightMotors.setPower(0);
    }

    private int getEncoderAverage(){
        return (leftMotors.getCurrentPosition() + rightMotors.getCurrentPosition()) / 2;
    }

    private boolean Go(int position, double power, int timeout) throws InterruptedException {
        ElapsedTime timer = new ElapsedTime();

       // resetEncoders();
        leftMotors.setPower(power);
        rightMotors.setPower(power);

        int currentPosition = getEncoderAverage();

        if(power>0) {
            int targetPosition = currentPosition + position;
            while ((currentPosition < targetPosition) && (timer.time() < timeout)) {
                currentPosition = getEncoderAverage();
                opMode.telemetry.addData("Pos: ", currentPosition);
                opMode.telemetry.addData("TPos: ", targetPosition);
            }
        } else if (power<0) {
            int targetPosition = currentPosition - position;
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

    private void PivotRight(double power){
        leftMotors.setPower(power);
        rightMotors.setPower(-power);
    }

    private void PivotLeft(double power){
        leftMotors.setPower(-power);
        rightMotors.setPower(power);
    }

    public boolean GoInches(double inches, double power, int seconds) throws InterruptedException {

        // int position = (int)((Math.abs(inches) / tireCircumference) * pulsesPerRevolution);

        if ( inches >= 0){
            opMode.telemetry.addData("Cmd: ", "GoInches forward");
            // go forward since inches is positive
            rightMotors.setDirection(DcMotor.Direction.FORWARD);
            rightMotors.setDirection(DcMotor.Direction.REVERSE);
        }
        else {
            opMode.telemetry.addData("Cmd: ", "GoInches backwards");
            // inches is negative then reverse the direction
            rightMotors.setDirection(DcMotor.Direction.FORWARD);
            rightMotors.setDirection(DcMotor.Direction.REVERSE);
            power = -power; // set the power negative
        }
        int position = (int) ((Math.abs(inches) * distancePerInch));
        return Go(position,power,seconds);
    }

    public void GoRightSide( int seconds, double power )throws InterruptedException{
        opMode.telemetry.addData("Mthd: ", "GoRightSide");

        ElapsedTime timer = new ElapsedTime();
        leftMotors.setPower(power);
        while(timer.time() < seconds){}
        stopMotors();
    }

    public void GoLeftSide( int seconds, double power )throws InterruptedException{
        opMode.telemetry.addData("Mthd: ", "GoLeftSide");

        ElapsedTime timer = new ElapsedTime();
        rightMotors.setPower(power);
        while(timer.time() < seconds){}
        stopMotors();
    }

    public boolean PivotTurn(int degrees, double power, int seconds) throws InterruptedException {
        opMode.telemetry.addData("Mthd: ", "PivotTurn");
        ElapsedTime timer = new ElapsedTime();

        Thread.sleep(500);
        gyroSensor.resetZAxisIntegrator(); // set heading to zero
        opMode.waitForNextHardwareCycle();
        heading = 0;

        opMode.telemetry.addData("  THdg: ", degrees);

        if (degrees > 0) { // right turn
            PivotRight(power);
            while (heading < degrees) {
                if (timer.time() > seconds) {
                    stopMotors();
                    return false;
                }
                opMode.telemetry.addData("  CHdgRight: ", heading);
                heading = gyroSensor.getHeading();
            }
            stopMotors();
        } else if (degrees < 0) { // left turn
            PivotLeft(power);
            while (heading > degrees) {
                if (timer.time() > seconds) {
                    stopMotors();
                    return false;
                }
                opMode.telemetry.addData("  CHdgLeft: ", heading);
                heading = gyroSensor.getHeading();
            }
            stopMotors();
        }
        return true;
    }
}
