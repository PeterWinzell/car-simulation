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

package eu.opends.audio;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.jme3.audio.AudioNode;
import com.jme3.audio.AudioRenderer;
//import com.jme3.audio.Environment;
import com.jme3.audio.Listener;
import com.jme3.audio.AudioSource.Status;
import com.jme3.renderer.Camera;

import eu.opends.main.Simulator;

/**
 * 
 * @author Rafael Math
 */
public class AudioCenter {
	private static Simulator sim;
	private static float engineVolume;
	private static AudioRenderer audioRenderer;
	private static Listener listener;
	private static Map<String, AudioNode> audioNodeList;
	private static Map<String, Float> audioNodeVolumeList;
	private static AudioNode enduring;
	private static float previousVolume;
	private static float previousPitch;

	public static void init(Simulator sim) {
		AudioCenter.sim = sim;
		audioRenderer = sim.getAudioRenderer();
		listener = sim.getListener();
		audioNodeList = AudioFiles.load(sim);
		audioNodeVolumeList = new HashMap<String, Float>();
		engineVolume = Simulator.getDrivingTask().getScenarioLoader().getEngineSoundIntensity(-1f);
		//enduring = audioNodeList.get("enduring");
		//enduring.setDirectional(false);
		//previousPitch = enduring.getPitch();
		//previousVolume = enduring.getVolume();

		// audioRenderer.setEnvironment(new Environment(Environment.Dungeon));

		if (sim.getCar().isEngineOn())
			playSound("engineIdle");
			//playSound("enduring");
	}

	public static void startEngine() {
		playSound("engineStart");
		fadeOut("engineStart", 0);

		playSoundDelayed("engineIdle", 500);
		//playSoundDelayed("enduring", 500);
	}

	public static void stopEngine() {
		//stopSound("enduring");
		playSound("engineStop");
		stopSound("engineIdle");
	}
	/*
	public static void increaseRPM(float incrVol, float incrPitch) {
		
		if ((previousPitch + incrPitch) < 1.4f) {
			previousPitch += incrPitch;
			enduring.setPitch(previousPitch);
		}
		
		if ((previousVolume + incrVol) < 5.0f) {
			previousVolume += incrVol;
			enduring.setVolume(previousVolume);
		}
	}
	
	public static void decreaseRPM(float incrVol, float incrPitch) {
		
		if ((previousPitch - incrPitch) > 0.8f) {
			previousPitch -= incrPitch;
			enduring.setPitch(previousPitch);
		}
		
		if ((previousVolume - incrVol) > 1.0f) {
			previousVolume -= incrVol;
			enduring.setVolume(previousVolume);
		}		
	}*/

	public static void playSound(String soundID) {
		if (soundID != null) {
			AudioNode audioNode = audioNodeList.get(soundID);
			if (audioNode != null)
				audioRenderer.playSource(audioNode);
			else
				System.err.println("AudioNode '" + soundID + "' does not exist!");
		}
	}

	public static void playSoundDelayed(String soundID, int milliSeconds) {
		AudioDelayThread t = new AudioDelayThread(soundID, milliSeconds, "playSound");
		t.start();
	}

	private static void fadeOut(String soundID, int milliSeconds) {
		AudioDelayThread t = new AudioDelayThread(soundID, milliSeconds, "fadeOut");
		t.start();
	}

	public static void stopSound(String soundID) {
		audioRenderer.stopSource(audioNodeList.get(soundID));
	}

	public static void setVolume(String soundID, float volume) {
		audioNodeVolumeList.put(soundID, volume);
	}

	public static void update(float tpf, Camera cam) {
		// when simulator is paused, all sound output will be paused
		if (sim.isPause())
			pauseAllSoundEffects();
		else
			resumeAllSoundEffects();

		// adjust listener's position to camera position
		listener.setLocation(cam.getLocation());
		listener.setRotation(cam.getRotation());

		// engine sound (pitch and volume) is adjusted to current RPM
		float engineSpeedPercentage = sim.getCar().getTransmission().getRPMPercentage();
		//float currentRPM = sim.getCar().getTransmission().getRPM();
		AudioNode engineIdle = audioNodeList.get("engineIdle");
		/*if (currentRPM < 2000)
			engineIdle = audioNodeList.get("idle_low");
		else
			engineIdle = audioNodeList.get("engineIdle");*/
		engineIdle.setPitch(1f + engineSpeedPercentage);

		if (engineVolume == -1)
			engineIdle.setVolume(0.25f + 0.5f * engineSpeedPercentage);
		else
			engineIdle.setVolume(engineVolume + 100 * engineSpeedPercentage);

		// perform volume updates
		for (Entry<String, Float> entry : audioNodeVolumeList.entrySet())
			getAudioNode(entry.getKey()).setVolume(entry.getValue());
	}

	private static void pauseAllSoundEffects() {
		for (Entry<String, AudioNode> entry : audioNodeList.entrySet())
			audioRenderer.pauseSource(entry.getValue());
	}
	
	public static void stopAllSoundEffects() {
		for (Entry<String, AudioNode> entry : audioNodeList.entrySet())
			audioRenderer.stopSource(entry.getValue());
	}

	private static void resumeAllSoundEffects() {
		for (Entry<String, AudioNode> entry : audioNodeList.entrySet()) {
			AudioNode audioNode = entry.getValue();
			if (audioNode.getStatus() == Status.Paused)
				audioRenderer.playSource(audioNode);

		}
	}

	public static AudioNode getAudioNode(String soundID) {
		return audioNodeList.get(soundID);
	}
}
