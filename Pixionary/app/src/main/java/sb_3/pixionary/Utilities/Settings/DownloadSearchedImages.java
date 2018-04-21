package sb_3.pixionary.Utilities.Settings;

public class DownloadSearchedImages {

    public DownloadSearchedImages(String key){
        Process p = new ProcessBuilder("myCommand", key).start();
    }
}
