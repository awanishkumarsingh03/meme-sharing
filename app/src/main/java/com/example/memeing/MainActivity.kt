package com.example.memeing

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class MainActivity : AppCompatActivity() {
    var currentImageUrl : String? = null
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadMeme()
        val nextButton = findViewById<Button>(R.id.nextButton)
        nextButton.setOnClickListener {
            nextMeme()
        }
        val shareButton = findViewById<Button>(R.id.shareButton)
        shareButton.setOnClickListener {
            shareMeme()
        }
    }

    fun loadMeme(){
        val progressBar = findViewById<ProgressBar>(R.id.progress)
        progressBar.visibility = View.VISIBLE;
        val queue = Volley.newRequestQueue(this)
        currentImageUrl = "https://meme-api.herokuapp.com/gimme"
        val image = findViewById<ImageView>(R.id.image)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, currentImageUrl,null,
            Response.Listener{ response ->
                currentImageUrl = response.getString("url");
                Glide.with(this).load(currentImageUrl).listener(object : RequestListener<Drawable>{
                    override fun onResourceReady(
                        resource : Drawable? ,
                        model : Any? ,
                        target : Target<Drawable>? ,
                        dataSource : DataSource? ,
                        isFirstResource : Boolean
                    ) : Boolean {
                        //resource is ready
                        progressBar.visibility = View.GONE
                        return  false
                    }

                    override fun onLoadFailed(
                        e : GlideException? ,
                        model : Any? ,
                        target : Target<Drawable>? ,
                        isFirstResource : Boolean
                    ) : Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }
                }).into(image)
            },
            Response.ErrorListener {

            })

        queue.add(jsonObjectRequest)
    }

    fun nextMeme(){
    loadMeme()
    }

    fun shareMeme(){
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT,"Hey I have send you a meme " + currentImageUrl)
        val chooser = Intent.createChooser(intent, "Share this meme using any of the apps")
        startActivity(chooser)
    }
}