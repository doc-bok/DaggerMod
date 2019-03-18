package com.docbok.dagger.items;

public class ItemDagger extends ItemWeapon
{
	public ItemDagger(ToolMaterial material)
	{
        super(material, "dagger", 1, DamageType.Piercing, 1, new WeaponTrait[]
        {
			WeaponTrait.Finesse,
			WeaponTrait.Light,
			WeaponTrait.ProneFighting,
			WeaponTrait.Silent,
			WeaponTrait.Thrown
        });
	}
}
