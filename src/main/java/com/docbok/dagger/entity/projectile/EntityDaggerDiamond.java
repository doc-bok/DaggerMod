package com.docbok.dagger.entity.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;

public class EntityDaggerDiamond  extends EntityWeapon
{	
	public EntityDaggerDiamond(World worldIn)
	{
		super(worldIn);
	}
	
	public EntityDaggerDiamond(World worldIn, EntityLivingBase throwerIn)
	{
		super(worldIn, throwerIn);
	}
	
	public EntityDaggerDiamond(World worldIn, double x, double y, double z)
	{
		super(worldIn, x, y, z);
	}
	
	public static void registerFixesDagger(DataFixer fixer)
	{
		EntityThrowable.registerFixesThrowable(fixer, "ThrownIronDagger");
	}
}
