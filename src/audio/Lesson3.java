package audio;

/* 
 * Copyright (c) 2004 LWJGL Project
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are 
 * met:
 * 
 * * Redistributions of source code must retain the above copyright 
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'LWJGL' nor the names of 
 *   its contributors may be used to endorse or promote products derived 
 *   from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING 
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Random;
 
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;
 
/**
 * $Id$
 * <p>
 * Lesson 3: Multiple Sources
 * </p>
 * @author Brian Matzon <brian@matzon.dk>
 * @version $Revision$
 */
public class Lesson3 {
   
  /** Maximum data buffers we will need. */
  public static final int NUM_BUFFERS = 3;
   
  /** Maximum emissions we will need. */
  public static final int NUM_SOURCES = 3;
   
  /** Index of battle sound */
  public static final int BATTLE = 0;
   
  /** Index of gun 1 sound */
  public static final int GUN1 = 1;
   
  /** Index of gun 2 sound */
  public static final int GUN2 = 2;
   
  /** Buffers hold sound data. */
  IntBuffer buffer = BufferUtils.createIntBuffer(NUM_BUFFERS);
 
  /** Sources are points emitting sound. */
  IntBuffer source = BufferUtils.createIntBuffer(NUM_BUFFERS);
   
  /** Position of the source sound. */
  FloatBuffer sourcePos = BufferUtils.createFloatBuffer(3*NUM_BUFFERS);
 
  /*
   * These are 3D cartesian vector coordinates. A structure or class would be
   * a more flexible of handling these, but for the sake of simplicity we will
   * just leave it as is.
   */  
   
  /** Velocity of the source sound. */
  FloatBuffer sourceVel = BufferUtils.createFloatBuffer(3*NUM_BUFFERS);
 
  /** Position of the listener. */
  FloatBuffer listenerPos = BufferUtils.createFloatBuffer(3).put(new float[] { -10.0f, 0.0f, 0.0f });
 
  /** Velocity of the listener. */
  FloatBuffer listenerVel = BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f });
 
  /** Orientation of the listener. (first 3 elements are "at", second 3 are "up")
      Also note that these should be units of '1'. */
  FloatBuffer listenerOri = BufferUtils.createFloatBuffer(6).put(new float[] { 0.0f, 0.0f, -1.0f,  0.0f, 1.0f, 0.0f });
   
  public Lesson3() {
    // CRUCIAL!
    // any buffer that has data added, must be flipped to establish its position and limits
    listenerPos.flip();
    listenerVel.flip();
    listenerOri.flip();
  }
   
  /**
   * boolean LoadALData()
   *
   *  This function will load our sample data from the disk using the Alut
   *  utility and send the data into OpenAL as a buffer. A source is then
   *  also created to play that buffer.
   */
  int loadALData() {
    // Load wav data into a buffers.
    AL10.alGenBuffers(buffer);
 
    if(AL10.alGetError() != AL10.AL_NO_ERROR)
      return AL10.AL_FALSE;
 
    WaveData waveFile = WaveData.create("audio/bounce.wav");
    AL10.alBufferData(buffer.get(BATTLE), waveFile.format, waveFile.data, waveFile.samplerate);
    waveFile.dispose();
     
    waveFile = WaveData.create("audio/bounce.wav");
    AL10.alBufferData(buffer.get(GUN1), waveFile.format, waveFile.data, waveFile.samplerate);
    waveFile.dispose();
 
    waveFile = WaveData.create("audio/bounce.wav");
    AL10.alBufferData(buffer.get(GUN2), waveFile.format, waveFile.data, waveFile.samplerate);
    waveFile.dispose();
     
 
    // Bind buffers into audio sources.
    AL10.alGenSources(source);
 
    if(AL10.alGetError() != AL10.AL_NO_ERROR)
      return AL10.AL_FALSE;
 
    AL10.alSourcei(source.get(BATTLE), AL10.AL_BUFFER,   buffer.get(BATTLE) );
    AL10.alSourcef(source.get(BATTLE), AL10.AL_PITCH,    1.0f          );
    AL10.alSourcef(source.get(BATTLE), AL10.AL_GAIN,     1.0f          );
    AL10.alSource (source.get(BATTLE), AL10.AL_POSITION, (FloatBuffer) sourcePos.position(BATTLE*3));
    AL10.alSource (source.get(BATTLE), AL10.AL_VELOCITY, (FloatBuffer) sourceVel.position(BATTLE*3));
    AL10.alSourcei(source.get(BATTLE), AL10.AL_LOOPING,  AL10.AL_TRUE  );
     
    AL10.alSourcei(source.get(GUN1), AL10.AL_BUFFER,   buffer.get(GUN1) );
    AL10.alSourcef(source.get(GUN1), AL10.AL_PITCH,    1.0f          );
    AL10.alSourcef(source.get(GUN1), AL10.AL_GAIN,     1.0f          );
    AL10.alSource (source.get(GUN1), AL10.AL_POSITION, (FloatBuffer) sourcePos.position(GUN1*3));
    AL10.alSource (source.get(GUN1), AL10.AL_VELOCITY, (FloatBuffer) sourceVel.position(GUN1*3));
    AL10.alSourcei(source.get(GUN1), AL10.AL_LOOPING,  AL10.AL_FALSE  );
     
    AL10.alSourcei(source.get(GUN2), AL10.AL_BUFFER,   buffer.get(GUN2) );
    AL10.alSourcef(source.get(GUN2), AL10.AL_PITCH,    1.0f          );
    AL10.alSourcef(source.get(GUN2), AL10.AL_GAIN,     1.0f          );
    AL10.alSource (source.get(GUN2), AL10.AL_POSITION, (FloatBuffer) sourcePos.position(GUN2*3));
    AL10.alSource (source.get(GUN2), AL10.AL_VELOCITY, (FloatBuffer) sourceVel.position(GUN2*3));
    AL10.alSourcei(source.get(GUN2), AL10.AL_LOOPING,  AL10.AL_FALSE  );    
     
    // Do another error check and return.
    if(AL10.alGetError() == AL10.AL_NO_ERROR)
      return AL10.AL_TRUE;
 
    return AL10.AL_FALSE;
  }  
   
  /**
   * void setListenerValues()
   *
   *  We already defined certain values for the Listener, but we need
   *  to tell OpenAL to use that data. This function does just that.
   */
  void setListenerValues() {
    AL10.alListener(AL10.AL_POSITION,    listenerPos);
    AL10.alListener(AL10.AL_VELOCITY,    listenerVel);
    AL10.alListener(AL10.AL_ORIENTATION, listenerOri);
  }  
 
  /**
   * void killALData()
   *
   *  We have allocated memory for our buffers and sources which needs
   *  to be returned to the system. This function frees that memory.
   */
  void killALData() {
    AL10.alDeleteSources(source);
    AL10.alDeleteBuffers(buffer);
  }
   
  /**
   *  Check for keyboard hit
   */ 
  private boolean kbhit() {
    try {
      return (System.in.available() != 0);
    } catch (IOException ioe) {
    }
    return false;
  }  
   
  public void execute() {
    // Initialize OpenAL and clear the error bit.
    try {
      AL.create();
    } catch (LWJGLException le) {
      le.printStackTrace();
      return;
    }
    AL10.alGetError();
 
    // Load the wav data.
    if(loadALData() == AL10.AL_FALSE) {
      System.out.println("Error loading data.");
      return;
    }
 
    setListenerValues();
 
    AL10.alSourcePlay(source.get(BATTLE));
     
    // loop 
    int play;
    Random random = new Random();
 
    System.out.println("Press ENTER to exit");
     
    while(!kbhit()) {
      for(int i=1; i<NUM_SOURCES; i++) {
        play = AL10.alGetSourcei(source.get(i), AL10.AL_SOURCE_STATE);
         
        if(play != AL10.AL_PLAYING) {
//          double theta = (double) (random.nextInt() % 360) * 3.14 / 180.0;
//           
//          sourcePos.put(i*3+0, -(float) (Math.cos(theta)));
//          sourcePos.put(i*3+1, -(float) (random.nextInt()%2));
//          sourcePos.put(i*3+2, -(float) (Math.sin(theta)));
           
          AL10.alSource(source.get(i), AL10.AL_POSITION, (FloatBuffer) sourcePos.position(i*3));
          AL10.alSourcePlay(source.get(i));          
        }
      }
      try{
        Thread.sleep(100);
        }catch(InterruptedException e){
        System.out.println("Sleep Interrupted");
      }
    }
    killALData();
    AL.destroy();
  }
   
  public static void main(String[] args) {
    new Lesson3().execute();    
  }
}