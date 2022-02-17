package com.lifefighter.ocr

/**
 * @author xzp
 * @created on 2022/2/15.
 */
class OcrResult {
    var x0 = 0f
    var y0 = 0f
    var x1 = 0f
    var y1 = 0f
    var x2 = 0f
    var y2 = 0f
    var x3 = 0f
    var y3 = 0f
    var text: String? = null
    var score = 0f
    override fun toString(): String {
        return """x0:$x0, y0:$y0, x1:$x1, y1:$y1,x2:$x2, y2:$y2,x3:$x3, y3:$y3;
            |text:$text
            |score:$score
        """.trimMargin()
    }
}