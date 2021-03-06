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
package com.egoal.darkestpixeldungeon.levels.painters;

import com.egoal.darkestpixeldungeon.levels.Level;
import com.egoal.darkestpixeldungeon.levels.Room;
import com.egoal.darkestpixeldungeon.levels.Terrain;

public class EntrancePainter extends Painter {

  public static void paint(Level level, Room room) {

    fill(level, room, Terrain.WALL);
    fill(level, room, 1, Terrain.EMPTY);

    for (Room.Door door : room.connected.values()) {
      door.set(Room.Door.Type.REGULAR);
    }

    do {
      level.entrance = level.pointToCell(room.random(1));
    } while (level.findMob(level.entrance) != null);
    set(level, level.entrance, Terrain.ENTRANCE);
  }

}
