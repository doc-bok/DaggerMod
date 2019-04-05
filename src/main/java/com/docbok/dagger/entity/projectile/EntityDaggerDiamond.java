package com.docbok.dagger.entity.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;

public class EntityDaggerDiamond  extends EntityDagger
{	
	public EntityDaggerDiamond(World worldIn)
	{
		super(worldIn, ToolMaterial.DIAMOND);
	}
	
	public EntityDaggerDiamond(World worldIn, EntityLivingBase throwerIn)
	{
		super(worldIn, throwerIn, ToolMaterial.DIAMOND);
	}
	
	public EntityDaggerDiamond(World worldIn, double x, double y, double z)
	{
		super(worldIn, x, y, z, ToolMaterial.DIAMOND);
	}
	
	public static void registerFixesDagger(DataFixer fixer)
	{
		EntityThrowable.registerFixesThrowable(fixer, "ThrownIronDagger");
	}
}
