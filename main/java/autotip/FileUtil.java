package autotip;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtil {
	// write local variables to file, run any time a setting is changed or
	// someone is tipped (for totaltips)
	public static void writeVars() throws Exception {
		FileWriter saveFile = new FileWriter("config/autotip.txt");
		saveFile.write(AutotipMod.toggle + "\n");
		saveFile.write(AutotipMod.showTips + "\n");
		saveFile.write(AutotipMod.anon + "\n");
		saveFile.write(AutotipMod.totalTips + "\n");
		saveFile.close();
	}

	// get the stored variables on startup of mc
	public static void getVars() throws Exception {
		BufferedReader saveFile = new BufferedReader(new FileReader("config/autotip.txt"));
		AutotipMod.toggle = Boolean.parseBoolean((saveFile.readLine())); // toggle
		AutotipMod.showTips = Boolean.parseBoolean((saveFile.readLine())); // messages
		AutotipMod.anon = Boolean.parseBoolean((saveFile.readLine())); // anon
		AutotipMod.totalTips = Integer.parseInt(saveFile.readLine()); // total
																		// tips

		saveFile.close();

	}

	public static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
}