public class SequentialPrinting
{
    private final Object monitor = new Object();
    private boolean isFirstThreadTurn = true;
    private volatile boolean running = false;
    private Thread thread1;
    private Thread thread2;

    public void start(){
        if (running){
            System.out.println("process had already been started!");
            return;
        }
        running = true;
        thread1 = createFirstThread();
        thread2 = createSecondThread();

        thread1.start();
        thread2.start();

        System.out.println("process has been started");
    }

    public void stop(){
        if (!running){
            System.out.println("process had already been stopped!");
            return;
        }

        running = false;
        synchronized (monitor) {
            monitor.notifyAll();
        }

        try {
            if (thread1 != null) thread1.join(2000);
            if (thread2 != null) thread2.join(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\nprocess has been stopped");
    }

    public void stopAfter(long milliseconds){
        new Thread(() -> {
            try{
                Thread.sleep(milliseconds);
                stop();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }).start();
    }
    private Thread createFirstThread() {
        return new Thread(() -> {
            while (running) {
                synchronized (monitor) {
                    while (!isFirstThreadTurn && running) {
                        try {
                            monitor.wait();
                        } catch (InterruptedException e) {
                            return;
                        }
                    }
                    if (!running) break;

                    System.out.print("1 ");
                    isFirstThreadTurn = false;
                    sleep(1000);
                    monitor.notifyAll();
                }
            }
        }, "Thread-1");
    }

    private Thread createSecondThread() {
        return new Thread(() -> {
            while (running) {
                synchronized (monitor) {
                    while (isFirstThreadTurn && running) {
                        try {
                            monitor.wait();
                        } catch (InterruptedException e) {
                            return;
                        }
                    }

                    if (!running) break;

                    System.out.print("2 ");
                    isFirstThreadTurn = true;
                    sleep(1000);
                    monitor.notifyAll();
                }

            }
        }, "Thread-2");
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
