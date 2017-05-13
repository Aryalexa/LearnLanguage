import java.io.IOException;
import java.util.ArrayList;

import testingAudio.*;
import blobDetection.*;


/**
 * MAIN PRINCIPAL
 * @author aryalexa
 *
 */


public class Main {

	static String fileName = "itadakimasuA";//prueba001;itadakimasuA
	static String rawFileName = fileName + ".raw";
	static String wavFileName = fileName + ".wav";

	static ArrayList<Short> raw_data;
	
	public static void main(String[] args) {
		
		readRaw();
        
    }
	
	static void readRaw(){
		// read shorts
        raw_data = ReadWriteRaw.readShortsfromWav(wavFileName);
        if (raw_data == null){
            System.out.println("wav - no ha leido nada!!!");
            return;
        }
        System.out.println("wav - lectura hecha .."+raw_data.size()+" samples." );
        // save audio file to verify
        try {
			ReadWriteRaw.writeShortsToRaw(raw_data, "out_"+rawFileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

}
