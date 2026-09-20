package useless.moonsteel.item;

import net.minecraft.core.block.Blocks;
import net.minecraft.core.entity.animal.MobWolf;
import net.minecraft.core.enums.HumanArmorShape;
import net.minecraft.core.enums.WolfArmorShape;
import net.minecraft.core.item.*;
import net.minecraft.core.item.material.ArmorMaterial;
import net.minecraft.core.item.material.ToolMaterial;
import net.minecraft.core.item.tag.ItemTags;
import net.minecraft.core.item.tool.ItemToolAxe;
import net.minecraft.core.item.tool.ItemToolHoe;
import net.minecraft.core.item.tool.ItemToolPickaxe;
import net.minecraft.core.item.tool.ItemToolShovel;
import net.minecraft.core.item.tool.ItemToolSword;
import org.jetbrains.annotations.NotNull;
import turniplabs.halplibe.helper.ArmorHelper;
import turniplabs.halplibe.helper.ItemBuilder;
import useless.moonsteel.MoonSteel;
import useless.moonsteel.api.backpack.BackpackProxy;
import useless.moonsteel.block.MoonSteelBlocks;
import useless.moonsteel.item.connectstar.ItemConnectedStar;

import static useless.moonsteel.MoonSteel.MOD_ID;

public class MoonSteelItems {
	private MoonSteelItems(){}


	public static final ToolMaterial MOON_STEEL_TOOL = new ToolMaterial().setDurability(1536).setEfficiency(7.0f, 14.0f).setMiningLevel(3).setDamage(2);
	public static final ArmorMaterial MOON_STEEL_ARMOR = ArmorHelper.createArmorMaterial(MOD_ID, "moonsteel", 800, 51f, 45f, 45f, 100f);

	public static Item INGOT_MOONSTEEL;
	public static Item INGOT_MOONSTEEL_CRUDE ;
	public static Item TOOL_PICKAXE_MOONSTEEL;
	public static Item TOOL_AXE_MOONSTEEL;
	public static Item TOOL_SHOVEL_MOONSTEEL;
	public static Item TOOL_HOE_MOONSTEEL;
	public static Item TOOL_SWORD_MOONSTEEL;


	public static Item ARMOR_HELMET_MOONSTEEL;
	public static Item ARMOR_CHESTPLATE_MOONSTEEL;
	public static Item ARMOR_LEGGINGS_MOONSTEEL;
	public static Item ARMOR_BOOTS_MOONSTEEL;

	public static Item STAR_FALLEN;
	public static Item STAR_CONNECTED;

	public static Item BACKPACK_COSMIC;

	// new Items
	public static Item STAR_JAR;
	public static Item ARMOR_WOLF_MOONSTEEL;

	public static void init() {
		// 7.3 and before Items
		INGOT_MOONSTEEL = new ItemBuilder(MOD_ID)
			.build(new Item("ingot.moonsteel", MOD_ID + ":item/ingot_moonsteel", MoonSteel.itemId++));
		INGOT_MOONSTEEL_CRUDE = new ItemBuilder(MOD_ID)
			.build(new Item("crude.moonsteel", MOD_ID + ":item/ingot_moonsteel_crude", MoonSteel.itemId++));
		TOOL_PICKAXE_MOONSTEEL = new ItemBuilder(MOD_ID)
			.build(new ItemToolPickaxe("tool.pickaxe.moonsteel", MOD_ID + ":item/tool_pickaxe_moonsteel", MoonSteel.itemId++, MOON_STEEL_TOOL));

		TOOL_AXE_MOONSTEEL = new ItemBuilder(MOD_ID)
			.build(new ItemToolAxe("tool.axe.moonsteel", MOD_ID + ":item/tool_axe_moonsteel", MoonSteel.itemId++, MOON_STEEL_TOOL));
		TOOL_SHOVEL_MOONSTEEL = new ItemBuilder(MOD_ID)
			.build(new ItemToolShovel("tool.shovel.moonsteel", MOD_ID + ":item/tool_shovel_moonsteel", MoonSteel.itemId++, MOON_STEEL_TOOL));
		TOOL_HOE_MOONSTEEL = new ItemBuilder(MOD_ID)
			.build(new ItemToolHoe("tool.hoe.moonsteel", MOD_ID + ":item/tool_hoe_moonsteel", MoonSteel.itemId++, MOON_STEEL_TOOL));
		TOOL_SWORD_MOONSTEEL = new ItemBuilder(MOD_ID)
			.addTags(ItemTags.PREVENT_CREATIVE_MINING)
			.build(new ItemToolSword("tool.sword.moonsteel", MOD_ID + ":item/tool_sword_moonsteel", MoonSteel.itemId++, MOON_STEEL_TOOL));

		ARMOR_HELMET_MOONSTEEL = new ItemBuilder(MOD_ID)
			.build(new ItemArmor<>("helmet.moonsteel", MOD_ID + ":item/armor_helmet_moonsteel", MoonSteel.itemId++, MOON_STEEL_ARMOR, HumanArmorShape.HEAD));
		ARMOR_CHESTPLATE_MOONSTEEL = new ItemBuilder(MOD_ID)
			.build(new ItemArmor<>("chestplate.moonsteel", MOD_ID + ":item/armor_chestplate_moonsteel", MoonSteel.itemId++, MOON_STEEL_ARMOR, HumanArmorShape.CHEST));
		ARMOR_LEGGINGS_MOONSTEEL = new ItemBuilder(MOD_ID)
			.build(new ItemArmor<>("leggings.moonsteel", MOD_ID + ":item/armor_leggings_moonsteel", MoonSteel.itemId++, MOON_STEEL_ARMOR, HumanArmorShape.LEGS));
		ARMOR_BOOTS_MOONSTEEL = new ItemBuilder(MOD_ID)
			.build(new ItemArmor<>("boots.moonsteel", MOD_ID + ":item/armor_boots_moonsteel", MoonSteel.itemId++, MOON_STEEL_ARMOR, HumanArmorShape.BOOTS));

		STAR_FALLEN = new ItemBuilder(MOD_ID)
			.build(new Item("star.fallen", MOD_ID + ":item/star_fallen", MoonSteel.itemId++));
		STAR_CONNECTED = new ItemBuilder(MOD_ID)
			.setStackSize(1)
			.build(new ItemConnectedStar("star.connected", MOD_ID + ":item/star_connected", MoonSteel.itemId++));

		MoonSteelItems.initBackpack();
		MoonSteel.starZombieSword = MoonSteelItems.TOOL_SWORD_MOONSTEEL.getDefaultStack();

		// 8.0.1 Items
		STAR_JAR = new ItemBuilder(MOD_ID)
			.build(new ItemPlaceable("jar.star", MOD_ID + ":item/jar_star", MoonSteel.itemId++, MoonSteelBlocks.JAR_STAR));

		ARMOR_WOLF_MOONSTEEL = new ItemBuilder(MOD_ID)
			.build(new ItemArmor<>("armor.wolf.moonsteel", MOD_ID + ":item/armor_wolf_moonsteel", MoonSteel.itemId++, MOON_STEEL_ARMOR, WolfArmorShape.BODY));
	}

	private static void initBackpack() {
		MoonSteel.LOGGER.info("Backpacks present: {}", MoonSteel.backpackPresent);
		if (MoonSteel.backpackPresent){
			BACKPACK_COSMIC = new ItemBuilder(MOD_ID)
				.setStackSize(1)
				.build( BackpackProxy.proxyBackpack("backpack.cosmic", MOD_ID + ":item/backpack_cosmic", MoonSteel.itemId++));
		} else {
			BACKPACK_COSMIC = new ItemBuilder(MOD_ID)
				.setStackSize(1)
				.setTags(ItemTags.NOT_IN_CREATIVE_MENU)
				.build(new Item("backpack.cosmic.missing", MOD_ID + ":item/backpack_cosmic", MoonSteel.itemId++));
		}
	}
}
