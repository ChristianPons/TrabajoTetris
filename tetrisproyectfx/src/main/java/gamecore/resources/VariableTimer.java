package gamecore.resources;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Abstract class that sets up a repeating task through ScheduledExecutorService
 * 
 * @author Miguel Ruiz
 *
 */
public abstract class VariableTimer {
	
	private ScheduledExecutorService scheduler=Executors.newSingleThreadScheduledExecutor();
	private boolean toEnd=false;
	private long interval=1000;
	private long initialDelay = 0;
	
	public VariableTimer(long interval) {
		this.interval=interval;
		setScheduler();
	}
	
	public VariableTimer() {
		
	}
	
	public void setInterval(long interval) {
		this.interval=interval;
		this.initialDelay=0;
		scheduler.shutdown();
		scheduler=Executors.newSingleThreadScheduledExecutor();
		setScheduler();
	}
	
	public void setInterval(long interval, long delay) {
		this.interval=interval;
		this.initialDelay=delay;
		scheduler.shutdown();
		scheduler=Executors.newSingleThreadScheduledExecutor();
		setScheduler();
	}
	
	public void setDelayedInterval(long interval) {
		this.interval=interval;
		this.initialDelay=0;
		toEnd=true;
	}
	
	public void setDelayedInterval(long interval, long delay) {
		this.interval=interval;
		this.initialDelay=delay;
		toEnd=true;
	}
	
	public void stop() {
		scheduler.shutdown();
	}
	
	private Runnable instantRunnable(){
		return new Runnable() {

			@Override
			public void run() {
				if(toEnd) {
					setInterval(interval, initialDelay);
					toEnd=false;
				}
				else {
					task();
				}
			}
		};
	}
	
	public abstract void task();
	
	private void setScheduler() {
		scheduler.scheduleAtFixedRate(instantRunnable(), initialDelay * 1000000, interval * 1000000, TimeUnit.NANOSECONDS);
	}
	
}