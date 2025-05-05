package bumh3r.interfaces;

public class CallBack {

    @FunctionalInterface
    public interface Success {
        void onSuccess(String str);
    }

    @FunctionalInterface
    public interface Fail {
        void onFail(String str);
    }


    @FunctionalInterface
    public interface FailWithTitle {
        void onFail(String title,String str);
    }

}
