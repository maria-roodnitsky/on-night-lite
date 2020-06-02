package on.night.ui.login;

import android.graphics.drawable.AnimationDrawable;
import android.util.Log;

public class AnimationDrawableWithCallback extends AnimationDrawable {

    interface IAnimationFinishListener {
        void onAnimationFinished();
    }

    private boolean finished = false;
    private IAnimationFinishListener animationFinishListener;

    @Override
    public void start() {
        //Log.
        super.start();
    }

    public AnimationDrawableWithCallback (AnimationDrawable a) {

        for (int i = 0; i  < a.getNumberOfFrames(); i++) {
            this.addFrame(a.getFrame(i), a.getDuration(i));
        }
        Log.d("anim", getNumberOfFrames()+"");

    }

    public IAnimationFinishListener getAnimationFinishListener() {
        return animationFinishListener;
    }




    void setAnimationFinishListener(IAnimationFinishListener animationFinishListener) {
        this.animationFinishListener = animationFinishListener;
    }

    @Override
    public boolean selectDrawable(int index) {


        if (index >= (this.getNumberOfFrames()-1))
        {
            if (!finished)
            {
                finished = true;
                if (animationFinishListener != null) animationFinishListener.onAnimationFinished();
            }
            return false;
        }
        boolean ret = super.selectDrawable(index);


        return ret;
    }
}
