package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

@TeleOp
public class MecanumTest extends LinearOpMode {

    enum Estado{
        coreção,
        livre
    }
    Estado estado = Estado.livre;
    DcMotor frontLeft;
    DcMotor frontRight;
    DcMotor backLeft;
    DcMotor backRight;

    IMU guinada;

    int relative_yaw = 0;
    double fator = 0;
    @Override
    public void runOpMode() throws InterruptedException {

        frontRight = hardwareMap.get(DcMotor.class,"frontRight");
        frontLeft = hardwareMap.get(DcMotor.class,"frontLeft");
        backLeft = hardwareMap.get(DcMotor.class,"backLeft");
        backRight = hardwareMap.get(DcMotor.class,"backRight");
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        guinada = hardwareMap.get(IMU.class,"imu");
        waitForStart();
        guinada.resetYaw();
        while (opModeIsActive()){

            telemetry.addData("front left power", frontLeft.getPower());
            telemetry.addData("back left power", backLeft.getPower());
            telemetry.addData("front right power", frontRight.getPower());
            telemetry.addData("back right power", backRight.getPower());

            telemetry.addData("gamepad y left", gamepad1.left_stick_y);
            telemetry.addData("gamepad x right", gamepad1.left_stick_x);
            telemetry.addData("guinada relativa", guinada.getRobotYawPitchRollAngles().getYaw());

            telemetry.update();

           switch (estado){
               case livre:
                   fator = 0;
                   if(gamepad1.left_stick_button){
                       estado = Estado.coreção;
                       guinada.resetYaw();
                       sleep(200);
                   }
                   break;
               case coreção:
                   fator = 1;
                   if (gamepad1.left_stick_button){
                       estado = Estado.livre;
                       sleep(200);
                   }
                   break;

           }

            mover_reto(zona_morta(gamepad1.left_stick_y*0.3),zona_morta( gamepad1.left_stick_x *0.3),zona_morta(gamepad1.right_stick_x *0.3));



        }


    }
    public void mover_reto(double power_y, double power_x, double power_z){
        double erro = 0 - guinada.getRobotYawPitchRollAngles().getYaw();
        Double correção = erro * 0.03 * fator;

        backLeft.setPower(power_y + power_x - power_z + correção);
        frontLeft.setPower(power_y - power_x - power_z + correção);
        frontRight.setPower(power_y + power_x + power_z - correção);
        backRight.setPower(power_y - power_x + power_z - correção);
    }

    double zona_morta(double valor){
        if (Math.abs(valor) < 0.1)
            return 0;
        else
            return valor;
    }


}
