package com.docbok.dagger.event;

import org.apache.logging.log4j.Logger;

import com.docbok.dagger.Reference;
import com.docbok.dagger.item.ItemWeapon;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid=Reference.MODID)
public class EntityEventListener 
{	
	public static void init(Logger logger)
	{
		_logger = logger;
		_logger.info("Init Entity Event Listener : " + Reference.MODNAME);
	}

	/*
	 * Allows for attacks by weapons without a sweep attack.
	 */
	@SubscribeEvent
	public static void onPlayerAttackTarget(AttackEntityEvent event)
	{
		EntityPlayer player = event.getEntityPlayer();
        ItemStack heldItem = player.getHeldItem(EnumHand.MAIN_HAND);
        Item item = heldItem.getItem();
        if (item instanceof ItemWeapon)
        {
        	ItemWeapon weapon = (ItemWeapon)item;
        	
        	//	Prevent normal attack code from running.
        	event.setCanceled(true);
        	
        	Entity targetEntity = event.getTarget();
    		if (targetEntity.canBeAttackedWithItem())
            {
                if (!targetEntity.hitByEntity(player))
                {
                    float attackDamage = (float)player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
                    
                    float modifier;
                    if (targetEntity instanceof EntityLivingBase)
                    {                       
                    	if (targetEntity instanceof AbstractSkeleton)
	                    {
	                    	if (!weapon.isBludgeoning())
	                    	{
	                    		attackDamage *= 0.5;
	                    	}
	                    }
                    	
                        modifier = EnchantmentHelper.getModifierForCreature(player.getHeldItemMainhand(), ((EntityLivingBase)targetEntity).getCreatureAttribute());
                    }
                    else
                    {
                        modifier = EnchantmentHelper.getModifierForCreature(player.getHeldItemMainhand(), EnumCreatureAttribute.UNDEFINED);
                    }

                    float cooledAttackStrength = player.getCooledAttackStrength(0.5F);
                    
                    attackDamage = attackDamage * (0.2F + cooledAttackStrength * cooledAttackStrength * 0.8F);
                    modifier = modifier * cooledAttackStrength;
                    player.resetCooldown();

                    if (attackDamage > 0.0F || modifier > 0.0F)
                    {
                        boolean isFullAttackStrength = cooledAttackStrength > 0.9F;
                        
                        int knockbackModifier = EnchantmentHelper.getKnockbackModifier(player);                        
                        if (isFullAttackStrength)
                        {
	                        if (player.isSprinting())
	                        {
	                        	weapon.playSound(player, SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK);
	                            ++knockbackModifier;
	                        }
                        }

                        boolean isCriticalHit = isFullAttackStrength &&
                        				player.fallDistance > 0.0F &&
                        				!player.onGround &&
                        				!player.isOnLadder() &&
                        				!player.isInWater() &&
                        				!player.isPotionActive(MobEffects.BLINDNESS) &&
                        				!player.isRiding() &&
                        				targetEntity instanceof EntityLivingBase && 
                        				!player.isSprinting();
                        				
                        float criticalHitModifier = 1.0f;
                        if (isCriticalHit)
                        {
                        	criticalHitModifier = 1.5f;
                        }

                        net.minecraftforge.event.entity.player.CriticalHitEvent hitResult = net.minecraftforge.common.ForgeHooks.getCriticalHit(player, targetEntity, isCriticalHit, criticalHitModifier);
                        if (hitResult != null)
                        {
                            attackDamage *= hitResult.getDamageModifier();
                        }

                        attackDamage = attackDamage + modifier;

                        float targetEntityHealth = 0.0F;
                        boolean startedBurning = false;
                        int fireAspectModifier = EnchantmentHelper.getFireAspectModifier(player);

                        if (targetEntity instanceof EntityLivingBase)
                        {
                            targetEntityHealth = ((EntityLivingBase)targetEntity).getHealth();

                            if (fireAspectModifier > 0 && !targetEntity.isBurning())
                            {
                                startedBurning = true;
                                targetEntity.setFire(1);
                            }
                        }

                        double targetMotionX = targetEntity.motionX;
                        double targetMotionY = targetEntity.motionY;
                        double targetMotionZ = targetEntity.motionZ;
                        boolean isAttacked = targetEntity.attackEntityFrom(DamageSource.causePlayerDamage(player), attackDamage);

                        if (isAttacked)
                        {
                            if (isCriticalHit)
                            {
                            	player.onCriticalHit(targetEntity);
                            	
                            	if (weapon.isSlashing())
                            	{
                            		float sweepingDamage = 1.0F + EnchantmentHelper.getSweepingDamageRatio(player) * attackDamage;

                                    for (EntityLivingBase entitylivingbase : player.world.getEntitiesWithinAABB(EntityLivingBase.class, targetEntity.getEntityBoundingBox().grow(1.0D, 0.25D, 1.0D)))
                                    {
                                        if (entitylivingbase != player && entitylivingbase != targetEntity && !player.isOnSameTeam(entitylivingbase) && player.getDistanceSq(entitylivingbase) < 9.0D)
                                        {
                                            entitylivingbase.knockBack(player, 0.4F, (double)MathHelper.sin(player.rotationYaw * 0.017453292F), (double)(-MathHelper.cos(player.rotationYaw * 0.017453292F)));
                                            entitylivingbase.attackEntityFrom(DamageSource.causePlayerDamage(player), sweepingDamage);
                                        }
                                    }

                                    weapon.playSound(player, SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP);
                                    player.spawnSweepParticles();
                            	}
                            	else
                            	{
                            		weapon.playSound(player, SoundEvents.ENTITY_PLAYER_ATTACK_CRIT);
                            	}
                            	
                            	if (weapon.isBludgeoning())
                            	{
                            		knockbackModifier = (knockbackModifier + 1) * 2;
                            	}
                            	
                            	if (weapon.isPiercing())
                            	{
                            		((EntityLivingBase)targetEntity).addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 6 * 20));
                            	}
                            }
                            else
                            {
                                if (isFullAttackStrength)
                                {
                                	weapon.playSound(player, SoundEvents.ENTITY_PLAYER_ATTACK_STRONG);
                                }
                                else
                                {
                                	weapon.playSound(player, SoundEvents.ENTITY_PLAYER_ATTACK_WEAK);
                                }
                            }
                            
                            if (knockbackModifier > 0)
                            {
                                if (targetEntity instanceof EntityLivingBase)
                                {
                                    ((EntityLivingBase)targetEntity).knockBack(player, (float)knockbackModifier * 0.5F, (double)MathHelper.sin(player.rotationYaw * 0.017453292F), (double)(-MathHelper.cos(player.rotationYaw * 0.017453292F)));
                                }
                                else
                                {
                                    targetEntity.addVelocity((double)(-MathHelper.sin(player.rotationYaw * 0.017453292F) * (float)knockbackModifier * 0.5F), 0.1D, (double)(MathHelper.cos(player.rotationYaw * 0.017453292F) * (float)knockbackModifier * 0.5F));
                                }

                                player.motionX *= 0.6D;
                                player.motionZ *= 0.6D;
                                player.setSprinting(false);
                            }

                            if (targetEntity instanceof EntityPlayerMP && targetEntity.velocityChanged)
                            {
                                ((EntityPlayerMP)targetEntity).connection.sendPacket(new SPacketEntityVelocity(targetEntity));
                                targetEntity.velocityChanged = false;
                                targetEntity.motionX = targetMotionX;
                                targetEntity.motionY = targetMotionY;
                                targetEntity.motionZ = targetMotionZ;
                            }

                            if (modifier > 0.0F)
                            {
                            	player.onEnchantmentCritical(targetEntity);
                            }

                            player.setLastAttackedEntity(targetEntity);

                            if (targetEntity instanceof EntityLivingBase)
                            {
                                EnchantmentHelper.applyThornEnchantments((EntityLivingBase)targetEntity, player);
                            }

                            EnchantmentHelper.applyArthropodEnchantments(player, targetEntity);
                            ItemStack mainhandItem = player.getHeldItemMainhand();
                            Entity entity = targetEntity;

                            if (targetEntity instanceof MultiPartEntityPart)
                            {
                                IEntityMultiPart ientitymultipart = ((MultiPartEntityPart)targetEntity).parent;

                                if (ientitymultipart instanceof EntityLivingBase)
                                {
                                    entity = (EntityLivingBase)ientitymultipart;
                                }
                            }

                            if (!mainhandItem.isEmpty() && entity instanceof EntityLivingBase)
                            {
                                ItemStack beforeHitCopy = mainhandItem.copy();
                                mainhandItem.hitEntity((EntityLivingBase)entity, player);

                                if (mainhandItem.isEmpty())
                                {
                                    net.minecraftforge.event.ForgeEventFactory.onPlayerDestroyItem(player, beforeHitCopy, EnumHand.MAIN_HAND);
                                    player.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
                                }
                            }

                            if (targetEntity instanceof EntityLivingBase)
                            {
                                float damageDealt = targetEntityHealth - ((EntityLivingBase)targetEntity).getHealth();
                                player.addStat(StatList.DAMAGE_DEALT, Math.round(damageDealt * 10.0F));

                                if (fireAspectModifier > 0)
                                {
                                    targetEntity.setFire(fireAspectModifier * 4);
                                }

                                if (player.world instanceof WorldServer && damageDealt > 2.0F)
                                {
                                    int numParticles = (int)((double)damageDealt * 0.5D);
                                    ((WorldServer)player.world).spawnParticle(EnumParticleTypes.DAMAGE_INDICATOR, targetEntity.posX, targetEntity.posY + (double)(targetEntity.height * 0.5F), targetEntity.posZ, numParticles, 0.1D, 0.0D, 0.1D, 0.2D);
                                }
                            }

                            player.addExhaustion(0.1F);
                        }
                        else
                        {
                        	weapon.playSound(player, SoundEvents.ENTITY_PLAYER_ATTACK_NODAMAGE);

                            if (startedBurning)
                            {
                                targetEntity.extinguish();
                            }
                        }
                    }
                }
            }
        }
	}
	
	private static Logger _logger;
}
