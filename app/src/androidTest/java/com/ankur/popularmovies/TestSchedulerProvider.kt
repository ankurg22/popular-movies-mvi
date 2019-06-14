package com.ankur.popularmovies

import com.ankur.popularmovies._repository.SchedulerProvider
import io.reactivex.schedulers.Schedulers

class TestSchedulerProvider: SchedulerProvider {
  override fun io() {
    Schedulers.trampoline()
  }
}
