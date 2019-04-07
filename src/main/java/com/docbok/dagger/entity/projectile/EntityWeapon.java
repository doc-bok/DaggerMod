package com.docbok.dagger.entity.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityWeapon extends EntityThrowable
{	
	public EntityWeapon(World worldIn, ToolMaterial material)
	{
		super(worldIn);
		_material = material;
	}
	
	public EntityWeapon(World worldIn, EntityLivingBase throwerIn, ToolMaterial material)
	{
		super(worldIn, throwerIn);
		_material = material;
	}
	
	public EntityWeapon(World worldIn, double x, double y, double z, ToolMaterial material)
	{
		super(worldIn, x, y, z);
		_material = material;
	}
	
	public void setItem(Item item, int damage) { _item = item; _damage = damage; }	

	@Override
	protected void onImpact(RayTraceResult result)
	{
		if (result.entityHit != null)
		{
			float damage = _material.getAttackDamage();
			result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this,  getThrower()), (float)damage);
			_damage += 1;
		}
		else
		{
			_damage += 2;
		}
		
		if (!world.isRemote)
		{
			ItemStack itemStack = new ItemStack(_item);
			if (_damage < itemStack.getMaxDamage())
			{
				itemStack.setItemDamage(_damage);
				world.spawnEntity(new EntityItem(world, result.hitVec.x, result.hitVec.y, result.hitVec.z, itemStack));
			}
			
			setDead();
		}
	}

	private final ToolMaterial _material;
	private Item _item;
	private int _damage;
}
