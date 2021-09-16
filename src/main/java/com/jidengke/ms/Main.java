package com.jidengke.ms;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Main
 *
 * @author jidengke
 * @date 2021/9/16 17:04
 */
public class Main {
    public static void main(String[] args) {
        int xCount = 50;
        int yCount = 50;
        int imgWidth = 1000;
        int imgHeight = 1000;
        String out = "C:\\Users\\jidengke\\Desktop\\c\\" + System.currentTimeMillis() + ".png";


        double[][] gridData = new double[yCount][xCount];

        for (int y = 0; y < yCount; y++) {
            for (int x = 0; x < xCount; x++) {
                gridData[y][x] = Math.round(Math.random() * 100);
                System.out.println(gridData[y][x]);
            }
        }
        Cell[][] cellGrid = new Cell[yCount - 1][xCount - 1];

        for (int y1 = 0; y1 < yCount - 1; y1++) {
            for (int x1 = 0; x1 < xCount - 1; x1++) {
                int x2 = x1 + 1;
                int y2 = y1 + 1;
                Cell cell = new Cell();
                cell.x1 = x1;
                cell.y1 = y1;
                cell.x2 = x2;
                cell.y2 = y2;
                cell.v1 = gridData[y1][x1];
                cell.v2 = gridData[y1][x2];
                cell.v3 = gridData[y2][x2];
                cell.v4 = gridData[y2][x1];
                cellGrid[y1][x1] = cell;
            }
        }

        //计算等压线
        BufferedImage bufferedImage = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D) bufferedImage.getGraphics();
        double[] dataStart = new double[]{0, 0};
        double[] dataEnd = new double[]{xCount - 1, yCount - 1};

        List<List<double[]>> lists = MarchingSquares.getContour(xCount, yCount, g2d, cellGrid, 50);
        for (List<double[]> list : lists) {
            GeneralPath path = new GeneralPath();
            double[] s = getPosition(list.get(0), dataStart, dataEnd, imgWidth, imgHeight);
            path.moveTo(s[0], s[1]);
            for (int i = 1; i < list.size(); i++) {
                double[] p = getPosition(list.get(i), dataStart, dataEnd, imgWidth, imgHeight);
                path.lineTo(p[0], p[1]);
            }
            g2d.setColor(Color.BLUE);
            g2d.draw(path);
        }
        g2d.setColor(Color.BLACK);
        g2d.drawRect(0, 0, imgWidth - 1, imgHeight - 1);
        g2d.dispose();
        try {
            ImageIO.write(bufferedImage, "png", new File(out));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 计算网格点在图片上得像素坐标
     *
     * @param point  point
     * @param start  start
     * @param end    end
     * @param width  width
     * @param height height
     * @return double[]
     */
    public static double[] getPosition(double[] point, double[] start, double[] end, double width, double height) {
        return new double[]{
                (width * (point[0] - start[0])) / (end[0] - start[0]),
                height - ((height * (point[1] - start[1])) / (end[1] - start[1]))
        };
    }
}
