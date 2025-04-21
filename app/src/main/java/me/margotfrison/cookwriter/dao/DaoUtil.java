package me.margotfrison.cookwriter.dao;

import android.app.Activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * Utility class to be simplify async request in the database (used in UI thread).
 * This class also remembers {@link Disposable}s from an issuer that use this class.
 * Allowing to auto dispose all previously requested data from the database all at once.
 */
public class DaoUtil {
    private final static Map<Object, List<Disposable>> DISPOSABLES = new HashMap<>();
    private final static Scheduler SCHEDULER = Schedulers.io();

    /**
     * Get data from the database and run the {@link Consumer} once retrieved.
     * The {@link Consumer} is ran in an non-UI thread. Use {@link DaoUtil#subscribeUIThread(Single, Consumer, Activity)}
     * if you want it to run in a UI thread.
     * @param single the request to the database
     * @param consumer the {@link Consumer} to be ran once the data has been fetched
     * @param issuer a unique identifier that will be used to register the {@link Disposable} from the request.
     *               Allowing you to use {@link DaoUtil#disposeAll(Object)}
     * @param <T> the type of data to be fetched
     */
    public static <T> void subscribe(Single<T> single, Consumer<? super T> consumer, Object issuer) {
        registerDisposable(issuer, single.subscribeOn(SCHEDULER).subscribe(consumer));
    }

    /**
     * Similar to {@link DaoUtil#subscribe(Single, Consumer, Object)} but with a {@link Completable}
     * and an {@link Action} instead
     * @param completable the request to the database
     * @param runnable the {@link Action} to be ran once the data has been processed
     * @param issuer a unique identifier that will be used to register the {@link Disposable} from the request.
     *               Allowing you to use {@link DaoUtil#disposeAll(Object)}
     */
    public static void subscribe(Completable completable, Action runnable, Object issuer) {
        registerDisposable(issuer, completable.subscribeOn(SCHEDULER).subscribe(runnable));
    }

    /**
     * Similar to {@link DaoUtil#subscribe(Single, Consumer, Object)} but the {@link Consumer} will be
     * ran on a UI thread instead
     * @param single the request to the database
     * @param consumer the {@link Consumer} to be ran once the data has been fetched
     * @param issuer a unique identifier that will be used to register the {@link Disposable} from the request.
     *               Allowing you to use {@link DaoUtil#disposeAll(Object)}
     * @param <T> the type of data to be fetched
     */
    public static <T> void subscribeUIThread(Single<T> single, Consumer<? super T> consumer, Activity issuer) {
        registerDisposable(issuer, single.subscribeOn(SCHEDULER).subscribe((result) -> {
            issuer.runOnUiThread(() -> {
                try {
                    consumer.accept(result);
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            });
        }));
    }

    /**
     * Dispose all {@link Disposable}s used by the provided issuer
     * @param issuer a unique identifier that has been used to register {@link Disposable}s from all requests.
     */
    public static void disposeAll(Object issuer) {
        Optional.ofNullable(DISPOSABLES.get(issuer))
                .orElse(List.of())
                .forEach(Disposable::dispose);
    }

    /**
     * Register a {@link Disposable} for this issuer
     * @param issuer the id of the issuer
     * @param disposable the {@link Disposable} to register
     */
    private static void registerDisposable(Object issuer, Disposable disposable) {
        List<Disposable> disposables = Optional.ofNullable(DISPOSABLES.get(issuer))
                .orElseGet(ArrayList::new);
        disposables.add(disposable);
        DISPOSABLES.put(issuer, disposables);
    }
}
