package net.epoxide.permissions.common.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.epoxide.permissions.common.api.Rank;

import com.google.common.collect.ImmutableMap;

public abstract class AbstractPermissionsFileReader implements IPermissionsFileReader
{
	protected final File file;
	protected final Map<Rank, ArrayList<String>> rankPermissions = new HashMap<Rank, ArrayList<String>>();
	protected final Map<String, ArrayList<String>> playerSpecificPermissions = new HashMap<String, ArrayList<String>>();
	
	public AbstractPermissionsFileReader(File file)
	{
		this.file = file;
	}
	
	public AbstractPermissionsFileReader(String filePath)
	{
		this(new File(filePath));
	}
	
	public ImmutableMap<Rank, ArrayList<String>> getRankPermissions()
	{
		return ImmutableMap.copyOf(this.rankPermissions);
	}
	
	public ImmutableMap<String, ArrayList<String>> getPlayerPermissions()
	{
		return ImmutableMap.copyOf(this.playerSpecificPermissions);
	}
	
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
