package com.docbok.dagger.entity.projectile;

import com.docbok.dagger.item.ItemWeapon;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityWeapon extends EntityThrowable
{	
    private static final DataParameter<ItemStack> ITEM = EntityDataManager.<ItemStack>createKey(EntityItem.class, DataSerializers.ITEM_STACK);
    
	public EntityWeapon(World worldIn)
	{
		super(worldIn);
		setItem(ItemStack.EMPTY);
	}
	
	public EntityWeapon(World worldIn, EntityLivingBase throwerIn)
	{
		super(worldIn, throwerIn);
		setItem(ItemStack.EMPTY);
	}
	
	public EntityWeapon(World worldIn, double x, double y, double z)
	{
		super(worldIn, x, y, z);
		setItem(ItemStack.EMPTY);
	}

    protected void entityInit()
    {
        getDataManager().register(ITEM, ItemStack.EMPTY);
    }
	
    public ItemStack getItemStack()
    {
        return (ItemStack)getDataManager().get(ITEM);
    }
	
	public void setItem(ItemStack weapon)
	{
		ItemStack copy = new ItemStack(weapon.getItem());
		copy.setItemDamage(weapon.getItemDamage());
        getDataManager().set(ITEM, copy);
        getDataManager().setDirty(ITEM);		
	}

	@Override
	protected void onImpact(RayTraceResult result)
	{		
		if (!world.isRemote)
		{
			ItemStack itemStack = getItemStack();
			ItemWeapon weapon = (ItemWeapon)itemStack.getItem();
			
			int damage = 2;
			if (result.entityHit != null)
			{
				float attackDamage = weapon.getThrownDamage();
				result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this,  getThrower()), (float)attackDamage);
				damage = 1;
			}
			
			damage += itemStack.getItemDamage();
			if (damage < itemStack.getMaxDamage())
			{
				itemStack.setItemDamage(damage);
				world.spawnEntity(new EntityItem(world, result.hitVec.x, result.hitVec.y, result.hitVec.z, itemStack));
			}
			
			setDead();
		}
	}
}
