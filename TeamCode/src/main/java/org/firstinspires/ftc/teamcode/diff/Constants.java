package org.firstinspires.ftc.teamcode.diff;

import com.qualcomm.robotcore.util.ElapsedTime;

public class Constants {
    static double trackwidth = 0.176123;

    static double raio = 0.045;
    static int ticks = 28;
    static double Qp1 = 3.61;
    static double Cp1 = 5.23;
    static double Exit_final = Qp1 * Cp1 * ticks;
    static double converssion_factore = Exit_final/(2*Math.PI*raio);
    static double cont = 1;
    static double KP = 0.1/6.35;
    static double lasttime = 0;
    static ElapsedTime time = new ElapsedTime();
    static double integral = 0;
    static double KI = 0.000005;
    static double horizontalAim = 2.00;
}
