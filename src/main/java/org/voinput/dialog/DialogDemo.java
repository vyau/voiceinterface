
package org.voinput.dialog;

import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.StringBuffer;
import java.lang.Thread;
import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;

/**
*  This is a demo program of how to act a generic voice input using SPhinx 4 voice recognition library.
*  http://cmusphinx.sourceforge.net/wiki/tutorialsphinx4
*  Please refer to the included Sphinx4 licensing term file.
* 
*  I need a generic voice input mechanism to work with a very specific language model so i can
*  use voice to drive home automation tasks.   
*  A few items I have planned:    
*   Turning on/off X10 light switches, manage Skype interactions (e.g. dialing, auto-answer calls)
* 
*  A lot of items I plan to drive needs to be in their own event loop.  Hence, I am making this class
*  a singleton thread class, to yield control to the caller and have voiceinput listening in the
*  background.
*  
*/


public class DialogDemo extends Thread {

    private final String ACOUSTIC_MODEL =
        "resource:/edu/cmu/sphinx/models/en-us/en-us";
    private final String DICTIONARY_PATH =
        "resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict";
    private final String GRAMMAR_PATH =
        "resource:/org/voinput/dialog/";
    private final String LANGUAGE_MODEL =
        "resource:/org/voinput/dialog/reference.lm";
    private static DialogDemo singleDialogInstance = null;
    private static final Map<String, Integer> DIGITS =
        new HashMap<String, Integer>();

    Configuration configuration = null;
    LiveSpeechRecognizer lmRecognizer = null;
    boolean listenState = false;
    boolean threadState = false;
    String utterance = "";
    PrintWriter writer = null;

    public DialogDemo() {
        configuration = new Configuration();
        configuration.setAcousticModelPath(ACOUSTIC_MODEL);
        configuration.setDictionaryPath(DICTIONARY_PATH);
        configuration.setGrammarPath(GRAMMAR_PATH);
        configuration.setUseGrammar(true);
        configuration.setUseGrammar(false);
        configuration.setLanguageModelPath(LANGUAGE_MODEL);
        try {
            writer = new PrintWriter("/tmp/voinput.txt", "UTF-8");
        } catch (Exception e) {
            System.out.println("PrintWriter problem");
        }

    }




    /**
    * This is helper method to turn strings into hex for web encoding purpose.
    */
    private final String[] hex = {
        "%00", "%01", "%02", "%03", "%04", "%05", "%06", "%07",
        "%08", "%09", "%0a", "%0b", "%0c", "%0d", "%0e", "%0f",
        "%10", "%11", "%12", "%13", "%14", "%15", "%16", "%17",
        "%18", "%19", "%1a", "%1b", "%1c", "%1d", "%1e", "%1f",
        "%20", "%21", "%22", "%23", "%24", "%25", "%26", "%27",
        "%28", "%29", "%2a", "%2b", "%2c", "%2d", "%2e", "%2f",
        "%30", "%31", "%32", "%33", "%34", "%35", "%36", "%37",
        "%38", "%39", "%3a", "%3b", "%3c", "%3d", "%3e", "%3f",
        "%40", "%41", "%42", "%43", "%44", "%45", "%46", "%47",
        "%48", "%49", "%4a", "%4b", "%4c", "%4d", "%4e", "%4f",
        "%50", "%51", "%52", "%53", "%54", "%55", "%56", "%57",
        "%58", "%59", "%5a", "%5b", "%5c", "%5d", "%5e", "%5f",
        "%60", "%61", "%62", "%63", "%64", "%65", "%66", "%67",
        "%68", "%69", "%6a", "%6b", "%6c", "%6d", "%6e", "%6f",
        "%70", "%71", "%72", "%73", "%74", "%75", "%76", "%77",
        "%78", "%79", "%7a", "%7b", "%7c", "%7d", "%7e", "%7f",
        "%80", "%81", "%82", "%83", "%84", "%85", "%86", "%87",
        "%88", "%89", "%8a", "%8b", "%8c", "%8d", "%8e", "%8f",
        "%90", "%91", "%92", "%93", "%94", "%95", "%96", "%97",
        "%98", "%99", "%9a", "%9b", "%9c", "%9d", "%9e", "%9f",
        "%a0", "%a1", "%a2", "%a3", "%a4", "%a5", "%a6", "%a7",
        "%a8", "%a9", "%aa", "%ab", "%ac", "%ad", "%ae", "%af",
        "%b0", "%b1", "%b2", "%b3", "%b4", "%b5", "%b6", "%b7",
        "%b8", "%b9", "%ba", "%bb", "%bc", "%bd", "%be", "%bf",
        "%c0", "%c1", "%c2", "%c3", "%c4", "%c5", "%c6", "%c7",
        "%c8", "%c9", "%ca", "%cb", "%cc", "%cd", "%ce", "%cf",
        "%d0", "%d1", "%d2", "%d3", "%d4", "%d5", "%d6", "%d7",
        "%d8", "%d9", "%da", "%db", "%dc", "%dd", "%de", "%df",
        "%e0", "%e1", "%e2", "%e3", "%e4", "%e5", "%e6", "%e7",
        "%e8", "%e9", "%ea", "%eb", "%ec", "%ed", "%ee", "%ef",
        "%f0", "%f1", "%f2", "%f3", "%f4", "%f5", "%f6", "%f7",
        "%f8", "%f9", "%fa", "%fb", "%fc", "%fd", "%fe", "%ff"
    };

    /**
    * Encode string into web URL ready string
    */
    private final String encode(String s)
    {
        StringBuffer sbuf = new StringBuffer();
        int len = s.length();
        for (int i = 0; i < len; i++) {
            int ch = s.charAt(i);
            if ('A' <= ch && ch <= 'Z') {     // 'A'..'Z'
                sbuf.append((char)ch);
            } else if ('a' <= ch && ch <= 'z') {  // 'a'..'z'
                sbuf.append((char)ch);
            } else if ('0' <= ch && ch <= '9') {  // '0'..'9'
                sbuf.append((char)ch);
            } else if (ch == ' ') {           // space
                sbuf.append('+');
            } else if (ch == '-' || ch == '_'     // unreserved
            || ch == '.' || ch == '!'
            || ch == '~' || ch == '*'
            || ch == '\'' || ch == '('
            || ch == ')') {
                sbuf.append((char)ch);
            } else if (ch <= 0x007f) {        // other ASCII
                sbuf.append(hex[ch]);
            } else if (ch <= 0x07FF) {        // non-ASCII <= 0x7FF
                sbuf.append(hex[0xc0 | (ch >> 6)]);
                sbuf.append(hex[0x80 | (ch & 0x3F)]);
            } else {                  // 0x7FF < ch <= 0xFFFF
                sbuf.append(hex[0xe0 | (ch >> 12)]);
                sbuf.append(hex[0x80 | ((ch >> 6) & 0x3F)]);
                sbuf.append(hex[0x80 | (ch & 0x3F)]);
            }
        }
        return sbuf.toString();
    }

    /**
    * Start recognition state
    * Originally implemented with event loop inside here 
    * but decided to pass the event loop logic to higher logic
    * Caller of this method should implement event loop logic to turn on/off 
    * recognition logic
    *
    * @see DialogDemo#stopRecognition()
    *
    */
    public void startRecognition() {
        try {
            while (true) {
                lmRecognizer =
                    new LiveSpeechRecognizer(configuration);
                lmRecognizer.startRecognition(true);
                listenState = true;
                utterance = lmRecognizer.getResult().getHypothesis();
                writer.println("matched " + utterance + "\n");
                writer.flush();
                System.out.println("matched " + utterance + "\n");
                
                // to do: 
                //  "command to start/exit recognition"
                stopRecognition();

            }
       } catch (Exception e) {
           lmRecognizer = null;
           listenState = false;
       } finally {
         writer.close();
       }
    }


    /**
    *  Stop recognition state
    *  @See DialogDemo#startRecognition()
    */
    public void stopRecognition() {
        lmRecognizer.stopRecognition();
        lmRecognizer = null;
        listenState = false;
    }


    /**
    * Get last voice input
    */
    public String lastMatchedInput() {
        return utterance;
    }

    /**
    * Return true if recognition engine is turned on, false otherwise
    */
    public boolean isInRecognitionLoop() {
        return listenState == true;
    }

    public void run() {
        
            threadState = true;
            startRecognition();
        
       
    }

    public static void main(String[] args) throws Exception {
       // System.out.println("No command line interface");
        DialogDemo dd = new DialogDemo();
        dd.start();

    }
}
