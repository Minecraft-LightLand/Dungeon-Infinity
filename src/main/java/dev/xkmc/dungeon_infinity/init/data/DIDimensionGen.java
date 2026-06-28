package dev.xkmc.dungeon_infinity.init.data;

import com.tterrag.registrate.providers.DataProviderInitializer;
import dev.xkmc.dungeon_infinity.content.chunkgen.MazeChunkGenerator;
import dev.xkmc.dungeon_infinity.init.DungeonInfinity;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TimelineTags;
import net.minecraft.util.Mth;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.attribute.*;
import net.minecraft.world.level.CardinalLighting;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.timeline.Timeline;

import javax.annotation.Nullable;
import java.util.Optional;

public class DIDimensionGen {


	public static final ResourceKey<DimensionType> DT_MAZE = ResourceKey.create(Registries.DIMENSION_TYPE, DungeonInfinity.loc("maze"));
	public static final ResourceKey<LevelStem> LEVEL_MAZE = ResourceKey.create(Registries.LEVEL_STEM, DungeonInfinity.loc("maze"));

	public static final ResourceKey<Biome> BIOME_DREAM = biome("maze");

	public static void init(DataProviderInitializer init) {

		init.add(Registries.BIOME, (ctx) -> {
			var pf = ctx.lookup(Registries.PLACED_FEATURE);
			var wc = ctx.lookup(Registries.CONFIGURED_CARVER);

			ctx.register(BIOME_DREAM, biome(
					new MobSpawnSettings.Builder(),
					new BiomeGenerationSettings.Builder(pf, wc)
			));
		});

		init.add(Registries.DIMENSION_TYPE, (ctx) -> {
			HolderGetter<Timeline> timelines = ctx.lookup(Registries.TIMELINE);
			var spawn = new DimensionType.MonsterSettings(UniformInt.of(0, 7), 0);
			EnvironmentAttributeMap attr = EnvironmentAttributeMap.builder()
					.set(EnvironmentAttributes.FOG_COLOR, -4138753)
					.set(EnvironmentAttributes.SKY_COLOR, OverworldBiomes.calculateSkyColor(0.8F))
					.set(EnvironmentAttributes.AMBIENT_LIGHT_COLOR, 0xff4f4a74)
					.set(EnvironmentAttributes.BACKGROUND_MUSIC, BackgroundMusic.OVERWORLD)
					.set(EnvironmentAttributes.BED_RULE, BedRule.EXPLODES)
					.set(EnvironmentAttributes.RESPAWN_ANCHOR_WORKS, true)
					.set(EnvironmentAttributes.NETHER_PORTAL_SPAWNS_PIGLINS, false)
					.set(EnvironmentAttributes.AMBIENT_SOUNDS, AmbientSounds.LEGACY_CAVE_SETTINGS)
					.set(EnvironmentAttributes.FAST_LAVA, true)
					.set(EnvironmentAttributes.PIGLINS_ZOMBIFY, false)
					.set(EnvironmentAttributes.CAN_START_RAID, false)
					.set(EnvironmentAttributes.SNOW_GOLEM_MELTS, false)
					.build();
			ctx.register(DT_MAZE, new DimensionType(
					true, false, false, false,
					1, 0, 256, 256,
					BlockTags.INFINIBURN_OVERWORLD,
					0.5f, spawn,
					DimensionType.Skybox.NONE,
					CardinalLighting.Type.NETHER, attr,
					timelines.getOrThrow(TimelineTags.IN_OVERWORLD),
					Optional.empty()
			));
		});

		init.add(Registries.LEVEL_STEM, (ctx) -> {
			var dt = ctx.lookup(Registries.DIMENSION_TYPE);
			var biome = ctx.lookup(Registries.BIOME);
			ctx.register(LEVEL_MAZE, new LevelStem(dt.getOrThrow(DT_MAZE),
					new MazeChunkGenerator(biome.getOrThrow(BIOME_DREAM))));
		});

	}

	private static ResourceKey<Biome> biome(String id) {
		return ResourceKey.create(Registries.BIOME, DungeonInfinity.loc(id));
	}

	private static Biome biome(
			MobSpawnSettings.Builder spawns,
			BiomeGenerationSettings.PlainBuilder gen
	) {
		return biome(false, 0.5f, 0.5f, spawns, gen);
	}

	private static Biome biome(
			boolean hasPercipitation, float temperature, float downfall,
			MobSpawnSettings.Builder spawns,
			BiomeGenerationSettings.PlainBuilder gen
	) {
		return biome(hasPercipitation, temperature, downfall, 4159204, null, null, spawns, gen);
	}

	private static Biome biome(
			boolean hasPrecipitation, float temperature, float downfall,
			int waterColor,
			@Nullable Integer grassCol, @Nullable Integer foliageCol,
			MobSpawnSettings.Builder spawns,
			BiomeGenerationSettings.PlainBuilder gen
	) {
		BiomeSpecialEffects.Builder biomespecialeffects$builder = new BiomeSpecialEffects.Builder()
				.waterColor(waterColor);
		if (grassCol != null) {
			biomespecialeffects$builder.grassColorOverride(grassCol);
		}
		if (foliageCol != null) {
			biomespecialeffects$builder.foliageColorOverride(foliageCol);
		}
		return new Biome.BiomeBuilder()
				.hasPrecipitation(hasPrecipitation)
				.temperature(temperature)
				.downfall(downfall)
				.specialEffects(biomespecialeffects$builder.build())
				.mobSpawnSettings(spawns.build())
				.generationSettings(gen.build())
				.build();
	}

	protected static int calculateSkyColor(float temperature) {
		float f = Mth.clamp(temperature / 3.0F, -1.0F, 1.0F);
		return Mth.hsvToRgb(0.62222224F - f * 0.05F, 0.5F + f * 0.1F, 1.0F);
	}

}
