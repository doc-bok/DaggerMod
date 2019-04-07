package com.docbok.dagger.entity.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;

public class EntityDaggerStone extends EntityWeapon
{	
	public EntityDaggerStone(World worldIn)
	{
		super(worldIn, ToolMaterial.STONE);
	}
	
	public EntityDaggerStone(World worldIn, EntityLivingBase throwerIn)
	{
		super(worldIn, throwerIn, ToolMaterial.STONE);
	}
	
	public EntityDaggerStone(World worldIn, double x, double y, double z)
	{
		super(worldIn, x, y, z, ToolMaterial.STONE);
	}
	
	public static void registerFixesDagger(DataFixer fixer)
	{
		EntityThrowable.registerFixesThrowable(fixer, "ThrownStoneDagger");
	}
}
