class Matrix3(val values: Array[Double]) {

    infix def *(other: Matrix3) = {
        val result: Array[Double] = new Array[Double](9);
        for (row: Int <- 0 until 3) {
            for (col: Int <- 0 until 3) {
                for (i: Int <- 0 until 3) {
                    val index = row * 3 + col
                    result.update(index, result(index) + this.values(row * 3 + i) * other.values(i * 3 + col))
                }
            }
        }
        Matrix3(result);
    }

    infix def transform(in: Vertex3) = {
        Vertex3(in.x * values(0) + in.y * values(3) + in.z * values(6),
               in.x * values(1) + in.y * values(4) + in.z * values(7),
               in.x * values(2) + in.y * values(5) + in.z * values(8))
    }

}

object Matrix3 {
  def pitch(angleDegrees: Double): Matrix3 = {
    val a = Math.toRadians(angleDegrees)
    new Matrix3(Array(
      1, 0, 0,
      0, Math.cos(a), Math.sin(a),
      0, -Math.sin(a), Math.cos(a)
    ))
  }

  def heading(angleDegrees: Double): Matrix3 = {
    val a = Math.toRadians(angleDegrees)
    new Matrix3(Array(
      Math.cos(a), 0, -Math.sin(a),
      0, 1, 0,
      Math.sin(a), 0, Math.cos(a)
    ))
  }

  def roll(angleDegrees: Double): Matrix3 = {
    val a = Math.toRadians(angleDegrees)
    new Matrix3(Array(
      Math.cos(a), Math.sin(a), 0,
      -Math.sin(a), Math.cos(a), 0,
      0, 0, 1
    ))
  }
}