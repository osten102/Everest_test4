package org.firstinspires.ftc.teamcode.diff;

import static org.firstinspires.ftc.teamcode.diff.Constants.*;
import static org.firstinspires.ftc.teamcode.diff.Constants.horizontalAim;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;

@TeleOp
public class test4 extends LinearOpMode {
enum Estado{
    stop,frente,giro
}
Estado estado = Estado.stop;
DcMotorEx MT,MTL,MTR;

IMU guinada;

    public void runOpMode() {

        MT = hardwareMap.get(DcMotorEx.class,"MT");
        MTL = hardwareMap.get(DcMotorEx.class,"MTL");
        MTR = hardwareMap.get(DcMotorEx.class,"MTR");
        MTR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        MTL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        MTL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        MTR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        MTR.setDirection(DcMotorSimple.Direction.REVERSE);
        guinada = hardwareMap.get(IMU.class,"imu");
        waitForStart();
        guinada.resetYaw();
        estado = Estado.frente;
        while (opModeIsActive()){

            telemetry.addData("posição",MTL.getCurrentPosition());
            telemetry.addData("posição",MTR.getCurrentPosition());
            telemetry.addData("guinada",guinada.getRobotYawPitchRollAngles().getYaw());
            telemetry.addData("dif",MTR.getCurrentPosition() - MTL.getCurrentPosition() );
            telemetry.addData("dif",-MTR.getCurrentPosition() - MTL.getCurrentPosition() );
            telemetry.addData("estado",estado);
            telemetry.addData("power",MTR.getPower());
            telemetry.addData("power",MTL.getPower());
            telemetry.update();


            switch (estado ){
                case giro:
                    MTR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                    MTL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                    giro(-90);
                    if (cond2(-90,2.2)) {
                        guinada.resetYaw();
                        integral = 0;
                        MTL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                        MTR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                        MTR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        MTL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        estado = Estado.stop;
                    }
                    break;
                case frente:
                    reta(horizontalAim,700);
                    if ( cond(horizontalAim,0.1)) {
                        estado = Estado.giro;
                        MTR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        MTL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    }
                    break;

                default:
                    estado = (cont < 4)?Estado.frente:Estado.stop;
                    cont ++;
                    horizontalAim = (cont%2==0)?2.10:2.00;
                    break;

            }



            /*while (estado == Estado.frente){
                reta(0.3,300);
                if ( cond(0.3,0.1)) {
                    estado = Estado.giro;
                    MTR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    MTL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                }
            }
            while (estado == Estado.giro){
                giro(90,500);
                if (cond2(90,1)){
                    MTR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    MTL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    estado = Estado.stop;
                }

            }
            if (estado == Estado.stop){
                estado = (cont <= 4)?Estado.frente:Estado.stop;
                cont ++;*/





        }








    }


    public void reta(double distancia,double velocidade){
        int ticks = (int)( distancia * converssion_factore);
        double erro = 0 - guinada.getRobotYawPitchRollAngles().getYaw();
        Double correção = erro * 0.1;

        MTL.setVelocity(velocidade - correção);
        MTR.setVelocity(velocidade + correção);
        MTL.setTargetPosition(ticks);
        MTR.setTargetPosition(ticks);
        MTL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        MTR.setMode(DcMotor.RunMode.RUN_TO_POSITION);

    }

    public void giro(double distancia){
        double power = PID(distancia,guinada.getRobotYawPitchRollAngles().getYaw());
        MTL.setPower(-power);
        MTR.setPower(power);


    }

    boolean cond(double alvo, double raiog){
        return  Math.abs(alvo * converssion_factore - MTL.getCurrentPosition()) < converssion_factore * raiog;
    }

    boolean cond2(double alvo, double raiog){
        return Math.abs(alvo - guinada.getRobotYawPitchRollAngles().getYaw()) < raiog;
    }



    double PID(double alvo,double atual){

        double erro = alvo - atual;



        return erro * KP + integral * KI;
    }
}
