package com.docbok.dagger.item;

public class ItemDagger extends ItemWeapon
{
	public ItemDagger(ToolMaterial material)
	{
        super(material, "dagger", 1, DamageType.Piercing, 1, new WeaponTrait[]
        {
			WeaponTrait.Thrown
        });
	}
}
