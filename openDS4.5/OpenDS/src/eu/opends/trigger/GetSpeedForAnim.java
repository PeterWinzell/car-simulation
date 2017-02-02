package eu.opends.trigger;

import java.util.ArrayList;
import java.util.HashMap;

import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import eu.opends.drivingTask.settings.SettingsLoader;
import eu.opends.drivingTask.settings.SettingsLoader.Setting;
import eu.opends.main.SimulationDefaults;
import eu.opends.main.Simulator;
import eu.opends.traffic.AnimationController;
import eu.opends.traffic.AnimationListener;

public class GetSpeedForAnim extends TriggerAction implements AnimationListener {
	private Simulator sim;
	private String modelID;
	private Node speedSignNode = new Node();
	private HashMap<Spatial, AnimationController> animControllerMap = new HashMap<>();
	private boolean first = true;

	// private AnimationController animationControllerOff;

	public GetSpeedForAnim(float delay, int maxRepeat, String animBlink, Simulator sim, String modelID) {
		super(delay, maxRepeat);
		this.sim = sim;
		this.modelID = modelID;
	}

	@Override
	protected void execute() {
		float currentSpeed = sim.getCar().getCurrentSpeedKmh();

		Spatial model = sim.getSceneNode().getChild(modelID);
		speedSignNode = (Node) model;
		Node speedSignSceneNode = (Node) speedSignNode.getChild(0);
		SettingsLoader sl = Simulator.getDrivingTask().getSettingsLoader();
		Boolean USMeasSys = sl.getSetting(Setting.General_USMeasurementSystem, SimulationDefaults.USMeasurementSystem);
		if (USMeasSys)
			currentSpeed = currentSpeed / 1.609344f;

		int speed3digit = (int) (currentSpeed % 10);
		int speed2digit = (int) ((currentSpeed % 100) - speed3digit) / 10;
		int speed1digit = (int) (currentSpeed / 100);
		ArrayList<String> figure1 = new ArrayList<>();
		ArrayList<String> figure2 = new ArrayList<>();
		ArrayList<String> figure3 = new ArrayList<>();

		// Make a list of armatures to animate on the first digit (0 or 1 only)
		switch (speed1digit) {
		case 0:
			figure1.add("Armature.000");
			figure1.add("Armature.001");
			figure1.add("Armature.002");
			figure1.add("Armature.003");
			figure1.add("Armature.005");
			figure1.add("Armature.006");
			break;
		case 1:
			figure1.add("Armature.000");
			figure1.add("Armature.001");
			break;
		}

		// List of armatures for second digit
		switch (speed2digit) {
		case 0:
			figure2.add("Armature.007");
			figure2.add("Armature.008");
			figure2.add("Armature.009");
			figure2.add("Armature.010");
			figure2.add("Armature.012");
			figure2.add("Armature.013");
			break;
		case 1:
			figure2.add("Armature.007");
			figure2.add("Armature.008");
			break;
		case 2:
			figure2.add("Armature.007");
			figure2.add("Armature.009");
			figure2.add("Armature.010");
			figure2.add("Armature.011");
			figure2.add("Armature.013");
			break;
		case 3:
			figure2.add("Armature.007");
			figure2.add("Armature.008");
			figure2.add("Armature.009");
			figure2.add("Armature.011");
			figure2.add("Armature.013");
			break;
		case 4:
			figure2.add("Armature.007");
			figure2.add("Armature.008");
			figure2.add("Armature.011");
			figure2.add("Armature.012");
			break;
		case 5:
			figure2.add("Armature.008");
			figure2.add("Armature.009");
			figure2.add("Armature.011");
			figure2.add("Armature.012");
			figure2.add("Armature.013");
			break;
		case 6:
			figure2.add("Armature.008");
			figure2.add("Armature.009");
			figure2.add("Armature.010");
			figure2.add("Armature.011");
			figure2.add("Armature.012");
			figure2.add("Armature.013");
			break;
		case 7:
			figure2.add("Armature.007");
			figure2.add("Armature.008");
			figure2.add("Armature.013");
			break;
		case 8:
			figure2.add("Armature.007");
			figure2.add("Armature.008");
			figure2.add("Armature.009");
			figure2.add("Armature.010");
			figure2.add("Armature.011");
			figure2.add("Armature.012");
			figure2.add("Armature.013");
			break;
		case 9:
			figure2.add("Armature.007");
			figure2.add("Armature.008");
			figure2.add("Armature.009");
			figure2.add("Armature.011");
			figure2.add("Armature.012");
			figure2.add("Armature.013");
			break;
		}

		// List of armatures for third digit
		switch (speed3digit) {
		case 0:
			figure3.add("Armature.014");
			figure3.add("Armature.015");
			figure3.add("Armature.016");
			figure3.add("Armature.017");
			figure3.add("Armature.019");
			figure3.add("Armature.020");
			break;
		case 1:
			figure3.add("Armature.014");
			figure3.add("Armature.015");
			break;
		case 2:
			figure3.add("Armature.014");
			figure3.add("Armature.016");
			figure3.add("Armature.017");
			figure3.add("Armature.018");
			figure3.add("Armature.020");
			break;
		case 3:
			figure3.add("Armature.014");
			figure3.add("Armature.015");
			figure3.add("Armature.016");
			figure3.add("Armature.018");
			figure3.add("Armature.020");
			break;
		case 4:
			figure3.add("Armature.014");
			figure3.add("Armature.015");
			figure3.add("Armature.018");
			figure3.add("Armature.019");
			break;
		case 5:
			figure3.add("Armature.015");
			figure3.add("Armature.016");
			figure3.add("Armature.018");
			figure3.add("Armature.019");
			figure3.add("Armature.020");
			break;
		case 6:
			figure3.add("Armature.015");
			figure3.add("Armature.016");
			figure3.add("Armature.017");
			figure3.add("Armature.018");
			figure3.add("Armature.019");
			figure3.add("Armature.020");
			break;
		case 7:
			figure3.add("Armature.014");
			figure3.add("Armature.015");
			figure3.add("Armature.020");
			break;
		case 8:
			figure3.add("Armature.014");
			figure3.add("Armature.015");
			figure3.add("Armature.016");
			figure3.add("Armature.017");
			figure3.add("Armature.018");
			figure3.add("Armature.019");
			figure3.add("Armature.020");
			break;
		case 9:
			figure3.add("Armature.014");
			figure3.add("Armature.015");
			figure3.add("Armature.016");
			figure3.add("Armature.018");
			figure3.add("Armature.019");
			figure3.add("Armature.020");
			break;
		}

		ArrayList<String> allFigures = new ArrayList<>();
		allFigures.addAll(figure1);
		allFigures.addAll(figure2);
		allFigures.addAll(figure3);

		ArrayList<String> allOtherFigures = new ArrayList<>();
		for (Spatial submodel : speedSignSceneNode.getChildren()) {
			String submodelName = submodel.getName();
			if (submodelName.contains("Armature") && (allFigures.indexOf(submodelName) == -1)) {
				allOtherFigures.add(submodelName);
			}
		}

		ArrayList<String> armatures = new ArrayList<>();
		armatures.addAll(allFigures);
		armatures.addAll(allOtherFigures);

		if (first) {
			for (String armature : armatures) {
				Node armatureNode = (Node) speedSignSceneNode.getChild(armature);
				AnimationController animController = new AnimationController(armatureNode);
				animController.setAnimationListener(this);
				animControllerMap.put(armatureNode, animController);
			}
			first = false;
		}

		for (AnimationController animController : animControllerMap.values()) {
			animController.animate("Stop", 1f, 0f, 0);
			animController.update(1f);
		}

		for (String submodelName : allFigures) {
			Node submodel = (Node) speedSignSceneNode.getChild(submodelName);
			AnimationController animController = animControllerMap.get(submodel);
			animController.animate("my_animation", 3f, 0f, 0);
		}
		updateCounter();
	}

	@Override
	public void onAnimCycleDone(String animationName) {
		// TODO Auto-generated method stub
	}
}
