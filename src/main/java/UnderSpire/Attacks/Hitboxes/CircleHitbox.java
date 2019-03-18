package UnderSpire.Attacks.Hitboxes;

import UnderSpire.UI.MinigameSoul;
import UnderSpire.Abstracts.Undertale.Attacks.AttackHitbox;
import UnderSpire.Util.Math;

public class CircleHitbox extends AttackHitbox {
    public float radius;
    public float xoffset;
    public float yoffset;

    /**
     * Constructor
     * @param radius Radius of circle
     * @param xoffset Offset of offset of circle
     * @param yoffset Offset of offset of circle
     */
    public CircleHitbox(float radius, float xoffset, float yoffset)
    {
        this.radius = radius;
        this.xoffset = xoffset;
        this.yoffset = yoffset;
    }

    @Override
    public boolean testHit(MinigameSoul p, float x, float y) {
        return (Math.dist(p.cX, x + xoffset, p.cY, y + yoffset) <= radius);
    }
}
