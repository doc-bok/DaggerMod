package com.docbok.dagger.entity.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;

public class EntityDaggerIron  extends EntityWeapon
{	
	public EntityDaggerIron(World worldIn)
	{
		super(worldIn);
	}
	
	public EntityDaggerIron(World worldIn, EntityLivingBase throwerIn)
	{
		super(worldIn, throwerIn);
	}
	
	public EntityDaggerIron(World worldIn, double x, double y, double z)
	{
		super(worldIn, x, y, z);
	}
	
	public static void registerFixesDagger(DataFixer fixer)
	{
		EntityThrowable.registerFixesThrowable(fixer, "ThrownIronDagger");
	}
}
