package com.docbok.dagger.item;

import com.docbok.dagger.entity.projectile.EntityDaggerDiamond;
import com.docbok.dagger.entity.projectile.EntityDaggerGold;
import com.docbok.dagger.entity.projectile.EntityDaggerIron;
import com.docbok.dagger.entity.projectile.EntityDaggerStone;
import com.docbok.dagger.entity.projectile.EntityDaggerWood;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.world.World;

public class ItemDagger extends ItemWeapon
{
	public ItemDagger(ToolMaterial material)
	{
        super(material, "dagger", 1, DamageType.Piercing, 1, new WeaponTrait[]
        {
			WeaponTrait.Thrown
        });
	}
    
    protected EntityThrowable getEntityThrowable(World worldIn, EntityPlayer playerIn)
    {
    	switch (getMaterial())
    	{
    	case STONE:
    		return new EntityDaggerStone(worldIn, playerIn);
    		
    	case IRON:
    		return new EntityDaggerIron(worldIn, playerIn);
    		
    	case GOLD:
    		return new EntityDaggerGold(worldIn, playerIn);
    		
    	case DIAMOND:
    		return new EntityDaggerDiamond(worldIn, playerIn);
    		
    	case WOOD:
    	default:
    	   	return new EntityDaggerWood(worldIn, playerIn);
    	}
    }
}
