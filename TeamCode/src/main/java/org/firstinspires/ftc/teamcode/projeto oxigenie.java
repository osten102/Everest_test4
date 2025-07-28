package org.firstinspires.ftc.teamcode.diff;

import static org.firstinspires.ftc.teamcode.diff.Constants.*;
import static org.firstinspires.ftc.teamcode.diff.Constants.horizontalAim;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;

public class MecanumTest extends LinearOpMode{
@TeleOp
    DcMotorEx frontRight,frontleft,backright,backleft;
        public void runOpMode() {
    
            frontRight = hardwareMap.get(DcMotorEx.class,"frontRight");
            frontleft = hardwareMap.get(DcMotorEx.class,"frontleft");
            backRight = hardwareMap.get(DcMotorEx.class,"backRight");
            backleft = hardwareMap.get(DcMotorEx.class,"backleft");
    
            frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
            backRight.setDirection(DcMotorSimple.Direction.REVERSE);
    
            waitForStart();
            while (opModeIsActive()){
                mover(zona_morta(),zona_morta(),zona_morta());    
            }
        }
        public void mover(double x,double y,double z){
            frontRight.setPower(y + x + - z);
            frontleft.setPower(y - x + z);
            backRight.setPower(y + x - z);
            backleft.setPower(y - x + z);
        }
    
        double zona_morta(double valor){
            if (Math.abs(valor) < 0.2)
                return 0;
            else
                return valor;
        }        
}
