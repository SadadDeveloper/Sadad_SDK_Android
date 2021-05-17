package cards.pay.paycardsrecognizer.sdk.ndk;


import android.graphics.Bitmap;

//@RestrictTo(RestrictTo.Scope.LIBRARY)
public interface RecognitionStatusListener {

    void onRecognitionComplete(RecognitionResult result);

    void onCardImageReceived(Bitmap bitmap);

}
