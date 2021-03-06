package delight.nashornsandbox.internal;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.atomic.AtomicBoolean;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.InputOutput;

@SuppressWarnings("all")
public class MonitorThread extends Thread {
  private final long maxCPUTime;
  
  private final AtomicBoolean stop;
  
  private final AtomicBoolean operationInterrupted;
  
  private Thread threadToMonitor;
  
  private Runnable onInvalid;
  
  private final AtomicBoolean cpuLimitExceeded;
  
  @Override
  public void run() {
    try {
      final ThreadMXBean bean = ManagementFactory.getThreadMXBean();
      final long startCPUTime = bean.getThreadCpuTime(this.threadToMonitor.getId());
      while ((!this.stop.get())) {
        {
          final long threadCPUTime = bean.getThreadCpuTime(this.threadToMonitor.getId());
          final long runtime = (threadCPUTime - startCPUTime);
          if ((runtime > this.maxCPUTime)) {
            this.cpuLimitExceeded.set(true);
            this.stop.set(true);
            this.onInvalid.run();
            Thread.sleep(50);
            boolean _get = this.operationInterrupted.get();
            boolean _equals = (_get == false);
            if (_equals) {
              String _plus = (this + ": Thread hard shutdown!");
              InputOutput.<String>println(_plus);
              this.threadToMonitor.stop();
            }
            return;
          }
          Thread.sleep(5);
        }
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public void stopMonitor() {
    this.stop.set(true);
  }
  
  public void setThreadToMonitor(final Thread t) {
    this.threadToMonitor = t;
  }
  
  public void setOnInvalidHandler(final Runnable r) {
    this.onInvalid = r;
  }
  
  public void notifyOperationInterrupted() {
    this.operationInterrupted.set(true);
  }
  
  public boolean isCPULimitExceeded() {
    return this.cpuLimitExceeded.get();
  }
  
  public boolean gracefullyInterrputed() {
    return this.operationInterrupted.get();
  }
  
  public MonitorThread(final long maxCPUTimne) {
    this.maxCPUTime = maxCPUTimne;
    AtomicBoolean _atomicBoolean = new AtomicBoolean(false);
    this.stop = _atomicBoolean;
    AtomicBoolean _atomicBoolean_1 = new AtomicBoolean(false);
    this.operationInterrupted = _atomicBoolean_1;
    AtomicBoolean _atomicBoolean_2 = new AtomicBoolean(false);
    this.cpuLimitExceeded = _atomicBoolean_2;
  }
}
