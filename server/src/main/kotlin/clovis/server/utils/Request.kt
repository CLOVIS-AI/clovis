package clovis.server.utils

import arrow.core.Either
import clovis.server.RequestFailure

typealias Request<T> = Either<RequestFailure, T>
