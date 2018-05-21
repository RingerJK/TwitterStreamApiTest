package com.ringerjk.twitterapitestapp.rule

import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

open class RxRule : TestRule {
    override fun apply(base: Statement, d: Description): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                RxJavaPlugins.setInitIoSchedulerHandler { Schedulers.trampoline() }
                RxJavaPlugins.setInitComputationSchedulerHandler { Schedulers.trampoline() }
                RxJavaPlugins.setInitNewThreadSchedulerHandler { Schedulers.trampoline() }
                RxJavaPlugins.setInitSingleSchedulerHandler { Schedulers.trampoline() }
                RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
                try {
                    base.evaluate()
                } finally {
                    RxJavaPlugins.reset()
                    RxAndroidPlugins.reset()
                }
            }
        }
    }
}
