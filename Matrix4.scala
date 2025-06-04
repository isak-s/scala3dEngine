class Matrix4(val values: Array[Double]) {

  require(values.length == 16, "Matrix4 needs 16 values")

  infix def *(other: Matrix4): Matrix4 = {
    val result = new Array[Double](16)
    for (row <- 0 until 4) {
      for (col <- 0 until 4) {
        var sum = 0.0
        for (i <- 0 until 4) {
          sum += this.values(row * 4 + i) * other.values(i * 4 + col)
        }
        result(row * 4 + col) = sum
      }
    }
    Matrix4(result)
  }

  infix def transform(v: Vertex4): Vertex4 = {
    val x = v.x * values(0) + v.y * values(4) + v.z * values(8)  + v.w * values(12)
    val y = v.x * values(1) + v.y * values(5) + v.z * values(9)  + v.w * values(13)
    val z = v.x * values(2) + v.y * values(6) + v.z * values(10) + v.w * values(14)
    val w = v.x * values(3) + v.y * values(7) + v.z * values(11) + v.w * values(15)
    Vertex4(x, y, z, w)
  }
}

object Matrix4 {

  def identity = Matrix4(Array(
    1,0,0,0,
    0,1,0,0,
    0,0,1,0,
    0,0,0,1
    ))

  def translation(tx: Double, ty: Double, tz: Double): Matrix4 = new Matrix4(Array(
    1, 0, 0, 0,
    0, 1, 0, 0,
    0, 0, 1, 0,
    tx, ty, tz, 1
  ))

  def scale(sx: Double, sy: Double, sz: Double): Matrix4 = new Matrix4(Array(
    sx, 0,  0,  0,
    0,  sy, 0,  0,
    0,  0,  sz, 0,
    0,  0,  0,  1
  ))

  def pitch(angleDegrees: Double): Matrix4 = {
    val a = Math.toRadians(angleDegrees)
    val c = Math.cos(a)
    val s = Math.sin(a)
    new Matrix4(Array(
      1, 0,  0, 0,
      0, c,  s, 0,
      0, -s, c, 0,
      0, 0,  0, 1
    ))
  }

  def yaw(angleDegrees: Double): Matrix4 = {
    val a = Math.toRadians(angleDegrees)
    val c = Math.cos(a)
    val s = Math.sin(a)
    new Matrix4(Array(
      c, 0, -s, 0,
      0, 1,  0, 0,
      s, 0,  c, 0,
      0, 0,  0, 1
    ))
  }

  def roll(angleDegrees: Double): Matrix4 = {
    val a = Math.toRadians(angleDegrees)
    val c = Math.cos(a)
    val s = Math.sin(a)
    new Matrix4(Array(
      c,  s, 0, 0,
      -s, c, 0, 0,
      0,  0, 1, 0,
      0,  0, 0, 1
    ))
  }

  def perspectiveProjection(using c: Camera) = {
    // Convert world space to view space before

    val f = 1.0 / Math.tan(c.fovY / 2.0)
    val nf = 1.0 / (c.near - c.far)

    Matrix4(Array(
      f / c.aspectRatio, 0, 0, 0,
      0, f, 0, 0,
      0, 0, (c.far + c.near) * nf, (2 * c.far * c.near) * nf,
      0, 0, -1, 0
    ))

  }
  // Translation matrix (camera position)
  private def camTranslation(using c: Camera): Matrix4 = {
    val p = c.position
    translation(-p.x, -p.y, -p.z)
  }

  def viewMatrix(using c: Camera): Matrix4 = {
    val f = c.forward
    val r = c.right
    val u = c.up
    val p = c.position

    // Rotation matrix (camera orientation)
    val rot = Matrix4(Array(
      r.x, u.x, -f.x, 0,
      r.y, u.y, -f.y, 0,
      r.z, u.z, -f.z, 0,
      0,   0,   0,    1
    ))

    rot * camTranslation
  }
}