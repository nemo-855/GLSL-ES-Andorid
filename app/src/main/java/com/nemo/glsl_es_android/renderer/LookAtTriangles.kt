package com.nemo.glsl_es_android.renderer

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import com.nemo.glsl_es_android.util.Utils
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class LookAtTriangles : GLSurfaceView.Renderer {
  // メンバー変数
  private var mNumVertices // 描画する頂点数
          = 0

  override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
    val program: Int = Utils.initShaders(VSHADER_SOURCE, FSHADER_SOURCE) // シェーダを初期化する
    mNumVertices = initVertexBuffers(program) // 頂点座標と色を設定する(青い三角形が手前にある)
    GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f) // 画面をクリアする色を設定する

    // u_ViewMatrix変数の格納場所を取得する
    val u_ViewMatrix = GLES20.glGetUniformLocation(program, "u_ViewMatrix")
    if (u_ViewMatrix == -1) {
      throw RuntimeException("u_ViewMatrixの格納場所の取得に失敗")
    }

    // 視点の位置、注視点、上方向を設定する
    val viewMatrix = FloatArray(16)
    Matrix.setLookAtM(viewMatrix, 0, 0.20f, 0.25f, 0.25f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f)

    // ビュー行列をuniform変数に設定する
    GLES20.glUniformMatrix4fv(u_ViewMatrix, 1, false, viewMatrix, 0)
  }

  override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
    // 表示領域を設定する
    val size = if (width <= height) width else height
    GLES20.glViewport((width - size) / 2, (height - size) / 2, size, size)
  }

  override fun onDrawFrame(gl: GL10) {
    GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT) // 画面をクリアする
    GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, mNumVertices) // 三角形を描画する
  }

  private fun initVertexBuffers(program: Int): Int {
    val verticesColors: FloatBuffer = Utils.makeFloatBuffer(
      floatArrayOf( // 頂点座標、       色(RGBA)
        0.0f, 0.5f, -0.4f, 0.4f, 1.0f, 0.4f,  // 緑が奥
        -0.5f, -0.5f, -0.4f, 0.4f, 1.0f, 0.4f,
        0.5f, -0.5f, -0.4f, 1.0f, 0.4f, 0.4f,
        0.5f, 0.4f, -0.2f, 1.0f, 0.4f, 0.4f,  // 黄色が真ん中
        -0.5f, 0.4f, -0.2f, 1.0f, 1.0f, 0.4f,
        0.0f, -0.6f, -0.2f, 1.0f, 1.0f, 0.4f,
        0.0f, 0.5f, 0.0f, 0.4f, 0.4f, 1.0f,  // 青が前
        -0.5f, -0.5f, 0.0f, 0.4f, 0.4f, 1.0f,
        0.5f, -0.5f, 0.0f, 1.0f, 0.4f, 0.4f
      )
    )
    val n = 9
    val FSIZE = java.lang.Float.SIZE / java.lang.Byte.SIZE // floatのバイト数

    // バッファオブジェクトを作成する
    val vertexColorbuffer = IntArray(1)
    GLES20.glGenBuffers(1, vertexColorbuffer, 0)

    // 頂点の座標と色をバッファオブジェクトに書き込む
    GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexColorbuffer[0])
    GLES20.glBufferData(
      GLES20.GL_ARRAY_BUFFER,
      FSIZE * verticesColors.limit(),
      verticesColors,
      GLES20.GL_STATIC_DRAW
    )

    // a_Positionにバッファオブジェクトを割り当て、有効化する
    val a_Position = GLES20.glGetAttribLocation(program, "a_Position")
    if (a_Position == -1) {
      throw RuntimeException("a_Positionの格納場所の取得に失敗")
    }
    GLES20.glVertexAttribPointer(a_Position, 3, GLES20.GL_FLOAT, false, FSIZE * 6, 0)
    GLES20.glEnableVertexAttribArray(a_Position)

    // a_Colorにバッファオブジェクトを割り当て、有効化する
    val a_Color = GLES20.glGetAttribLocation(program, "a_Color")
    if (a_Color == -1) {
      throw RuntimeException("a_Colorの格納場所の取得に失敗")
    }
    GLES20.glVertexAttribPointer(a_Color, 3, GLES20.GL_FLOAT, false, FSIZE * 6, FSIZE * 3)
    GLES20.glEnableVertexAttribArray(a_Color)
    return n
  }

  companion object {
    // 頂点シェーダのプログラム
    private const val VSHADER_SOURCE = "attribute vec4 a_Position;\n" +
            "attribute vec4 a_Color;\n" +
            "uniform mat4 u_ViewMatrix;\n" +
            "varying vec4 v_Color;\n" +
            "void main() {\n" +
            "  gl_Position = u_ViewMatrix * a_Position;\n" +
            "  v_Color = a_Color;\n" +
            "}\n"

    // フラグメントシェーダのプログラム
    private const val FSHADER_SOURCE = "#ifdef GL_ES\n" +
            "precision mediump float;\n" +
            "#endif\n" +
            "varying vec4 v_Color;\n" +
            "void main() {\n" +
            "  gl_FragColor = v_Color;\n" +
            "}\n"

    fun single(): GLSurfaceView.Renderer {
      return LookAtTriangles()
    }
  }
}