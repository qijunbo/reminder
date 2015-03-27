package home.tony.reminder.player;

import home.tony.reminder.R;

import java.util.HashMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class AudioPlayer {

	HashMap<Integer, Integer> spMap;
	SoundPool soundPool;
	Context context;
	private static int streamID;

	public AudioPlayer(Context context) {
		soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
		spMap = new HashMap<Integer, Integer>();
		this.context = context;

		spMap.put(1, soundPool.load(context, R.raw.simplelow, 1));
		spMap.put(2, soundPool.load(context, R.raw.simplehigh, 1));
	}

	public int play(int sound) {
		AudioManager am = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		float audioMaxVolumn = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		float volumnCurrent = am.getStreamVolume(AudioManager.STREAM_MUSIC);
		float volumnRatio = volumnCurrent / audioMaxVolumn;

		streamID = soundPool.play(spMap.get(sound), volumnRatio, volumnRatio,
				1, -1, 1f);
		return streamID;
	}

	public void stop() {
		soundPool.stop(streamID);
	}

}
