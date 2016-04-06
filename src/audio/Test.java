package audio;

import java.io.*;

public class Test {
	public static void main(String[] args) throws IOException{
		AudioMaster.init();
		AudioMaster.setListenerData();
		int buffer = AudioMaster.loadSound("audio/laughfuzzball.wav");
	    Source sourceA = new Source(10, 1, 10);
	    //sourceA.setVelocity(100, 100, 100);
	    Source sourceB = new Source(10, -1, -10);
	    //sourceB.setVelocity(100, -100, 100);
	    
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
