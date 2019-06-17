package com.ankur.popularmovies._repository

import io.reactivex.Scheduler

interface SchedulerProvider {
  fun io(): Scheduler
}
