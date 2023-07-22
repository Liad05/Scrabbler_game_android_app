package com.example.scrabbler_game


import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    private lateinit var gameIdEntry: EditText
    private lateinit var errorLabel: TextView
    private lateinit var submitButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gameIdEntry = findViewById(R.id.game_id_entry)
        errorLabel = findViewById(R.id.error_label)
        submitButton = findViewById(R.id.submit_button)

        submitButton.setOnClickListener {
            submit()
        }
    }

    private fun submit() {
        val gameId = gameIdEntry.text.toString()
        val serverIP = "" //enter your server IP here
        val urlString = "http://$serverIP:8080/test_tomcat_4_war_exploded/api/hello-world/getStateById?ID=$gameId"

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val url = URL(urlString)
                val urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.requestMethod = "GET"
                val responseCode = urlConnection.responseCode

                if(responseCode == 200){
                    val inReader = BufferedReader(InputStreamReader(urlConnection.inputStream))
                    val response = StringBuilder()

                    inReader.use { it.readLines().forEach { response.append(it).append("\n") } }
                    runOnUiThread{val resultTextView = findViewById<TextView>(R.id.resultTextView)
                        resultTextView.text = response.toString()}
                    // Parse the response as needed
                    // Use Dispatchers.Main to update the UI

                } else {
                    runOnUiThread {
                        errorLabel.text = "No game with this ID"
                    }
                }

                urlConnection.disconnect()

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}