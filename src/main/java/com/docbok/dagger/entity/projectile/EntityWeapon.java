package com.docbok.dagger.entity.projectile;

import com.docbok.dagger.item.ItemWeapon;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityWeapon extends EntityThrowable
{	
	public EntityWeapon(World worldIn)
	{
		super(worldIn);
	}
	
	public EntityWeapon(World worldIn, EntityLivingBase throwerIn)
	{
		super(worldIn, throwerIn);
	}
	
	public EntityWeapon(World worldIn, double x, double y, double z)
	{
		super(worldIn, x, y, z);
	}
	
	public ItemWeapon getItem() { return _item; }
	public void setItem(ItemWeapon item, int damage) { _item = item; _damage = damage; }

	@Override
	protected void onImpact(RayTraceResult result)
	{		
		if (!world.isRemote)
		{
			if (result.entityHit != null)
			{
				float damage = _item.getThrownDamage();
				result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this,  getThrower()), (float)damage);
				_damage += 1;
			}
			else
			{
				_damage += 2;
			}
			
			ItemStack itemStack = new ItemStack(_item);
			if (_damage < itemStack.getMaxDamage())
			{
				itemStack.setItemDamage(_damage);
				world.spawnEntity(new EntityItem(world, result.hitVec.x, result.hitVec.y, result.hitVec.z, itemStack));
			}
			
			setDead();
		}
	}

	private ItemWeapon _item;
	private int _damage;
}
