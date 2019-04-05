package com.docbok.dagger.entity.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.util.DamageSource;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityDaggerWood  extends EntityThrowable
{	
	public EntityDaggerWood(World worldIn)
	{
		super(worldIn);
	}
	
	public EntityDaggerWood(World worldIn, EntityLivingBase throwerIn)
	{
		super(worldIn, throwerIn);
	}
	
	public EntityDaggerWood(World worldIn, double x, double y, double z)
	{
		super(worldIn, x, y, z);
	}
	
	public static void registerFixesDagger(DataFixer fixer)
	{
		EntityThrowable.registerFixesThrowable(fixer, "ThrownWoodDagger");
	}

	@Override
	protected void onImpact(RayTraceResult result)
	{
		if (result.entityHit != null)
		{
			float damage = ToolMaterial.WOOD.getAttackDamage();
			result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this,  getThrower()), (float)damage);
		}
		
		if (!world.isRemote)
		{
			setDead();
		}
	}
}
