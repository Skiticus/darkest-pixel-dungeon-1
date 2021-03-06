/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2016 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.egoal.darkestpixeldungeon.windows;

import com.egoal.darkestpixeldungeon.DungeonTilemap;
import com.egoal.darkestpixeldungeon.ui.CustomTileVisual;
import com.egoal.darkestpixeldungeon.ui.RenderedTextMultiline;
import com.egoal.darkestpixeldungeon.Dungeon;
import com.egoal.darkestpixeldungeon.actors.blobs.Blob;
import com.egoal.darkestpixeldungeon.levels.Level;
import com.egoal.darkestpixeldungeon.levels.Terrain;
import com.egoal.darkestpixeldungeon.scenes.PixelScene;
import com.egoal.darkestpixeldungeon.ui.Window;
import com.watabou.noosa.Image;

public class WndInfoCell extends Window {

  private static final float GAP = 2;

  private static final int WIDTH = 120;

  public WndInfoCell(int cell) {

    super();

    int tile = Dungeon.level.map[cell];
    if (Level.water[cell]) {
      tile = Terrain.WATER;
    } else if (Level.pit[cell]) {
      tile = Terrain.CHASM;
    }

    CustomTileVisual vis = null;
    int x = cell % Dungeon.level.width();
    int y = cell / Dungeon.level.width();
    for (CustomTileVisual i : Dungeon.level.customTiles) {
      if ((x >= i.tileX && x < i.tileX + i.tileW) &&
              (y >= i.tileY && y < i.tileY + i.tileH)) {
        if (i.desc() != null) {
          vis = i;
          break;
        }
      }
    }


    String desc = "";

    IconTitle titlebar = new IconTitle();
    if (vis != null) {
      titlebar.icon(new Image(vis));
      titlebar.label(vis.name);
      desc += vis.desc();
    } else {

      if (tile == Terrain.WATER) {
        Image water = new Image(Dungeon.level.waterTex());
        water.frame(0, 0, DungeonTilemap.SIZE, DungeonTilemap.SIZE);
        titlebar.icon(water);
      } else {
        titlebar.icon(DungeonTilemap.tile(tile));
      }
      titlebar.label(Dungeon.level.tileName(tile));
      desc += Dungeon.level.tileDesc(tile);

    }
    titlebar.setRect(0, 0, WIDTH, 0);
    add(titlebar);

    RenderedTextMultiline info = PixelScene.renderMultiline(6);
    add(info);

    for (Blob blob : Dungeon.level.blobs.values()) {
      if (blob.cur[cell] > 0 && blob.tileDesc() != null) {
        if (desc.length() > 0) {
          desc += "\n\n";
        }
        desc += blob.tileDesc();
      }
    }

    info.text(desc);
    info.maxWidth(WIDTH);
    info.setPos(titlebar.left(), titlebar.bottom() + GAP);

    resize(WIDTH, (int) (info.top() + info.height()));
  }
}
