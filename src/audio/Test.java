package audio;

import java.io.*;

public class Test {
	public static void main(String[] args) throws IOException{
		AudioMaster.init();
		AudioMaster.setListenerData();
		int buffer = AudioMaster.loadSound("audio/bounce.wav");
	    Source sourceA = new Source(40, 10, 0);
	    Source sourceB = new Source(-10, 0, 0);
	    
	    char c = ' ';
	    while(c != 'q') {
	    	c = (char)System.in.read();
	    	if(c == 'a') {
	    		sourceA.play(buffer);
	    	}
	    	if(c == 'b') {
	    		sourceB.play(buffer);
	    	}
	    }
	    sourceA.delete();
	    sourceB.delete();
	    AudioMaster.cleanUp();
	}
}
