import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;

public class WaifuBot {
	
	//hashmap to store directories and files inside it
	private static HashMap<String, File[]> waifuMap = new HashMap<String, File[]>();
	
	//filter for images only
	private final static FilenameFilter IMAGES = new FilenameFilter() {
		public boolean accept(File dir, String name) {
				String[] extensions = {"gif", "png", "jpg", "bmp"};
				for (String ext : extensions) 
					if (name.endsWith(ext)) { return true; }
				return false;
		}
	};

	public static void main(String[] args) {
			File waifusFolder = new File("./waifus");
			File[] waifus = waifusFolder.listFiles(File::isDirectory);
			
			for (File waifu : waifus) 
				waifuMap.put(waifu.getName(), waifu.listFiles(IMAGES));
			
			System.out.println(waifuMap.get("mio")[0]);
			
	}

}
