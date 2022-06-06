// Copyright 2021 The MediaPipe Authors.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.mediapipe.examples.facemesh;

import static com.google.mediapipe.solutions.facemesh.FaceMeshConnections.*;

import android.opengl.GLES20;

import androidx.appcompat.app.AppCompatActivity;

import com.google.common.collect.ImmutableSet;
import com.google.mediapipe.formats.proto.LandmarkProto.NormalizedLandmark;
import com.google.mediapipe.solutioncore.ResultGlRenderer;
import com.google.mediapipe.solutions.facemesh.FaceMeshResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.List;

/** A custom implementation of {@link ResultGlRenderer} to render {@link FaceMeshResult}. */
public class FaceMeshResultGlRenderer extends AppCompatActivity implements ResultGlRenderer<FaceMeshResult> {
  private static final String TAG = "FaceMeshResultGlRenderer";

  private static final float[] TESSELATION_COLOR = new float[] {0.75f, 0.75f, 0.75f, 0.5f};
  private static final int TESSELATION_THICKNESS = 5;
  private static final float[] RIGHT_EYE_COLOR = new float[] {1f, 0.2f, 0.2f, 1f};
  private static final int RIGHT_EYE_THICKNESS = 8;
  private static final float[] RIGHT_EYEBROW_COLOR = new float[] {1f, 0.2f, 0.2f, 1f};
  private static final int RIGHT_EYEBROW_THICKNESS = 8;
  private static final float[] LEFT_EYE_COLOR = new float[] {0.2f, 1f, 0.2f, 1f};
  private static final int LEFT_EYE_THICKNESS = 8;
  private static final float[] LEFT_EYEBROW_COLOR = new float[] {0.2f, 1f, 0.2f, 1f};
  private static final int LEFT_EYEBROW_THICKNESS = 8;
  private static final float[] FACE_OVAL_COLOR = new float[] {0.9f, 0.9f, 0.9f, 1f};
  private static final int FACE_OVAL_THICKNESS = 8;
  private static final float[] LIPS_COLOR = new float[] {0.9f, 0.9f, 0.9f, 1f};
  private static final int LIPS_THICKNESS = 8;
  private static final float[] TEST_COLOR = new float[] {1f, 0f, 0f, 1f};
  private static final int TEST_THICKNESS = 8;
  private static final String VERTEX_SHADER =
      "uniform mat4 uProjectionMatrix;\n"
          + "attribute vec4 vPosition;\n"
          + "void main() {\n"
          + "  gl_Position = uProjectionMatrix * vPosition;\n"
          + "  gl_PointSize = 12.0;\n"
          + "}";
  private static final String FRAGMENT_SHADER =
      "precision mediump float;\n"
          + "uniform vec4 uColor;\n"
          + "void main() {\n"
          + "  gl_FragColor = uColor;\n"
          + "}";
  private int program;
  private int positionHandle;
  private int projectionMatrixHandle;
  private int colorHandle;

  private int loadShader(int type, String shaderCode) {
    int shader = GLES20.glCreateShader(type);
    GLES20.glShaderSource(shader, shaderCode);
    GLES20.glCompileShader(shader);
    return shader;
  }

  @Override
  public void setupRendering() {
    program = GLES20.glCreateProgram();
    int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, VERTEX_SHADER);
    int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, FRAGMENT_SHADER);
    GLES20.glAttachShader(program, vertexShader);
    GLES20.glAttachShader(program, fragmentShader);
    GLES20.glLinkProgram(program);
    positionHandle = GLES20.glGetAttribLocation(program, "vPosition");
    projectionMatrixHandle = GLES20.glGetUniformLocation(program, "uProjectionMatrix");
    colorHandle = GLES20.glGetUniformLocation(program, "uColor");
  }

  GlobalVariable gv = GlobalVariable.getInstance();
  String[] acupoint = gv.getAcupoint();
  JSONArray acuJsonArry = gv.getAcuJson();
  int[] acuIdx = gv.getAcuIdx();

  @Override
  public void renderResult(FaceMeshResult result, float[] projectionMatrix) {
    if (result == null) {
      return;
    }
    GLES20.glUseProgram(program);
    GLES20.glUniformMatrix4fv(projectionMatrixHandle, 1, false, projectionMatrix, 0);

//    Log.i("acupoint", Arrays.toString(acupoint));

    int numFaces = result.multiFaceLandmarks().size();
    for (int i = 0; i < numFaces; ++i) {
      for (int idx : acuIdx) {
        try {
          JSONObject acu_data = (JSONObject) acuJsonArry.getJSONObject(idx).get("穴道位置");
          JSONArray jsonArray = acu_data.getJSONArray("內容");
          int[] points = new int[jsonArray.length()];
          for (int j=0; j<jsonArray.length(); j++) {
            points[j] = jsonArray.getInt(j);
          }
          if (acu_data.getInt("模式") == 1) {
            for (int j=0; j<acu_data.getInt("數量"); j++) {
              drawPoint(
                      result.multiFaceLandmarks().get(i).getLandmarkList(),
                      points[j],
                      TEST_COLOR,
                      TEST_THICKNESS);
            }
          } else {
            for (int j=0; j<acu_data.getInt("數量"); j++) {
              int[] point = {points[j*2], points[j*2+1]};
              drawAvgPoint(
                      result.multiFaceLandmarks().get(i).getLandmarkList(),
                      point,
                      TEST_COLOR,
                      TEST_THICKNESS);
            }
          }
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }






      /*int[] L = new int[2];
      int[] R = new int[2];
      float[] prop = new float[2];
      for(String s : acupoint) {
        switch (s){
          case "絲竹空":
            L[0] = 46;
            L[1] = 70;
            R[0] = 276;
            R[1] = 300;
            drawAvgPoint(
                    result.multiFaceLandmarks().get(i).getLandmarkList(),
                    L,
                    TEST_COLOR,
                    TEST_THICKNESS);
            drawAvgPoint(
                    result.multiFaceLandmarks().get(i).getLandmarkList(),
                    R,
                    TEST_COLOR,
                    TEST_THICKNESS);
            break;
          case "印堂":
            drawPoint(
                    result.multiFaceLandmarks().get(i).getLandmarkList(),
                    9,
                    TEST_COLOR,
                    TEST_THICKNESS);
            break;
          case "魚腰":
            L[0] = 65;
            L[1] = 66;
            R[0] = 295;
            R[1] = 296;
            drawAvgPoint(
                    result.multiFaceLandmarks().get(i).getLandmarkList(),
                    L,
                    TEST_COLOR,
                    TEST_THICKNESS);
            drawAvgPoint(
                    result.multiFaceLandmarks().get(i).getLandmarkList(),
                    R,
                    TEST_COLOR,
                    TEST_THICKNESS);
            break;
          case "球後":
            drawPoint(
                    result.multiFaceLandmarks().get(i).getLandmarkList(),
                    110,
                    TEST_COLOR,
                    TEST_THICKNESS);
            drawPoint(
                    result.multiFaceLandmarks().get(i).getLandmarkList(),
                    339,
                    TEST_COLOR,
                    TEST_THICKNESS);
            break;
          case "上迎香":
            drawPoint(
                    result.multiFaceLandmarks().get(i).getLandmarkList(),
                    198,
                    TEST_COLOR,
                    TEST_THICKNESS);
            drawPoint(
                    result.multiFaceLandmarks().get(i).getLandmarkList(),
                    420,
                    TEST_COLOR,
                    TEST_THICKNESS);
            break;
          case "俠承漿":
            L[0] = 182;
            L[1] = 204;
            R[0] = 406;
            R[1] = 424;
            drawAvgPoint(
                    result.multiFaceLandmarks().get(i).getLandmarkList(),
                    L,
                    TEST_COLOR,
                    TEST_THICKNESS);
            drawAvgPoint(
                    result.multiFaceLandmarks().get(i).getLandmarkList(),
                    R,
                    TEST_COLOR,
                    TEST_THICKNESS);
            break;
          case "睛明":
            drawPoint(
                    result.multiFaceLandmarks().get(i).getLandmarkList(),
                    243,
                    TEST_COLOR,
                    TEST_THICKNESS);
            drawPoint(
                    result.multiFaceLandmarks().get(i).getLandmarkList(),
                    463,
                    TEST_COLOR,
                    TEST_THICKNESS);
            break;
          case "攢竹":
            drawPoint(
                    result.multiFaceLandmarks().get(i).getLandmarkList(),
                    55,
                    TEST_COLOR,
                    TEST_THICKNESS);
            drawPoint(
                    result.multiFaceLandmarks().get(i).getLandmarkList(),
                    285,
                    TEST_COLOR,
                    TEST_THICKNESS);
            break;
          case "瞳子髎":
            L[0] = 113;
            L[1] = 226;
            R[0] = 342;
            R[1] = 446;
            drawAvgPoint(
                    result.multiFaceLandmarks().get(i).getLandmarkList(),
                    L,
                    TEST_COLOR,
                    TEST_THICKNESS);
            drawAvgPoint(
                    result.multiFaceLandmarks().get(i).getLandmarkList(),
                    R,
                    TEST_COLOR,
                    TEST_THICKNESS);
            break;
          case "陽白":
            drawPoint(
                    result.multiFaceLandmarks().get(i).getLandmarkList(),
                    69,
                    TEST_COLOR,
                    TEST_THICKNESS);
            drawPoint(
                    result.multiFaceLandmarks().get(i).getLandmarkList(),
                    299,
                    TEST_COLOR,
                    TEST_THICKNESS);
            break;
          case "承泣":
            drawPoint(
                    result.multiFaceLandmarks().get(i).getLandmarkList(),
                    23,
                    TEST_COLOR,
                    TEST_THICKNESS);
            drawPoint(
                    result.multiFaceLandmarks().get(i).getLandmarkList(),
                    253,
                    TEST_COLOR,
                    TEST_THICKNESS);
            break;
          case "四白":
            drawPoint(
                    result.multiFaceLandmarks().get(i).getLandmarkList(),
                    119,
                    TEST_COLOR,
                    TEST_THICKNESS);
            drawPoint(
                    result.multiFaceLandmarks().get(i).getLandmarkList(),
                    348,
                    TEST_COLOR,
                    TEST_THICKNESS);
            break;
          case "巨髎":
            L[0] = 203;
            L[1] = 205;
            R[0] = 423;
            R[1] = 425;
            prop[0] = 0.5F;
            prop[1] = 0.5F;
            drawProPoint(
                    result.multiFaceLandmarks().get(i).getLandmarkList(),
                    L,
                    prop,
                    TEST_COLOR,
                    TEST_THICKNESS);
            drawProPoint(
                    result.multiFaceLandmarks().get(i).getLandmarkList(),
                    R,
                    prop,
                    TEST_COLOR,
                    TEST_THICKNESS);
            break;
          case "地倉":
            L[0] = 57;
            L[1] = 62;
            R[0] = 291;
            R[1] = 287;
            drawAvgPoint(
                    result.multiFaceLandmarks().get(i).getLandmarkList(),
                    L,
                    TEST_COLOR,
                    TEST_THICKNESS);
            drawAvgPoint(
                    result.multiFaceLandmarks().get(i).getLandmarkList(),
                    R,
                    TEST_COLOR,
                    TEST_THICKNESS);
            break;
          case "顴髎":
            L[0] = 50;
            L[1] = 187;
            R[0] = 280;
            R[1] = 411;
            drawAvgPoint(
                    result.multiFaceLandmarks().get(i).getLandmarkList(),
                    L,
                    TEST_COLOR,
                    TEST_THICKNESS);
            drawAvgPoint(
                    result.multiFaceLandmarks().get(i).getLandmarkList(),
                    R,
                    TEST_COLOR,
                    TEST_THICKNESS);
            break;
          case "水溝":
            drawPoint(
                    result.multiFaceLandmarks().get(i).getLandmarkList(),
                    164,
                    TEST_COLOR,
                    TEST_THICKNESS);
            break;
          case "禾髎":
            drawPoint(
                    result.multiFaceLandmarks().get(i).getLandmarkList(),
                    165,
                    TEST_COLOR,
                    TEST_THICKNESS);
            drawPoint(
                    result.multiFaceLandmarks().get(i).getLandmarkList(),
                    391,
                    TEST_COLOR,
                    TEST_THICKNESS);
            break;
          case "迎香":
            drawPoint(
                    result.multiFaceLandmarks().get(i).getLandmarkList(),
                    102,
                    TEST_COLOR,
                    TEST_THICKNESS);
            drawPoint(
                    result.multiFaceLandmarks().get(i).getLandmarkList(),
                    358,
                    TEST_COLOR,
                    TEST_THICKNESS);
            break;
          case "承漿":
            drawPoint(
                    result.multiFaceLandmarks().get(i).getLandmarkList(),
                    18,
                    TEST_COLOR,
                    TEST_THICKNESS);
            break;
          case "太陽":
            L[0] = 139;
            L[1] = 156;
            R[0] = 368;
            R[1] = 383;
            drawAvgPoint(
                    result.multiFaceLandmarks().get(i).getLandmarkList(),
                    L,
                    TEST_COLOR,
                    TEST_THICKNESS);
            drawAvgPoint(
                    result.multiFaceLandmarks().get(i).getLandmarkList(),
                    R,
                    TEST_COLOR,
                    TEST_THICKNESS);
            break;
          case "All":
            drawAllPoint(result.multiFaceLandmarks().get(i).getLandmarkList());
            break;
        }
      }*/
      /* // 絲竹空
      L[0] = 46;
      L[1] = 70;
      R[0] = 276;
      R[1] = 300;
      drawAvgPoint(
              result.multiFaceLandmarks().get(i).getLandmarkList(),
              L,
              TEST_COLOR,
              TEST_THICKNESS);
      drawAvgPoint(
              result.multiFaceLandmarks().get(i).getLandmarkList(),
              R,
              TEST_COLOR,
              TEST_THICKNESS);
      // 印堂
      drawPoint(
              result.multiFaceLandmarks().get(i).getLandmarkList(),
              9,
              TEST_COLOR,
              TEST_THICKNESS);
      // 魚腰
      L[0] = 65;
      L[1] = 66;
      R[0] = 295;
      R[1] = 296;
      drawAvgPoint(
              result.multiFaceLandmarks().get(i).getLandmarkList(),
              L,
              TEST_COLOR,
              TEST_THICKNESS);
      drawAvgPoint(
              result.multiFaceLandmarks().get(i).getLandmarkList(),
              R,
              TEST_COLOR,
              TEST_THICKNESS);
      // 球後
      drawPoint(
              result.multiFaceLandmarks().get(i).getLandmarkList(),
              110,
              TEST_COLOR,
              TEST_THICKNESS);
      drawPoint(
              result.multiFaceLandmarks().get(i).getLandmarkList(),
              339,
              TEST_COLOR,
              TEST_THICKNESS);
      // 上迎香
      drawPoint(
              result.multiFaceLandmarks().get(i).getLandmarkList(),
              198,
              TEST_COLOR,
              TEST_THICKNESS);
      drawPoint(
              result.multiFaceLandmarks().get(i).getLandmarkList(),
              420,
              TEST_COLOR,
              TEST_THICKNESS);
      // 夾承漿
      L[0] = 182;
      L[1] = 204;
      R[0] = 406;
      R[1] = 424;
      drawAvgPoint(
              result.multiFaceLandmarks().get(i).getLandmarkList(),
              L,
              TEST_COLOR,
              TEST_THICKNESS);
      drawAvgPoint(
              result.multiFaceLandmarks().get(i).getLandmarkList(),
              R,
              TEST_COLOR,
              TEST_THICKNESS);
      // 睛明
      drawPoint(
          result.multiFaceLandmarks().get(i).getLandmarkList(),
          243,
          TEST_COLOR,
          TEST_THICKNESS);
      drawPoint(
          result.multiFaceLandmarks().get(i).getLandmarkList(),
          463,
          TEST_COLOR,
          TEST_THICKNESS);
      // 攢竹
      drawPoint(
          result.multiFaceLandmarks().get(i).getLandmarkList(),
          55,
          TEST_COLOR,
          TEST_THICKNESS);
      drawPoint(
          result.multiFaceLandmarks().get(i).getLandmarkList(),
          285,
          TEST_COLOR,
          TEST_THICKNESS);
      // 瞳子膠
      L[0] = 113;
      L[1] = 226;
      R[0] = 342;
      R[1] = 446;
      drawAvgPoint(
              result.multiFaceLandmarks().get(i).getLandmarkList(),
              L,
              TEST_COLOR,
              TEST_THICKNESS);
      drawAvgPoint(
              result.multiFaceLandmarks().get(i).getLandmarkList(),
              R,
              TEST_COLOR,
              TEST_THICKNESS);
      // 陽白
      drawPoint(
              result.multiFaceLandmarks().get(i).getLandmarkList(),
              69,
              TEST_COLOR,
              TEST_THICKNESS);
      drawPoint(
              result.multiFaceLandmarks().get(i).getLandmarkList(),
              299,
              TEST_COLOR,
              TEST_THICKNESS);
      // 承泣
      drawPoint(
              result.multiFaceLandmarks().get(i).getLandmarkList(),
              23,
              TEST_COLOR,
              TEST_THICKNESS);
      drawPoint(
              result.multiFaceLandmarks().get(i).getLandmarkList(),
              253,
              TEST_COLOR,
              TEST_THICKNESS);
      // 四白
      drawPoint(
              result.multiFaceLandmarks().get(i).getLandmarkList(),
              119,
              TEST_COLOR,
              TEST_THICKNESS);
      drawPoint(
              result.multiFaceLandmarks().get(i).getLandmarkList(),
              348,
              TEST_COLOR,
              TEST_THICKNESS);
      // 巨膠
      L[0] = 203;
      L[1] = 205;
      R[0] = 423;
      R[1] = 425;
      float[] prop = {0.5F, 0.5F};
      drawProPoint(
              result.multiFaceLandmarks().get(i).getLandmarkList(),
              L,
              prop,
              TEST_COLOR,
              TEST_THICKNESS);
      drawProPoint(
              result.multiFaceLandmarks().get(i).getLandmarkList(),
              R,
              prop,
              TEST_COLOR,
              TEST_THICKNESS);
      // 地倉
      L[0] = 57;
      L[1] = 62;
      R[0] = 291;
      R[1] = 287;
      drawAvgPoint(
              result.multiFaceLandmarks().get(i).getLandmarkList(),
              L,
              TEST_COLOR,
              TEST_THICKNESS);
      drawAvgPoint(
              result.multiFaceLandmarks().get(i).getLandmarkList(),
              R,
              TEST_COLOR,
              TEST_THICKNESS);
//      drawPoint(
//              result.multiFaceLandmarks().get(i).getLandmarkList(),
//              57,
//              TEST_COLOR,
//              TEST_THICKNESS);
//      drawPoint(
//              result.multiFaceLandmarks().get(i).getLandmarkList(),
//              287,
//              TEST_COLOR,
//              TEST_THICKNESS);
      // 顴髎
      L[0] = 50;
      L[1] = 187;
      R[0] = 280;
      R[1] = 411;
      drawAvgPoint(
              result.multiFaceLandmarks().get(i).getLandmarkList(),
              L,
              TEST_COLOR,
              TEST_THICKNESS);
      drawAvgPoint(
              result.multiFaceLandmarks().get(i).getLandmarkList(),
              R,
              TEST_COLOR,
              TEST_THICKNESS);
      // 水溝
      drawPoint(
              result.multiFaceLandmarks().get(i).getLandmarkList(),
              164,
              TEST_COLOR,
              TEST_THICKNESS);
      // 禾髎
      drawPoint(
              result.multiFaceLandmarks().get(i).getLandmarkList(),
              165,
              TEST_COLOR,
              TEST_THICKNESS);
      drawPoint(
              result.multiFaceLandmarks().get(i).getLandmarkList(),
              391,
              TEST_COLOR,
              TEST_THICKNESS);
      // 迎香
      drawPoint(
              result.multiFaceLandmarks().get(i).getLandmarkList(),
              102,
              TEST_COLOR,
              TEST_THICKNESS);
      drawPoint(
              result.multiFaceLandmarks().get(i).getLandmarkList(),
              358,
              TEST_COLOR,
              TEST_THICKNESS);
      // 承漿
      drawPoint(
              result.multiFaceLandmarks().get(i).getLandmarkList(),
              18,
              TEST_COLOR,
              TEST_THICKNESS);*/
    }
    drawFaceBox(
            LEFT_EYEBROW_COLOR,
            LEFT_EYEBROW_THICKNESS);
  }

  /**
   * Deletes the shader program.
   *
   * <p>This is only necessary if one wants to release the program while keeping the context around.
   */
  public void release() {
    GLES20.glDeleteProgram(program);
  }

  private void drawLandmarks(
      List<NormalizedLandmark> faceLandmarkList,
      ImmutableSet<Connection> connections,
      float[] colorArray,
      int thickness) {
    GLES20.glUniform4fv(colorHandle, 1, colorArray, 0);
    GLES20.glLineWidth(thickness);
    for (Connection c : connections) {
      NormalizedLandmark start = faceLandmarkList.get(c.start());
      NormalizedLandmark end = faceLandmarkList.get(c.end());
//      float[] vertex = {start.getX(), start.getY(), end.getX(), end.getY()};
      float[] vertex = {start.getX(), start.getY()};
//      Log.i("vertex[0]", String.valueOf(vertex[0]));
//      Log.i("vertex[1]", String.valueOf(vertex[1]));
      FloatBuffer vertexBuffer =
          ByteBuffer.allocateDirect(vertex.length * 4)
              .order(ByteOrder.nativeOrder())
              .asFloatBuffer()
              .put(vertex);
      vertexBuffer.position(0);
      GLES20.glEnableVertexAttribArray(positionHandle);
      GLES20.glVertexAttribPointer(positionHandle, 2, GLES20.GL_FLOAT, false, 0, vertexBuffer);
//      GLES20.glDrawArrays(GLES20.GL_LINES, 0, 2);
      GLES20.glDrawArrays(GLES20.GL_POINTS, 0, 1);
    }
  }


  private void drawPoint(
      List<NormalizedLandmark> faceLandmarkList,
      int point,
      float[] colorArray,
      int thickness) {
    GLES20.glUniform4fv(colorHandle, 1, colorArray, 0);
    GLES20.glLineWidth(thickness);
    NormalizedLandmark p = faceLandmarkList.get(point);
    float[] vertex = {p.getX(), p.getY()};
//    Log.i("vertex[0]", String.valueOf(vertex[0]));
//    Log.i("vertex[1]", String.valueOf(vertex[1]));
    FloatBuffer vertexBuffer =
        ByteBuffer.allocateDirect(vertex.length * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .put(vertex);
    vertexBuffer.position(0);
    GLES20.glEnableVertexAttribArray(positionHandle);
    GLES20.glVertexAttribPointer(positionHandle, 2, GLES20.GL_FLOAT, false, 0, vertexBuffer);
    GLES20.glDrawArrays(GLES20.GL_POINTS, 0, 1);
  }

  private void drawAvgPoint(
          List<NormalizedLandmark> faceLandmarkList,
          int[] point,
          float[] colorArray,
          int thickness) {
    GLES20.glUniform4fv(colorHandle, 1, colorArray, 0);
    GLES20.glLineWidth(thickness);
    float X = 0.0F, Y = 0.0F;
    for (int p : point){
      NormalizedLandmark p_nor = faceLandmarkList.get(p);
      X += p_nor.getX();
      Y += p_nor.getY();
    }
    int size = point.length;
    float[] vertex = {X/size, Y/size};
//    Log.i("vertex[0]", String.valueOf(vertex[0]));
//    Log.i("vertex[1]", String.valueOf(vertex[1]));
    FloatBuffer vertexBuffer =
            ByteBuffer.allocateDirect(vertex.length * 4)
                    .order(ByteOrder.nativeOrder())
                    .asFloatBuffer()
                    .put(vertex);
    vertexBuffer.position(0);
    GLES20.glEnableVertexAttribArray(positionHandle);
    GLES20.glVertexAttribPointer(positionHandle, 2, GLES20.GL_FLOAT, false, 0, vertexBuffer);
    GLES20.glDrawArrays(GLES20.GL_POINTS, 0, 1);
  }

  private void drawProPoint(
          List<NormalizedLandmark> faceLandmarkList,
          int[] point,
          float[] proportion,
          float[] colorArray,
          int thickness) {
    GLES20.glUniform4fv(colorHandle, 1, colorArray, 0);
    GLES20.glLineWidth(thickness);
    int size = point.length;
    float X = 0.0F, Y = 0.0F;
    for (int i = 0; i < size; i++){
      NormalizedLandmark p_nor = faceLandmarkList.get(point[i]);
      X += p_nor.getX() * proportion[i];
      Y += p_nor.getY() * proportion[i];
    }
    float[] vertex = {X, Y};
//    Log.i("vertex[0]", String.valueOf(vertex[0]));
//    Log.i("vertex[1]", String.valueOf(vertex[1]));
    FloatBuffer vertexBuffer =
            ByteBuffer.allocateDirect(vertex.length * 4)
                    .order(ByteOrder.nativeOrder())
                    .asFloatBuffer()
                    .put(vertex);
    vertexBuffer.position(0);
    GLES20.glEnableVertexAttribArray(positionHandle);
    GLES20.glVertexAttribPointer(positionHandle, 2, GLES20.GL_FLOAT, false, 0, vertexBuffer);
    GLES20.glDrawArrays(GLES20.GL_POINTS, 0, 1);
  }

  private void drawFaceBox(
          float[] colorArray,
          int thickness) {
    GLES20.glUniform4fv(colorHandle, 1, colorArray, 0);
    GLES20.glLineWidth(thickness);
//    float point[][] = {{0.25F, 0.1F}, {0.25F, 0.9F}, {0.75F, 0.9F}, {0.75F, 0.1F}, {0.25F, 0.1F}};
    float point[][] = {{0.3F, 0.1F}, {0.3F, 0.9F}, {0.7F, 0.9F}, {0.7F, 0.1F}, {0.3F, 0.1F}};
    for (int i = 0; i < 4; i++){
      float[] vertex = {point[i][0], point[i][1], point[i+1][0], point[i+1][1]};
      FloatBuffer vertexBuffer =
              ByteBuffer.allocateDirect(vertex.length * 4)
                      .order(ByteOrder.nativeOrder())
                      .asFloatBuffer()
                      .put(vertex);
      vertexBuffer.position(0);
      GLES20.glEnableVertexAttribArray(positionHandle);
      GLES20.glVertexAttribPointer(positionHandle, 2, GLES20.GL_FLOAT, false, 0, vertexBuffer);
      GLES20.glDrawArrays(GLES20.GL_LINES, 0, 2);
    }
  }

  public void drawAllPoint(List<NormalizedLandmark> faceLandmarkList) {
    int[] L = new int[2];
    int[] R = new int[2];
    float[] prop = new float[2];
    // 絲竹空
    L[0] = 46;
    L[1] = 70;
    R[0] = 276;
    R[1] = 300;
    drawAvgPoint(
            faceLandmarkList,
            L,
            TEST_COLOR,
            TEST_THICKNESS);
    drawAvgPoint(
            faceLandmarkList,
            R,
            TEST_COLOR,
            TEST_THICKNESS);
    // 印堂
    drawPoint(
            faceLandmarkList,
            9,
            TEST_COLOR,
            TEST_THICKNESS);
    // 魚腰
    L[0] = 65;
    L[1] = 66;
    R[0] = 295;
    R[1] = 296;
    drawAvgPoint(
            faceLandmarkList,
            L,
            TEST_COLOR,
            TEST_THICKNESS);
    drawAvgPoint(
            faceLandmarkList,
            R,
            TEST_COLOR,
            TEST_THICKNESS);
    // 球後
    drawPoint(
            faceLandmarkList,
            110,
            TEST_COLOR,
            TEST_THICKNESS);
    drawPoint(
            faceLandmarkList,
            339,
            TEST_COLOR,
            TEST_THICKNESS);
    // 上迎香
    drawPoint(
            faceLandmarkList,
            198,
            TEST_COLOR,
            TEST_THICKNESS);
    drawPoint(
            faceLandmarkList,
            420,
            TEST_COLOR,
            TEST_THICKNESS);
    // 夾承漿
    L[0] = 182;
    L[1] = 204;
    R[0] = 406;
    R[1] = 424;
    drawAvgPoint(
            faceLandmarkList,
            L,
            TEST_COLOR,
            TEST_THICKNESS);
    drawAvgPoint(
            faceLandmarkList,
            R,
            TEST_COLOR,
            TEST_THICKNESS);
    // 睛明
    drawPoint(
            faceLandmarkList,
        243,
        TEST_COLOR,
        TEST_THICKNESS);
    drawPoint(
            faceLandmarkList,
        463,
        TEST_COLOR,
        TEST_THICKNESS);
    // 攢竹
    drawPoint(
            faceLandmarkList,
        55,
        TEST_COLOR,
        TEST_THICKNESS);
    drawPoint(
            faceLandmarkList,
        285,
        TEST_COLOR,
        TEST_THICKNESS);
    // 瞳子膠
    L[0] = 113;
    L[1] = 226;
    R[0] = 342;
    R[1] = 446;
    drawAvgPoint(
            faceLandmarkList,
            L,
            TEST_COLOR,
            TEST_THICKNESS);
    drawAvgPoint(
            faceLandmarkList,
            R,
            TEST_COLOR,
            TEST_THICKNESS);
    // 陽白
    drawPoint(
            faceLandmarkList,
            69,
            TEST_COLOR,
            TEST_THICKNESS);
    drawPoint(
            faceLandmarkList,
            299,
            TEST_COLOR,
            TEST_THICKNESS);
    // 承泣
    drawPoint(
            faceLandmarkList,
            23,
            TEST_COLOR,
            TEST_THICKNESS);
    drawPoint(
            faceLandmarkList,
            253,
            TEST_COLOR,
            TEST_THICKNESS);
    // 四白
    drawPoint(
            faceLandmarkList,
            119,
            TEST_COLOR,
            TEST_THICKNESS);
    drawPoint(
            faceLandmarkList,
            348,
            TEST_COLOR,
            TEST_THICKNESS);
    // 巨膠
    L[0] = 203;
    L[1] = 205;
    R[0] = 423;
    R[1] = 425;
    prop[0] = 0.5F;
    prop[1] = 0.5F;
    drawProPoint(
            faceLandmarkList,
            L,
            prop,
            TEST_COLOR,
            TEST_THICKNESS);
    drawProPoint(
            faceLandmarkList,
            R,
            prop,
            TEST_COLOR,
            TEST_THICKNESS);
    // 地倉
    L[0] = 57;
    L[1] = 62;
    R[0] = 291;
    R[1] = 287;
    drawAvgPoint(
            faceLandmarkList,
            L,
            TEST_COLOR,
            TEST_THICKNESS);
    drawAvgPoint(
            faceLandmarkList,
            R,
            TEST_COLOR,
            TEST_THICKNESS);
    // 顴髎
    L[0] = 50;
    L[1] = 187;
    R[0] = 280;
    R[1] = 411;
    drawAvgPoint(
            faceLandmarkList,
            L,
            TEST_COLOR,
            TEST_THICKNESS);
    drawAvgPoint(
            faceLandmarkList,
            R,
            TEST_COLOR,
            TEST_THICKNESS);
    // 水溝
    drawPoint(
            faceLandmarkList,
            164,
            TEST_COLOR,
            TEST_THICKNESS);
    // 禾髎
    drawPoint(
            faceLandmarkList,
            165,
            TEST_COLOR,
            TEST_THICKNESS);
    drawPoint(
            faceLandmarkList,
            391,
            TEST_COLOR,
            TEST_THICKNESS);
    // 迎香
    drawPoint(
            faceLandmarkList,
            102,
            TEST_COLOR,
            TEST_THICKNESS);
    drawPoint(
            faceLandmarkList,
            358,
            TEST_COLOR,
            TEST_THICKNESS);
    // 承漿
    drawPoint(
            faceLandmarkList,
            18,
            TEST_COLOR,
            TEST_THICKNESS);
    // 太陽
    L[0] = 139;
    L[1] = 156;
    R[0] = 368;
    R[1] = 383;
    drawAvgPoint(
            faceLandmarkList,
            L,
            TEST_COLOR,
            TEST_THICKNESS);
    drawAvgPoint(
            faceLandmarkList,
            R,
            TEST_COLOR,
            TEST_THICKNESS);
  }
}
