import java.util.concurrent.Semaphore;

public class Bar {
    static Semaphore rellenar = new Semaphore(0); //señal para rellenar
    static Semaphore barril = new Semaphore(1); //protege las operaciones sobre vasos
    static Semaphore espera = new Semaphore(0); //clientes esperan a que se termine de rellenar
    static int MAX_VALUE = 5;
    static int vasos = MAX_VALUE; //cantidad de vasos disponibles dentro del barril

    static class Camarero extends Thread{
        public void run(){
            try {
                while(true){
                    rellenar.acquire();
                    barril.acquire();
                    vasos = MAX_VALUE;
                    System.out.println("Camarero cambio el barril");
                    barril.release();
                    if(espera.getQueueLength() > 0){
                        espera.release();
                        // solo hago un release aunque tenga personas esperando
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    static class Cliente extends Thread{
        private int id;
        private int cantidad;
        private int servidos;

        public Cliente (int num, int id){
            this.id = id;
            this.cantidad = num;
            this.servidos = 0;
        }

        void servir() throws InterruptedException {
            barril.acquire();
            while(vasos <= 0){
                System.out.println("Esperar al cambio de barril");
                if(rellenar.getQueueLength() > 0){
                    rellenar.release();
                }
                barril.release();
                espera.acquire();
                barril.acquire();
            }
            servidos ++;
            System.out.println("Cliente " + id + " se sirvio " + servidos + " vasos");
            vasos -= 1;
            barril.release();
            if(espera.getQueueLength() > 0){
                espera.release();
            }
        }
        
        public void run(){
            try {
                System.out.println("Cliente " + id + " quiere " + cantidad + " vasos");
                for (int i = 1; i <= cantidad; i++) {
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
        Cliente cli2 = new Cliente(4,2);
        cli2.start();
        Cliente cli3 = new Cliente(7,3);
        cli3.start();

    }
}
