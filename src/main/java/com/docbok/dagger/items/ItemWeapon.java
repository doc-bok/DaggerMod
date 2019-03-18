package com.docbok.dagger.items;

import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.Multimap;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import scala.actors.threadpool.Arrays;

public class ItemWeapon extends ItemSword
{
	/*
	 * Creates a new weapon with the specified attributes and traits.
	 */
	@SuppressWarnings("unchecked")
	public ItemWeapon(ToolMaterial material, String weaponName, double damage, DamageType damageType, double weight, WeaponTrait[] traits)
	{
        super(material);
        
        _weaponTraits = new HashSet<>(Arrays.asList(traits));
        _material = material;
        
        _damage = damage;
        _damageType = damageType;
        _weight = weight;
        
        //	Light weapons attack faster.
        _attackDamage = _damage + _material.getAttackDamage();
        
        if (hasWeaponTrait(WeaponTrait.Light))
        {
        	weight -= 1.0d;
        }
        
        _attackSpeed = -2 - (weight * 0.1d);
        _weaponName = weaponName;
        
        if (material == ToolMaterial.WOOD)
        {
        	_burnTime = (int)(_weight * 50);
        }
        else
        {
        	_burnTime = 0;
        }        
        
        setMaxDamage(material.getMaxUses() / 2);
        
        String materialName = material.toString().toLowerCase();
        setRegistryName(materialName + "_" + weaponName);
        
        materialName = materialName.substring(0, 1).toUpperCase() + materialName.substring(1);
        setUnlocalizedName(weaponName + materialName);
	}

	/*
	 * Overridden so that weapons in general cannot destroy webs.
	 */
	@Override
    public float getDestroySpeed(ItemStack stack, IBlockState state)
    {
        Material material = state.getMaterial();
        return material != Material.PLANTS && 
        	   material != Material.VINE &&
        	   material != Material.CORAL &&
        	   material != Material.LEAVES &&
        	   material != Material.GOURD ? 1.0F : 1.5F;
    }

    /*
     * By default weapons can't harvest blocks.
     */
	@Override
    public boolean canHarvestBlock(IBlockState blockIn)
    {
        return false;
    }

    /*
     * Overrides the attack damage and speed with values specific to the current weapon.
     */
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot)
    {
        Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);
       
        if (equipmentSlot == EntityEquipmentSlot.MAINHAND)
        {
        	//	Remove sword values
        	multimap.removeAll(SharedMonsterAttributes.ATTACK_DAMAGE.getName());
        	multimap.removeAll(SharedMonsterAttributes.ATTACK_SPEED.getName());
        	
        	//	Update to dagger values
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", _attackDamage, 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", _attackSpeed, 0));
        }

        return multimap;
    }
    
    /*
     * Allows for custom weapon burn times.
     */
    @Override
    public int getItemBurnTime(ItemStack itemStack)
    {
        return _burnTime;
    }
    
    /*
     * Allows for special actions, such as being able to throw a weapon.
     */
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
    	if (hasWeaponTrait(WeaponTrait.Thrown))
    	{
	        return throwWeapon(worldIn, playerIn, handIn);
    	}

        ItemStack itemstack = playerIn.getHeldItem(handIn);
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
    }
	
    /*
     * Plays a sound if the weapon isn't silent.
     */
    public void playSound(EntityPlayer player, SoundEvent sound)
    {
    	playSound(player, sound, player.getSoundCategory(), 1.0f, 1.0f);
    }
    
    public void playSound(EntityPlayer player, SoundEvent sound, SoundCategory category, float volume, float pitch)
    {
    	playSound(player, player.posX, player.posY, player.posZ, sound, player.getSoundCategory(), volume, pitch);
    }
    
	public void playSound(EntityPlayer player, double x, double y, double z, SoundEvent sound, SoundCategory category, float volume, float pitch)
	{
		if (hasWeaponTrait(WeaponTrait.Silent))
		{
			volume *= 0.2;
		}
		
		player.world.playSound((EntityPlayer)null, x, y, z, sound, category, volume, pitch);
	}
    
    /*
     * Throws a weapon.
     */
    private ActionResult<ItemStack> throwWeapon(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
    	if (!playerIn.capabilities.isCreativeMode)
        {
            itemstack.shrink(1);
        }

        playSound(playerIn, SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!worldIn.isRemote)
        {
            EntitySnowball entitysnowball = new EntitySnowball(worldIn, playerIn);
            entitysnowball.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.5F, 1.0F);
            worldIn.spawnEntity(entitysnowball);
        }

        playerIn.addStat(StatList.getObjectUseStats(this));
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
    }
    
    public boolean isBludgeoning() { return _damageType == DamageType.Bludgeoning; }
    public boolean isPiercing() { return _damageType == DamageType.Piercing ||
    									 _damageType == DamageType.PiercingAndSlashing; }
    public boolean isSlashing() { return _damageType == DamageType.Slashing ||
    									 _damageType == DamageType.PiercingAndSlashing; }
    
    public boolean hasWeaponTrait(WeaponTrait trait) { return _weaponTraits.contains(trait); }
    
    public enum WeaponTrait
    {
    	Finesse,
    	Light,
    	ProneFighting,
    	Silent,
    	Thrown
    }
    
    protected enum DamageType
    {
    	Bludgeoning,
    	Piercing,
    	Slashing,
    	PiercingAndSlashing
    }
    
    private final ToolMaterial _material;
    private final double _damage;
    private final double _weight;
    private final int _burnTime;
    private final DamageType _damageType;
    private final double _attackDamage;
    private final double _attackSpeed;
    private final Set<WeaponTrait> _weaponTraits;
    private final String _weaponName;
}
