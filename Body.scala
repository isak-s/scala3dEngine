import scala.collection.mutable.ArrayBuffer
class Body(pos: Vertex4, ts: ArrayBuffer[Triangle]) {

    infix def *(scale: Double) = {
        ts.map(t => {Triangle(
            t.v1 * scale,
            t.v2 * scale,
            t.v3 * scale,
            t.color)
        })
    }

}

object Body {

}