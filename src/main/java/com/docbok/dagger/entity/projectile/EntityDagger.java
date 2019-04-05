package com.docbok.dagger.entity.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityDagger extends EntityThrowable
{	
	public EntityDagger(World worldIn, ToolMaterial material)
	{
		super(worldIn);
		_material = material;
	}
	
	public EntityDagger(World worldIn, EntityLivingBase throwerIn, ToolMaterial material)
	{
		super(worldIn, throwerIn);
		_material = material;
	}
	
	public EntityDagger(World worldIn, double x, double y, double z, ToolMaterial material)
	{
		super(worldIn, x, y, z);
		_material = material;
	}

	@Override
	protected void onImpact(RayTraceResult result)
	{
		if (result.entityHit != null)
		{
			float damage = _material.getAttackDamage();
			result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this,  getThrower()), (float)damage);
		}
		
		if (!world.isRemote)
		{
			setDead();
		}
	}

	private final ToolMaterial _material;
}
