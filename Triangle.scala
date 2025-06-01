import java.awt.Color

case class Triangle(v1: Vertex4, v2: Vertex4, v3: Vertex4, color: Color)

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
    def sameSide(a: Vertex4, b: Vertex4, c: Vertex4, p: Vertex4): Boolean = {
        val ab = b - a
        val ac = c - a
        val ap = p - a

        (ab cross ac).z * (ab cross ap).z >= 0
    }

    def pointInTriangle(a: Vertex4, b: Vertex4, c: Vertex4, p: Vertex4): Boolean = {
        sameSide(a, b, c, p) &&
        sameSide(b, c, a, p) &&
        sameSide(c, a, b, p)
    }
}