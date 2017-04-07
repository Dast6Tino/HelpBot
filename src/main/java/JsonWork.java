import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonWork {
    public static void setJson(ToDoObject file, String name) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        File dir = new File(name).getParentFile();
        if(!dir.exists())
            dir.mkdir();
        try {
            BufferedWriter out = new BufferedWriter( new FileWriter(name));
            out.write(gson.toJson(file));
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ToDoObject getJson(String name) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson1 = builder.create();
        try {
            BufferedReader reader = new BufferedReader(
                    new FileReader("Data\\outfilename"));
            ToDoObject object = gson1.fromJson(reader, ToDoObject.class);
            return object;
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static void makeDir(String name) {

    }
}
