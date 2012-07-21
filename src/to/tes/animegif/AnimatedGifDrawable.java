/**
 * Animated GIF Drawable
 */
package to.tes.animegif;

import java.lang.reflect.Field;
import android.graphics.drawable.AnimationDrawable;

public class AnimatedGifDrawable extends AnimationDrawable {
    private int mPauseFrame = 0;

    /**
     * get current frame (use reflection)
     *
     * @return
     */
    public int getCurrentFrame() {
        try {
            Field f = AnimationDrawable.class.getDeclaredField("mCurFrame");
            f.setAccessible(true);
            return f.getInt(this);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * pause animation
     */
    public void pause() {
        if (isRunning()) {
            mPauseFrame = getCurrentFrame();
            stop();
        }
    }

    /**
     * resume animation
     */
    public void resume() {
        resume(mPauseFrame);
    }

    /**
     * resume animation with frame
     *
     * @param frame
     */
    public void resume(int frame) {
        stop();

        try {
            Field f = AnimationDrawable.class.getDeclaredField("mCurFrame");
            f.setAccessible(true);
            f.setInt(this, Math.max(0, frame) - 1);
        } catch (Exception e) {
        }
        run();
    }
}
