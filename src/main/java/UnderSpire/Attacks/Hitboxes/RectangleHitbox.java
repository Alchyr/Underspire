package UnderSpire.Attacks.Hitboxes;

import UnderSpire.Abstracts.Undertale.Attacks.AttackHitbox;
import UnderSpire.UI.MinigameSoul;

public class RectangleHitbox extends AttackHitbox {
    public float width;
    public float height;
    public float xoffset;
    public float yoffset;

    /**
     * Constructor
     * @param width Hitbox width.
     * @param height Hitbox height.
     * @param xoffset Offset of bottom left corner, from x position.
     * @param yoffset Offset of bottom left corner, from y position.
     */
    public RectangleHitbox(float width, float height, float xoffset, float yoffset)
    {
        this.width = width;
        this.height = height;
        this.xoffset = xoffset;
        this.yoffset = yoffset;
    }

    @Override
    public boolean testHit(MinigameSoul p, float x, float y) {
        float left = x + xoffset;
        float right = left + width;
        float bottom = y + yoffset;
        float top = bottom + height;

        return (p.cX > left && p.cX < right && p.cY > bottom && p.cY < top);
    }
}
