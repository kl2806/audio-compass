package audio;
import org.lwjgl.openal.AL10;
/* Source */
public class Source {
	private int sourceId;
	
	public Source(float x, float y, float z) {
		sourceId = AL10.alGenSources();
		AL10.alSourcef(sourceId, AL10.AL_GAIN, 1);
		AL10.alSourcef(sourceId, AL10.AL_PITCH, 1);
		AL10.alSource3f(sourceId, AL10.AL_POSITION, x, y, z);
	}
	
	public void setVelocity(float x, float y, float z) {
		AL10.alSource3f(sourceId, AL10.AL_VELOCITY, x, y, z);
	}
	
	/* Set the volume */
	public void setVolume(float v) {
		AL10.alSourcef(sourceId, AL10.AL_VELOCITY, v);
	}
	
	public void play(int buffer) {
		AL10.alSourcei(sourceId, AL10.AL_BUFFER, buffer);
		AL10.alSourcePlay(sourceId);
	}
	
	public void delete() {
		AL10.alDeleteSources(sourceId);
	}
}
