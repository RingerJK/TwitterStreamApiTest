package com.ringerjk.twitterapitestapp.ui.main

import com.ringerjk.twitterapitestapp.model.Twit
import com.ringerjk.twitterapitestapp.model.User
import com.ringerjk.twitterapitestapp.repository.StreamRepository
import com.ringerjk.twitterapitestapp.rule.RxRuleLazy
import io.reactivex.Observable
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.powermock.modules.junit4.PowerMockRunner
import java.util.concurrent.TimeUnit


@RunWith(PowerMockRunner::class)
class MainPresenterTest : TestCase() {

	lateinit var mainPresenter: MainPresenter
	@Mock
	lateinit var streamRepository: StreamRepository
	@Mock
	lateinit var mainView: MainContract.View

	@Rule
	var rxRule = RxRuleLazy()

	@Before
	fun before() {
		mainPresenter = MainPresenter(streamRepository)
		mainPresenter.attachView(mainView)
	}

	@After
	fun after() {
	}

	@Test
	fun testStream() {
		val twit1 = Twit(0, "", "text1", User(0, "Name1", "Url1"))
		val twit2 = Twit(1, " ", "text2", User(2, "Name2", "Url2"))
		`when`(streamRepository.sampleStream()).thenReturn(Observable.just(twit1, twit2))
		val testSubscriber = mainPresenter.getStream()
				.test()
				.awaitDone(500, TimeUnit.MILLISECONDS)

		testSubscriber.assertValues(twit1, twit2)
		testSubscriber.onError(Exception())
		testSubscriber.assertError(Exception::class.java)
	}
}