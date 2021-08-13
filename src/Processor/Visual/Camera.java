package Processor.Visual;

public class Camera {
    public static int x = 0;
    public static int y = 0;

    public static int limit(int cAtual, int cMax){
        if(cAtual <= 0){
            cAtual = 0;
        }
        if(cAtual > cMax){
            cAtual = cMax;
        }
        return cAtual;
    }
}
