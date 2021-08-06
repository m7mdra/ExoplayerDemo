package com.m7mdra.exoplayerdemo

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.m7mdra.exoplayerdemo.model.Surah
import kotlinx.android.synthetic.main.activity_player.*
import java.lang.reflect.Type


class PlayerActivity : AppCompatActivity() {
    companion object {

        private const val PROGRESS_BAR_MAX = 1000
        private const val MAX_POSITION_FOR_SEEK_TO_PREVIOUS: Long = 3000
    }

    private lateinit var currentWindow: Timeline.Window
    private lateinit var player: ExoPlayer
    private val handler = Handler(Looper.getMainLooper())
    private var surahList = mutableListOf<Surah>()
    private lateinit var adapter: SurahAdapter


    private fun publishProgress(): Long {
        val position = player.currentPosition
        val duration = player.duration
        if (duration > 0) {
            val pos = 1000L * position / duration
            seekBar.progress = pos.toInt()
        }
        return position

    }

    private val progressCallback: Runnable = object : Runnable {
        override fun run() {

            if (player.isPlaying) {
                val pos = publishProgress()
                handler.postDelayed(this, 1000 - pos % 1000)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        player.release()
    }

    private fun loadData() {
        val readText = assets.open("data.json").reader().readText()
        val listType: Type = object : TypeToken<List<Surah>>() {}.type
        val list: List<Surah> = Gson().fromJson(readText, listType)
        surahList.addAll(list)

        adapter.notifyDataSetChanged()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        adapter = SurahAdapter(surahList, onClickListener = { index, surah ->
            player.seekTo(index, 0L)
            player.playWhenReady = true
        })
        loadData()

        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        player = createExoPlayerInstance()
        currentWindow = Timeline.Window()
        createPlaylist()
        nextButton.setOnClickListener {
            next()
        }

        previousButton.setOnClickListener {
            previous()

        }
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (!fromUser)
                    return
               player.seekTo(positionValue(progress))
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
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

    private fun createPlaylist() {
        val playlist = surahList.map { surah ->
            MediaItem.Builder()
                .setUri(surah.audio)
                .setTag(surah)
                .build()
        }
        player.addMediaItems(playlist)
        player.prepare()
    }


    private val listener = object : Player.Listener {
        override fun onPlayerError(error: ExoPlaybackException) {
            playerStatusTextView.text = "حصل خطا"
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            super.onIsPlayingChanged(isPlaying)
            playPauseButton.setImageResource(
                if (isPlaying) {
                    handler.post(progressCallback)
                    R.drawable.ic_baseline_pause_circle_filled_24
                } else {
                    handler.removeCallbacks(progressCallback)
                    R.drawable.ic_baseline_play_circle_filled_24
                }
            )
        }

        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            super.onMediaItemTransition(mediaItem, reason)
            val item = mediaItem ?: return
            val properties = item.playbackProperties ?: return
            val surah = properties.tag as? Surah ?: return
            surahNameTextView.text = surah.name
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
            .setTrackSelector(DefaultTrackSelector(this))
            .build()
    }

    private fun progressBarValue(position: Long): Long {
        val duration = player.duration
        return if (duration == C.TIME_UNSET || duration == 0L) 0 else (position * PROGRESS_BAR_MAX / duration)
    }

    private fun positionValue(progress: Int): Long {
        val duration = player.duration
        return if (duration == C.TIME_UNSET) 0 else duration * progress / PROGRESS_BAR_MAX
    }

    private fun previous() {
        val currentTimeline = player.currentTimeline
        if (currentTimeline.isEmpty) {
            return
        }
        val currentWindowIndex = player.currentWindowIndex
        currentTimeline.getWindow(currentWindowIndex, currentWindow)
        if (currentWindowIndex > 0 && (player.currentPosition <= MAX_POSITION_FOR_SEEK_TO_PREVIOUS
                    || currentWindow.isDynamic && !currentWindow.isSeekable)
        ) {
            player.seekTo(currentWindowIndex - 1, C.TIME_UNSET)
        } else {
            player.seekTo(0)
        }
    }

    private operator fun next() {
        val currentTimeline = player.currentTimeline
        if (currentTimeline.isEmpty) {
            return
        }
        val currentWindowIndex = player.currentWindowIndex
        if (currentWindowIndex < currentTimeline.windowCount - 1) {
            player.seekTo(currentWindowIndex + 1, C.TIME_UNSET)
        } else if (currentTimeline.getWindow(currentWindowIndex, currentWindow).isDynamic) {
            player.seekTo(currentWindowIndex, C.TIME_UNSET)
        }
    }
}

