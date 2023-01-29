package com.example.nitroce

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.nitroce.databinding.ActivityLiveViewBinding
import com.example.nitroce.utils.FileItem
import org.videolan.libvlc.LibVLC
import org.videolan.libvlc.Media
import org.videolan.libvlc.MediaPlayer
import org.w3c.dom.Document
import org.w3c.dom.NamedNodeMap
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory


class LiveViewActivity : AppCompatActivity() {
    private var url: String = "http://192.168.1.254"

    private lateinit var binding: ActivityLiveViewBinding
    private lateinit var item: FileItem
    private lateinit var libVlc: LibVLC
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityLiveViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.progressBar.visibility = View.GONE
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        libVlc = LibVLC(this)
        mediaPlayer = MediaPlayer(libVlc)
    }

    override fun onStart()
    {
        super.onStart()

        mediaPlayer.attachViews(binding.videoView, null, false, false)

        val media = Media(libVlc, Uri.parse("$url:8192"))
        media.setHWDecoderEnabled(true, false)
        media.addOption(":network-caching=300")

        mediaPlayer.media = media
        media.release()
        mediaPlayer.play()

        capturePhoto()
    }

    override fun onStop()
    {
        super.onStop()

        mediaPlayer.stop()
        mediaPlayer.detachViews()
    }

    override fun onDestroy()
    {
        super.onDestroy()

        mediaPlayer.release()
        libVlc.release()
    }

    private fun capturePhoto() {
        binding.btnCapture.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            RequestTask().execute("$url?custom=1&cmd=1001")

            showResult()
        }
    }

    private fun showResult() {
        Handler(Looper.getMainLooper()).postDelayed({

            item = FileItem()

            val fileListUrl = "$url?custom=1&cmd=3015"
            parse(fileListUrl)

            val moveWithObjectIntent = Intent(this@LiveViewActivity, DetectionActivity::class.java)
            moveWithObjectIntent.putExtra(DetectionActivity.NAME, item.name)
            moveWithObjectIntent.putExtra(DetectionActivity.IMAGE, item.path)
            startActivity(moveWithObjectIntent)
            binding.progressBar.visibility = View.GONE
        }, 5000) // 3
    }

    private fun parse(fileListUrl: String) {
        val url = URL(fileListUrl)
        var text = ""
        item = FileItem()

        val http: HttpURLConnection = url.openConnection() as HttpURLConnection
        http.doInput = true
        http.connect()
        val inputStream: InputStream = http.inputStream

        try {
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val parser = factory.newPullParser()
            parser.setInput(inputStream, null)
            var eventType = parser.eventType
            var foundItem = false
            while (eventType != XmlPullParser.END_DOCUMENT) {
                val name = parser.name
                when (eventType) {
                    XmlPullParser.START_TAG -> if (name.equals("File")) {
                        foundItem = true
                    } else if (foundItem && name.equals("NAME")) {
                        item.name = parser.nextText()
                    } else if (foundItem && name.equals("FPATH")) {
                        item.path = parser.nextText().replace("\\", "/").replace("A:", "http://192.168.1.254")
                    } else if (foundItem && name.equals("SIZE")) {
                        item.size = parser.nextText()
                    }

                    XmlPullParser.TEXT -> text = parser.text

                    XmlPullParser.END_TAG -> if (name.equals("File")) {
                        foundItem = false
                    }
                }
                eventType = parser.next()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}