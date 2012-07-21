/**
 * Animated GIF utility class
 */
package to.tes.animegif;

import java.io.File;
import java.io.FileInputStream;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

public class AnimeUtils extends Activity {
    /**
     * start/stop animation
     *
     * @param view
     * @param start
     */
    static public void startViewAnimation(View view, boolean start) {
        if (view != null) {
            // background drawable
            startDrawableAnimation(view.getBackground(), start);
            if (view instanceof ImageView) {
                // image drawable
                startDrawableAnimation(((ImageView)view).getDrawable(), start);
            }
        }
    }

    /**
     * start/stop animation
     *
     * @param d
     * @param start
     */
    static public void startDrawableAnimation(Drawable d, boolean start) {
        if (d instanceof AnimationDrawable) {
            if (start) {
                ((AnimationDrawable)d).start();
            } else {
                ((AnimationDrawable)d).stop();
            }
        }
    }

    /**
     * load drawable from resource id
     *
     * @param rsrc
     * @param resid
     * @return
     */
    static public Drawable loadDrawableFromResource(Resources rsrc, int resid) {
        // load from resource
        Movie movie = Movie.decodeStream(rsrc.openRawResource(resid));
        if ((movie != null) && movie.duration() > 0) {
            return makeMovieDrawable(rsrc, movie);
        } else {
            // not animated GIF
            return rsrc.getDrawable(resid);
        }
    }

    /**
     * load drawable from file path
     *
     * @param rsrc
     * @param path
     * @return
     */
    static public Drawable loadDrawableFromFile(Resources rsrc, String path) {
        // load from file
        // Movie movie = Movie.decodeFile(path);
        Movie movie = null;
        try {
            File file = new File(path);
            FileInputStream is = new FileInputStream(file);
            byte data[] = new byte[(int)file.length()];
            is.read(data);
            is.close();
            movie = Movie.decodeByteArray(data, 0, data.length);
        } catch (Exception e) {
        }

        if ((movie != null) && movie.duration() > 0) {
            return makeMovieDrawable(rsrc, movie);
        } else {
            // not animated GIF
            return Drawable.createFromPath(path);
        }
    }

    /**
     * make AnimationDrawable from Movie instance
     *
     * @param rsrc
     * @param movie
     * @return
     */
    static private Drawable makeMovieDrawable(Resources rsrc, Movie movie) {
        int duration = movie.duration();
        int width = movie.width(), height = movie.height();

        AnimationDrawable result = new AnimationDrawable();
        result.setOneShot(false); // for loop

        Drawable frame = null;
        int start = 0;
        for (int time = 0; time < duration; time += 10) {
            if (movie.setTime(time)) {
                if (frame != null) {
                    // add previous frame
                    result.addFrame(frame, time - start);
                }

                // make frame
                Bitmap bitmap = Bitmap.createBitmap(width, height,
                        Bitmap.Config.RGB_565);	// save heap
                        //Bitmap.Config.ARGB_8888);	// high quality
                movie.draw(new Canvas(bitmap), 0, 0);
                frame = new BitmapDrawable(rsrc, bitmap);
                start = time;
            }
        }

        if (frame != null) {
            // add last frame
            result.addFrame(frame, duration - start);
        }
        return result;
    }
}
