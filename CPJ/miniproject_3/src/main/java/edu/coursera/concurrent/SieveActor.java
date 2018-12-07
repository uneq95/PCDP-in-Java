package edu.coursera.concurrent;

import edu.rice.pcdp.Actor;

import static edu.rice.pcdp.PCDP.finish;
import static javafx.application.Platform.exit;

/**
 * An actor-based implementation of the Sieve of Eratosthenes.
 * <p>
 * TODO Fill in the empty SieveActorActor actor class below and use it from
 * countPrimes to determin the number of primes <= limit.
 */
public final class SieveActor extends Sieve {
    /**
     * {@inheritDoc}
     * <p>
     * TODO Use the SieveActorActor class to calculate the number of primes <=
     * limit in parallel. You might consider how you can model the Sieve of
     * Eratosthenes as a pipeline of actors, each corresponding to a single
     * prime number.
     */


    @Override
    public int countPrimes(final int limit) {
        int primeCount = 0;
        //give the first local prime as 2
        SieveActorActor sieveActorActor = new SieveActorActor(2);
        finish(() -> {
            for (int i = 3; i <= limit; i = i + 2) {
                sieveActorActor.send(i);
            }
            sieveActorActor.send(0);
        });

        SieveActorActor itrActor = sieveActorActor;
        while (itrActor != null) {
            primeCount += itrActor.getNumLocalPrimes();
            itrActor = itrActor.getNextActor();
        }
        return primeCount;
    }

    /**
     * An actor class that helps implement the Sieve of Eratosthenes in
     * parallel.
     */
    public static final class SieveActorActor extends Actor {

        private int MAX_LOCAL_PRIMES = 1000;
        private int[] localPrimes;

        public SieveActorActor getNextActor() {
            return nextActor;
        }

        private SieveActorActor nextActor;

        public int getNumLocalPrimes() {
            return numLocalPrimes;
        }

        private int numLocalPrimes;

        public SieveActorActor(int localPrime) {
            localPrimes = new int[MAX_LOCAL_PRIMES];
            localPrimes[0] = localPrime;
            numLocalPrimes = 1;
            this.nextActor = null;
        }

        /**
         * Process a single message sent to this actor.
         * <p>
         * TODO complete this method.
         *
         * @param msg Received message
         */
        @Override
        public void process(final Object msg) {

            int num = (int) msg;

            if (num <= 0) {
                if (nextActor != null) {
                    nextActor.send(msg);
                }
                exit();
            } else {
                if (isLocallyPrime(num)) {
                    if (numLocalPrimes < MAX_LOCAL_PRIMES) {
                        localPrimes[numLocalPrimes] = num;
                        numLocalPrimes++;
                    } else {
                        if (nextActor == null) {
                            nextActor = new SieveActorActor(num);
                        } else {
                            nextActor.send(msg);
                        }

                    }
                }
            }
        }

        private boolean isLocallyPrime(int num) {
            for (int i = 0; i < numLocalPrimes; i++) {
                if (num % localPrimes[i] == 0) {
                    return false;
                }
            }
            return true;
        }
    }
}
