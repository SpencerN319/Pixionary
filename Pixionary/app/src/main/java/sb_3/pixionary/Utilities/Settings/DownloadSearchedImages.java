package sb_3.pixionary.Utilities.Settings;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

public class DownloadSearchedImages {

    private static final String pull = "/Users/spencern319/AndroidStudioProjects/SB_3_ImageGuesser/Pixionary/app/src/main/res/BashScripts/imgtake.sh";
    private static final String clean = "/Users/spencern319/AndroidStudioProjects/SB_3_ImageGuesser/Pixionary/app/src/main/res/BashScripts/imgcleaner.sh";
    private String output;

    public DownloadSearchedImages(String key){
        try{
            String[] command = {pull, key};
            ProcessBuilder pb = new ProcessBuilder(command);
            Process process = pb.start();
            InputStream inputStream = process.getInputStream();

            StringBuilder cmdReturn = new StringBuilder();
            int c;
            while ((c = inputStream.read()) != -1) {
                cmdReturn.append((char) c);
            }
            output = cmdReturn.toString();
            Log.i("Returned ", cmdReturn.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected String get_output(){
        return this.output;
    }

    protected void clean(){
        try{
            String[] command = {clean};
            ProcessBuilder pb = new ProcessBuilder(command);
            Process process = pb.start();
            InputStream inputStream = process.getInputStream();

            StringBuilder cmdReturn = new StringBuilder();
            int c;
            while ((c = inputStream.read()) != -1) {
                cmdReturn.append((char) c);
            }
            output = cmdReturn.toString();
            Log.i("Returned ", cmdReturn.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
