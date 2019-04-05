package com.docbok.dagger.client.renderer;

import com.docbok.dagger.item.ItemList;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderDaggerWood<T extends Entity> extends RenderWeapon<T>
{
	public RenderDaggerWood(RenderManager renderManagerIn)
	{
		super(renderManagerIn, ItemList.ITEM_DAGGER_WOOD, 10.0F);
	}
}
