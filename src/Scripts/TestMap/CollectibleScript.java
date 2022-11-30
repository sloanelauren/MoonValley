
package Scripts.TestMap;

import Level.MusicManager;
import Scripts.SimpleTextScript;
import Utils.Sound;

public class CollectibleScript extends SimpleTextScript {

    protected Sound sound;
    protected String textItem;
    protected MusicManager musicManager;

    public CollectibleScript(String text, boolean isKeyCollectible, MusicManager musicManager) {
        super("You have found a " + text + "!");
        textItem = "You have found a " + text + "!";
        this.musicManager = musicManager;
        sound = musicManager.getCollectibleSound(isKeyCollectible);
    }

    @Override
    protected void setup() {
        lockPlayer();
        showTextbox();
        addTextToTextboxQueue(textItem);
        musicManager.getCurrentSound().pause();
        sound.play();
    }

    @Override
    protected void cleanup() {
        unlockPlayer();
        hideTextbox();
        sound.stop();
        musicManager.getCurrentSound().play();
    }
}