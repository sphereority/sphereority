package common;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Map {
	public static final String DEFAULT_MAP = "10 10\n"+
    "++++++++++\n" +
    "+        +\n" +
    "+        +\n" +
    "+        +\n" +
    "+        +\n" +
    "+        +\n" +
    "+        +\n" +
    "+        +\n" +
    "+        +\n" +
    "++++++++++\n"; 

	
    String name;       // map name
    String data;       // map raw data
    int x_size;        // map x size
    int y_size;        // map y size
    char[][] mapping;  // two dimensional array of char for mapping

    
    /**
     * Default Map constructor
     * Creates an empty Map that is 10 by 10 bounded by x's 
     */
    public Map() {
        name = "default";
        data = DEFAULT_MAP;
        parseData();
    }
    
    /**
     * 
     * @param mapname the name of the map to be read (default filepath is maps directory)
     */
    public Map(String mapname) {
        name = mapname.toString();
        data = "";
        try {
            File file = new File("maps/" + name + ".map"); //will this screw up in windows?
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                data = data + scanner.nextLine() + "\n";
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        parseData();
    }
    
    /**
     * Convenient procedure to parse raw data in constructors.
     *
     */
    private void parseData() {
        // parse raw data here
        Scanner parser = new Scanner(data);
        String line;
        x_size = parser.nextInt();
        y_size = parser.nextInt();
        mapping = new char[x_size][y_size];
        if (parser.nextLine().toString().startsWith(" ")) {
            // do nothing
        }
        int y = 0;
        while (parser.hasNextLine()) {
            line = parser.nextLine().toString();
            mapping[y] = line.toCharArray();
            y++;
        }        
    }
    
    /**
     * 
     * @return the name of the map
     */
    public String getName() {
        return name;
    }
    
    /** 
     * 
     * @return the raw text data input
     */
    public String getData() {
        return data;
    }
    
    /**
     * 
     * @return the maximum x of the map
     */
    public int getXSize() {
        return x_size;
    }
    
    /**
     * 
     * @return the maximum y of the map
     */
    public int getYSize() {
        return y_size;
    }
    
    /**
     * 
     * @return string representation of raw Map data
     */
    public String toString() {
        int i = 0;
        int j = 0;
        for (i = 0; i < y_size; i++) {
            for (j =0; j < x_size; j++) {
                System.out.print(mapping[i][j]);
            }
            System.out.println();
        }
        //System.out.print(mapping[9][10]);
        return "";
    }
    
    public boolean isWall(int x, int y)
    {
    	return (mapping[x][y] == '+');
    }
}
