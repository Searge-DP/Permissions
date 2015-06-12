package net.epoxide.permissions.common.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FileUtilities 
{

	public static String readFileAsString(File file) throws IOException 
	{
		BufferedReader bf = new BufferedReader(new FileReader(file));
		StringBuffer strBuf = new StringBuffer();
		String str;
		while((str = bf.readLine()) != null)
		{
			strBuf.append(str);
		}
		bf.close();
		return strBuf.toString();
	}

}
