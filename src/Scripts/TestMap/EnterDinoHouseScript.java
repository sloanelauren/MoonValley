package Scripts.TestMap;

import java.awt.Color;

import Engine.Config;
import Game.Game;
import Level.Player;
import Level.Script;
import Level.ScriptState;
import Maps.DinoMap;
import Screens.PlayLevelScreen;

// trigger script at beginning of game to set that heavy emotional plot
public class EnterDinoHouseScript extends Script {
	@Override
	protected void setup() {
		lockPlayer();
		showTextbox();
		addTextToTextboxQueue("Entering house...");
	}

	@Override
	protected void cleanup() {
		hideTextbox();
		unlockPlayer();
	}

	@Override
	public ScriptState execute() {
		PlayLevelScreen.isInHouse = true;
		Game.getRunningInstance().getScreenCoordinator().getPlayLevelScreen().teleport(Player.MapEntityManager.getSavedMap(6), 350, 450);
		return ScriptState.COMPLETED;
	}
	
	
}
