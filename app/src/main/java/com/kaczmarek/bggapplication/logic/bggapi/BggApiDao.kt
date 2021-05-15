package com.kaczmarek.bggapplication.logic.bggapi

import com.kaczmarek.bggapplication.entities.bggapi.BggApiResponse
import com.kaczmarek.bggapplication.entities.bggapi.BggBoardGameCollectionItem
import com.kaczmarek.bggapplication.entities.bggapi.BggBoardGameDetails
import com.kaczmarek.bggapplication.entities.bggapi.BggBoardGameOverview
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

class BggApiDao(
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) {

    companion object {
        private const val RECONNECT_WAIT_TIME = 500L
        private const val API_URL = "https://www.boardgamegeek.com/xmlapi2/"
        private const val GET_BY_NAME_URL = "${API_URL}search?query=%s&type=boardgame"
        private const val GET_DETAILS_URL = "${API_URL}thing?id=%d"
        private const val GET_COLLECTION_URL =
            "${API_URL}collection?username=%s&subtype=boardgame&stats=1&own=1"
    }

    suspend fun getGameOverviewsByName(name: String):
            BggApiResponse<List<BggBoardGameOverview>> {

        return withContext(dispatcher) {
            try {
                val requestUrl = String.format(GET_BY_NAME_URL, name)
                val connection = waitForConnection(requestUrl)
                val parser = BggBoardGameOverviewListParser()
                val response = readResponse(connection, parser)
                BggApiResponse.Success(response)
            } catch (e: Exception) {
                BggApiResponse.Error(e)
            }
        }
    }

    suspend fun getGameDetails(id: Long): BggApiResponse<BggBoardGameDetails> {
        return withContext(dispatcher) {
            try {
                val requestUrl = String.format(GET_DETAILS_URL, id)
                val connection = waitForConnection(requestUrl)
                val parser = BggBoardGameDetailsParser()
                val response = readResponse(connection, parser)
                BggApiResponse.Success(response)
            } catch (e: Exception) {
                BggApiResponse.Error(e)
            }
        }
    }

    suspend fun getCollectionOfUser(username: String):
            BggApiResponse<List<BggBoardGameCollectionItem>> {

        return withContext(dispatcher) {
            try {
                val requestUrl = String.format(GET_COLLECTION_URL, username)
                val connection = waitForConnection(requestUrl)
                val parser = BggBoardGameCollectionItemListParser()
                val response = readResponse(connection, parser)
                BggApiResponse.Success(response)
            } catch (e: Exception) {
                BggApiResponse.Error(e)
            }
        }
    }

    private fun connect(requestUrl: String): HttpURLConnection {
        val url = URL(requestUrl)
        val connection = url.openConnection() as HttpURLConnection
        connection.connect()
        return connection
    }

    private suspend fun waitForConnection(requestUrl: String): HttpURLConnection {
        var connection = connect(requestUrl)
        while (connection.responseCode == 202) {
            delay(RECONNECT_WAIT_TIME)
            connection = connect(requestUrl)
        }
        return connection
    }

    private fun <T> readResponse(connection: HttpURLConnection, parser: BggResponseParser<T>): T {
        val inputStream = connection.inputStream
        return parser.parse(inputStream)
    }
}