package Terrain.IA;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import Terrain.Tile;
import Terrain.World;
import Terrain.Type.Wall;

public class AAstar {
    public static double lastTime = System.currentTimeMillis();
    private static Comparator<MAstar> pathSorter = new Comparator<MAstar>(){
        public int compare(MAstar s0, MAstar s1){
            if(s1.fCost < s0.fCost)
                return +1;
            if(s1.fCost > s0.fCost)
                return -1;
            return 0;
        }
    };

    public static boolean clear(){
        if(System.currentTimeMillis() - lastTime >= 1000){
            return true;
        }
        return false;
    }

    public static List<MAstar> findPath(World world, PAstar start, PAstar end){
        lastTime = System.currentTimeMillis();
        List<MAstar> possibilities = new ArrayList<MAstar>();
        List<MAstar> failures = new ArrayList<MAstar>();

        MAstar current = new MAstar(start, null, 0, getDistance(start, end));
        possibilities.add(current);
        while(possibilities.size() > 0){
            Collections.sort(possibilities, pathSorter);
            current = possibilities.get(0);
            if(current.tile.equals(end)){
                //Chegamos no ponto final
                List<MAstar> path = new ArrayList<MAstar>();
                while(current.parent != null){
                    path.add(current);
                    current = current.parent;
                }
                possibilities.clear();
                failures.clear();
                return path;
            }
            possibilities.remove(current);
            failures.add(current);

            for(int i = 0; i < 9; i++){
                if(i == 4) continue;
                //L, R, T, B
                int x = current.tile.x;
                int y = current.tile.y;
                //T+L, T+R, B+L, B+R
                int xv = (i%3) - 1;
                int yv = (i/3) - 1;

                Tile tile = World.tiles[x+xv+((y+yv)*World.WIDTH)];
                if(tile == null) continue;
                if(tile instanceof Wall) continue;
                if(i == 0){
                    Tile validate = World.tiles[x+xv+1+((y+yv)* World.WIDTH)];
                    Tile confirmation = World.tiles[x+xv+((y+yv+1)* World.WIDTH)];
                    if(validate instanceof Wall || confirmation instanceof Wall){
                        continue;
                    }
                }
                else if(i == 2){
                    Tile validate = World.tiles[x+xv-1+((y+yv)* World.WIDTH)];
                    Tile confirmation = World.tiles[x+xv+((y+yv + 1)* World.WIDTH)];
                    if(validate instanceof Wall || confirmation instanceof Wall){
                        continue;
                    }
                }
                else if(i == 6){
                    Tile validate = World.tiles[x+xv+((y+yv-1)* World.WIDTH)];
                    Tile confirmation = World.tiles[x+xv+1+((y+yv)* World.WIDTH)];
                    if(validate instanceof Wall || confirmation instanceof Wall){
                        continue;
                    }
                }
                else if(i == 8){
                    Tile validate = World.tiles[x+xv+((y+yv-1)* World.WIDTH)];
                    Tile confirmation = World.tiles[x+xv-1+((y+yv)* World.WIDTH)];
                    if(validate instanceof Wall || confirmation instanceof Wall){
                        continue;
                    }
                }

                PAstar pos = new PAstar(x+xv, y+yv);
                double gCost = current.gCost + getDistance(current.tile, pos);
                double hCost = getDistance(pos, end);

                MAstar mstar = new MAstar(pos, current, gCost, hCost);

                if(pathInList(failures,pos) && gCost >= current.gCost) continue;

                if(!pathInList(possibilities, pos)){
                    possibilities.add(mstar);
                }else if(gCost < current.gCost){
                    possibilities.remove(current);
                    possibilities.add(mstar);
                }
            }
        }
        failures.clear();
        return null;
    }

    private static boolean pathInList(List<MAstar> list, PAstar position){
       for(int i = 0; i < list.size(); i++){
           if(list.get(i).tile.equals(position)){
               return true;
           }
       } 
       return false;
    }

    private static double getDistance(PAstar tile, PAstar goal){
        double dx = tile.x - goal.x;
        double dy = tile.y - goal.y;

        return Math.sqrt(dx*dx + dy*dy);
    }
   
}
