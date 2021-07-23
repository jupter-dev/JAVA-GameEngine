package Terrain.IA;

public class PAstar {
    public int x,y;

    public PAstar(int x, int y){
        this.x = x;
        this.y = y;
    }

    public boolean equals(Object object){
        PAstar pos = (PAstar) object;
        if(pos.x == this.x && pos.y == this.y){
            return true;
        }
        return false;
    }
}
