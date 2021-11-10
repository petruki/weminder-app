package com.weminder.api

import android.content.Context
import com.google.gson.Gson
import com.weminder.BuildConfig
import com.weminder.utils.AppUtils
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

object SocketHandler {

    @JvmStatic
    private val gson = Gson()
    private lateinit var socket: Socket

    @Synchronized
    fun initSocket(context: Context) {
        try {
            val opts = IO.Options()
            opts.query = "auth=${AppUtils.getUserId(context)}"
            socket = IO.socket(BuildConfig.API_URL, opts)
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
    }

    @Synchronized
    fun getClient(): Socket {
        if (!socket.connected())
            connect()

        return socket
    }

    @Synchronized
    fun connect() {
        socket.connect()
    }

    @Synchronized
    fun disconnect() {
        socket.emit("logout")
        socket.disconnect()
    }

    fun <T> getDTO(dtoType: Class<T>, vararg args: Any): T {
        return SocketHandler.gson.fromJson((args[0] as Array<*>)[0].toString(), dtoType)
    }
}