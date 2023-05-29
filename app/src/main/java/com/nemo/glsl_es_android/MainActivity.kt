package com.nemo.glsl_es_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nemo.glsl_es_android.renderer.HelloPoint2
import com.nemo.glsl_es_android.util.Utils

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val lookAtTrianglesView = Utils.initGLES20(this, HelloPoint2.single())
        setContentView(lookAtTrianglesView)
    }
}