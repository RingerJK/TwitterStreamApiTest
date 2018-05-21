package com.ringerjk.twitterapitestapp.rule


import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

open class RxRuleLazy : TestRule {
    private val scheduler by lazy { Schedulers.trampoline() }

    override fun apply(base: Statement, d: Description): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {

                try {
                    RxJavaPlugins.setComputationSchedulerHandler { scheduler }
                    RxJavaPlugins.setIoSchedulerHandler { scheduler }
                    RxJavaPlugins.setNewThreadSchedulerHandler { scheduler }
                    RxJavaPlugins.setSingleSchedulerHandler { scheduler }
                    RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler }
                    base.evaluate()
                } finally {
                    RxJavaPlugins.reset()
                    RxAndroidPlugins.reset()
                }
            }
        }
    }
}