public class Livelock {
    static class TextBook{
        private Student owner;

        public synchronized void use() {
            System.out.printf("%s use textbook!%n", owner.name);
        }

        public TextBook(Student firstOwner){
            owner = firstOwner;
        }

        public synchronized void setOwner(Student newOwner) {
            owner = newOwner;
        }
    }

    static class Student{
       private String name;
       private boolean needsBook;

        public Student(String newName){
            name = newName;
            needsBook = true;
        }

        public void studyWith(TextBook textBook, Student roommate){
            while (needsBook && !Thread.currentThread().isInterrupted()) {

                if (textBook.owner != this) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        System.out.printf("%s: I was interrupted, stopping preparation%n", name);
                        Thread.currentThread().interrupt();
                        return;
                    }
                    continue;
                }

                if (roommate.needsBook && !Thread.currentThread().isInterrupted()){
                    System.out.printf("%s: %s you can take the textbook first", name, roommate.name);
                    textBook.setOwner(roommate);
                }

                if (!Thread.currentThread().isInterrupted()) {
                    textBook.use();
                    needsBook = false;
                    System.out.printf("%s: I have finished the preparation%n", name);
                }

                if (Thread.currentThread().isInterrupted()){
                    System.out.printf("%s: Terminating work on interruption%n", name);
                }
            }
        }
   }
}
