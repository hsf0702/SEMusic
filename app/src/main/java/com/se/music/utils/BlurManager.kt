package com.se.music.utils

import android.graphics.Bitmap
import java.io.FileOutputStream
import java.util.concurrent.Callable
import java.util.concurrent.Executors

/**
 *Author: gaojin
 *Time: 2018/8/19 下午5:36
 */

class BlurManager constructor(bitmap: Bitmap) {

    companion object {
        val EXECUTOR_THREADS = Runtime.getRuntime().availableProcessors()
        val EXECUTOR = Executors.newFixedThreadPool(EXECUTOR_THREADS)
    }

    /**
     * Original image
     */
    private var _image: Bitmap = bitmap
    /**
     * Method of blurring
     */
    private var _blurProcess: JavaBlurProcess
    /**
     * Most recent result of blurring
     */
    private var _result: Bitmap? = null

    /**
     * Constructor method (basic initialization and construction of the pixel array)
     *
     * @param image The image that will be analyed
     */
    init {
        _blurProcess = JavaBlurProcess()
    }

    /**
     * Process the image on the given radius. Radius must be at least 1
     */
    fun process(radius: Int): Bitmap? {
        _result = _blurProcess.blur(_image, radius.toFloat())
        return _result
    }

    /**
     * Returns the blurred image as a bitmap
     *
     * @return blurred image
     */
    fun returnBlurredImage(): Bitmap? {
        return _result
    }

    /**
     * Save the image into the file system
     *
     * @param path The path where to save the image
     */
    fun saveIntoFile(path: String) {
        try {
            val out = FileOutputStream(path)
            _result!!.compress(Bitmap.CompressFormat.PNG, 90, out)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * Returns the original image as a bitmap
     *
     * @return the original bitmap image
     */
    fun getImage(): Bitmap {
        return this._image
    }

    class JavaBlurProcess {
        /**
         * Process the given image, blurring by the supplied radius. If radius is 0, this will return original
         *
         * @param original the bitmap to be blurred
         * @param radius   the radius in pixels to blur the image
         * @return the blurred version of the image.
         */
        fun blur(original: Bitmap, radius: Float): Bitmap? {
            val w = original.width
            val h = original.height
            val currentPixels = IntArray(w * h)
            original.getPixels(currentPixels, 0, w, 0, 0, w, h)
            val cores = BlurManager.EXECUTOR_THREADS

            val horizontal = ArrayList<BlurTask>(cores)
            val vertical = ArrayList<BlurTask>(cores)
            for (i in 0 until cores) {
                horizontal.add(BlurTask(currentPixels, w, h, radius.toInt(), cores, i, 1))
                vertical.add(BlurTask(currentPixels, w, h, radius.toInt(), cores, i, 2))
            }

            try {
                BlurManager.EXECUTOR.invokeAll(horizontal)
            } catch (e: InterruptedException) {
                return null
            }

            try {
                BlurManager.EXECUTOR.invokeAll(vertical)
            } catch (e: InterruptedException) {
                return null
            }

            return Bitmap.createBitmap(currentPixels, w, h, Bitmap.Config.ARGB_8888)
        }

        private class BlurTask(private val _src: IntArray, private val _w: Int, private val _h: Int, private val _radius: Int, private val _totalCores: Int, private val _coreIndex: Int,
                               private val _round: Int) : Callable<Void> {
            @Throws(Exception::class)
            override fun call(): Void? {
                blurIteration(_src, _w, _h, _radius, _totalCores, _coreIndex, _round)
                return null
            }
        }

        companion object {

            private val stackblur_mul = shortArrayOf(512, 512, 456, 512, 328, 456, 335, 512, 405, 328, 271, 456, 388, 335, 292, 512, 454, 405, 364, 328, 298, 271, 496, 456, 420, 388, 360, 335, 312, 292, 273, 512, 482, 454, 428, 405, 383, 364, 345, 328, 312, 298, 284, 271, 259, 496, 475, 456, 437, 420, 404, 388, 374, 360, 347, 335, 323, 312, 302, 292, 282, 273, 265, 512, 497, 482, 468, 454, 441, 428, 417, 405, 394, 383, 373, 364, 354, 345, 337, 328, 320, 312, 305, 298, 291, 284, 278, 271, 265, 259, 507, 496, 485, 475, 465, 456, 446, 437, 428, 420, 412, 404, 396, 388, 381, 374, 367, 360, 354, 347, 341, 335, 329, 323, 318, 312, 307, 302, 297, 292, 287, 282, 278, 273, 269, 265, 261, 512, 505, 497, 489, 482, 475, 468, 461, 454, 447, 441, 435, 428, 422, 417, 411, 405, 399, 394, 389, 383, 378, 373, 368, 364, 359, 354, 350, 345, 341, 337, 332, 328, 324, 320, 316, 312, 309, 305, 301, 298, 294, 291, 287, 284, 281, 278, 274, 271, 268, 265, 262, 259, 257, 507, 501, 496, 491, 485, 480, 475, 470, 465, 460, 456, 451, 446, 442, 437, 433, 428, 424, 420, 416, 412, 408, 404, 400, 396, 392, 388, 385, 381, 377, 374, 370, 367, 363, 360, 357, 354, 350, 347, 344, 341, 338, 335, 332, 329, 326, 323, 320, 318, 315, 312, 310, 307, 304, 302, 299, 297, 294, 292, 289, 287, 285, 282, 280, 278, 275, 273, 271, 269, 267, 265, 263, 261, 259)

            private val stackblur_shr = byteArrayOf(9, 11, 12, 13, 13, 14, 14, 15, 15, 15, 15, 16, 16, 16, 16, 17, 17, 17, 17, 17, 17, 17, 18, 18, 18, 18, 18, 18, 18, 18, 18, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24)

            private fun blurIteration(src: IntArray, w: Int, h: Int, radius: Int, cores: Int, core: Int,
                                      step: Int) {
                var x: Int
                var y: Int
                var xp: Int
                var yp: Int
                var i: Int
                var sp: Int
                var stack_start: Int
                var stack_i: Int

                var src_i: Int
                var dst_i: Int

                var sum_r: Long
                var sum_g: Long
                var sum_b: Long
                var sum_in_r: Long
                var sum_in_g: Long
                var sum_in_b: Long
                var sum_out_r: Long
                var sum_out_g: Long
                var sum_out_b: Long

                val wm = w - 1
                val hm = h - 1
                val div = radius * 2 + 1
                val mul_sum = stackblur_mul[radius].toInt()
                val shr_sum = stackblur_shr[radius].toInt()
                val stack = IntArray(div)

                if (step == 1) {
                    val minY = core * h / cores
                    val maxY = (core + 1) * h / cores

                    y = minY
                    while (y < maxY) {
                        sum_out_b = 0
                        sum_out_g = sum_out_b
                        sum_out_r = sum_out_g
                        sum_in_b = sum_out_r
                        sum_in_g = sum_in_b
                        sum_in_r = sum_in_g
                        sum_b = sum_in_r
                        sum_g = sum_b
                        sum_r = sum_g

                        src_i = w * y // start of line (0,y)

                        i = 0
                        while (i <= radius) {
                            stack_i = i
                            stack[stack_i] = src[src_i]
                            sum_r += ((src[src_i].ushr(16) and 0xff) * (i + 1)).toLong()
                            sum_g += ((src[src_i].ushr(8) and 0xff) * (i + 1)).toLong()
                            sum_b += ((src[src_i] and 0xff) * (i + 1)).toLong()
                            sum_out_r += (src[src_i].ushr(16) and 0xff).toLong()
                            sum_out_g += (src[src_i].ushr(8) and 0xff).toLong()
                            sum_out_b += (src[src_i] and 0xff).toLong()
                            i++
                        }

                        i = 1
                        while (i <= radius) {
                            if (i <= wm) {
                                src_i += 1
                            }
                            stack_i = i + radius
                            stack[stack_i] = src[src_i]
                            sum_r += ((src[src_i].ushr(16) and 0xff) * (radius + 1 - i)).toLong()
                            sum_g += ((src[src_i].ushr(8) and 0xff) * (radius + 1 - i)).toLong()
                            sum_b += ((src[src_i] and 0xff) * (radius + 1 - i)).toLong()
                            sum_in_r += (src[src_i].ushr(16) and 0xff).toLong()
                            sum_in_g += (src[src_i].ushr(8) and 0xff).toLong()
                            sum_in_b += (src[src_i] and 0xff).toLong()
                            i++
                        }

                        sp = radius
                        xp = radius
                        if (xp > wm) {
                            xp = wm
                        }
                        src_i = xp + y * w
                        dst_i = y * w
                        x = 0
                        while (x < w) {
                            src[dst_i] = ((src[dst_i] and -0x1000000).toLong()
                                    or ((sum_r * mul_sum).ushr(shr_sum) and 0xff shl 16)
                                    or ((sum_g * mul_sum).ushr(shr_sum) and 0xff shl 8)
                                    or ((sum_b * mul_sum).ushr(shr_sum) and 0xff)).toInt()
                            dst_i += 1

                            sum_r -= sum_out_r
                            sum_g -= sum_out_g
                            sum_b -= sum_out_b

                            stack_start = sp + div - radius
                            if (stack_start >= div) {
                                stack_start -= div
                            }
                            stack_i = stack_start

                            sum_out_r -= (stack[stack_i].ushr(16) and 0xff).toLong()
                            sum_out_g -= (stack[stack_i].ushr(8) and 0xff).toLong()
                            sum_out_b -= (stack[stack_i] and 0xff).toLong()

                            if (xp < wm) {
                                src_i += 1
                                ++xp
                            }

                            stack[stack_i] = src[src_i]

                            sum_in_r += (src[src_i].ushr(16) and 0xff).toLong()
                            sum_in_g += (src[src_i].ushr(8) and 0xff).toLong()
                            sum_in_b += (src[src_i] and 0xff).toLong()
                            sum_r += sum_in_r
                            sum_g += sum_in_g
                            sum_b += sum_in_b

                            ++sp
                            if (sp >= div) {
                                sp = 0
                            }
                            stack_i = sp

                            sum_out_r += (stack[stack_i].ushr(16) and 0xff).toLong()
                            sum_out_g += (stack[stack_i].ushr(8) and 0xff).toLong()
                            sum_out_b += (stack[stack_i] and 0xff).toLong()
                            sum_in_r -= (stack[stack_i].ushr(16) and 0xff).toLong()
                            sum_in_g -= (stack[stack_i].ushr(8) and 0xff).toLong()
                            sum_in_b -= (stack[stack_i] and 0xff).toLong()
                            x++
                        }
                        y++

                    }
                } else if (step == 2) {
                    val minX = core * w / cores
                    val maxX = (core + 1) * w / cores

                    x = minX
                    while (x < maxX) {
                        sum_out_b = 0
                        sum_out_g = sum_out_b
                        sum_out_r = sum_out_g
                        sum_in_b = sum_out_r
                        sum_in_g = sum_in_b
                        sum_in_r = sum_in_g
                        sum_b = sum_in_r
                        sum_g = sum_b
                        sum_r = sum_g

                        src_i = x // x,0
                        i = 0
                        while (i <= radius) {
                            stack_i = i
                            stack[stack_i] = src[src_i]
                            sum_r += ((src[src_i].ushr(16) and 0xff) * (i + 1)).toLong()
                            sum_g += ((src[src_i].ushr(8) and 0xff) * (i + 1)).toLong()
                            sum_b += ((src[src_i] and 0xff) * (i + 1)).toLong()
                            sum_out_r += (src[src_i].ushr(16) and 0xff).toLong()
                            sum_out_g += (src[src_i].ushr(8) and 0xff).toLong()
                            sum_out_b += (src[src_i] and 0xff).toLong()
                            i++
                        }
                        i = 1
                        while (i <= radius) {
                            if (i <= hm) {
                                src_i += w
                            }

                            stack_i = i + radius
                            stack[stack_i] = src[src_i]
                            sum_r += ((src[src_i].ushr(16) and 0xff) * (radius + 1 - i)).toLong()
                            sum_g += ((src[src_i].ushr(8) and 0xff) * (radius + 1 - i)).toLong()
                            sum_b += ((src[src_i] and 0xff) * (radius + 1 - i)).toLong()
                            sum_in_r += (src[src_i].ushr(16) and 0xff).toLong()
                            sum_in_g += (src[src_i].ushr(8) and 0xff).toLong()
                            sum_in_b += (src[src_i] and 0xff).toLong()
                            i++
                        }

                        sp = radius
                        yp = radius
                        if (yp > hm) {
                            yp = hm
                        }
                        src_i = x + yp * w
                        dst_i = x
                        y = 0
                        while (y < h) {
                            src[dst_i] = ((src[dst_i] and -0x1000000).toLong()
                                    or ((sum_r * mul_sum).ushr(shr_sum) and 0xff shl 16)
                                    or ((sum_g * mul_sum).ushr(shr_sum) and 0xff shl 8)
                                    or ((sum_b * mul_sum).ushr(shr_sum) and 0xff)).toInt()
                            dst_i += w

                            sum_r -= sum_out_r
                            sum_g -= sum_out_g
                            sum_b -= sum_out_b

                            stack_start = sp + div - radius
                            if (stack_start >= div) {
                                stack_start -= div
                            }
                            stack_i = stack_start

                            sum_out_r -= (stack[stack_i].ushr(16) and 0xff).toLong()
                            sum_out_g -= (stack[stack_i].ushr(8) and 0xff).toLong()
                            sum_out_b -= (stack[stack_i] and 0xff).toLong()

                            if (yp < hm) {
                                src_i += w // stride
                                ++yp
                            }

                            stack[stack_i] = src[src_i]

                            sum_in_r += (src[src_i].ushr(16) and 0xff).toLong()
                            sum_in_g += (src[src_i].ushr(8) and 0xff).toLong()
                            sum_in_b += (src[src_i] and 0xff).toLong()
                            sum_r += sum_in_r
                            sum_g += sum_in_g
                            sum_b += sum_in_b

                            ++sp
                            if (sp >= div) {
                                sp = 0
                            }
                            stack_i = sp

                            sum_out_r += (stack[stack_i].ushr(16) and 0xff).toLong()
                            sum_out_g += (stack[stack_i].ushr(8) and 0xff).toLong()
                            sum_out_b += (stack[stack_i] and 0xff).toLong()
                            sum_in_r -= (stack[stack_i].ushr(16) and 0xff).toLong()
                            sum_in_g -= (stack[stack_i].ushr(8) and 0xff).toLong()
                            sum_in_b -= (stack[stack_i] and 0xff).toLong()
                            y++
                        }
                        x++
                    }
                }

            }

        }
    }
}