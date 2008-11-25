package kevin;
import common.Map;


public class MapTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
        // Map map = new Map();
        //Map map = new Map("widefield", );
        Map map = new Map("widefield", "10 10\n"+
                "++++++++++\n" +
                "+        +\n" +
                "+        +\n" +
                "+        +\n" +
                "+        +\n" +
                "+        +\n" +
                "+        +\n" +
                "+        +\n" +
                "+        +\n" +
                "++++++++++\n");
        //System.out.println(map.getName());
        //System.out.println(map.getData());
        //System.out.println(map.getXSize() + " x " + map.getYSize());
        map.dumpVars();
        //System.out.println(map.isWall(1, 1));
        //System.out.println(map.isWall(9, 9));
        map.dumpSpawnPoints();
	}

}
