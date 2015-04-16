/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openstreetmap.josm.plugins.tofix.draw;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import org.openstreetmap.josm.data.Bounds;
import org.openstreetmap.josm.gui.MapView;
import org.openstreetmap.josm.gui.layer.MapViewPaintable;

/**
 *
 * @author ruben
 */
public class DrawWay implements MapViewPaintable {

    @Override
    public void paint(Graphics2D g, MapView mv, Bounds bounds) {

        g.setColor(Color.blue);
        g.drawLine(0, 70, 100, 70);
        g.drawRect(150, 70, 50, 70);
        g.drawRoundRect(250, 70, 50, 70, 6, 6);
        g.drawOval(350, 70, 50, 70);
        int[] vx1 = {500, 550, 450};
        int[] vy1 = {70, 120, 120};
        g.drawPolygon(vx1, vy1, 3);

        g.setColor(Color.red);
        g.fillRect(150, 270, 50, 70);
        g.fillRoundRect(250, 270, 50, 70, 6, 6);
        g.fillOval(350, 270, 50, 70);
        int[] vx2 = {500, 550, 450};
        int[] vy2 = {270, 320, 320};
        g.fillPolygon(vx2, vy2, 3);
    }

}
