package net.epoxide.permissions.common.utility;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

public abstract class AbstractPermissionsFileReader implements IPermissionsFileReader
{
	public final File file;
	public final Map<Rank, List<String>> rankPermissions = new HashMap<Rank, List<String>>();
	public final Map<String, List<String>> playerSpecificPermissions = new HashMap<String, List<String>>();
	
	public AbstractPermissionsFileReader(File file)
	{
		this.file = file;
	}
	
	public AbstractPermissionsFileReader(String filePath)
	{
		this(new File(filePath));
	}
	
	public ImmutableMap<Rank, List<String>> getRankPermissions()
	{
		return ImmutableMap.copyOf(this.rankPermissions);
	}
	
	public ImmutableMap<String, List<String>> getPlayerPermissions()
	{
		return ImmutableMap.copyOf(this.playerSpecificPermissions);
	}
}