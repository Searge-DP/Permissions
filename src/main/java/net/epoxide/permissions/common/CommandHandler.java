package net.epoxide.permissions.common;

import net.epoxide.permissions.common.api.PermissionsRegistry;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraftforge.event.CommandEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class CommandHandler 
{
	@SubscribeEvent
	public void onCommandUsed(CommandEvent event)
	{
		if(!PermissionsRegistry.instance().isPlayerPermitted(CommandBase.getCommandSenderAsPlayer(event.sender), PermissionsRegistry.getPermStringForCommand(event.command)))
		{
			event.sender.addChatMessage(new ChatComponentTranslation("permissions.not.permitted", new Object[0]));
			event.setCanceled(true);
		}
	}
}
