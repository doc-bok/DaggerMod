package com.docbok.dagger.client.renderer;

import com.docbok.dagger.item.ItemList;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderDaggerStone<T extends Entity> extends RenderWeapon<T>
{
	public RenderDaggerStone(RenderManager renderManagerIn)
	{
		super(renderManagerIn, ItemList.ITEM_DAGGER_STONE, 10.0F);
	}
}
