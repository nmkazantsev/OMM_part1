package com.nikitos;


import org.jzy3d.analysis.AWTAbstractAnalysis;
import org.jzy3d.chart.factories.AWTChartFactory;
import org.jzy3d.chart.factories.IChartFactory;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.maths.Range;
import org.jzy3d.plot3d.builder.Func3D;
import org.jzy3d.plot3d.builder.SurfaceBuilder;
import org.jzy3d.plot3d.builder.concrete.OrthonormalGrid;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.rendering.canvas.Quality;

import static java.lang.Math.PI;
import static java.lang.Math.max;

public class Plotter extends AWTAbstractAnalysis {
    private final Layer layer;
    private final ModelConfig modelConfig;

    public Plotter(Layer layer, ModelConfig config) {
        this.layer = layer;
        this.modelConfig = config;
    }

    @Override
    public void init() {
        // Define a function to plot
        Func3D func = new Func3D((x, y) ->  layer.getData()[(int)(x/Math.PI* (modelConfig.pointsX-1))][(int)(y/Math.PI* (modelConfig.pointsY-1))]);
        Range range = new Range(0,PI);
        int steps = max(modelConfig.pointsX, modelConfig.pointsY);

        // Create the object to represent the function over the given range.
        final Shape surface =
                new SurfaceBuilder().orthonormal(new OrthonormalGrid(range, steps), func);
        surface
                .setColorMapper(new ColorMapper(new ColorMapRainbow(), surface, new Color(1, 1, 1, .5f)));
        surface.setFaceDisplayed(true);
        surface.setWireframeDisplayed(true);
        surface.setWireframeColor(Color.BLACK);
        IChartFactory f = new AWTChartFactory();

        chart = f.newChart(Quality.Advanced().setHiDPIEnabled(true));
        chart.getScene().getGraph().add(surface);
    }
}