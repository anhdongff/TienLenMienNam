package com.gamebai.tienlenmiennam.hotro;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.media.SoundPool;

import com.gamebai.tienlenmiennam.R; // Thay bằng package của bạn

import java.util.HashMap;

public class SoundManager {

    private static SoundManager instance;

    private SoundPool soundPool;
    private HashMap<Sfx, Integer> sfxMap;

    private MediaPlayer musicPlayer;
    private  MediaPlayer musicIngamePlayer;
    private ValueAnimator crossfadeAnimator;
    private SharedPreferences prefs;
    private static final String PREFS_NAME = "SoundSettings";
    private static final String KEY_MUSIC_ENABLED = "music_enabled";
    private static final String KEY_SFX_ENABLED = "sfx_enabled";

    private boolean isMusicEnabled;
    private boolean isSfxEnabled;
    private boolean isIngame;

    // Enum để quản lý các file SFX một cách dễ dàng
    public enum Sfx {
        BUTTON_CLICK,
        WIN,
        PICK_UP,
        UN_PICK_UP,
        PLAY_HAND,
        THROWING_CARDS,
    }

    // Constructor là private để đảm bảo là Singleton
    private SoundManager(Context context) {
        // 1. Tải cài đặt của người dùng
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        isMusicEnabled = prefs.getBoolean(KEY_MUSIC_ENABLED, false); // Mặc định là tắt
        isSfxEnabled = prefs.getBoolean(KEY_SFX_ENABLED, true);    // Mặc định là bật

        // 2. Cấu hình SoundPool cho SFX
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder()
                .setMaxStreams(60) // Cho phép phát tối đa 60 âm thanh cùng lúc
                .setAudioAttributes(audioAttributes)
                .build();

        // 3. Tải trước các file SFX
        sfxMap = new HashMap<>();
        sfxMap.put(Sfx.BUTTON_CLICK, soundPool.load(context, R.raw.balatro_button, 1));
        sfxMap.put(Sfx.WIN, soundPool.load(context, R.raw.balatro_win, 1));
        sfxMap.put(Sfx.PICK_UP, soundPool.load(context, R.raw.balatro_pick_card, 1));
        sfxMap.put(Sfx.UN_PICK_UP, soundPool.load(context, R.raw.balatro_unpick_card, 1));
        sfxMap.put(Sfx.PLAY_HAND, soundPool.load(context, R.raw.balatro_play_hand, 1));
        sfxMap.put(Sfx.THROWING_CARDS, soundPool.load(context, R.raw.balatro_throwing_cards_to_players,1));
        // 4. Chuẩn bị MediaPlayer cho nhạc nền
        isIngame=false;
        // Chúng ta sẽ tạo nó khi được yêu cầu để tiết kiệm bộ nhớ
    }

    // Phương thức để lấy thực thể Singleton
    public static synchronized SoundManager getInstance(Context context) {
        if (instance == null) {
            instance = new SoundManager(context.getApplicationContext());
        }
        return instance;
    }

    // --- Các phương thức điều khiển ---

    public void playSfx(Sfx sfx) {
        if (!isSfxEnabled || soundPool == null || !sfxMap.containsKey(sfx)) return;

        Integer soundId = sfxMap.get(sfx);
        if (soundId != null) {
            soundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f);
        }
    }

    public void playMusic(Context context) {
        if (!isMusicEnabled) return;

        if (musicPlayer == null) {
            musicPlayer = MediaPlayer.create(context, R.raw.balatro_music_outgame_mp3);
            if (musicPlayer == null) return;
            musicPlayer.setLooping(true); // Lặp lại nhạc nền
        }
        if (musicIngamePlayer == null) {
            musicIngamePlayer = MediaPlayer.create(context, R.raw.balatro_music_ingame_mp3);
            if (musicIngamePlayer == null) return;
            musicIngamePlayer.setLooping(true); // Lặp lại nhạc nền
        }

        if(isIngame){
            musicPlayer.setVolume(0,0);
            musicIngamePlayer.setVolume(0.5f,0.5f);
        }else{
            musicPlayer.setVolume(0.5f,0.5f);
            musicIngamePlayer.setVolume(0,0);
        }

        if (!musicPlayer.isPlaying()) {
            musicPlayer.start();
        }
        if (!musicIngamePlayer.isPlaying()) {
            musicIngamePlayer.start();
        }
    }

    public void pauseMusic() {
        if (musicPlayer != null && musicPlayer.isPlaying()) {
            musicPlayer.pause();
        }
        if (musicIngamePlayer != null && musicIngamePlayer.isPlaying()) {
            musicIngamePlayer.pause();
        }
    }

    public void stopMusic() {
        if (musicPlayer != null) {
            musicPlayer.stop();
            musicPlayer.release();
            musicPlayer = null;
        }
        if (musicIngamePlayer != null) {
            musicIngamePlayer.stop();
            musicIngamePlayer.release();
            musicIngamePlayer = null;
        }
    }

    public void toggleMusic() {
        isMusicEnabled = !isMusicEnabled;
        prefs.edit().putBoolean(KEY_MUSIC_ENABLED, isMusicEnabled).apply();
        if (isMusicEnabled) {
            // Cần context để có thể play lại nhạc, nên ta sẽ gọi playMusic từ Activity
        } else {
            stopMusic();
        }
    }

    public void toggleSfx() {
        isSfxEnabled = !isSfxEnabled;
        prefs.edit().putBoolean(KEY_SFX_ENABLED, isSfxEnabled).apply();
    }

    public void toggleIngame() {
        isIngame = !isIngame;
        if (isMusicEnabled) {
            crossfadeMusic();
        }
    }

    private void crossfadeMusic() {
        // Hủy animator cũ nếu nó đang chạy để tránh xung đột
        if (crossfadeAnimator != null && crossfadeAnimator.isRunning()) {
            crossfadeAnimator.cancel();
        }

        // Xác định bản nhạc nào sẽ to lên (target) và bản nào sẽ nhỏ đi (fade-out)
        final MediaPlayer targetPlayer = isIngame ? musicIngamePlayer : musicPlayer;
        final MediaPlayer fadeOutPlayer = isIngame ? musicPlayer : musicIngamePlayer;

        // Đảm bảo cả hai player đã được khởi tạo
        if (targetPlayer == null || fadeOutPlayer == null) return;

        // Tạo một ValueAnimator chạy từ 0.0f đến 0.5f
        crossfadeAnimator = ValueAnimator.ofFloat(0.0f, 0.5f);
        crossfadeAnimator.setDuration(1500); // Thời gian chuyển nhạc: 1.5 giây

        crossfadeAnimator.addUpdateListener(animation -> {
            float currentVolume = (float) animation.getAnimatedValue();
            float fadedVolume = 0.5f - currentVolume; // Âm lượng của bản nhạc nhỏ đi

            // Áp dụng âm lượng cho cả hai bản nhạc trong mỗi frame của animation
            targetPlayer.setVolume(currentVolume, currentVolume);
            fadeOutPlayer.setVolume(fadedVolume, fadedVolume);
        });

        // Bắt đầu hiệu ứng
        crossfadeAnimator.start();
    }
    public boolean isMusicEnabled() {
        return isMusicEnabled;
    }

    public boolean isSfxEnabled() {
        return isSfxEnabled;
    }

    public boolean isIngame(){
        return isIngame;
    }

    // Gọi hàm này khi ứng dụng bị hủy để giải phóng tài nguyên
    public void release() {
        stopMusic();
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
        sfxMap.clear();
        instance = null;
    }
}

