# Leanpub-Client
> (find a better name. maybe) A Leanpub-API client library

This client library provides an object-oriented interface to the [Leanpub API](https://leanpub.com/help/api)
and is written in Scala. You can preview and publish your books through this library, create and update coupons
and get sales information. Previewing a single file, polling the job status and getting the latest version of your
book is not yet implemented.

## Usage Example

We need to set an API-Key as an environment variable. We also need to set some implicits to place a request.
If we want to get all the coupons for our book "myfancybook", we would do it like so:

```scala
implicit val system = ActorSystem()
implicit val materializer = ActorMaterializer()
import system.dispatcher

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

If we want to create a coupon for our book, we can do it (using the same setup) like this:

```scala
val coupon = CreateCoupon("testcoupon", List(PackageDiscount("book", 1.0)), LocalDate.of(2016, 7, 1))
val response = client.createCoupon("myfancybook", coupon)
```

