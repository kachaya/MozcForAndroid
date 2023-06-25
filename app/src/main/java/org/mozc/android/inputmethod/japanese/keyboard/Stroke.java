/*
 * Copyright 2023  kachaya
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mozc.android.inputmethod.japanese.keyboard;

import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.graphics.RectF;

import java.util.ArrayList;

public class Stroke {

    public PointF[] points;
    public RectF bounds;

    public static class Result {
        public char a;
        public char n;
        public char p;
        public double distance;
        public int rotate;
        public Stroke modelStroke;
        public Stroke inputStroke;

        public Result(char a, char n, char p, double distance, int rotate, Stroke modelStroke, Stroke inputStroke) {
            this.a = a;
            this.n = n;
            this.p = p;
            this.distance = distance;
            this.rotate = rotate;
            this.modelStroke = modelStroke;
            this.inputStroke = inputStroke;
        }
    }

    public static class Chars {
        public char a;
        public char n;
        public char p;

        public Chars(char a, char n, char p) {
            this.a = a;
            this.n = n;
            this.p = p;
        }
    }

    public static class Model {
        public Chars[] chars;
        public Path path;
        public Stroke stroke;

        public Model(Chars[] chars, String svg) {
            this.chars = chars;
            this.path = createPath(svg);
            this.stroke = new Stroke(path);
        }
    }

    private static final ArrayList<Model> models = new ArrayList<Model>() {
        {
            // 縦横
            add(new Model(new Chars[]{
                    new Chars('i', '1', '\''),
                    new Chars(' ', ' ', '-'),
                    new Chars('C', 'N', '!'),
                    new Chars('B', 'B', 'B'),
            }, "m 6,0 v 12"));   // '1'
            // 斜め
            add(new Model(new Chars[]{
                    new Chars('E', 'E', ','),
                    new Chars('S', 'S', '\\'),
                    new Chars('M', 'M', '/'),
                    null,
            }, "m 12,0 -12,12"));   // ','
            // 縦横往復
            add(new Model(new Chars[]{
                    new Chars('D', 'D', ':'),
                    new Chars('R', 'R', '_'),
                    new Chars('U', 'U', '|'),
                    new Chars('L', 'L', 'N'),
            }, "m 6,0 v 12 -12"));      // ':'
            // 斜め往復
            add(new Model(new Chars[]{
                    new Chars('N', '8', ';'),
                    null,
                    new Chars('P', 'P', 'P'),
                    null,
            }, "m 12,0 -12,12 12,-12"));      // ';'
            // L型
            add(new Model(new Chars[]{
                    new Chars('l', '4', '('),
                    null,
                    null,
                    new Chars('f', 'N', 'N'),
            }, "m 0,0 v 12 h 12"));      // 'l'
            add(new Model(new Chars[]{
                    new Chars('t', '7', '?'),
                    new Chars('f', 'N', 'N'),
                    null,
                    new Chars('j', 'N', ','),
            }, "m 0,0 h 12 v 12"));      // 't'
            // <>
            add(new Model(new Chars[]{
                    new Chars('N', 'N', '<'),
                    new Chars('v', '0', 'N'),
                    new Chars('t', '7', ')'),
                    new Chars('a', '1', '^'),
            }, "m 12,12 -12,-6 12,-6"));            // '<'
            add(new Model(new Chars[]{
                    new Chars('N', 'N', '>'),
                    new Chars('N', 'N', '^'),
                    new Chars('c', '4', '('),
                    new Chars('u', '0', 'N'),
                    null,
            }, "m 0,12 12,-6 -12,-6"));            // '>'
            // ()
            add(new Model(new Chars[]{
                    new Chars('c', '4', '('),
                    new Chars('u', '0', 'N'),
                    new Chars('N', 'N', '>'),
                    new Chars('N', 'N', '^'),
            }, "m 9,0 -6,3 v 6 l 6,3"));      // '('
            add(new Model(new Chars[]{
                    new Chars('t', '7', ')'),
                    new Chars('a', '1', '^'),
                    new Chars('N', 'N', '<'),
                    new Chars('v', '0', 'N'),
            }, "m 3,0 6,3 v 6 l -6,3"));      // ')'
            add(new Model(new Chars[]{
                    new Chars('c', '4', '('),
                    new Chars('u', '0', 'N'),
                    new Chars('N', 'N', '>'),
                    new Chars('N', 'N', '^'),
            }, "m 12,0 h -8 l -4,4 v 4 l 4,4 h 8"));      // '('
            add(new Model(new Chars[]{
                    new Chars('t', '7', ')'),
                    new Chars('a', '1', '^'),
                    new Chars('N', 'N', '<'),
                    new Chars('v', '0', 'N'),
            }, "m 0,0 h 8 l 4,4 v 4 l -4,4 h -8"));      // ')'
            // '{','}'
            add(new Model(new Chars[]{
                    new Chars('e', '9', '{'),
                    new Chars('w', 'N', 'N'),
                    new Chars('N', 'N', ']'),
                    null,
            }, "m 12,0 -12,3 12,3 -12,3 12,3"));   // '{'
            add(new Model(new Chars[]{
                    new Chars('b', '3', '}'),
                    new Chars('m', 'N', 'N'),
                    new Chars('N', 'N', '['),
                    null,
            }, "m 0,0 12,3 -12,3 12,3 -12,3")); // '}'
            // 'k'
            add(new Model(new Chars[]{
                    new Chars('k', 'N', '+'),
                    new Chars('y', '8', '&'),
                    null,
                    null,
            }, "m 12,0 -9,9 -3,-3 3,-3 9,9"));   // 'k'
            // 'x'
            add(new Model(new Chars[]{
                    new Chars('x', '6', '*'),
                    null,
                    null,
                    null,
            }, "m 0,0 9,9 3,-3 -3,-3 -9,9"));   // 'x'
            // Z型
            add(new Model(new Chars[]{
                    new Chars('z', '2', '='),
                    new Chars('n', 'N', '"'),
                    null,
                    new Chars('N', 'N', '~'),
            }, "m 0,0 h 12 l -12,12 12,0"));   // 'z'
            add(new Model(new Chars[]{
                    new Chars('h', '9', '#'),
                    null,
                    new Chars('N', 'N', '~'),
                    new Chars('s', '5', '$'),
            }, "m 0,0 v 12 l 12,-12 v 12"));   // '#'
            // '2','5'
            add(new Model(new Chars[]{
                    new Chars('z', '2', '='),
                    new Chars('n', 'N', '"'),
                    null,
                    new Chars('N', 'N', '~'),
            }, "m 2,0 h 8 v 6 h -8 v 6 h 8"));   // '2'
            add(new Model(new Chars[]{
                    new Chars('s', '5', '$'),
                    new Chars('h', '9', '#'),
                    null,
                    new Chars('N', 'N', '~'),
            }, "m 10,0 h -8 v 6 h 8 l 0,6 h -8"));   // '5'
            // 左回り o 0 @
            add(new Model(new Chars[]{
                    new Chars('o', '0', '@'),
                    null,
                    null,
                    null,
            }, "m 6,0 -4,2 -2,4 2,4 4,2 4,-2 2,-4 -2,-4 -4,-2"));      // 上から左回り
            add(new Model(new Chars[]{
                    new Chars('o', '0', '@'),
                    null,
                    null,
                    null,
            }, "m 2,2 -2,4 2,4 4,2 4,-2 2,-4 -2,-4 -4,-2 -4,2"));      // 左上から左回り
            add(new Model(new Chars[]{
                    new Chars('o', '0', '@'),
                    null,
                    null,
                    null,
            }, "m 10,2 -4,-2 -4,2 -2,4 2,4 4,2 4,-2 2,-4 -2,-4"));      // 右上から左回り
            // 右回り o 0 @
            add(new Model(new Chars[]{
                    new Chars('o', '0', '@'),
                    null,
                    null,
                    null,
            }, "m 6,0 4,2 2,4 -2,4 -4,2 -4,-2 -2,-4 2,-4 4,-2"));      // 上から右回り
            add(new Model(new Chars[]{
                    new Chars('o', '0', '@'),
                    null,
                    null,
                    null,
            }, "m 10,2 2,4 -2,4 -4,2 -4,-2 -2,-4 2,-4 4,-2 4,2"));      // 右上から右回り
            add(new Model(new Chars[]{
                    new Chars('o', '0', '@'),
                    null,
                    null,
                    null,
            }, "m 2,2 4,-2 4,2 2,4 -2,4 -4,2 -4,-2 -2,-4 2,-4"));      // 左上から右回り
            // 'b'
            add(new Model(new Chars[]{
                    new Chars('b', 'N', 'N'),
                    null, null, null
            }, "m 362,120 v 12 -12 h 8 v 3 l -8,3 8,3 v 3 h -8")); // 左上
            add(new Model(new Chars[]{
                    new Chars('b', 'N', 'N'),
                    null, null, null
            }, "m 2,12 v -12 l 8,0 0,3 -8,3 8,3 0,3 -8,0")); // 左下
            // 'd'
            add(new Model(new Chars[]{
                    new Chars('d', 'N', 'N'),
                    null, null, null
            }, "m 2,0 v 12 -12 l 8,4 v 4 l -8,4")); // 左上
            add(new Model(new Chars[]{
                    new Chars('d', 'N', 'N'),
                    null, null, null
            }, "m 2,12 v -12 l 8,4 v 4 l -8,4")); // 左下
            // 'p'
            add(new Model(new Chars[]{
                    new Chars('p', 'N', 'N'),
                    null, null, null
            }, "m 2,0 v 12 -12 l 8,0 0,4 h -8")); // 左上
            add(new Model(new Chars[]{
                    new Chars('p', 'N', 'N'),
                    null, null, null
            }, "m 2,12 v -12 l 8,0 v 4 h -8")); // 左下
            // 'r'
            add(new Model(new Chars[]{
                    new Chars('r', 'N', 'N'),
                    null, null, null
            }, "m 2,0 v 12 -12 l 8,0 v 4 h -8 l 8,8")); // 左上
            add(new Model(new Chars[]{
                    new Chars('r', 'N', 'N'),
                    null, null, null
            }, "m 2,12 v -12 h 8 v 4 h -8 l 8,8")); // 左下
            // 縦棒で始まる'5'
            add(new Model(new Chars[]{
                    new Chars('s', '5', '$'),
                    null, null, null
            }, "m 2,0 v 8 h 8 v 4 h -8"));
            // 数字の'6'
            add(new Model(new Chars[]{
                    new Chars('g', '6', 'N'),
                    null, null, null
            }, "m 8,0 -4,0 -2,8 v 4 h 8 v -4 h -8"));
            add(new Model(new Chars[]{
                    new Chars('y', '8', '&'),
                    null, null, null
            }, "m 10,3 -4,-3 -4,3 8,6 -4,3 -4,-3 8,-6")); // 右上からの'8'
            add(new Model(new Chars[]{
                    new Chars('y', '8', '&'),
                    null, null, null
            }, "m 2,3 4,-3 4,3 -8,6 4,3 4,-3 -8,-6")); // 左上からの'8'
            // '9'
            add(new Model(new Chars[]{
                    new Chars('s', '9', 'N'),
                    null, null, null
            }, "m 0,1 -8,-1 0,5 8,-3 -3,10")); // 数字の'9'
            // 'G'
            add(new Model(new Chars[]{
                    new Chars('g', '6', 'N'),
                    null, null, null
            }, "m 8,0 -4,0 -2,8 v 4 h 8 v -4 h -8 8")); // 大文字の'G'
            // 小文字の'h'
            add(new Model(new Chars[]{
                    new Chars('h', '9', '#'),
                    null, null, null
            }, "m 3,0 v 12 l 0,-4 h 6 l 0,4"));
            // 左上から始まる'm'
            add(new Model(new Chars[]{
                    new Chars('m', 'N', 'N'),
                    null, null, null
            }, "m 360,40 v 12 l 3,-12 3,12 3,-12 3,12"));
            // 'q'
            add(new Model(new Chars[]{
                    new Chars('q', 'N', 'N'),
                    null,
                    null,
                    new Chars('N', '9', 'N'),
            }, "m 4,0 -4,4 v 8 l 6,0 0,-8 -2,-4 h 8")); // 'q'
            // 'v'
            add(new Model(new Chars[]{
                    new Chars('v', 'N', 'N'),
                    null,
                    null,
                    new Chars('N', '9', 'N'),
            }, "m 0,0 2,12 2,-12 8,0")); // 左上から始まる'V'
            // 小文字の'y'
            add(new Model(new Chars[]{
                    new Chars('y', '8', '&'),
                    null, null, null
            }, "m 2,0 0,5 6,-5 0,12 h -6 l 8,-8"));
            // '%'
            add(new Model(new Chars[]{
                    new Chars('N', 'N', '%'),
                    null, null, null
            }, "m 0,0 5,8 -2,4 -2,-4 4,-6 h 2 l 4,6 -2,4 -2,-4 5,-8"));
            // '?'
            add(new Model(new Chars[]{
                    new Chars('t', '7', '?'),
                    new Chars('f', 'N', 'N'),
                    null,
                    new Chars('j', 'N', ','),
            }, "m 2,3 4,-3 4,3 -6,9"));      // '?'
        }
    };

    /*
     * 簡易SVG→Path変換
     */
    private static Path createPath(String svg) {
        Path path = new Path();
        String[] ss = svg.split(" ");
        String state = "";
        float dx, x1, x2, x3;
        float dy, y1, y2, y3;
        String[] cs;
        for (int i = 0; i < ss.length; i++) {
            switch (ss[i]) {
                case "m":
                case "h":
                case "v":
                case "c":
                case "l":
                    state = ss[i];
                    break;
                default:
                    switch (state) {
                        case "m":
                            cs = ss[i].split(",");
                            dx = Float.parseFloat(cs[0]);
                            dy = Float.parseFloat(cs[1]);
                            path.rMoveTo(dx, dy);
                            state = "l";
                            break;
                        case "l":
                            cs = ss[i].split(",");
                            dx = Float.parseFloat(cs[0]);
                            dy = Float.parseFloat(cs[1]);
                            path.rLineTo(dx, dy);
                            break;
                        case "h":
                            dx = Float.parseFloat(ss[i]);
                            dy = 0;
                            path.rLineTo(dx, dy);
                            break;
                        case "v":
                            dx = 0;
                            dy = Float.parseFloat(ss[i]);
                            path.rLineTo(dx, dy);
                            break;
                        case "c":
                            state = "c1";
                            break;
                        case "c1":
                            state = "c2";
                            break;
                        case "c2":
                            cs = ss[i - 2].split(",");
                            x1 = Float.parseFloat(cs[0]);
                            y1 = Float.parseFloat(cs[1]);
                            cs = ss[i - 1].split(",");
                            x2 = Float.parseFloat(cs[0]);
                            y2 = Float.parseFloat(cs[1]);
                            cs = ss[i].split(",");
                            x3 = Float.parseFloat(cs[0]);
                            y3 = Float.parseFloat(cs[1]);
                            path.rCubicTo(x1, y1, x2, y2, x3, y3);
                            state = "c";
                            break;
                    }
                    break;
            }
        }
        return path;
    }

    public Stroke(Path path) {
        points = new PointF[96];
        RectF rect = new RectF();

        path.computeBounds(rect, true);
        float xOrigin = rect.centerX();
        float yOrigin = rect.centerY();
        PathMeasure pm = new PathMeasure(path, false);
        float pl = pm.getLength();
        float[] pos = new float[2];
        double dMax = -Double.MAX_VALUE;
        for (int i = 0; i < points.length; i++) {
            float distance = (pl * i) / (float) (points.length - 1);
            pm.getPosTan(distance, pos, null);
            float x = pos[0] - xOrigin;
            float y = pos[1] - yOrigin;
            points[i] = new PointF(x, y);
            dMax = Math.max(dMax, Math.sqrt(x * x + y * y));
        }

        float xMin, xMax;
        float yMin, yMax;
        xMin = yMin = Float.MAX_VALUE;
        xMax = yMax = -Float.MAX_VALUE;
        float ratio = (float) (100.0f / dMax);
        for (PointF point : points) {
            point.x *= ratio;
            point.y *= ratio;
            xMin = Math.min(xMin, point.x);
            yMin = Math.min(yMin, point.y);
            xMax = Math.max(xMax, point.x);
            yMax = Math.max(yMax, point.y);
        }
        bounds = new RectF(xMin, yMin, xMax, yMax);
    }

    private double distance(Stroke dest) {
        double d = 0;
        for (int i = 0; i < points.length; i++) {
            float dx = dest.points[i].x - this.points[i].x;
            float dy = dest.points[i].y - this.points[i].y;
            d += Math.sqrt(dx * dx + dy * dy);
        }
        return d / points.length;
    }

    public static Result recognize(Path path) {
        double bestDistance = Double.MAX_VALUE;
        Model bestModel = null;
        int bestRotate = 0;
        int bestIndex = 0;
        Stroke inputStroke = new Stroke(path);
        Matrix matrixRotate = new Matrix();
        for (int index = 0; index < 4; index++) {
            for (int rotate = -15; rotate <= 15; rotate += 5) {
                matrixRotate.reset();
                matrixRotate.postRotate(90 * index + rotate);
                Path testPath = new Path(path);
                testPath.transform(matrixRotate);
                Stroke testStroke = new Stroke(testPath);
                for (Model model : models) {
                    double distance = testStroke.distance(model.stroke);
                    if (bestDistance > distance) {
                        bestDistance = distance;
                        bestModel = model;
                        bestIndex = index;
                        bestRotate = rotate;
                    }
                }
            }
        }
        char a = 'N';
        char n = 'N';
        char p = 'N';
        Chars cs = bestModel.chars[bestIndex];
        if (cs != null) {
            a = cs.a;
            n = cs.n;
            p = cs.p;
        }
//        Log.i("Model", "a=" + bestModel.chars[0].a + ",n=" + bestModel.chars[0].n + ",p=" + bestModel.chars[0].p + "," + bestDistance);
        matrixRotate.reset();
        matrixRotate.postRotate(-90 * bestIndex);
        Path modelPath = new Path(bestModel.path);
        modelPath.transform(matrixRotate);
        Stroke modelStroke = new Stroke(modelPath);
        return new Result(a, n, p, bestDistance, bestRotate, modelStroke, inputStroke);
    }
}
