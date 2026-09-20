package useless.moonsteel.api.paxel;

import luke.paxels.ItemToolPaxel;
import luke.paxels.PaxelItems;
import luke.paxels.PaxelMod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import turniplabs.halplibe.helper.ItemBuilder;
import turniplabs.halplibe.helper.creativeInventory.CreativeInventoryPlacement;
import turniplabs.halplibe.helper.recipeBuilders.RecipeBuilderShaped;
import useless.moonsteel.item.MoonSteelItems;

import static net.minecraft.client.render.item.model.ItemModelDispatcher.*;
import static net.minecraft.client.render.item.model.ItemModelDispatcher.HANDHELD_THIRD_PERSON_LEFT_HAND;
import static useless.moonsteel.MoonSteel.MOD_ID;
import static useless.moonsteel.item.MoonSteelItems.MOON_STEEL_TOOL;

public class MoonSteelPaxel {

	private MoonSteelPaxel(){}

	public static final int ID = 21000;
	public static Item TOOL_PAXEL_MOONSTEEL;

	public static void initItem(){
		TOOL_PAXEL_MOONSTEEL = new ItemBuilder(MOD_ID)
			.setCreativeInventoryPlacement(new CreativeInventoryPlacement.After(() -> Items.TOOL_SWORD_STEEL))
			.build(new ItemToolPaxel("tool.paxel.moonsteel", MOD_ID + ":item/tool_paxel_moonsteel", ID, MOON_STEEL_TOOL));
	}

	public static void recipeReady(){
		new RecipeBuilderShaped(PaxelMod.MOD_ID, "A5P", " S ", " S ")
			.addInput('A', MoonSteelItems.TOOL_AXE_MOONSTEEL)
			.addInput('5', MoonSteelItems.TOOL_SHOVEL_MOONSTEEL)
			.addInput('P', MoonSteelItems.TOOL_PICKAXE_MOONSTEEL)
			.addInput('S', Items.STICK)
			.create("moonsteel_paxel", new ItemStack(TOOL_PAXEL_MOONSTEEL));
	}

	@Environment(EnvType.CLIENT)
	public static void initItemModels(ItemModelDispatcher dispatcher) {
		dispatcher.addDispatch(new ItemModelStandard(TOOL_PAXEL_MOONSTEEL).setIcon("moonsteel:item/moonsteel_paxel")
			.setDisplayPos("firstperson_righthand", HANDHELD_FIRST_PERSON_RIGHT_HAND)
			.setDisplayPos("firstperson_lefthand", HANDHELD_FIRST_PERSON_LEFT_HAND)
			.setDisplayPos("thirdperson_righthand", HANDHELD_THIRD_PERSON_RIGHT_HAND)
			.setDisplayPos("thirdperson_lefthand", HANDHELD_THIRD_PERSON_LEFT_HAND)
		);
	}
}
