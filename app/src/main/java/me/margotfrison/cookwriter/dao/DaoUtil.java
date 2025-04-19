package me.margotfrison.cookwriter.dao;

import android.app.Activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DaoUtil {
    private final static Map<Object, List<Disposable>> DISPOSABLES = new HashMap<>();

    public static <T> void subscribe(Single<T> single, Consumer<? super T> consumer, Object issuer) {
        registerDisposable(issuer, single.subscribeOn(Schedulers.io()).subscribe(consumer));
    }

    public static void subscribe(Completable completable, Action runnable, Object issuer) {
        registerDisposable(issuer, completable.subscribeOn(Schedulers.io()).subscribe(runnable));
    }

    public static <T> void subscribeUIThread(Single<T> single, Consumer<? super T> consumer, Activity issuer) {
        registerDisposable(issuer, single.subscribeOn(Schedulers.io()).subscribe((result) -> {
                            issuer.runOnUiThread(() -> {
                                try {
                                    consumer.accept(result);
                                } catch (Throwable e) {
                                    throw new RuntimeException(e);
                                }
                            });
                        }));
    }

    public static void disposeAll(Object issuer) {
        Optional.ofNullable(DISPOSABLES.get(issuer))
                .orElse(List.of())
                .forEach(Disposable::dispose);
    }

    private static void registerDisposable(Object issuer, Disposable disposable) {
        List<Disposable> disposables = Optional.ofNullable(DISPOSABLES.get(issuer))
                .orElseGet(ArrayList::new);
        disposables.add(disposable);
        DISPOSABLES.put(issuer, disposables);
    }
}
