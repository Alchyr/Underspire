package UnderSpire.Util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.megacrit.cardcrawl.cards.AbstractCard;

import java.util.HashMap;

import static UnderSpire.UnderSpire.assetPath;
import static UnderSpire.UnderSpire.logger;

public class TextureLoader {
    private static HashMap<String, Texture> textures = new HashMap<String, Texture>();

    /**
     * @param textureString - String path to the texture you want to load relative to resources,
     * Example: "img/ui/missingtexture.png"
     * @return <b>com.badlogic.gdx.graphics.Texture</b> - The texture from the path provided
     */
    public static Texture getTexture(final String textureString) {
        if (textures.get(textureString) == null) {
            try {
                loadTexture(textureString);
            } catch (GdxRuntimeException e) {
                try
                {
                    return getTexture(assetPath("img/MissingImage.png"));
                }
                catch (GdxRuntimeException ex) {
                    logger.info("The MissingImage is missing!");
                    return null;
                }
            }
        }
        return textures.get(textureString);
    }
    public static Texture getTextureNull(final String textureString) {
        if (textures.get(textureString) == null) {
            try {
                loadTexture(textureString);
            } catch (GdxRuntimeException e) {
                return null;
            }
        }
        return textures.get(textureString);
    }

    public static String getCardTextureString(final String cardName, final AbstractCard.CardType cardType)
    {
        String textureString;

        switch (cardType)
        {
            case ATTACK:
                textureString = assetPath("img/Cards/Attacks/" + cardName + ".png");
                break;
            case SKILL:
                textureString = assetPath("img/Cards/Skills/" + cardName + ".png");
                break;
            case POWER:
                textureString = assetPath("img/Cards/Powers/" + cardName + ".png");
                break;
            default:
                textureString = assetPath("img/Cards/UnknownCard.png");
                break;
        }

        if (textures.get(textureString) == null) {
            try {
                loadTexture(textureString);
            } catch (GdxRuntimeException e) {
                switch (cardType) {
                    case ATTACK:
                        textureString = assetPath("img/Cards/Attacks/default.png");
                        break;
                    case SKILL:
                        textureString = assetPath("img/Cards/Skills/default.png");
                        break;
                    case POWER:
                        textureString = assetPath("img/Cards/Powers/default.png");
                        break;
                    default:
                        textureString = assetPath("img/MissingImage.png");
                        break;
                }
            }
        }
        //no exception, file exists
        return textureString;
    }

    public static String getSelectedCardTextureString(final String cardName, final AbstractCard.CardType cardType)
    {
        String textureString;

        switch (cardType)
        {
            case ATTACK:
                textureString = assetPath("img/Cards/Attacks/" + cardName + "_s.png");
                break;
            case SKILL:
                textureString = assetPath("img/Cards/Skills/" + cardName + "_s.png");
                break;
            case POWER:
                textureString = assetPath("img/Cards/Powers/" + cardName + "_s.png");
                break;
            default:
                textureString = assetPath("img/Cards/UnknownCard.png");
                break;
        }

        if (textures.get(textureString) == null) {
            try {
                loadTexture(textureString);
            } catch (GdxRuntimeException e) {
                switch (cardType) {
                    case ATTACK:
                        textureString = assetPath("img/Cards/Attacks/default.png");
                        break;
                    case SKILL:
                        textureString = assetPath("img/Cards/Skills/default.png");
                        break;
                    case POWER:
                        textureString = assetPath("img/Cards/Powers/default.png");
                        break;
                    default:
                        textureString = assetPath("img/MissingImage.png");
                        break;
                }
            }
        }
        //no exception, file exists
        return textureString;
    }


    private static void loadTexture(final String textureString) throws GdxRuntimeException {
        loadTexture(textureString, false);
    }

    private static void loadTexture(final String textureString, boolean linearFilter) throws GdxRuntimeException {
        Texture texture =  new Texture(textureString);
        if (linearFilter)
        {
            texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }
        else
        {
            texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        }
        textures.put(textureString, texture);
    }
}