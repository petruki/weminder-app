package com.weminder.api

import android.content.Context
import com.google.gson.Gson
import com.weminder.BuildConfig
import com.weminder.utils.AppUtils
import com.weminder.utils.USER_ID
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import java.net.URISyntaxException

/**
 * Singleton implementation to handle WebSocket requests
 */
object SocketHandler {

    @JvmStatic
    private val gson = Gson()
    private lateinit var socket: Socket

    @Synchronized
    fun initSocket(context: Context, channel: String) {
        try {
            val opts = IO.Options()
            opts.query = "auth=${AppUtils.getUser(context, USER_ID)}&channel=${channel}"
            socket = IO.socket(BuildConfig.API_URL, opts)
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
    }

    @Synchronized
    fun getClient(): Socket {
        if (!socket.connected())
            socket.connect()

        return socket
    }

    fun disconnect() {
        socket.emit("logout")
        socket.disconnect()
    }

    /**
     * Send events to API
     */
    fun emit(event: WEvent, args: Any?) {
        if (args != null)
            getClient().emit(event.arg, gson.toJson(args))
        else
            getClient().emit(event.arg)
    }

    /**
     * Listening events from the API
     */
    fun subscribe(event: WEvent, listener: Emitter.Listener) {
        getClient().on(event.arg, listener)
    }

    /**
     * Return DTO based on a JSON object
     */
    fun <T> getDTO(dtoType: Class<T>, vararg args: Any): T {
        return gson.fromJson((args[0] as Array<*>)[0].toString(), dtoType)
    }

    /**
     * Return a Array<DTO> based on a JSON list of objects
     */
    fun <T> getDTOList(dtoType: Class<Array<T>>, vararg args: Any): List<T> {
        return gson.fromJson((args[0] as Array<*>)[0].toString(), dtoType).toList()
    }

}