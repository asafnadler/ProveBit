package org.provebit.daemon;

import org.apache.commons.io.monitor.FileAlterationObserver;
import org.provebit.merkle.Merkle;
public class MerkleDaemon extends Thread {
    int period;
    FileAlterationObserver observer;
    DirectoryMonitor listener;
    
    /**
     * Daemon constructor, 
     * @param dir - Directory to run daemon on
     * @param period - Daemon polling period (msec)
     */
    public MerkleDaemon(Merkle mTree, int period) {
        observer = new FileAlterationObserver(mTree.getDir().getAbsolutePath());
        listener = new DirectoryMonitor(mTree);
        observer.addListener(listener);
        this.period = period;
        setDaemon(true);
    }
    
    /**
     * Initializes the observer and starts monitoring the directory
     */
    public void run() {       
        if (!Thread.currentThread().isDaemon()) {
            return;
        }
        
        try {
			observer.initialize();
		} catch (Exception e) {
			e.printStackTrace();
			Thread.currentThread().interrupt();
		}
        monitorDirectory();
    }

    /**
     * Main periodic method that checks for modifications in the
     * desired directory
     */
	private void monitorDirectory() {
		try {
			while (true) {
				observer.checkAndNotify();
				Thread.sleep(period);
			}
		} catch (InterruptedException ie) {
			System.out.println("Monitor interrupted, exiting...");
			try {
				observer.destroy();
			} catch (Exception e) {
				System.out.println("Observer destruction failed, messy exit...");
				e.printStackTrace();
			}
		} finally {
			Thread.currentThread().interrupt();
		}
	}
    
    /**
     * Get the current merkle tree
     * @return Currently constructed merkle tree
     */
    public Merkle getTree() {
        return listener.getTree();
    }
    
    /**
     * Get number of changes detected since daemon launched
     * @return number of changes since launch
     */
    public int getChanges() {
        return listener.getChanges();
    }
}