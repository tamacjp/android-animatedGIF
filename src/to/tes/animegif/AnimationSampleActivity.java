package to.tes.animegif;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;

public class AnimationSampleActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Drawable d;
        d = AnimeUtils.loadDrawableFromResource(getResources(),
                R.drawable.sample1);
        ((ImageView)findViewById(R.id.imageView1)).setImageDrawable(d);

        String path = Environment.getExternalStorageDirectory() + "/sample.gif";
        d = AnimeUtils.loadDrawableFromFile(getResources(), path);
        findViewById(R.id.textView1).setBackgroundDrawable(d);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        // start/stop animation
        AnimeUtils.startViewAnimation(findViewById(R.id.imageView1), hasFocus);
        AnimeUtils.startViewAnimation(findViewById(R.id.textView1), hasFocus);
    }
}
