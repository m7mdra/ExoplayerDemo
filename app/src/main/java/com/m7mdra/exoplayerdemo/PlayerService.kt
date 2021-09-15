package com.m7mdra.exoplayerdemo

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.graphics.Bitmap
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.ui.PlayerNotificationManager.BitmapCallback
import com.google.android.exoplayer2.ui.PlayerNotificationManager.MediaDescriptionAdapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.m7mdra.exoplayerdemo.model.Surah
import java.lang.reflect.Type


class PlayerService : Service() {

    companion object {
        private const val PROGRESS_BAR_MAX = 1000
        private const val MAX_POSITION_FOR_SEEK_TO_PREVIOUS: Long = 3000
    }

    private lateinit var mediaPlayer: ExoPlayer
    private lateinit var currentWindow: Timeline.Window
    private val handler = Handler(Looper.getMainLooper())
    private var surahList = mutableListOf<Surah>()
    private lateinit var playerNotificationManager: PlayerNotificationManager
    private val notificationListener = object : PlayerNotificationManager.NotificationListener {
        override fun onNotificationCancelled(
            notificationId: Int,
            dismissedByUser: Boolean
        ) {
            super.onNotificationCancelled(notificationId, dismissedByUser)
            stopSelf()
        }

        override fun onNotificationPosted(
            notificationId: Int,
            notification: Notification,
            ongoing: Boolean
        ) {
            super.onNotificationPosted(notificationId, notification, ongoing)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                startForeground(
                    notificationId,
                    notification,
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
                )
            } else {
                startForeground(notificationId, notification)
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = createExoPlayerInstance()
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    "12",
                    "player",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
            )
        }

        playerNotificationManager =
            PlayerNotificationManager.Builder(
                this,
                12,
                "12",
                DescriptionAdapter(this)
            ).setNotificationListener(notificationListener)

                .build()
        playerNotificationManager.apply {
            setUseStopAction(true)
            setColorized(true)
            setUseChronometer(true)
            setSmallIcon(R.drawable.reader)
            setBadgeIconType(NotificationCompat.BADGE_ICON_NONE)
        }

        loadData()
        createPlaylist()
        currentWindow = Timeline.Window()
        playerNotificationManager.setPlayer(mediaPlayer)
    }

    private fun createPlaylist() {
        val playlist = surahList.map { surah ->
            MediaItem.Builder()
                .setUri(surah.audio)
                .setTag(surah)
                .build()
        }
        mediaPlayer.addMediaItems(playlist)
        mediaPlayer.prepare()
    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createExoPlayerInstance(): SimpleExoPlayer {
        return SimpleExoPlayer.Builder(this)

            .build()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stop()
        mediaPlayer.release()
        playerNotificationManager.setPlayer(null)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        when (intent.action) {
            PlayerEvents.PLAY -> {
                mediaPlayer.playWhenReady = true
            }
            PlayerEvents.PAUSE->{
                mediaPlayer.pause()
            }
            PlayerEvents.STOP -> {
                stopSelf()
            }
            PlayerEvents.NEXT -> {
                next()
            }
            PlayerEvents.PREVIOUS -> {
                previous()
            }

        }
        return START_STICKY
    }

    private fun previous() {
        val currentTimeline = mediaPlayer.currentTimeline
        if (currentTimeline.isEmpty) {
            return
        }
        val currentWindowIndex = mediaPlayer.currentWindowIndex
        currentTimeline.getWindow(currentWindowIndex, currentWindow)
        if (currentWindowIndex > 0 && (mediaPlayer.currentPosition <= MAX_POSITION_FOR_SEEK_TO_PREVIOUS
                    || currentWindow.isDynamic && !currentWindow.isSeekable)
        ) {
            mediaPlayer.seekTo(currentWindowIndex - 1, C.TIME_UNSET)
        } else {
            mediaPlayer.seekTo(0)
        }
    }

    private operator fun next() {
        val currentTimeline = mediaPlayer.currentTimeline
        if (currentTimeline.isEmpty) {
            return
        }
        val currentWindowIndex = mediaPlayer.currentWindowIndex
        if (currentWindowIndex < currentTimeline.windowCount - 1) {
            mediaPlayer.seekTo(currentWindowIndex + 1, C.TIME_UNSET)
        } else if (currentTimeline.getWindow(currentWindowIndex, currentWindow).isDynamic) {
            mediaPlayer.seekTo(currentWindowIndex, C.TIME_UNSET)
        }
    }

    private fun loadData() {
        val readText = assets.open("data.json").reader().readText()
        val listType: Type = object : TypeToken<List<Surah>>() {}.type
        val list: List<Surah> = Gson().fromJson(readText, listType)
        surahList.addAll(list)
    }

}

private class DescriptionAdapter(private val context: Context) : MediaDescriptionAdapter {
    override fun getCurrentContentTitle(player: Player): String {
        return "مشاري راشد العفاسي"
    }

    override fun getCurrentContentText(player: Player): String {
        val surah = (player.currentMediaItem?.playbackProperties?.tag as Surah)
        return surah.name
    }

    override fun getCurrentLargeIcon(
        player: Player,
        callback: BitmapCallback
    ): Bitmap {
        val drawable = ResourcesCompat.getDrawable(
            context.resources,
            R.drawable.reader,
            context.resources.newTheme()
        )
        return drawable?.toBitmap()!!
    }

    override fun createCurrentContentIntent(player: Player): PendingIntent? {
        return null
    }
}

object PlayerEvents {
    const val PLAY = "player.action.start"
    const val STOP = "player.action.stop"
    const val PAUSE = "player.action.pause"
    const val NEXT = "player.action.next"
    const val PREVIOUS = "player.action.previous"

}