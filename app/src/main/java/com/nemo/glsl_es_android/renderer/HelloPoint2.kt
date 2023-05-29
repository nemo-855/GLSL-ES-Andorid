package com.nemo.glsl_es_android.renderer

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.os.Bundle
import com.nemo.glsl_es_android.util.Utils
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class HelloPoint2 : GLSurfaceView.Renderer {
    private val VSHADER_SOURCE = """attribute vec4 a_Position;
void main() {
  gl_Position = a_Position;
  gl_PointSize = 20.0;
}
"""

    // フラグメントシェーダのプログラム
    private val FSHADER_SOURCE = """void main() {
    gl_FragColor = vec4(1.0, 0.0, 0.0, 1.0);
}
"""

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // シェーダを初期化する
        val program: Int = Utils.initShaders(VSHADER_SOURCE, FSHADER_SOURCE)

        // attribute変数の格納場所を取得する
        val a_Position = GLES20.glGetAttribLocation(program, "a_Position")
        if (a_Position == -1) {
            throw RuntimeException("attribute変数の格納場所の取得に失敗")
        }
        // attribute変数に点の座標を代入する
        GLES20.glVertexAttrib3f(a_Position, 0.0f, 0.7f, 0.0f)
        // 表示されない場合は、Utils.glVertexAttrib3f(ma_Position, 0.0f, 0.0f, 0.0f);

        // 画面をクリアする色を設定する
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height) // 描画領域を設定
    }

    override fun onDrawFrame(gl: GL10?) {
        // 画面をクリアする
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        // 点を描画する
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, 1)
    }

    companion object {
        fun single(): HelloPoint2 {
            return HelloPoint2()
        }
    }
}
