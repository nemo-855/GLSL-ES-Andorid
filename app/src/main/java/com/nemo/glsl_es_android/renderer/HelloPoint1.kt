package com.nemo.glsl_es_android.renderer

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import com.nemo.glsl_es_android.util.Utils
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class HelloPoint1 : GLSurfaceView.Renderer {
    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        // シェーダを初期化する
        Utils.initShaders(VSHADER_SOURCE, FSHADER_SOURCE)

        // 画面をクリアする色を設定する
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height) // 描画領域を設定
    }

    override fun onDrawFrame(gl: GL10) {
        // 画面をクリアする
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        // 点を描画する
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, 1)
    }

    companion object {
        // 頂点シェーダのプログラム
        private const val VSHADER_SOURCE = "void main() {\n" +
                "    gl_Position = vec4(0.5, 0.6, 0.0, 1.0);\n" +
                "    gl_PointSize = 20.0;\n" +
                "}\n"

        // フラグメントシェーダのプログラム
        private const val FSHADER_SOURCE = "void main() {\n" +
                "    gl_FragColor = vec4(1.0, 0.0, 0.0, 1.0);\n" +
                "}\n"

        fun single(): HelloPoint1 {
            return HelloPoint1()
        }
    }
}