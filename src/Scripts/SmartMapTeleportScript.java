package Scripts;

import java.util.OptionalDouble;
import java.util.function.Supplier;
import Game.Game;
import Level.Map;
import Level.ScriptState;
import Registry.ItemRegistry.Item;
import Utils.Side;

/**
 * A MapTeleportScript that teleports players to the correct location on the target map to emulate a continuous flow between maps.
 */
public class SmartMapTeleportScript extends MapTeleportScript {
    public Side side;
    public Item requiredItem;

    public SmartMapTeleportScript(Supplier<Map> mapCreator, Side side) {
        this(mapCreator, side, null);
    }

    public SmartMapTeleportScript(Supplier<Map> mapCreator, Side side, Item requiredItem) {
        super(mapCreator, OptionalDouble.empty(), OptionalDouble.empty());

        this.side = side;
        this.requiredItem = requiredItem;
    }

    @Override
    protected void setup() {
        lockPlayer();
        showTextbox();
        addTextToTextboxQueue("Hmm, maybe I should find a " + this.requiredItem.toString().replace("Item ", "") + "\nto access this area.");
        if (side == Side.LEFT) {
            player.moveRight(1.1f);
        } else if (side == Side.RIGHT) {
            player.moveLeft(1.1f);
        } else if (side == Side.TOP) {
            player.moveDown(1.1f);
        } else if (side == Side.BOTTOM) {
            player.moveUp(1.1f);
        }
    }

    @Override
    protected void cleanup() {
        unlockPlayer();
        hideTextbox();
    }
    
    @Override
    protected float getToX() {
        if (this.side.parallelToXAxis()) {
            // How far along the X axis has the player traveled?
            double traveledFraction = this.getPlayer().getX() / this.getMap().getWidthPixels();

            // Where does this fraction land on the new map?
            return (float) (this.toMap.getWidthPixels() * traveledFraction);
        } else {
            // either left or right border
            if (this.side == Side.LEFT) {
                // we'll pop out on the right on the other map
                return (float) (this.toMap.getWidthPixels() - this.getPlayer().getWidth());
            } else { // right side
                return 0.0f;
            }
        }
    }

    @Override
    protected float getToY() {
        if (this.side.parallelToYAxis()) {
            // How far along the Y axis has the player traveled?
            double traveledFraction = this.getPlayer().getY() / this.getMap().getHeightPixels();

            // Where does this fraction land on the new map?
            return (float) (this.toMap.getHeightPixels() * traveledFraction);
        } else {
            // either top or bottom border
            if (this.side == Side.TOP) {
                // we'll pop out on the bottom on the other map
                return (float) (this.toMap.getHeightPixels() - this.getPlayer().getHeight());
            } else { // bottom side
                return 0.0f;
            }
        }
    }

    @Override
    protected ScriptState execute() {
        if (this.requiredItem != null) {
            if (!Game.getRunningInstance().getScreenCoordinator().getPlayLevelScreen().getPlayerInventory().containsItem(this.requiredItem)) {
                start();
                if (!isTextboxQueueEmpty()) {
                    return ScriptState.RUNNING;
                }
            } else {
                return super.execute();
            }
        } else if (this.requiredItem == null) {
            return super.execute();
        }
        end();
        return ScriptState.COMPLETED;
    }
}
