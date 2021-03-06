/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2017 TechReborn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package techreborn.dev;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import net.minecraft.util.EnumFacing;
import reborncore.RebornCore;
import reborncore.common.blocks.BlockMachineBase;
import techreborn.Core;

import java.io.*;

/**
 * Created by Mark on 24/04/2016.
 */

//TODO DO NOT SHIP THIS!
public class JsonGenerator {

	public void generate() throws IOException {
		File mcDir = new File(".");
		File exportFolder = new File(mcDir, "export");
		if (!exportFolder.exists()) {
			exportFolder.mkdir();
		}
		File assetsFolder = new File(exportFolder, "assets");
		if (!assetsFolder.exists()) {
			assetsFolder.mkdir();
		}
		File modFolder = new File(assetsFolder, "techreborn");
		if (!modFolder.exists()) {
			modFolder.mkdir();
		}
		File blockstates = new File(modFolder, "blockstates");
		if (!blockstates.exists()) {
			blockstates.mkdir();
		}
		File models = new File(modFolder, "models");
		if (!models.exists()) {
			models.mkdir();
		}
		File blockModels = new File(models, "block");
		if (!blockModels.exists()) {
			blockModels.mkdir();
		}
		File itemModles = new File(models, "item");
		if (!itemModles.exists()) {
			itemModles.mkdir();
		}
		File baseJsonFiles = new File(mcDir, "basejsons");
		if (!baseJsonFiles.exists()) {
			Core.logHelper.error("Could not find base jsons dir!");
			throw new FileNotFoundException();
		}
		File machineBaseFile = new File(baseJsonFiles, "machineBase.json");
		String machineBase = Files.toString(machineBaseFile, Charsets.UTF_8);
		for (Object object : RebornCore.jsonDestroyer.objectsToDestroy) {
			if (object instanceof BlockMachineBase) {
				BlockMachineBase base = (BlockMachineBase) object;
				String name = base.getRegistryName().getResourcePath().replace("tile.techreborn.", "");
				File state = new File(blockstates, name + ".json");
				if (state.exists()) {
					state.delete();
				}
				String output = machineBase;
				output = output.replaceAll("%OFF_TEXTURE%", base.getTextureNameFromState(base.getDefaultState(), EnumFacing.NORTH));
				output = output.replaceAll("%ON_TEXTURE%", base.getTextureNameFromState(base.getDefaultState().withProperty(BlockMachineBase.ACTIVE, true), EnumFacing.NORTH));
				output = output.replaceAll("%SIDE_TEXTURE%", base.getTextureNameFromState(base.getDefaultState(), EnumFacing.EAST));
				output = output.replaceAll("%TOP_TEXTURE%", base.getTextureNameFromState(base.getDefaultState(), EnumFacing.UP));
				try {
					FileOutputStream is = new FileOutputStream(state);
					OutputStreamWriter osw = new OutputStreamWriter(is);
					Writer w = new BufferedWriter(osw);
					w.write(output);
					w.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
