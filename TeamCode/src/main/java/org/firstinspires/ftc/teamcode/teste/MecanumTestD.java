package org.firstinspires.ftc.teamcode.teste;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.teamcode.MecanumTest;

@TeleOp(name = "Mecanum")
public class MecanumTestD extends LinearOpMode {

    enum Estado{
        coreção,
        livre,
        campo
    }
    Estado estado = Estado.livre;
    DcMotor frontLeft;
    DcMotor frontRight;
    DcMotor backLeft;
    DcMotor backRight;

    IMU guinada;

    int relative_yaw = 0;
    double fator = 0;
    double lastValue;
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
        lastValue = 0.0;
        double dValue;
        while (opModeIsActive()){
            dValue  = Math.abs(gamepad1.right_stick_x - lastValue);
            switch (estado){
                case livre:
                    fator = 0;
                    mover_reto(zona_morta(gamepad1.left_stick_y*0.3),zona_morta( gamepad1.left_stick_x *0.3),zona_morta(gamepad1.right_stick_x *0.3));
                    if(dValue<=0){
                        estado = Estado.coreção;
                        guinada.resetYaw();
                    }
                    if(gamepad1.right_stick_button){
                        estado = Estado.campo;
                        sleep(200);
                    }
                    break;
                case coreção:
                    fator = 1;
                    mover_reto(zona_morta(gamepad1.left_stick_y*0.3),zona_morta( gamepad1.left_stick_x *0.3),zona_morta(gamepad1.right_stick_x *0.3));
                    if (dValue>0){
                        estado = Estado.livre;
                    }
                    break;
                case campo:
                    fator = 0;
                    fieldRelative(zona_morta(gamepad1.left_stick_y*0.3),zona_morta( gamepad1.left_stick_x *0.3),zona_morta(gamepad1.right_stick_x *0.3));
                    if(gamepad1.right_stick_button){
                        estado = Estado.livre;
                        sleep(200);
                    }
                    break;
            }

            lastValue = Math.abs(gamepad1.right_stick_x *0.3);
            telemetry.addData("Estado", estado);
            telemetry.addData("DValye", dValue*1000);
            telemetry.update();

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
    public void fieldRelative(double x, double y, double z){
        double angulo = guinada.getRobotYawPitchRollAngles().getYaw();
        angulo = Math.toRadians(angulo);
        double x2 = y*Math.sin(angulo)-x*Math.cos(angulo);
        double y2 = y*Math.cos(angulo)+x*Math.sin(angulo);
        mover_reto(y2, x2, z);
    }
    double zona_morta(double valor){
        if (Math.abs(valor) < 0.1)
            return 0;
        else
            return valor;
    }

}
