import java.util.concurrent.Semaphore;

public class SistemaImpresion {
  static Semaphore colaBN = new Semaphore(0);
  static Semaphore colaC = new Semaphore(0);

  static class ImpresoraBN extends Thread{
    void  impresoraBN() throws InterruptedException {
      while(true){
        colaBN.acquire();
        System.out.println("BN: Imprimiendo");
      }
    }

    public void run(){
      try {
        impresoraBN();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  static class ImpresoraC extends Thread{
    void  impresoraColor() throws InterruptedException {
      while(true){
        if(colaC.availablePermits() > 0){
          colaC.acquire();
          System.out.println("C: Imprimiendo");
        }
        if(colaBN.availablePermits() > 0){
          colaBN.acquire();
          System.out.println("BN: Imprimiendo");
        }
      }
    }

    public void run(){
      try {
        impresoraColor();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  static class Aplicacion extends Thread{
    private boolean impresionColor;

    public Aplicacion(boolean impresionColor){
      this.impresionColor = impresionColor;
    }

    public void encolarImpresion() throws InterruptedException{
      if(impresionColor){
        colaC.release();
      } else {
        colaBN.release();
      }
    }

    public void run(){
      try {
        encolarImpresion();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    
  }

  public static void main(String[] args) throws Exception {

    ImpresoraBN bn = new ImpresoraBN();
    bn.start();
    ImpresoraC c = new ImpresoraC();
    c.start();

    Aplicacion a1 = new Aplicacion(true);
    a1.start();
    Aplicacion a2 = new Aplicacion(false);
    a2.start();
    Aplicacion a3 = new Aplicacion(true);
    a3.start();
    Aplicacion a4 = new Aplicacion(true);
    a4.start();
    Aplicacion a5 = new Aplicacion(false);
    a5.start();
    Aplicacion a6 = new Aplicacion(false);
    a6.start();

  }
  
}
