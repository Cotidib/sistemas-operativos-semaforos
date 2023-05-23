import java.util.concurrent.Semaphore;

public class Bar {
    static Semaphore barril = new Semaphore(1);
    static Semaphore rellenar = new Semaphore(0);
    static int vasos = 10; 

    static class Camarero extends Thread{
        public void run(){
            try {
                rellenar.acquire();
                barril.acquire();
                vasos = 20;
                barril.release();
                System.out.println("Camarero cambio el barril");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    static class Cliente extends Thread{
        private int id;
        private int cantidad;

        public Cliente (int num, int id){
            this.id = id;
            this.cantidad = num;
        }

        static void servir() throws InterruptedException {
            barril.acquire();
            vasos -= 1;
            if(vasos == 0){
                barril.release();
                System.out.println("Esperar al cambio de barril");
                rellenar.release();
            }
            barril.release();
        }
        
        public void run(){
            try {
                System.out.println("Cliente " + id + " quiere " + cantidad + " vasos");

                for (int i = 1; i <= cantidad; i++) {
                    Thread.sleep(2000);
                    System.out.println("Cliente " + id + " se sirvio " + i + " vasos");
                    servir();
                  }
                
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
      
    }
      
    public static void main(String[] args) throws Exception {

        Camarero cam1 = new Camarero();
        cam1.start();
        Cliente cli1 = new Cliente(3,1);
        cli1.start();
        Cliente cli2 = new Cliente(5,2);
        cli2.start();
        Cliente cli3 = new Cliente(7,3);
        cli3.start();

    }
}
