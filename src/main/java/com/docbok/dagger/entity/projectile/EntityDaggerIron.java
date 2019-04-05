package com.docbok.dagger.entity.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;

public class EntityDaggerIron  extends EntityDagger
{	
	public EntityDaggerIron(World worldIn)
	{
		super(worldIn, ToolMaterial.IRON);
	}
	
	public EntityDaggerIron(World worldIn, EntityLivingBase throwerIn)
	{
		super(worldIn, throwerIn, ToolMaterial.IRON);
	}
	
	public EntityDaggerIron(World worldIn, double x, double y, double z)
	{
		super(worldIn, x, y, z, ToolMaterial.IRON);
	}
	
	public static void registerFixesDagger(DataFixer fixer)
	{
		EntityThrowable.registerFixesThrowable(fixer, "ThrownIronDagger");
	}
}
