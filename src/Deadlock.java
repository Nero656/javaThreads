public class Deadlock {
    record Thread(String NAME) {
        public String getName() {
            return this.NAME;
        }

        public synchronized void bow(Thread bower) {
            System.out.format("%s: %s has bowed to me!%n",
                    this.NAME, bower.getName());
            bower.bowBack(this);
        }

        public synchronized void bowBack(Thread bower) {
            System.out.format("%s: %s has bowed back to me!%n",
                    this.NAME, bower.getName());
        }
    }
}
