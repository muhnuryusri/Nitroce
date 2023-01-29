package com.example.nitroce.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.nitroce.DetectionActivity
import com.example.nitroce.ItemAdapter
import com.example.nitroce.databinding.ActivityAlbumBinding
import com.example.nitroce.databinding.ActivityLiveViewBinding
import com.example.nitroce.utils.FileItem
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class AlbumActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAlbumBinding
    private var arrayList = arrayListOf<FileItem>()
    private var adapter = ItemAdapter(arrayList)
    private var item = FileItem()

    private var url: String = "http://192.168.1.254"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlbumBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.progressBar.visibility = View.GONE

        adapter = ItemAdapter(arrayList)
        binding.rvAlbum.adapter = adapter
        binding.rvAlbum.layoutManager = GridLayoutManager(this, 2)

        adapter.setOnItemClickCallback(object : ItemAdapter.OnItemClickCallback {
            override fun onItemClicked(item: FileItem) {
                binding.progressBar.visibility = View.VISIBLE
                val moveWithObjectIntent = Intent(this@AlbumActivity, DetectionActivity::class.java)
                moveWithObjectIntent.putExtra(DetectionActivity.NAME, item.name)
                moveWithObjectIntent.putExtra(DetectionActivity.IMAGE, item.path)
                startActivity(moveWithObjectIntent)
            }
        })

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val fileListUrl = "$url?custom=1&cmd=3015"
        parse(fileListUrl)
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
                        arrayList.add(item)
                    } else if (foundItem && name.equals("NAME")) {
                        item.name = parser.nextText()
                    } else if (foundItem && name.equals("FPATH")) {
                        item.path = parser.nextText().replace("\\", "/").replace("A:", "http://192.168.1.254")
                    } else if (foundItem && name.equals("SIZE")) {
                        item.size = parser.nextText()
                    }

                    XmlPullParser.TEXT -> text = parser.text

                    XmlPullParser.END_TAG -> if (name.equals("File")) {
                        if (item.name?.endsWith(".JPG") == true) {
                            item = FileItem()
                            foundItem = false
                        }
                    }
                }
                eventType = parser.next()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}