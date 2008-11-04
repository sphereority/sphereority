package kevin;
import common.Map;


public class MapTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
        // Map map = new Map();
        Map map = new Map("sample-map");
        //System.out.println(map.getName());
        //System.out.println(map.getData());
        //System.out.println(map.getXSize() + " x " + map.getYSize());
        System.out.println(map.toString());
        System.out.println(map.isWall(1, 1));
        System.out.println(map.isWall(9, 9));
	}

}
