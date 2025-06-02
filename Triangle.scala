import java.awt.Color
import java.util.ArrayList
import scala.collection.mutable.ArrayBuffer

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

    def expandTriangles(triangles: ArrayBuffer[Triangle]): ArrayBuffer[Triangle] = {

        def triangleRadius(t: Triangle): Double = {
            val center = Vertex4.midPoint(t.v1, t.v2, t.v3)
            math.sqrt(center dot center)
        }

        val radius = triangleRadius(triangles(0))

        val result = triangles.flatMap(t => {
            val m1 = Vertex4.midPoint(t.v1, t.v2)
            val m2 = Vertex4.midPoint(t.v1, t.v3)
            val m3 = Vertex4.midPoint(t.v2, t.v3)
            Array(
            Triangle(t.v1, m1, m2, t.color),
            Triangle(t.v2, m1, m3, t.color),
            Triangle(t.v3, m2, m3, t.color),
            Triangle(m1, m2, m3, t.color))
        })

        result.foreach(t => {
            for (v <- Seq(t.v1, t.v2, t.v3)) {
                val currentLength = math.sqrt(v.x * v.x + v.y * v.y + v.z * v.z)
                val scale = currentLength / radius
                v.x /= scale
                v.y /= scale
                v.z /= scale
            }
        })

        result
    }
}