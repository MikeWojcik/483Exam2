package rose.wojcikmg.exam2wojcikmg;


import android.content.Context;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt Boutell on 3/28/2018.
 * Rose-Hulman Institute of Technology.
 * Covered by MIT license.
 */
public class FileUtils {
    public static List<Territory> loadFromJsonArray(Context context) {
        InputStream is = context.getResources().openRawResource(R.raw.state_info_json);
        List<Territory> territories = null;
        try {
            territories = new ObjectMapper().readValue(is, new TypeReference<List<Territory>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return territories;
    }

    public static List<Territory> loadFromCsv(Context context) {
        InputStream is = context.getResources().openRawResource(R.raw.state_info_csv);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        List<Territory> territories = new ArrayList<>();
        try {
            while (true) {
                String line = reader.readLine();
                if (line == null || line.length() == 0) {
                    break;
                }
                String[] parts = line.split("\t");
                String name = parts[0];
                String capital = parts[1];
                int area = Integer.parseInt(parts[2]);
                String nickname = parts[3];
                String governor = parts[4];
                String abbreviation = parts[5];

                // Assumes a constructor that takes params in this order
                territories.add(new Territory(name, capital, area));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return territories;
    }
}

//Note: I named my model object the more general term Territory and not State because someday I’d like to add all the countries of the world:

class Territory {
    public String name;
    public String capital;
    public int area;
    public String nickname;
    public String governor;
    public String abbreviation;
    // … other fields and methods as needed
    public Territory() { /* Empty constructor needed for Jackson json formatter */   }

    public Territory(String name, String capital, int area){
        name = name;
        capital = capital;
        area = area;
    }
}
