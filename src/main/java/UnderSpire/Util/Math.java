package UnderSpire.Util;

import com.badlogic.gdx.math.MathUtils;

import static com.badlogic.gdx.math.MathUtils.atan2;

public class Math {
    public static float dist(float x1, float x2, float y1, float y2)
    {
    return (float)java.lang.Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
    }
    public static float angle(float x1, float x2, float y1, float y2)
    {
        return atan2(y2 - y1, x2 - x1) * 180.0f / MathUtils.PI;
    }
    public static float radAngle(float x1, float x2, float y1, float y2)
    {
        return atan2(y2 - y1, x2 - x1);
    }
}
