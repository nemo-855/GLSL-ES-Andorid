package com.nemo.glsl_es_android.util

import android.app.Activity
import android.opengl.GLES20
import android.opengl.GLSurfaceView

object Utils {
    private const val TAG = "GLES20"

    /**
     * プログラムオブジェクトを生成し、WebGLシステムに設定する
     * @param vshader 頂点シェーダのプログラム(文字列)
     * @param fshader フラグメントシェーダのプログラム(文字列)
     * @return プログラムオブジェクト
     */
    fun initShaders(vshader: String?, fshader: String?): Int {
        val program = createProgram(vshader, fshader)
        GLES20.glUseProgram(program)
        return program
    }



    /**
     * リンク済みのプログラムオブジェクトを生成する
     * @param vshader 頂点シェーダのプログラム(文字列)
     * @param fshader フラグメントシェーダのプログラム(文字列)
     * @return 作成したプログラムオブジェクト
     */
    private fun createProgram(vshader: String?, fshader: String?): Int {
        // シェーダオブジェクトを作成する
        val vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vshader)
        val fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fshader)

        // プログラムオブジェクトを作成する
        val program = GLES20.glCreateProgram()
        if (program == 0) {
            throw RuntimeException("failed to create program")
        }

        // シェーダオブジェクトを設定する
        GLES20.glAttachShader(program, vertexShader)
        GLES20.glAttachShader(program, fragmentShader)

        // プログラムオブジェクトをリンクする
        GLES20.glLinkProgram(program)

        // リンク結果をチェックする
        val linked = IntArray(1)
        GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linked, 0)
        if (linked[0] != GLES20.GL_TRUE) {
            val error = GLES20.glGetProgramInfoLog(program)
            throw RuntimeException("failed to link program: $error")
        }
        return program
    }

    /**
     * シェーダオブジェクトを作成する
     * @param type 作成するシェーダタイプ
     * @param source シェーダのプログラム(文字列)
     * @return 作成したシェーダオブジェクト。
     */
    private fun loadShader(type: Int, source: String?): Int {
        // シェーダオブジェクトを作成する
        val shader = GLES20.glCreateShader(type)
        if (shader == 0) {
            throw RuntimeException("unable to create shader")
        }

        // シェーダのプログラムを設定する
        GLES20.glShaderSource(shader, source)

        // シェーダをコンパイルする
        GLES20.glCompileShader(shader)

        // コンパイル結果を検査する
        val compiled = IntArray(1)
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0)
        if (compiled[0] != GLES20.GL_TRUE) {
            val error = GLES20.glGetShaderInfoLog(shader)
            throw RuntimeException("failed to compile shader: $error")
        }
        return shader
    }

    /**
     * OpenGL ES 2.0が使用できるように初期化する
     * @param activity OpenGL ES 2.0を使用するアプリケーション
     * @param rendererOpenGL ES 2.0を使用するアプリケーション
     */
    fun initGLES20(activity: Activity?, renderer: GLSurfaceView.Renderer?): GLSurfaceView {
        val glSurfaceView = GLSurfaceView(activity) // 描画領域の作成
        // OpenGL ES 2.0を使用する
        glSurfaceView.setEGLContextClientVersion(2)
        // 作成したGLSurfaceViewにこのアプリケーションから描画する
        glSurfaceView.setRenderer(renderer)
        return glSurfaceView
    }
}
