package net.sf.persism.dao.northwind;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Data object for the Northwind categories table.
 * Demonstrates reading binary data (picture) which is an OLE wrapping a JPEG file.
 * Demonstrates having a calculated field in the data object which converts the picture into a BufferedImage.
 *
 * @author Dan Howard
 * @since 5/3/12 8:50 PM
 */
public class Category {

    private int categoryId;
    private String categoryName;
    private String description;
    private byte[] picture;

    private BufferedImage image = null;

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    /**
     * Convert picture data into a BufferedImage
     *
     * @return JPEG BufferedImage
     * @throws IOException
     */
    public BufferedImage getImage() throws IOException {
        if (image == null) {
            // OLE header is 1st 78 bytes so we strip it.
            byte[] imageData = new String(picture).substring(78).getBytes();

            InputStream in = new ByteArrayInputStream(imageData);
            image = ImageIO.read(in);
            in.close();

        }
        return image;
    }

    @Override
    public String toString() {
        return "Category{" +
                "categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", description='" + description + '\'' +
                ", picture=" + picture +
                "}";
    }
}
