package Processor;

public class Camera {
    public static int x = 0;
    public static int y = 0;

    public static int limit(int cAtual, int cMax){
        System.out.println("Atualmente: " + cAtual + " Maximo: " + cMax);
        if(cAtual <= 0){
            cAtual = 0;
        }
        if(cAtual > cMax){
            cAtual = cMax;
        }
        return cAtual;
    }
}
