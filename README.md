# Leanpub-Client
> A Leanpub-API client library

This client library provides an object-oriented interface to the [Leanpub API](https://leanpub.com/help/api)
and is written in Scala, using the Akka HTTP client. You can preview and publish your books through this library, create and update coupons
and get sales information. Previewing a single file, polling the job status and getting the latest version of your book is not yet implemented.
At the moment, Scala 2.12 is not supported. Please use 2.11 instead.

## Usage Example

To create an instance of `LeanPubClient`, we need to pass it a few things: an `akka.http.scaladsl.HttpExt`, a LeanPub API key, a request timeout, as well as
an implicit `akka.stream.Materializer` and a `scala.concurrent.ExecutionContext`.
One recommended way of providing the API key is to read it from an environment
variable.

If we want to get all the coupons for our book "myfancybook", we would do it
like so:

```scala
import com.innoq.leanpubclient.LeanPubClient
import scala.concurrent.duration._

implicit val system = ActorSystem()
implicit val materializer = ActorMaterializer()
import system.dispatcher // to get an implicit ExecutionContext

val http = Http()

try {
  val client = new LeanPubClient(http, sys.env("LEANPUB_API_KEY"), 2000.millis)
  val response = client.getCoupons("myfancybook")
  println(Await.result(response, 5.seconds))
}
finally {
  http.shutdownAllConnectionPools() andThen { case _ => system.terminate() }
}
```

If we want to create a coupon for our book, we can do it (using the same setup)
like this:

```scala
val coupon = CreateCoupon("testcoupon", List(PackageDiscount("book", 1.0)), LocalDate.of(2016, 7, 1))
val response = client.createCoupon("myfancybook", coupon)
```

## Release status

No binaries of this project have been released yet.

## Contributors

Tina Schönborn

## License

MIT 

## Copyright

Copyright 2016 innoQ Deutschland GmbH
