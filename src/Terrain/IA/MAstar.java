package Terrain.IA;

public class MAstar {

    public PAstar tile;
    public MAstar parent;
    //Custos pra chegar até o player, ou até aonde eu quero.
    public double fCost, gCost, hCost;

    public MAstar(PAstar tile, MAstar parent, double gCost, double hCost){
        this.tile = tile;
        this.parent = parent;
        this.gCost = gCost;
        this.hCost = hCost;
        this.fCost = gCost + hCost;
    }
    
}
