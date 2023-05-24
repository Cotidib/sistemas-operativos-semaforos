import java.util.concurrent.Semaphore;

public class Taller {
  static Semaphore aceite = new Semaphore(3);
  static Semaphore revision = new Semaphore(5);
  static int bidones = 5;
  static Semaphore bidonesMutex = new Semaphore(1); //protege recurso bidones
  static Semaphore reponer = new Semaphore(0); //señal de aviso de reponer
  static Semaphore espera = new Semaphore(0);

  static class Montacarga extends Thread{
    static void robotMontacargas() throws InterruptedException {
      while(true){
        reponer.acquire();
        bidonesMutex.acquire();
        bidones = 5;
        System.out.println("Montacarga repuso bidones");
        bidonesMutex.release();
        if(espera.getQueueLength() > 0){
          //mantener semaforo en mutex
          espera.release();
        }
      }
    }

    public void run(){
      try{
        robotMontacargas();
      } catch (InterruptedException e){
        e.printStackTrace();
      }
    }
  }

  static class Conductor extends Thread{
    private int id;
    public Conductor (int id){
        this.id = id;
    }

    void cambiarAceite() throws InterruptedException {
      bidonesMutex.acquire();
      if(bidones == 0){
        System.out.println("Cliente " + id + " espera por Montacarga");
        if(reponer.getQueueLength() > 0){
          //mantener semaforo en mutex
          reponer.release();
        }
        bidonesMutex.release();
        espera.acquire();
        bidonesMutex.acquire();
      }
      bidones -= 1;
      System.out.println("Conductor "+ id + " cambio el aceite");
      bidonesMutex.release();
      if(espera.getQueueLength() > 0){
        //mantener semaforo en mutex
        espera.release();
      }
    }

    void vehiculo() throws InterruptedException {
      aceite.acquire();
      cambiarAceite();
      aceite.release();
      revision.acquire();
      System.out.println("Vehiculo " + id + " en revision");
      revision.release();
      System.out.println("Vehiculo " + id + " revisado");
    }

    public void run(){
      try{
        vehiculo();
      } catch (InterruptedException e){
        e.printStackTrace();
      }
    }

  }
  
  public static void main(String[] args) throws Exception {
    Montacarga mc = new Montacarga();
    mc.start();
    Conductor c1 = new Conductor(1);
    c1.start();
    Conductor c2 = new Conductor(2);
    c2.start();
    Conductor c3 = new Conductor(3);
    c3.start();
    Conductor c4 = new Conductor(4);
    c4.start();
    Conductor c5 = new Conductor(5);
    c5.start();
    Conductor c6 = new Conductor(6);
    c6.start();
    Conductor c7 = new Conductor(7);
    c7.start();
    Conductor c8 = new Conductor(8);
    c8.start();
    Conductor c9 = new Conductor(9);
    c9.start();
    Conductor c10 = new Conductor(10);
    c10.start();
  }
  
}
