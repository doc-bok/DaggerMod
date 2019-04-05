package com.docbok.dagger.entity.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;

public class EntityDaggerGold  extends EntityDagger
{	
	public EntityDaggerGold(World worldIn)
	{
		super(worldIn, ToolMaterial.GOLD);
	}
	
	public EntityDaggerGold(World worldIn, EntityLivingBase throwerIn)
	{
		super(worldIn, throwerIn, ToolMaterial.GOLD);
	}
	
	public EntityDaggerGold(World worldIn, double x, double y, double z)
	{
		super(worldIn, x, y, z, ToolMaterial.GOLD);
	}
	
	public static void registerFixesDagger(DataFixer fixer)
	{
		EntityThrowable.registerFixesThrowable(fixer, "ThrownIronDagger");
	}
}