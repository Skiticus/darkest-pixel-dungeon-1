/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
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
package com.egoal.darkestpixeldungeon.levels.features;

import com.egoal.darkestpixeldungeon.Assets;
import com.egoal.darkestpixeldungeon.Dungeon;
import com.egoal.darkestpixeldungeon.effects.CellEmitter;
import com.egoal.darkestpixeldungeon.effects.particles.ElmoParticle;
import com.egoal.darkestpixeldungeon.levels.DeadEndLevel;
import com.egoal.darkestpixeldungeon.levels.Terrain;
import com.egoal.darkestpixeldungeon.messages.Languages;
import com.egoal.darkestpixeldungeon.messages.Messages;
import com.egoal.darkestpixeldungeon.scenes.GameScene;
import com.egoal.darkestpixeldungeon.utils.GLog;
import com.egoal.darkestpixeldungeon.windows.WndMessage;
import com.watabou.noosa.audio.Sample;

import java.util.HashSet;

public class Sign {

  private static final String[] teaser_texts = new String[]{
          "...",
          "...",
          "..."
  };

  private static final int[] signDepth = new int[]{
          0, 5, 6, 10, 11, 15, 16, 20, 21,
  };

  public static boolean showInDepth(int depth) {
    for (int i : signDepth) {
      if (i == depth) return true;
    }
    return false;
  }

  public static void read(int pos) {

    if (Dungeon.level instanceof DeadEndLevel) {

      GameScene.show(new WndMessage(Messages.get(Sign.class, "dead_end")));

    } else {

      if (Dungeon.depth <= 21) {
        // read sign
        if (showInDepth(Dungeon.depth))
          GameScene.show(new WndMessage(Messages.get(Sign.class, "tip_" + 
                  Dungeon.depth)));
      } else {

        //if we are at depths 22-24 and in english
        if (Dungeon.depth - 21 <= 3 && Messages.lang() == Languages.ENGLISH) {
          GameScene.show(new WndMessage(teaser_texts[Dungeon.depth - 22]));
        }

        Dungeon.level.destroy(pos);
        GameScene.updateMap(pos);
        GameScene.discoverTile(pos, Terrain.SIGN);

        GLog.w(Messages.get(Sign.class, "burn"));

        CellEmitter.get(pos).burst(ElmoParticle.FACTORY, 6);
        Sample.INSTANCE.play(Assets.SND_BURNING);
      }

    }
  }
}
