package bumh3r.utils;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GarbageCollectorScheduler {
    private static final long INTERVALO_SEGUNDOS = 10; // Cada 30 segundos

    public static void iniciarGCProgramado() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(GarbageCollectorScheduler::debeEjecutarGC, 0, INTERVALO_SEGUNDOS, TimeUnit.SECONDS);
    }

    private static void debeEjecutarGC() {
        MemoryMXBean memoria = ManagementFactory.getMemoryMXBean();
        long memoriaUsada = memoria.getHeapMemoryUsage().getUsed();
        long memoriaMaxima = memoria.getHeapMemoryUsage().getMax();
        long uso =  memoriaUsada / 1024 / 1024;
        System.out.println("Uso de memoria: " + uso + " MB");

        long memoriaMaximaMB = memoriaMaxima / (1024 * 1024);

        if (memoriaMaximaMB < 512 && uso > 150) {
            System.out.println("Ejecutando GC");
            System.gc();
        }else if (memoriaMaximaMB <= 1024 && uso > 200) {
            System.out.println("Ejecutando GC");
            System.gc();
        }else if (memoriaMaximaMB > 1024 && uso > 150) {
            System.out.println("Ejecutando GC");
            System.gc();
        }

    }

}
