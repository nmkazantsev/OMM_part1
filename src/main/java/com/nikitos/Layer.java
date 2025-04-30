package com.nikitos;

import static com.nikitos.Main.f;
import static java.lang.Math.*;

/**
 * Layer is a class of single XY layer. It keeps all the points, able to complete progonka.
 */
public class Layer {
    public static final int TYPE_X = 0, TYPE_Y = 1;
    private double[][] data; //points
    private double time_index;//may be int and half int

    public Layer(double time_index) {
        this.time_index = time_index;
    }

    //for setup conditions on t=0
    public void init(double[][] data) {
        this.data = data;
    }

    /**
     * calculates the whole step from one layer to another.
     */
    public Layer progonka(int type, ModelConfig config) {
        //create new layer
        Layer layer = new Layer(time_index);
        if (type == TYPE_X) {
            for (int y = 0; y < config.sizey; y++) {
                progonka_1d(type, y, config, layer);
            }
        } else { //TYPE Y
            for (int x = 0; x < config.sizex; x++) {
                progonka_1d(type, x, config, layer);
            }
        }
        return layer;
    }

    private void progonka_1d(int type, int pos, ModelConfig config, Layer layer) {
        if (type == TYPE_X) {
            //search solution like y_{n-1} = an * yn + bn
            //here on index n we keep n+1 index
            double[] a = new double[config.sizex - 1], b = new double[config.sizex - 1];
            //**************
            //border conditions is u(x=0)=u(x=pi)=0
            a[0]=0;
            a[a.length-1]=0;
            b[0]=0;
            b[b.length-1]=0;
            //*************
            //for each index get coefficients
            double A, B, C, F;
            //todo: border conditions
            A = B = 0.5 / pow(config.sizex, 2) * config.tau;
            C = 1 + config.tau / pow(config.hx, 2);
            //so pos is fixed y-index
            //straight pass
            for (int x = 1; x < config.sizex - 1; x++) { //do not affect the borders (that is why form 1 to length -1)
                F = 0.5 * config.tau / pow(config.hy, 2) * data[x][pos - 1] +
                        (1 + config.tau / pow(config.hy, 2)) * data[x][pos] +
                        0.5 * config.tau / pow(config.hy, 2) * data[x][pos - 1]
                        + 0.5 * config.tau * f(x * config.hx + config.initX, pos * config.hy + config.initY, time_index * config.tau + config.tau / 2);
                //for a and b on index n is n+1 index
                double divisor = C - A * a[x - 1];
                a[x - 1] = B / divisor;
                b[x - 1] = (F + A * b[x - 1]) / divisor;
            }
            //reverse pass
            for (int x = config.sizex - 2; x > 1; x--) {
                //for a and b on index n is n+1 index
                layer.data[x][pos] = a[x] * data[x + 1][pos] + b[x];
            }
        } else {
            //so pos is fixed x-index

        }
    }
}
