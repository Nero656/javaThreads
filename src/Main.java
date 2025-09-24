import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int option = 0;

        while (true) {
            System.out.print("Options:");
            System.out.println("1: See DeadLock ");
            System.out.println("2: See LiveLock");
            System.out.println("3: See sequential print");
            System.out.print("What u want to do? : ");

            String input = scanner.nextLine();

            if (input.matches("\\d+")) {
                option = Integer.parseInt(input);
                if (option >= 1 && option <= 3) {
                    break; // правильный ввод → выходим из цикла
                } else {
                    System.out.println("Ошибка: введите число от 1 до 3.");
                }
            } else {
                System.out.println("Ошибка: вводите только числа.");
            }
        }

        switch (option) {
            case 1: {
                final Deadlock.Thread Thread_1 = new Deadlock.Thread("Thread_1");
                final Deadlock.Thread Thread_2 = new Deadlock.Thread("Thread_2");
                new Thread(() -> Thread_1.bow(Thread_2)).start();
                new Thread(() -> Thread_2.bow(Thread_1)).start();
                break;
            }
            case 2: {
                Livelock.Student student1 = new Livelock.Student("John");
                Livelock.Student student2 = new Livelock.Student("Kevin");

                Livelock.TextBook textBook = new Livelock.TextBook(student1);

                Thread thread1 = new Thread(() -> student1.studyWith(textBook, student2));
                Thread thread2 = new Thread(() -> student2.studyWith(textBook, student1));

                thread1.start();
                thread2.start();

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                thread1.isInterrupted();
                thread2.isInterrupted();

                System.out.println("Both students completed their training (or were interrupted)");
                break;
            }
            case 3: {
                SequentialPrinting printer = new SequentialPrinting();

                printer.start();

                printer.stopAfter(10000);
                break;
            }
        }
        scanner.close();
    }
}