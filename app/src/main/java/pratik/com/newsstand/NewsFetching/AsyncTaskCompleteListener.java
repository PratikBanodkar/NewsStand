package pratik.com.newsstand.NewsFetching;

/**
 * Created by Pratz on 24-01-2018.
 */
/**
 * This is a useful callback mechanism so we can abstract our AsyncTasks out into separate, re-usable
 * and testable classes yet still retain a hook back into the calling activity. Basically, it'll make classes
 * cleaner and easier to unit test.
 *
 * @param <T>
 */

public interface AsyncTaskCompleteListener<T,U> {
    /**
            * Invoked when the AsyncTask has completed its execution.
            * @param result The resulting object from the AsyncTask.
     */
    public void onTaskComplete(T result,U ca);
}
