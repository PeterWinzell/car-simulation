/*
*  This file is part of OpenDS (Open Source Driving Simulator).
*  Copyright (C) 2016 Rafael Math
*
*  OpenDS is free software: you can redistribute it and/or modify
*  it under the terms of the GNU General Public License as published by
*  the Free Software Foundation, either version 3 of the License, or
*  (at your option) any later version.
*
*  OpenDS is distributed in the hope that it will be useful,
*  but WITHOUT ANY WARRANTY; without even the implied warranty of
*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*  GNU General Public License for more details.
*
*  You should have received a copy of the GNU General Public License
*  along with OpenDS. If not, see <http://www.gnu.org/licenses/>.
*/

package eu.opends.traffic;

import java.util.ArrayList;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import eu.opends.drivingTask.settings.SettingsLoader;
import eu.opends.drivingTask.settings.SettingsLoader.Setting;
import eu.opends.main.SimulationDefaults;

import eu.opends.main.Simulator;


/**
 * 
 * @author Rafael Math
 */
public class PhysicalTraffic extends Thread
{
	private static ArrayList<TrafficCarData> vehicleDataList = new ArrayList<TrafficCarData>();
	private static ArrayList<PedestrianData> pedestrianDataList = new ArrayList<PedestrianData>();
    private static ArrayList<TrafficObject> trafficObjectList = new ArrayList<TrafficObject>();
    private static ArrayList<AnimatedRoadSignData> animatedRoadSignDataList = new ArrayList<AnimatedRoadSignData>();
	private boolean isRunning = true;
	private int updateIntervalMsec = 20;
	private long lastUpdate = 0;

	
	private boolean multiThreadingEnabled = false;
    private int multiThreading_numberOfThreads = 0;
    ScheduledThreadPoolExecutor executor; 
	public PhysicalTraffic(Simulator sim)
	{
		multiThreadingEnabled = Simulator.getSettingsLoader().getSetting(Setting.MultiThreading_enableThreads, SimulationDefaults.MultiThreading_enableThreads);
		multiThreading_numberOfThreads = Simulator.getSettingsLoader().getSetting(Setting.MultiThreading_numberOfThreads, SimulationDefaults.multiThreading_numberOfThreads);	
		if (multiThreadingEnabled){
				executor = new ScheduledThreadPoolExecutor(multiThreading_numberOfThreads);
		}
		for(TrafficCarData vehicleData : vehicleDataList)
		{
			// build and add traffic cars
			trafficObjectList.add(new TrafficCar(sim, vehicleData));
		}

		for(PedestrianData pedestrianData : pedestrianDataList)
		{
			// build and add pedestrians
			trafficObjectList.add(new Pedestrian(sim, pedestrianData));
		}
		
		for(AnimatedRoadSignData animatedRoadSignData : animatedRoadSignDataList)
		{
			// build and add animated road signs
			trafficObjectList.add(new AnimatedRoadSign(sim, animatedRoadSignData));
		}
	}
	
	
    public static ArrayList<TrafficCarData> getVehicleDataList()
    {
    	return vehicleDataList;
    }
    
    
    public static ArrayList<PedestrianData> getPedestrianDataList()
    {
    	return pedestrianDataList;
    }

    
	public static ArrayList<TrafficObject> getTrafficObjectList() 
	{
		return trafficObjectList;		
	}
	
	public static ArrayList<AnimatedRoadSignData> getAnimatedRoadSignDataList() 
	{
		return animatedRoadSignDataList;		
	}


	
	public TrafficObject getTrafficObject(String trafficObjectName) 
	{
		for(TrafficObject trafficObject : trafficObjectList)
		{
			if(trafficObject.getName().equals(trafficObjectName))
				return trafficObject;
		}
		
		return null;
	}
	
	
	public void run()
	{
		if(trafficObjectList.size() >= 1)
		{
			/*
			for(TrafficObject trafficObject : trafficObjectList)
				trafficObject.showInfo();
			*/
			
			while (isRunning) 
			{
				long elapsedTime = System.currentTimeMillis() - lastUpdate;
				
				if (elapsedTime > updateIntervalMsec) 
				{
					lastUpdate = System.currentTimeMillis();
					
					float tpf = elapsedTime/1000f;
					// update every traffic object
					for(TrafficObject trafficObject : trafficObjectList)
						trafficObject.update(tpf, trafficObjectList);
				}
				else
				{
					// sleep until update interval has elapsed
					try {
						Thread.sleep(updateIntervalMsec - elapsedTime);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			//System.out.println("PhysicalTraffic closed");
		}
	}
	
	
	
	public void update(float tpf)
	{
		if (getMultiThreadingEnable()){
			for (int i=0; i<getTrafficObjectList().size(); i++){
				TrafficObject singleCar = getTrafficObjectList().get(i);
				Runnable worker = new MyRunnable(tpf, singleCar);
				executor.execute(worker);
			}
		}
		else {
			for(TrafficObject trafficObject : trafficObjectList){
				trafficObject.update(tpf, trafficObjectList);
			}
		}
	}
	
	public void executorShutdown(){
		executor.shutdown();		
	}
	
	public Boolean getMultiThreadingEnable() {
			return this.multiThreadingEnabled;
	}

	public synchronized void close() 
	{
		isRunning = false;
		
		// close all traffic objects
		for(TrafficObject trafficObject : trafficObjectList)
			if(trafficObject instanceof TrafficCar)
				((TrafficCar) trafficObject).close();
	}

	
	public static class MyRunnable implements Runnable {
		private TrafficObject singleVehicle;
		private float tpf;
		
		MyRunnable(float tpf, TrafficObject singleVehicle){
			this.singleVehicle = singleVehicle;
			this.tpf = tpf;
		}
		
		@Override
		public void run() {
			try {
				singleVehicle.update(tpf, trafficObjectList);
			} catch (Exception e){
				System.out.println(e);
			}
			
		}
	}
}
