package cards.pay.paycardsrecognizer.sdk.ndk;

//@RestrictTo(RestrictTo.Scope.LIBRARY)
public interface TorchStatusListener {

    void onTorchStatusChanged(boolean turnTorchOn);

}
