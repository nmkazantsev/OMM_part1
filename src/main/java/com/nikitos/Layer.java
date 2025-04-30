package com.nikitos;

import static java.lang.Math.pow;

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
        if (type == TYPE_X) {
            for (int y = 0; y < config.sizey; y++) {
                progonka_1d(type, y, config);
            }
        } else { //TYPE Y
            for (int x = 0; x < config.sizex; x++) {
                progonka_1d(type, x, config);
            }
        }
    }

    private void progonka_1d(int type, int pos, ModelConfig config) {
        if (type == TYPE_X) {
            //search solution like y_{n-1} = an * yn + bn
            //here on index n we keep n+1 index
            double[] a = new double[config.sizex - 1], b = new double[config.sizex - 1];
            //for each index get coefficients
            double A, B, C, F;
            //todo: border conditions
            A = B = 0.5 / pow(config.sizex, 2) * config.tau;
            C = 1 + config.tau / pow(config.hx, 2);
            //so pos is fixed y-index
            //straight pass
            for (int x = 1; x < config.sizex - 1; x++) { //do not affect the borders (that is why form 1 to length -1)
                F = 0.5 * config.tau / pow(config.hy, 2) * data[x][pos - 1] + (1 + config.tau / pow(config.hy, 2)) * data[x][pos] + 0.5 * config.tau / pow(config.hy, 2) * data[x][pos - 1];
                //for a and b on index n is n+1 index
                a[x - 1] = B / (C - A * a[x - 1]);
                b[x - 1] = (F + A * b[x - 1]) / (C - A * a[x - 1]);
            }
            //reverse pass
            for (int x = config.sizex - 2; x > 1; x--) {
                //for a and b on index n is n+1 index
                data[x][pos] = a[x] * data[x + 1][pos] + b[x];
            }
        } else {
            //so pos is fixed x-index

        }
    }
}
