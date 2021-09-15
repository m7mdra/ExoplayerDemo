package com.m7mdra.exoplayerdemo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_player.*


class PlayerActivity : AppCompatActivity() {

    var playing = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)


        playPauseButton.setOnClickListener {
            Log.d("TAG", "onCreate: $playing")
            val intent = Intent(this, PlayerService::class.java)
            if (playing) {
                intent.action = PlayerEvents.PAUSE
            } else {
                intent.action = PlayerEvents.PLAY
            }
            playing = !playing
            startService(intent)
        }
        nextButton.setOnClickListener {
            val intent = Intent(this, PlayerService::class.java)
            intent.action = PlayerEvents.NEXT
            startService(intent)
        }

        previousButton.setOnClickListener {
            val intent = Intent(this, PlayerService::class.java)
            intent.action = PlayerEvents.PREVIOUS
            startService(intent)

        }

        /*  recyclerView.adapter = adapter
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


          player.addListener(listener)*/
    }

/*    private fun createPlaylist() {
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
    }*/
}

