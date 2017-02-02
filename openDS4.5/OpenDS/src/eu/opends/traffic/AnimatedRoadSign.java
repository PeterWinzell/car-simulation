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

import com.jme3.asset.AssetKey;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.light.AmbientLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Node;

import eu.opends.main.Simulator;

import java.util.ArrayList;

/**
 * @author Tommi S.E. Laukkanen, Rafael Math
 */
public class AnimatedRoadSign implements AnimationListener, TrafficObject
{
	private Simulator sim;
	private Node personNode = new Node();
	private BetterCharacterControl characterControl;
	private AnimationController animationController;
	private String name;
	private float mass = 5f;
	
	// the animation commands
	private String animationBlinkCommand = "Cown Lamp";

	AnimatedRoadSignData animatedRoadSignData;
	boolean initialized = false;
    public AnimatedRoadSign(Simulator sim, AnimatedRoadSignData animatedRoadSignData) 
    {
    	this.sim = sim;
    	this.animatedRoadSignData = animatedRoadSignData;
    
    	name = animatedRoadSignData.getName();
    	
    	animationBlinkCommand = animatedRoadSignData.getAnimationBlink();

		AssetManager assetManager = sim.getAssetManager();
		Node roadSign = (Node) assetManager.loadModel(animatedRoadSignData.getModelPath());
		personNode.setName(animatedRoadSignData.getName());
		personNode.attachChild(roadSign);
		roadSign.setLocalScale(animatedRoadSignData.getLocalScale()); // adjust scale of model
		roadSign.setLocalTranslation(animatedRoadSignData.getLocalTranslation()); // adjust position to ensure collisions occur correctly
		roadSign.setLocalRotation(animatedRoadSignData.getLocalRotation()); // adjust rotation of model
		
		// TODO adjust ambient light in OgreXML file
		AmbientLight light = new AmbientLight();
		light.setColor(ColorRGBA.White.mult(0.7f));
		roadSign.addLight(light);
		
		// shadow of character
		roadSign.setShadowMode(ShadowMode.Cast);
		
		// construct character (if character bounces, try increasing height and weight)
		mass = animatedRoadSignData.getMass();
		characterControl = new BetterCharacterControl(0.3f, 1.8f, mass); // TODO get from scenario.xml
		personNode.addControl(characterControl);
    }
    
    private void init()
    {
		// add to physics state
		sim.getBulletAppState().getPhysicsSpace().add(characterControl); 
		sim.getBulletAppState().getPhysicsSpace().addAll(personNode); 
		sim.getSceneNode().attachChild(personNode);
		  
		animationController = new AnimationController(personNode);
		animationController.setAnimationListener(this);
		animationController.animate(animationBlinkCommand, 1f, 0f, 0);
			
		initialized = true;
    }  
    
    @Override
	public void update(float tpf, ArrayList<TrafficObject> vehicleList) 
    {
    	// prevent pedestrians from high jump when adding to the physics engine
    	if(tpf < 1.0f && !initialized)
    		init();
    	
    	if(initialized)
    	{
			if(!sim.isPause())
			{
		    	animationController.update(tpf);   	
		    }
    	}
    }
    
    public void setEnabled(boolean enabled){}
    
    public void getCurrentSpeedKmh(){}
    	
	public void setToWayPoint(String wayPointID){}
	
	public void setToWayPoint(int index){}
	
    @Override
    public void onAnimCycleDone(final String animationName){}
    
	@Override
	public Vector3f getPosition()
	{
		return personNode.getLocalTranslation();
	}

	@Override
	public void setPosition(Vector3f position) 
	{
		characterControl.warp(position);
	}

	@Override
	public void setRotation(Quaternion quaternion) 
	{
		// automatic orientation in next update()
	}

	@Override
	public float getMaxBrakeForce() 
	{
		// needed for follow box reduced speed computation
		// not relevant for pedestrians --> return 0
		return 0;
	}

	@Override
	public float getMass() 
	{
		return mass;
	}

	@Override
	public String getName() 
	{
		return name;
	}
}