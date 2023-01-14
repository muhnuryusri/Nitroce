package com.example.nitroce

import android.os.AsyncTask
import com.albroco.barebonesdigest.DigestAuthentication
import com.albroco.barebonesdigest.DigestChallengeResponse
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class RequestTask :
    AsyncTask<String?, String?, Boolean?>() {
    override fun doInBackground(vararg params: String?): Boolean? {
        // Step 1. Create the connection
        val url = URL(params[0])
        var connection =
            url.openConnection() as HttpURLConnection

        // Step 2. Make the request and check to see if the response contains an authorization challenge
        if (connection.responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
            // Step 3. Create a authentication object from the challenge...
            val auth: DigestAuthentication = DigestAuthentication.fromResponse(connection)
            // ...with correct credentials
            auth.username("admin").password("admin")

            // Step 5. Create a new connection, identical to the original one...
            connection = url.openConnection() as HttpURLConnection
            // ...and set the Authorization header on the request, with the challenge response
            connection.setRequestProperty(
                DigestChallengeResponse.HTTP_HEADER_AUTHORIZATION,
                auth.getAuthorizationForRequest("GET", connection.url.path)
            )
        }

        connection.connect() //there you can also get inputStream from connection for local store of snapshot
        val responseCode = connection.responseCode
        val cLength = connection.contentLength
        return responseCode == 200
    }
}