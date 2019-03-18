package UnderSpire.Util;

import org.clapper.util.classutil.ClassFilter;
import org.clapper.util.classutil.ClassFinder;
import org.clapper.util.classutil.ClassInfo;

//I totally didn't copy this from Hubris, made by kiooeht.

public class CardFilter implements ClassFilter
{
    private static final String PACKAGE = "UnderSpire.Cards";

    @Override
    public boolean accept(ClassInfo classInfo, ClassFinder classFinder)
    {
        return classInfo.getClassName().startsWith(PACKAGE);
    }
}