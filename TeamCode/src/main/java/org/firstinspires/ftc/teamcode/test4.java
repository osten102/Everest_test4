package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp
public class test4 extends LinearOpMode {

DcMotorEx MT,MTL,MTR;

double trackwidth = 0.176123;

double raio = 0.045;

int ticks = 28;

double Qp1 = 3.61;

double Cp1 = 5.23;

double Exit_final = Qp1 * Cp1 * ticks;


double converssion_factore = Exit_final/(2*Math.PI*raio);




    public void runOpMode() {

        MT = hardwareMap.get(DcMotorEx.class,"MT");
        MTL = hardwareMap.get(DcMotorEx.class,"MTL");
        MTR = hardwareMap.get(DcMotorEx.class,"MTR");
        MTR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        MTL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        MTL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        MTR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        MTL.setDirection(DcMotorSimple.Direction.REVERSE);
        waitForStart();
        while (opModeIsActive()){


        }


        





    }


    public void reta(double distancia,double velocidade){
        int ticks = (int)( distancia * converssion_factore);
        MTL.setVelocity(velocidade);
        MTR.setVelocity(velocidade);
        MTL.setTargetPosition(ticks);
        MTR.setTargetPosition(ticks);
        MTL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        MTR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void giro(double distancia,double velocidade){
        int ticks = (int)(trackwidth*distancia*Math.PI/180);
        MTL.setVelocity(velocidade);
        MTR.setVelocity(velocidade);
        MTL.setTargetPosition(ticks);
        MTR.setTargetPosition(-ticks);
        MTL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        MTR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

}