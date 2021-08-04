package com.m7mdra.exoplayerdemo

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.Player.MEDIA_ITEM_TRANSITION_REASON_PLAYLIST_CHANGED
import com.m7mdra.exoplayerdemo.model.Ayah
import com.m7mdra.exoplayerdemo.model.Surah
import kotlinx.android.synthetic.main.activity_player.*
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class PlayerActivity : AppCompatActivity() {
    private lateinit var player: ExoPlayer
    private val surah: Surah by lazy {
        intent.getParcelableExtra("surah")!!
    }
    private val trustAllCerts: Array<TrustManager> = arrayOf<TrustManager>(
        object : X509TrustManager {
            @SuppressLint("TrustAllX509TrustManager")
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {

            }

            @SuppressLint("TrustAllX509TrustManager")
            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
                //ignore
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }

        }
    )

    override fun onStop() {
        super.onStop()
        player.release()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        initSSL()
        surahNameTextView.text = surah.name

        player = createExoPlayerInstance()
        surah.ayahs.map {
            val mediaItem = MediaItem.Builder()
                .setTag(it)
                .setUri(it.audio)
                .setCustomCacheKey("${surah.number}:${it.numberInSurah}:${it.number}")
                .build()
            if(it.numberInSurah==1){
                listener.onMediaItemTransition(mediaItem,MEDIA_ITEM_TRANSITION_REASON_PLAYLIST_CHANGED)
            }
            Log.d("MEGA", "${it.audio}")

            player.addMediaItem(mediaItem)
        }
        player.prepare()
        playPauseButton.setOnClickListener {
            if (player.isPlaying) {
                player.pause()
            } else {
                player.playWhenReady = true
            }
        }


        player.addListener(listener)
    }

    private fun initSSL() {
        try {
            val sc: SSLContext = SSLContext.getInstance("TLS")
            sc.init(null, trustAllCerts, SecureRandom())
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory())
        } catch (e: KeyManagementException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
    }

    private val listener = object : Player.Listener {
        override fun onPlayerError(error: ExoPlaybackException) {
            playerStatusTextView.text = "حصل خطا"
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            super.onIsPlayingChanged(isPlaying)
            playPauseButton.setImageResource(if (isPlaying) R.drawable.ic_baseline_pause_circle_filled_24 else R.drawable.ic_baseline_play_circle_filled_24)
        }


        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            super.onMediaItemTransition(mediaItem, reason)
            val item = mediaItem ?: return
            val ayah = item.playbackProperties?.tag as? Ayah ?: return
            ayahNumberTextView.text = "اية رقم ${ayah.numberInSurah}"

        }


        override fun onPlaybackStateChanged(state: Int) {
            super.onPlaybackStateChanged(state)
            playerStatusTextView.text = when (state) {
                Player.STATE_BUFFERING -> "جاري التحميل"
                Player.STATE_ENDED -> "انتهى"
                Player.STATE_IDLE -> "متوقف"
                Player.STATE_READY -> "جاهز للبدء"
                else -> "غير معروف"
            }
        }
    }

    private fun createExoPlayerInstance(): SimpleExoPlayer {
        return SimpleExoPlayer.Builder(this)
            .setLoadControl(DefaultLoadControl())

            .build()
    }
}

