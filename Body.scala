import scala.collection.mutable.ArrayBuffer
import java.awt.Color
trait Body() {
    val pos: Vertex4
    val tris: ArrayBuffer[Triangle]

    private infix def *(scale: Double) = {
        tris.map(t => {Triangle(
            t.v1 * scale,
            t.v2 * scale,
            t.v3 * scale,
            t.color)
        })
    }

    def scaled(scale: Double) = this match {
        case Tetrahedron(pos, _) =>
            Tetrahedron(pos * scale, this * scale)
        case Globe(pos, _) =>
            Globe(pos * scale, this * scale)
}


}

object Body {

}

case class Tetrahedron(pos: Vertex4, tris: ArrayBuffer[Triangle] = createTetrahedron()) extends Body

case class Globe(pos: Vertex4, tris: ArrayBuffer[Triangle] = createGlobe())
    extends Body

private def createGlobe(): ArrayBuffer[Triangle] = {
    Triangle.expandTriangles(
        Triangle.expandTriangles(
            Triangle.expandTriangles(
                Triangle.expandTriangles(createTetrahedron())
            )
        )
    )
}

private def createTetrahedron(): ArrayBuffer[Triangle] = {
    val tris = new ArrayBuffer[Triangle]()
    tris += new Triangle(new Vertex4(100, 100, 100, 1),
                        new Vertex4(-100, -100, 100, 1),
                        new Vertex4(-100, 100, -100, 1),
                        Color.WHITE)
    tris += new Triangle(new Vertex4(100, 100, 100, 1),
                        new Vertex4(-100, -100, 100, 1),
                        new Vertex4(100, -100, -100, 1),
                        Color.RED)
    tris += new Triangle(new Vertex4(-100, 100, -100, 1),
                        new Vertex4(100, -100, -100, 1),
                        new Vertex4(100, 100, 100, 1),
                        Color.GREEN)
    tris += new Triangle(new Vertex4(-100, 100, -100, 1),
                        new Vertex4(100, -100, -100, 1),
                        new Vertex4(-100, -100, 100, 1),
                        Color.BLUE)
    // Triangle.expandTriangles(Triangle.expandTriangles(Triangle.expandTriangles(Triangle.expandTriangles(tris))))
    tris
}