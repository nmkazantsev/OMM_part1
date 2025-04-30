package com.nikitos;


import org.jzy3d.analysis.AnalysisLauncher;

import static java.lang.Math.*;

public class Main {
    public static void main(String[] args) throws Exception {
        ModelConfig config = new ModelConfig();
        config.tau = 0.1;
        config.pointsX = 100;
        config.pointsY = 100;
        config.hx = 0.01;
        config.hy = 0.01;
        config.initX = 0;
        config.initY = 0;
        Layer[] layers = new Layer[100];
        layers[0] = new Layer(0);
        fillLayer(layers[0], config);
        boolean halfStep = true;
        for (int i = 1; i < layers.length; i++) {
            layers[i] = layers[i - 1].progonka(halfStep ? Layer.TYPE_X : Layer.TYPE_Y, config);
            halfStep = !halfStep;
        }
        Plotter plotter = new Plotter(layers[0], config);
        AnalysisLauncher.open(plotter);
    }

    private static void fillLayer(Layer layer, ModelConfig config) {
        double[][] array = new double[config.pointsX][config.pointsY];
        for (int i = 0; i < config.pointsX; i++) {
            for (int j = 0; j < config.pointsY; j++) {
                double x = i * config.hx + config.initX;
                double y = j * config.hy + config.initY;
                array[i][j] = sin(2 * x) * cos(y);
            }
        }
        layer.init(array);
    }

    public static double f(double x, double y, double t) {
        return exp(t) * sin(x) * cos(y);
    }
}