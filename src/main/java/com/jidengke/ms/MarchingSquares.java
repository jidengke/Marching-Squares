package com.jidengke.ms;

import java.awt.*;
import java.util.List;
import java.util.*;

/**
 * MarchingSquares
 *
 * @author jidengke
 * @date 2021/9/13 10:06
 */
public class MarchingSquares {

    public static double[][][][] cases = new double[][][][]{
            {},
            {{{0.5, 0}, {0, 0.5}}},
            {{{1.0, 0.5}, {0.5, 0}}},
            {{{1.0, 0.5}, {0, 0.5}}},
            {{{0.5, 1.0}, {1.0, 0.5}}},
            {{{0.5, 0}, {0, 0.5}}, {{0.5, 1.0}, {1.0, 0.5}}},
            {{{0.5, 1.0}, {0.5, 0}}},
            {{{0.5, 1.0}, {0, 0.5}}},
            {{{0, 0.5}, {0.5, 1.0}}},
            {{{0.5, 0}, {0.5, 1.0}}},
            {{{0, 0.5}, {0.5, 1.0}}, {{1.0, 0.5}, {0.5, 0}}},
            {{{1.0, 0.5}, {0.5, 1.0}}},
            {{{0, 0.5}, {1.0, 0.5}}},
            {{{0.5, 0}, {1.0, 0.5}}},
            {{{0, 0.5}, {0.5, 0}}},
            {}
    };

    public static List<List<double[]>> getContour(int xCount, int yCount, Graphics2D g2d,  Cell[][] cellGrid, double contourValue) {
        List<double[][]> lineList = new ArrayList<>();
        for (int y = 0; y < yCount - 1; y++) {
            for (int x = 0; x < xCount - 1; x++) {
                g2d.setColor(Color.BLACK);
                g2d.setStroke(new BasicStroke(2));
                double[][][] lines = getCaseLine(cellGrid[y][x], contourValue);
                lineList.addAll(Arrays.asList(lines));
            }
        }

        //拼接曲线 构造返回数据
        List<List<double[]>> resList = new ArrayList<>();
        Map<String, double[][]> startLineMap = new HashMap<>(lineList.size());
        Map<String, double[][]> endLineMap = new HashMap<>(lineList.size());
        for (double[][] line : lineList) {
            String startKey = line[0][0] + "-" + line[0][1];
            String endKey = line[1][0] + "-" + line[1][1];
            startLineMap.put(startKey, line);
            endLineMap.put(endKey, line);
        }

        while (startLineMap.size() > 0) {
            String mapStart = (String) startLineMap.keySet().toArray()[0];
            double[][] line1 = startLineMap.get(mapStart);
            List<double[]> list = new ArrayList<>(Arrays.asList(line1));
            remove(startLineMap, endLineMap, line1);
            boolean hasNext = true;
            while (hasNext) {
                String startKey = list.get(0)[0] + "-" + list.get(0)[1];
                String endKey = list.get(list.size() - 1)[0] + "-" + list.get(list.size() - 1)[1];

                double[][] forward = endLineMap.get(startKey);
                if (forward != null) {
                    remove(startLineMap, endLineMap, forward);
                    list.add(0, forward[0]);
                }

                double[][] backward = startLineMap.get(endKey);
                if (backward != null) {
                    remove(startLineMap, endLineMap, backward);
                    list.add(backward[1]);
                }

                if (forward == null && backward == null) {
                    hasNext = false;
                }
            }
            resList.add(list);
        }
        return resList;
    }

    public static void remove(Map<String, double[][]> startLineMap, Map<String, double[][]> endLineMap, double[][] line) {
        String startKey = line[0][0] + "-" + line[0][1];
        String endKey = line[1][0] + "-" + line[1][1];
        startLineMap.remove(startKey);
        endLineMap.remove(endKey);
    }

    public static double[][][] getCaseLine(Cell cell, double value) {
        int caseIndex = getCaseType(cell, value);
        double[][][] lines = cases[caseIndex];
        double[][][] res = new double[lines.length][2][2];
        for (int i = 0; i < lines.length; i++) {
            double[][] line = lines[i];
            double[] start = new double[]{line[0][0] + cell.x1, line[0][1] + cell.y1};
            double[] end = new double[]{line[1][0] + cell.x1, line[1][1] + cell.y1};
            res[i][0] = start;
            res[i][1] = end;
        }
        return res;
    }

    public static int getCaseType(Cell cell, double value) {
        int a = cell.v1 >= value ? 0 : 1;
        int b = cell.v2 >= value ? 0 : 1;
        int c = cell.v3 >= value ? 0 : 1;
        int d = cell.v4 >= value ? 0 : 1;
        return a | b << 1 | c << 2 | d << 3;
    }

}
