package com.ankur.popularmovies._repository

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

class SchedulerProviderImpl : SchedulerProvider {
  override fun io(): Scheduler {
    return Schedulers.io()
  }
}
