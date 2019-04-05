package com.docbok.dagger.client.renderer;

import com.docbok.dagger.item.ItemList;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderDaggerDiamond<T extends Entity> extends RenderWeapon<T>
{
	public RenderDaggerDiamond(RenderManager renderManagerIn)
	{
		super(renderManagerIn, ItemList.ITEM_DAGGER_DIAMOND, 10.0F);
	}
}
