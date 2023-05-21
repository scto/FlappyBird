package com.etb.flappybird.game.thread

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.os.SystemClock
import android.util.Log
import android.view.SurfaceHolder
import androidx.core.content.res.ResourcesCompat
import com.etb.flappybird.R
import com.etb.flappybird.game.model.BackgroundImage
import com.etb.flappybird.game.model.Bird
import com.etb.flappybird.game.model.Cot
import com.etb.flappybird.game.model.ScreenSize
import com.etb.flappybird.game.utils.DialogUtils
import java.util.*
import kotlin.random.Random


class PlayThread : Thread {

    private val TAG: String = "PlayThread"
    private lateinit var holder: SurfaceHolder
    private lateinit var resources: Resources

    private val FPS: Int = (1000.0 / 60.0).toInt()
    private val backgroundImage = BackgroundImage()
    private var bitmapImage: Bitmap? = null
    private var startTime: Long = 0
    private var frameTime: Long = 0
    private val velocity = 3
    private val bird: Bird


    private var state: Int = 1
    private var velocityBird: Int = 3

    private lateinit var context: Context

    var cot: Cot? = null
    val numCot = 2
    val velocityCot = 10
    val minY = 250
    val maxY = ScreenSize.SCREEN_HEIGHT - minY - 500
    val kc = ScreenSize.SCREEN_WIDTH * 3 / 4
    var cotArray: ArrayList<Cot> = arrayListOf()
    var ran: Random = Random

    private var score = 0
    private var paint: Paint = Paint().apply {
        color = Color.BLACK
        textSize = 65f
        textAlign = Paint.Align.CENTER
    }
    private lateinit var scoreUpdateTimer: Timer
    private val scoreUpdateInterval: Long = 1000
    private var isScoreUpdatePaused: Boolean = false

    var gap = 200 // Defina o espaçamento desejado entre os cots


    var isPaused: Boolean = false
    private var pauseStartTime: Long = 0
    private var pauseTime: Long = 0
    var pauseImage: Bitmap? = null
    var pauseImageRect: Rect? = null
    var resumeImage: Bitmap? = null
    var resumeImageRect: Rect? = null
    var pauseImageX: Int = 0
        get() = field
        set(value) {
            field = value
        }
    var pauseImageY: Int = 0
        get() = field
        set(value) {
            field = value
        }


    var iCot = 0
    var isDead = false

    var isRunning: Boolean = false
        get() = field
        set(value) {
            field = value
        }

    constructor(holder: SurfaceHolder, resources: Resources, context: Context) {
        this.holder = holder
        this.resources = resources
        this.context = context
        isRunning = true

        bird = Bird(resources)

        bitmapImage = BitmapFactory.decodeResource(resources, R.drawable.run_background)
        bitmapImage = this.bitmapImage?.let { scaleResize(it) }

        pauseImage = BitmapFactory.decodeResource(resources, R.drawable.ic_pause)
        val pauseImageSize = 100 // Tamanho desejado em pixels


        pauseImage = Bitmap.createScaledBitmap(pauseImage!!, pauseImageSize, pauseImageSize, false)
        // resumeImage = Bitmap.createScaledBitmap(resumeImage!!, pauseImageSize, pauseImageSize, false)

        val pauseImageLeft = (ScreenSize.SCREEN_WIDTH - pauseImageSize) / 2
        val pauseImageTop = (ScreenSize.SCREEN_HEIGHT - pauseImageSize) / 2
        val pauseImageRight = pauseImageLeft + pauseImageSize
        val pauseImageBottom = pauseImageTop + pauseImageSize
        pauseImageRect = Rect(pauseImageLeft, pauseImageTop, pauseImageRight, pauseImageBottom)

        /*  val resumeImageLeft = (ScreenSize.SCREEN_WIDTH - pauseImageSize) / 2
        val resumeImageTop = (ScreenSize.SCREEN_HEIGHT - pauseImageSize) / 2
        val resumeImageRight = resumeImageLeft + pauseImageSize
        val resumeImageBottom = resumeImageTop + pauseImageSize
        pauseImageRect = Rect(resumeImageLeft, resumeImageTop, resumeImageRight, resumeImageBottom)
*/




        cot = Cot(resources)
        createCot(resources)
    }

    override fun run() {
        Log.d(TAG, "Thread Started")

        while (isRunning) {
            if (isPaused) {
                pauseStartTime = SystemClock.elapsedRealtime()
                // Espera até que o estado de pausa seja alterado
                while (isPaused) {
                    // Se necessário, você pode adicionar algum código aqui
                }
                val elapsedPauseTime = SystemClock.elapsedRealtime() - pauseStartTime
                // Atualiza o tempo de pausa total
                pauseTime += elapsedPauseTime
            }
            if (holder == null) return
            startTime = System.nanoTime()
            val canvas = holder.lockCanvas()
            if (canvas != null) {
                try {
                    synchronized(holder) {
                        initScoreUpdate()
                        render(canvas)
                        renderBird(canvas)
                        renderCot(canvas)
                        renderScore(canvas)
                        renderPause(canvas)
                    }
                } finally {
                    holder.unlockCanvasAndPost(canvas)
                }
            }
            frameTime = (System.nanoTime() - startTime) / 1000000
            if (frameTime < FPS) {
                try {
                    Thread.sleep(FPS - frameTime)
                } catch (e: InterruptedException) {
                    Log.e("Interrupted Stuff", "Thread is asleep. Error.")
                }
            }
        }
        Log.d(TAG, "Thread has reached its finale.")
    }


    private fun createCot(resources: Resources) {
        for (i in 0 until numCot) {
            val cot = Cot(resources)
            cot.x = ScreenSize.SCREEN_WIDTH + kc * i
            cot.ccY = ran.nextInt(maxY - minY) + minY
            cotArray.add(cot)
        }
    }

    private fun scaleResize(bitmap: Bitmap): Bitmap {
        var ratio: Float = (bitmap.width / bitmap.height).toFloat()
        val scaleWidth: Int = (ratio * ScreenSize.SCREEN_HEIGHT).toInt()
        return Bitmap.createScaledBitmap(bitmap, scaleWidth, ScreenSize.SCREEN_HEIGHT, false)

    }


    private fun renderCotTest(canvas: Canvas?) {
        if (state == 1) {
            val gap = 200 // Defina o espaçamento desejado entre os cots

            for (i in 0 until numCot) {
                val currentCot = cotArray[i]

                if (currentCot.x + cot!!.w < bird.x) {
                    currentCot.x += (numCot * kc).toInt()
                    currentCot.ccY = ran.nextInt(maxY - minY) + minY
                }

                if (!isDead) {
                    currentCot.x -= velocityCot
                }

                val topY = currentCot.ccY - cot!!.cotTop.height
                val bottomY = currentCot.getBottomY() + gap

                // rendering top pipes
                cot?.cotTop?.let { topPipe ->
                    canvas?.drawBitmap(topPipe, currentCot.x.toFloat(), topY.toFloat(), null)
                }

                // rendering bottom pipes (cotTop.x = cotBottom.x)
                cot?.cotBottom?.let { bottomPipe ->
                    canvas?.drawBitmap(bottomPipe, currentCot.x.toFloat(), bottomY.toFloat(), null)
                }

                if (bird.x + bird.getBirb(0).width > currentCot.x && bird.x < currentCot.x + cot!!.w) {
                    if (bird.y < topY || bird.y + bird.getBirb(0).height > bottomY) {
                        isDead = true
                        onDeath()
                    }
                } else if (bird.y + bird.getBirb(0).height > topY && bird.x + bird.getBirb(0).width > currentCot.x && bird.x < currentCot.x + cot!!.w) {
                    isDead = true
                    onDeath()
                }
            }
        }
    }


    private fun renderCot(canvas: Canvas?) {
        if (state == 1) {
            if (cotArray[iCot].x < bird.x - cot!!.w) {
                iCot = (iCot + 1) % numCot
            } else if (cotArray[iCot].x < bird.x + bird.getBirb(0).width && (cotArray[iCot].ccY > bird.y + bird.getBirb(
                    0
                ).height || cotArray[iCot].getBottomY() < bird.y)
            ) {
                isDead = true
                onDeath()
            }


            for (i in 0 until numCot) {
                if (cotArray[i].x < -cot!!.w) {
                    cotArray[i].x += numCot * kc
                    cotArray[i].ccY = ran.nextInt(maxY - minY) + minY
                }



                if (!isDead) {
                    cotArray[i].x -= velocityCot
                }

                // rendering top pipes
                cot?.cotTop?.let {
                    canvas?.drawBitmap(
                        it, cotArray[i].x.toFloat(), cotArray[i].getTopY().toFloat(), null
                    )
                }

                var bottomY = cotArray[i].getBottomY() + gap

                // rendering bottom pipes (cotTop.x = cotBottom.x)
                cot?.cotBottom?.let {
                    canvas?.drawBitmap(
                        it, cotArray[i].x.toFloat(), bottomY.toFloat(), null
                    )
                }
            }
        }
    }


    private fun renderBird(canvas: Canvas?) {
        if (state == 1 && !isDead) {
            if (bird.y < (ScreenSize.SCREEN_HEIGHT - bird.getBirb(0).height)) {
                velocityBird += 2 // fall down
                bird.y += velocityBird // fly up
            }
        }

        if (canvas != null && !isDead) {
            var current = bird.currentFrame
            if (current <= bird.maxFrame) {
                canvas.drawBitmap(bird.getBirb(current), bird.x.toFloat(), bird.y.toFloat(), null)
            }
            current++

            if (current >= bird.maxFrame) current = 0
            bird.currentFrame = current
        }
    }


    private fun render(canvas: Canvas?) {
        if (!isDead) {
            backgroundImage.x = backgroundImage.x - velocity
        }
        if (backgroundImage.x < -bitmapImage!!.width) {
            backgroundImage.x = 0
        }
        bitmapImage?.let { canvas!!.drawBitmap(it, (backgroundImage.x).toFloat(), (backgroundImage.y).toFloat(), null) }

        if (backgroundImage.x < -(bitmapImage!!.width - ScreenSize.SCREEN_WIDTH)) {
            bitmapImage?.let {
                canvas!!.drawBitmap(
                    it, (backgroundImage.x + bitmapImage!!.width).toFloat(), (backgroundImage.y).toFloat(), null
                )

            }
        }

    }

    fun jump() {
        Log.i("JUMP", "Initialize Jump")
        state = 1
        if (bird.y > 0) {
            velocityBird = -30
        }
    }

    fun onDeath() {
        isRunning = false
        cancelScoreUpdate()
        (context as Activity).runOnUiThread {
            val dialogUtils = DialogUtils.getInstance()
            dialogUtils.createDialogDeath(context)
        }


    }

    private fun renderScore(canvas: Canvas?) {
        val typeface = ResourcesCompat.getFont(context, R.font.pixel_font)
        paint.typeface = typeface

        canvas?.drawText("Score: $score", canvas.width / 2f, 200f, paint)
    }


    fun initScoreUpdate() {
        scoreUpdateTimer = Timer()
        scoreUpdateTimer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                if (!isScoreUpdatePaused) {
                    1.increaseScore() // Aumenta a pontuação em 1 ponto a cada intervalo
                }
            }
        }, scoreUpdateInterval, scoreUpdateInterval)
    }

    private fun Int.increaseScore() {
        if (!isScoreUpdatePaused) {
            score += this
        }
    }


    fun cancelScoreUpdate() {
        scoreUpdateTimer.cancel()
        scoreUpdateTimer.purge()
    }

    fun pauseScoreUpdate() {
        isScoreUpdatePaused = true
    }

    fun resumeScoreUpdate() {
        isScoreUpdatePaused = false
    }


    fun pauseGame() {
        pauseImage = BitmapFactory.decodeResource(resources, R.drawable.ic_pause)
        pauseScoreUpdate()
        isPaused = true

    }

    fun resumeGame() {
        pauseImage = BitmapFactory.decodeResource(resources, R.drawable.ic_play)
        resumeScoreUpdate()
        isPaused = false
    }

    fun resetGame() {
        isPaused = false
        isScoreUpdatePaused = false
        pauseStartTime = 0
        pauseTime = 0
    }

    fun onClickPause() {
        if (isPaused) {
            resumeGame()
        } else {
            pauseGame()
        }

    }

    fun renderPause(canvas: Canvas) {
        Log.i("MY SIZE", "X: $pauseImageX\nY: $pauseImageY")


        pauseImage?.let {
            pauseImageX = (ScreenSize.SCREEN_WIDTH - it.width)
            pauseImageY = 0

            canvas.drawBitmap(it, this.pauseImageX.toFloat(), this.pauseImageY.toFloat(), null)
        }

    }


}

