package com.etb.flappybird.game.thread

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.util.Log
import android.view.SurfaceHolder
import com.etb.flappybird.R
import com.etb.flappybird.game.model.BackgroundImage
import com.etb.flappybird.game.model.Bird
import com.etb.flappybird.game.model.Cot
import com.etb.flappybird.game.model.ScreenSize
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


    private var state: Int = 0
    private var velocityBird: Int = 3

    var cot: Cot? = null
    val numCot = 2
    val velocityCot = 10
    val minY = 250
    val maxY = ScreenSize.SCREEN_HEIGHT - minY - 500
    val kc = ScreenSize.SCREEN_WIDTH * 3 / 4
    var cotArray: ArrayList<Cot> = arrayListOf()
    var ran: Random = Random


    var iCot = 0
    var isDead = false

    var isRunning: Boolean = false
        get() = field
        set(value) {
            field = value
        }

    constructor(holder: SurfaceHolder, resources: Resources) {
        this.holder = holder
        this.resources = resources
        isRunning = true

        bird = Bird(resources)

        bitmapImage = BitmapFactory.decodeResource(resources, R.drawable.run_background)
        bitmapImage = this.bitmapImage?.let { scaleResize(it) }

        cot = Cot(resources)
        createCot(resources)
    }

    override fun run() {
        Log.d(TAG, "Thread Started")

        while (isRunning){
            if (holder == null) return
            startTime = System.nanoTime()
            val canvas = holder.lockCanvas()
            if(canvas != null){
                try{
                    synchronized(holder){
                        render(canvas)
                        renderBirdTest(canvas)
                        renderCotTest(canvas)
                    }
                }
                finally{
                    holder.unlockCanvasAndPost(canvas)
                }
            }
            frameTime = (System.nanoTime() - startTime) / 1000000
            if (frameTime < FPS) {
                try{
                    Thread.sleep( FPS - frameTime)
                }catch (e : InterruptedException){
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
            if (cotArray[iCot].x < bird.x - cot!!.w) {
                iCot++
                if (iCot > numCot - 1) {
                    iCot = 0
                }
            } else if (
                bird.x + bird.getBirb(0).width > cotArray[iCot].x &&
                bird.x < cotArray[iCot].x + cot!!.w &&
                (bird.y < cotArray[iCot].ccY || bird.y + bird.getBirb(0).height > cotArray[iCot].getBottomY())
            ) {
                isDead = true
            }

            for (i in 0 until numCot) {
                if (cotArray[i].x < -cot!!.w) {
                    cotArray[i].x += numCot * kc
                    cotArray[i].ccY = ran.nextInt(maxY - minY) + minY
                }

                if (!isDead) {
                    cotArray[i].x -= velocityCot
                }

                canvas?.drawBitmap(
                    cot!!.cotTop,
                    cotArray[i].x.toFloat(),
                    cotArray[i].getTopY().toFloat(),
                    null
                )

                canvas?.drawBitmap(
                    cot!!.cotBottom,
                    cotArray[i].x.toFloat(),
                    cotArray[i].getBottomY().toFloat(),
                    null
                )
            }
        }
    }

    private fun birdDied(){

    }

    private fun renderCot(canvas: Canvas?){
        if(state == 1) {
            if (cotArray.get(iCot).x < bird.x - cot!!.w) {
                iCot++
                if (iCot > numCot - 1) {
                    iCot = 0
                }
            } else if (((cotArray.get(iCot).x) < bird.getBirb(0).width) &&
                (cotArray.get(iCot).ccY > bird.y || cotArray.get(iCot).getBottomY() < bird.y + bird.getBirb(0).height)
            )
                isDead = true


            for (i in 0 until numCot) {// 0, 1, 2
                if (cotArray.get(i).x < - cot!!.w){

                    cotArray.get(i).x = cotArray.get(i).x + numCot * kc
                    cotArray.get(i).ccY = ran.nextInt(maxY - minY) + minY

                }

                if(!isDead) {
                    cotArray.get(i).x = cotArray.get(i).x - velocityCot
                }


                canvas!!.drawBitmap(
                    cot!!.cotTop,
                    cotArray.get(i).x.toFloat(),
                    cotArray.get(i).getTopY().toFloat(),
                    null
                )

                          canvas!!.drawBitmap(
                    cot!!.cotBottom,
                    cotArray.get(i).x.toFloat(),
                    cotArray.get(i).getBottomY().toFloat(),
                    null
                )

            }
        }
    }

    private fun  renderBird(canvas: Canvas?){
        if(state == 1){
            if(!isDead) {
                if (bird.y < (ScreenSize.SCREEN_HEIGHT - bird.getBirb(0).height)) {
                    velocityBird = velocityBird + 2 // fall down
                    bird.y = bird.y + velocityBird // fly up
                }
            }
        }
        if(canvas != null && !isDead){
            var current : Int = bird.currentFrame
            if (current <= bird.maxFrame) {
                canvas!!.drawBitmap(bird.getBirb(current), bird.x.toFloat(), bird.y.toFloat(), null)
            }
            current++

            if(current >= bird.maxFrame)
                current = 0
            bird.currentFrame = current
        }
    }

    private fun renderBirdTest(canvas: Canvas?) {
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

            if (current >= bird.maxFrame)
                current = 0
            bird.currentFrame = current
        }
    }


    private fun render(canvas: Canvas?){
        if (!isDead){
            backgroundImage.x = backgroundImage.x - velocity
        }
        if (backgroundImage.x < - bitmapImage!!.width){
            backgroundImage.x = 0
        }
        bitmapImage?.let { canvas!!.drawBitmap(it, (backgroundImage.x).toFloat(), (backgroundImage.y).toFloat(), null) }

        if (backgroundImage.x < - (bitmapImage!!.width - ScreenSize.SCREEN_WIDTH)){
            bitmapImage?.let { canvas!!.drawBitmap(it, (backgroundImage.x + bitmapImage!!.width).toFloat(), (backgroundImage.y).toFloat(), null) }
        }
    }

    fun  jump(){
        Log.i("JUMP", "Initialize Jump")
        state = 1
         if (bird.y > 0 ){
             velocityBird = -30
         }
    }
}