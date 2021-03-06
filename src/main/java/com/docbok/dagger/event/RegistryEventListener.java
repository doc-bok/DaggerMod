package com.docbok.dagger.event;

import org.apache.logging.log4j.Logger;

import com.docbok.dagger.Reference;
import com.docbok.dagger.client.renderer.RenderWeapon;
import com.docbok.dagger.entity.projectile.EntityWeapon;
import com.docbok.dagger.item.ItemDagger;
import com.docbok.dagger.item.ItemList;
import com.docbok.dagger.material.MaterialList;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;

@Mod.EventBusSubscriber(modid = Reference.MODID)
public class RegistryEventListener
{
	public static void init(Logger logger)
	{
		_logger = logger;
		_logger.info("Init Mod Items : " + Reference.MODNAME);

		ItemList.ITEM_DAGGER_BONE = new ItemDagger(MaterialList.BONE);
		ItemList.ITEM_DAGGER_FLINT = new ItemDagger(MaterialList.FLINT);
		ItemList.ITEM_DAGGER_WOOD = new ItemDagger(ToolMaterial.WOOD);
		ItemList.ITEM_DAGGER_STONE = new ItemDagger(ToolMaterial.STONE);
		ItemList.ITEM_DAGGER_IRON = new ItemDagger(ToolMaterial.IRON);
		ItemList.ITEM_DAGGER_GOLD = new ItemDagger(ToolMaterial.GOLD);
		ItemList.ITEM_DAGGER_DIAMOND = new ItemDagger(ToolMaterial.DIAMOND);

		_items = new Item[] 
		{ 
			ItemList.ITEM_DAGGER_BONE,
			ItemList.ITEM_DAGGER_FLINT,
			ItemList.ITEM_DAGGER_WOOD,
			ItemList.ITEM_DAGGER_STONE,
			ItemList.ITEM_DAGGER_IRON,
			ItemList.ITEM_DAGGER_GOLD, 
			ItemList.ITEM_DAGGER_DIAMOND
		};

		int networkId = 0;
		_entities = new EntityEntry[]
		{ 
			EntityEntryBuilder.create().entity(EntityWeapon.class).id(Reference.MODID + ":" + "entity_weapon", networkId++).name("weapon").tracker(80, 3, true).build()
		};

		RenderingRegistry.registerEntityRenderingHandler(EntityWeapon.class, RenderWeapon::new);
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		_logger.info("Register Mod Items : " + Reference.MODNAME);
		event.getRegistry().registerAll(_items);
	}

	@SubscribeEvent
	public static void registerRenders(ModelRegistryEvent event) 
	{
		for (Item item : _items)
		{
			ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
		}
	}

	@SubscribeEvent
	public static void registerEntities(RegistryEvent.Register<EntityEntry> event) 
	{
		event.getRegistry().registerAll(_entities);
	}

	private static EntityEntry[] _entities;
	private static Item[] _items;
	private static Logger _logger;
}
