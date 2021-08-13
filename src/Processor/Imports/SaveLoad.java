package Processor.Imports;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SaveLoad {

    public static void StartLoad(int encode){
        String infos = loadInfoSave(encode);
        LoadGame(infos);
    }

    public static String loadInfoSave(int encode){
        String line = "";
        File file = new File("save.sg");
        if(file.exists()){
            try {
                String singleLine = null;
                BufferedReader reader = new BufferedReader(new FileReader("save.sg"));
                try {
                    while((singleLine = reader.readLine()) != null){
                        String[] separe = singleLine.split(":");
                        char[] value = separe[1].toCharArray();
                        separe[1] = "";
                        for(int i = 0; i < value.length; i++){
                            value[i]-=encode;
                            separe[1] += value[i];
                        }
                        line+=separe[0];
                        line+=":";
                        line+=separe[1];
                        line+="/";
                    }
                } catch (IOException e) {}
            } catch (FileNotFoundException e) {}
        }
        return line;
    }

    public static void LoadGame(String str){
        String[] content = str.split("/");
        for(int i = 0; i < content.length; i++){
            String[] content2 = content[i].split(":");
            switch(content2[0]){
                case "level":
                    System.out.println("Informação: " + content2[1]);
                break;
            }
        }
    }

    public static void SaveGame(String[] info1, int[] info2, int encode){
        BufferedWriter information = null;

        try {
            information = new BufferedWriter(new FileWriter("save.sg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < info1.length; i++){
            String currentInfo = info1[i];
            currentInfo += ":";
            char[] values = Integer.toString(info2[i]).toCharArray();
            for(int n = 0; n < values.length; n++){
                values[n]+=encode;
                currentInfo+=values[n];
            }
            try {
                information.write(currentInfo);
                if(i < info1.length - 1){
                    information.newLine();
                }
            } catch (IOException e) {}
        }
        try {
            information.flush();
            information.close();
        } catch (IOException e) {}
    }
}
