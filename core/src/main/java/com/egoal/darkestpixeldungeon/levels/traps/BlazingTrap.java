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
package com.egoal.darkestpixeldungeon.levels.traps;

import com.egoal.darkestpixeldungeon.effects.CellEmitter;
import com.egoal.darkestpixeldungeon.Assets;
import com.egoal.darkestpixeldungeon.actors.blobs.Blob;
import com.egoal.darkestpixeldungeon.actors.blobs.Fire;
import com.egoal.darkestpixeldungeon.effects.particles.FlameParticle;
import com.egoal.darkestpixeldungeon.levels.Level;
import com.egoal.darkestpixeldungeon.scenes.GameScene;
import com.egoal.darkestpixeldungeon.sprites.TrapSprite;
import com.egoal.darkestpixeldungeon.utils.BArray;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;

public class BlazingTrap extends Trap {

  {
    color = TrapSprite.ORANGE;
    shape = TrapSprite.STARS;
  }


  @Override
  public void activate() {
    PathFinder.buildDistanceMap(pos, BArray.not(Level.solid, null), 2);
    for (int i = 0; i < PathFinder.distance.length; i++) {
      if (PathFinder.distance[i] < Integer.MAX_VALUE) {
        if (Level.pit[i] || Level.water[i])
          GameScene.add(Blob.seed(i, 1, Fire.class));
        else
          GameScene.add(Blob.seed(i, 5, Fire.class));
        CellEmitter.get(i).burst(FlameParticle.FACTORY, 5);
      }
    }
    Sample.INSTANCE.play(Assets.SND_BURNING);
  }
}
