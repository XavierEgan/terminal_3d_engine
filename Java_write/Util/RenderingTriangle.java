package Java_write.Util;

public class RenderingTriangle{
    public char texture;
    public String color;
    public Triangle tri;

    public RenderingTriangle(Triangle tri, char texture, String color) {
        this.tri = tri;
        this.texture = texture;
        this.color = color;
    }
}
