import java.awt.Color

object SimpleShader {
    def getShade(color: Color, shade: Double): Color = {

        // Some evil scaled format to linear and back bs
        val redLinear: Double = (Math.pow(color.getRed(), 2.2) * shade)
        val greenLinear: Double = (Math.pow(color.getGreen(), 2.2) * shade)
        val blueLinear: Double = (Math.pow(color.getBlue(), 2.2) * shade)

        val red = Math.pow(redLinear, 1 / 2.2).toInt
        val green = Math.pow(greenLinear, 1 / 2.2).toInt
        val blue = Math.pow(blueLinear, 1 / 2.2).toInt

        new Color(red, green, blue);
}
}