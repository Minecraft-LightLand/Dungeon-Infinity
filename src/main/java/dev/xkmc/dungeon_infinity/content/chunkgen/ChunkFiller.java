package dev.xkmc.dungeon_infinity.content.chunkgen;

import dev.xkmc.dungeon_infinity.init.DungeonInfinity;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.RandomSupport;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.XoroshiroRandomSource;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class ChunkFiller {


	public ChunkFiller() {
	}

	public void fillChunk(MazeDimHolder maze, ChunkPos pos, ChunkAccess access, RandomState random) {

	}

	public void decorateChunk(MazeDimHolder maze, ChunkPos pos, WorldGenLevel level, ChunkAccess access, RandomState random, StructureTemplateManager templates) {
		WorldgenRandom r = new WorldgenRandom(new XoroshiroRandomSource(RandomSupport.generateUniqueSeed()));
		var col = maze.getColumn(pos.x(), pos.z());
		for (int i = 0; i < col.length; i++) {
			long seed = r.setDecorationSeed(level.getSeed(), pos.x(), pos.z());
			var p = new BlockPos(pos.x() << 4, i << 4, pos.z() << 4);
			fillCell(col[i], p, level, access, r, templates);
		}
	}

	public void fillCell(int cell, BlockPos o, WorldGenLevel level, ChunkAccess access, RandomSource random, StructureTemplateManager templates) {
		var ins = CellInterpreter.getTemplate(cell);
		if (ins == CellInterpreter.SKIP) return;
		if (ins == CellInterpreter.MISSING) {
			DungeonInfinity.LOGGER.error("unexpected cell type " + cell + " at " + o);
			return;
		}
		var id = DungeonInfinity.loc("maze/" + ins.id());
		var opt = templates.get(id);
		if (opt.isEmpty()) {
			DungeonInfinity.LOGGER.error("template " + ins.id() + " not found");
			return;
		}
		var template = opt.get();
		var settings = new StructurePlaceSettings()
				.setKnownShape(true).setIgnoreEntities(true)
				.setRotation(ins.rot()).setMirror(ins.mir());

		var dst = StructureTemplate.transform(new BlockPos(15, 0, 15), ins.mir(), ins.rot(), BlockPos.ZERO);
		var src = StructureTemplate.transform(new BlockPos(ins.x0(), 0, ins.z0()), ins.mir(), ins.rot(), BlockPos.ZERO);

		o = o.offset(Math.max(0, -dst.getX()), 0, Math.max(0, -dst.getZ())).subtract(src);
		template.placeInWorld(level, o, o, settings, random, 18);
	}

}
