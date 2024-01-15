package classes;

import javafx.scene.media.AudioClip;

import java.io.File;
import java.net.MalformedURLException;
//Sound Effect by https://pixabay.com//?utm_source=link-attribution&utm_medium=referral&utm_campaign=music&utm_content=21468
public class AudioEffect {
    private  AudioClip audioClip;
    private  boolean musicPlaying;
    private File audioFile;

    /*----------------------CONSTRUCTOR---------------------------*/
    public AudioEffect(File audioFile) throws MalformedURLException { //Consider refactoring. Make audioEffect into an object and use it in the Controller class.
        this.audioFile = audioFile;
        File file = new File("src" + File.separator + "resources" + File.separator + "audioEffects" + File.separator + audioFile);
        String path = file.toURI().toURL().toString();
        audioClip = new AudioClip(path);

    }

    public File getAudioFile() {
        return audioFile;
    }


    /*-------------------------------------------------------------------------------------------------------------------*/
    public  void playEffect() {
        if (this.audioClip != null){
            audioClip.stop();
            audioClip.play();
            musicPlaying = true;
        }

    }
    public void stopMusic(){
        if (this.audioClip != null){
            audioClip.stop();
            musicPlaying = false;
        }else {
            musicPlaying = true;
        }

    }
}