import java.awt.Color

case class Triangle(v1: Vertex, v2: Vertex, v3: Vertex, color: Color)

object Triangle {
    /**
      * Determines whether a point p is within the triangle using Rasterization.
      * Checks if p is on the same side of all edges.
      * @param a trangle edge
      * @param b trangle edge
      * @param c trangle edge
      * @param p arbitrary point
      * @return true if p is within the triangle
      */
    def sameSide(a: Vertex, b: Vertex, c: Vertex, p: Vertex): Boolean = {
        val ab = b - a
        val ac = c - a
        val ap = p - a

        ((ab cross ac) dot (ab cross ap)) >= 0
    }
}