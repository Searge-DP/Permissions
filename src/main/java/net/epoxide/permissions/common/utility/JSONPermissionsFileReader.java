package net.epoxide.permissions.common.utility;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.epoxide.permissions.common.api.Rank;
import net.epoxide.permissions.common.ref.Constants;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class JSONPermissionsFileReader extends AbstractPermissionsFileReader 
{	
	public static final Gson JSON = new GsonBuilder().registerTypeAdapter(PermissionsJson.class, new PermissionsJsonDeserializer())
			.registerTypeAdapter(PermissionsJson.class, new PermissionsJsonSerializer())
			.create();
	
	public static class PermissionsJson
	{
		public final ImmutableMap<Rank, List<String>> ranks;
		public final ImmutableMap<String, List<String>> playerPerms;
		
		public PermissionsJson(Map<Rank, List<String>> ranks, Map<String, List<String>> playerPerms) 
		{		
			if(ranks != null)
				this.ranks = ImmutableMap.copyOf(ranks);
			else
				this.ranks = null;
			
			if(playerPerms != null)
				this.playerPerms = ImmutableMap.copyOf(playerPerms);
			else
				this.playerPerms = null;
		}

		public void print() 
		{
			for(Rank rank : ranks.keySet())
			{
				Constants.LOGGER.error(rank.getRankName());
				for(String perm : ranks.get(rank))
				{
					Constants.LOGGER.error(perm);
				}
			}
		}
	}
	
	public static PermissionsJson readJson(File inputFile)
	{
		try 
		{
			return JSON.fromJson(new JsonReader(new BufferedReader(new FileReader(inputFile))), PermissionsJson.class);
		} 
		catch (JsonIOException e) 
		{
			Constants.LOGGER.error(e.getMessage());
			e.printStackTrace();
		} 
		catch (JsonSyntaxException e) 
		{
			Constants.LOGGER.error(e.getMessage());
			e.printStackTrace();
		}
		catch (FileNotFoundException e) 
		{
			Constants.LOGGER.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	public static void writeJson(File outputFile, PermissionsJson permissionsJson)
	{
		try
		{
			BufferedWriter bufWriter = new BufferedWriter(new FileWriter(outputFile));
			JsonWriter writer = new JsonWriter(bufWriter);
			writer.setIndent("    ");
			JSON.toJson(permissionsJson, PermissionsJson.class, writer);
			writer.flush();
			writer.close();
		}
		catch(IOException e)
		{
			Constants.LOGGER.error(e.getMessage());
		}
	}
	
	public static class PermissionsJsonDeserializer implements JsonDeserializer<PermissionsJson>
	{
		@Override
		public PermissionsJson deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException 
		{
			JsonObject jsonObj = json.getAsJsonObject();
			JsonObject rankObj = jsonObj.get("ranks").getAsJsonObject();
			Map<Rank, List<String>> ranks = new HashMap<Rank, List<String>>();
			if(rankObj != null)
			{
				Constants.LOGGER.error("BEFORE");
				Constants.LOGGER.error(rankObj.toString());
				for(Entry<String, JsonElement> entry: rankObj.entrySet())
				{
					Constants.LOGGER.error("TEST-2");
					List<String> permList = new ArrayList<String>();
					for(JsonElement perm : entry.getValue().getAsJsonArray())
					{
						permList.add(perm.getAsString());
					}
					ranks.put(new Rank(entry.getKey(), 1), permList);
				}
			}
			Constants.LOGGER.error("AFTER");
			JsonObject playerPermObj = jsonObj.get("player-perms").getAsJsonObject();
			Map<String, List<String>> playerPerms = new HashMap<String, List<String>>();
			if(playerPermObj != null)
			{
				for(Entry<String, JsonElement> entry: rankObj.entrySet())
				{
					List<String> permList = new ArrayList<String>();
					for(JsonElement perm : entry.getValue().getAsJsonArray())
					{
						permList.add(perm.getAsString());
					}
					playerPerms.put(entry.getKey(), permList);
				}
			}
			return new PermissionsJson(ranks, playerPerms);
		}	
	}
	
	public static class PermissionsJsonSerializer implements JsonSerializer<PermissionsJson>
	{
		@Override
		public JsonElement serialize(PermissionsJson src, Type typeOfSrc, JsonSerializationContext context) 
		{
			JsonObject baseElement = new JsonObject();
			JsonObject ranks = new JsonObject();
			if(src.ranks != null)
			{
				for(Rank rank : src.ranks.keySet())
				{
					JsonArray jsonArray = new JsonArray();
					for(String perm : src.ranks.get(rank))
					{
						jsonArray.add(new JsonPrimitive(perm));
					}
					Constants.LOGGER.error(rank.getRankName());
					ranks.add(rank.getRankName(), jsonArray);
				}
			}
			
			JsonObject playerPerms = new JsonObject();
			if(src.playerPerms != null)
			{
				for(Entry<String, List<String>> entry : src.playerPerms.entrySet())
				{
					JsonArray jsonArray = new JsonArray();
					for(String perm : entry.getValue())
					{
						jsonArray.add(new JsonPrimitive(perm));
					}
					playerPerms.add(entry.getKey(), jsonArray);
				}
			}
			baseElement.add("ranks", ranks);
			baseElement.add("player-perms", playerPerms);
			return baseElement;
		}
	}
	
	public JSONPermissionsFileReader(String fileName)
	{
		this(new File(fileName));
	}
	
	public JSONPermissionsFileReader(File file) 
	{
		super(file);
	}

	@Override
	public void readFromFile() 
	{
		PermissionsJson json = readJson(file);
		this.rankPermissions = json.ranks;
		this.playerSpecificPermissions = json.playerPerms;
	}
}