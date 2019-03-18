package com.docbok.dagger;

import org.apache.logging.log4j.Logger;

import com.docbok.dagger.init.ModItems;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid=Reference.MODID, name=Reference.MODNAME, version=Reference.VERSION)
public class DaggerMod 
{	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		_logger = event.getModLog();
		_logger.info("Mod initializing : " + Reference.MODNAME);
		ModItems.init(_logger);
	}
	
	@EventHandler
	public void Init(FMLInitializationEvent event)
	{
		_logger.info("Mod initialized : " + Reference.MODNAME);
	}
	
	@EventHandler
	public void PostInit(FMLPostInitializationEvent event)
	{
		_logger.info("Mod post initialization : " + Reference.MODNAME);
	}
	
	@Instance
	public static DaggerMod INSTANCE;
	
	private static Logger _logger;
}
