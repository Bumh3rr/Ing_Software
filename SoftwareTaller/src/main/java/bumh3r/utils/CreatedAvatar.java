package bumh3r.utils;

import bumh3r.archive.PathResources;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import raven.extras.AvatarIcon;

public class CreatedAvatar {


    public static AvatarIcon created(Object imageIcon, int width, int height, float round) {
        if (imageIcon instanceof String) {
            return createIcon((String) imageIcon, width, height, round);
        } else if (imageIcon instanceof ImageIcon) {
            return createIcon((ImageIcon) imageIcon, width, height, round);
        } else if (imageIcon instanceof URL) {
            return createIcon((URL) imageIcon, width, height, round);
        } else if (imageIcon instanceof byte[]) {
            return createIcon(convertirBytesAImagen((byte[]) imageIcon), width, height, round);

        } else {
            throw new IllegalArgumentException("Invalid type");
        }
    }

    public static ImageIcon convertirBytesAImagen(byte[] datosImagen) {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(datosImagen)) {
            BufferedImage imagen = ImageIO.read(bais);
            return new ImageIcon(imagen);
        } catch (IOException e) {
            return null;
        }
    }

    private static AvatarIcon createIcon(String imageIcon, int width, int height, float round) {
        return createAvatarIcon(new AvatarIcon(imageIcon, width, height, round));
    }

    private static AvatarIcon createIcon(ImageIcon imageIcon, int width, int height, float round) {
        return createAvatarIcon(new AvatarIcon(imageIcon, width, height, round));
    }

    private static AvatarIcon createIcon(URL imageIcon, int width, int height, float round) {
        return createAvatarIcon(new AvatarIcon(imageIcon, width, height, round));
    }

    private static AvatarIcon createAvatarIcon(AvatarIcon icon) {
        icon.setType(AvatarIcon.Type.MASK_SQUIRCLE);
        icon.setBorder(2, 2);
        icon.setBorderColor(new AvatarIcon.BorderColor(Color.decode("#6d6d6d"), 0.1f));
        return icon;
    }

    public static AvatarIcon createdTaller(byte[]imageIcon) {
        AvatarIcon icon;
        if (imageIcon == null || imageIcon.length == 0) {
            icon = new AvatarIcon(PathResources.Default.LOGO_DEFAULT_TALLER_IMAGE, 150, 150, 3.5f);
        } else {
            icon = new AvatarIcon(convertirBytesAImagen(imageIcon), 150, 150, 3.5f);
        }
        icon.setType(AvatarIcon.Type.MASK_SQUIRCLE);
        icon.setBorder(4, 4);
        icon.setBorderColor(new AvatarIcon.BorderColor(UIManager.getColor("Component.accentColor"), 0.7f));
        return icon;
    }


}
