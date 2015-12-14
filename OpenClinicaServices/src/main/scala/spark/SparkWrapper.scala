package spark

trait SparkWrapper {
  def convertToRequest(path: String, f: (Request, Response) => AnyRef): Route = {
    new Route() {
      override def handle(request: Request, response: Response): AnyRef = {
        f(request, response)
      }
    }
  }

  def convertToFilter(path: String, f: (Request, Response) => Unit): Filter = {
    new Filter() {
      override def handle(request: Request, response: Response): Unit = {
        f(request, response)
      }
    }
  }

  def get(path: String)(implicit f: (Request, Response) => AnyRef): Unit = {
    Spark.get(path, convertToRequest(path, f))
  }

  def post(path: String)(implicit f: (Request, Response) => AnyRef): Unit = {
    Spark.post(path, convertToRequest(path, f))
  }

  def put(path: String)(implicit f: (Request, Response) => AnyRef): Unit = {
    Spark.put(path, convertToRequest(path, f))
  }

  def patch(path: String)(implicit f: (Request, Response) => AnyRef): Unit = {
    Spark.patch(path, convertToRequest(path, f))
  }

  def delete(path: String)(implicit f: (Request, Response) => AnyRef): Unit = {
    Spark.delete(path, convertToRequest(path, f))
  }

  def head(path: String)(implicit f: (Request, Response) => AnyRef): Unit = {
    Spark.head(path, convertToRequest(path, f))
  }

  def trace(path: String)(implicit f: (Request, Response) => AnyRef): Unit = {
    Spark.trace(path, convertToRequest(path, f))
  }

  def connect(path: String)(implicit f: (Request, Response) => AnyRef): Unit = {
    Spark.connect(path, convertToRequest(path, f))
  }

  def options(path: String)(implicit f: (Request, Response) => AnyRef): Unit = {
    Spark.options(path, convertToRequest(path, f))
  }

  def before(path: String)(implicit f: (Request, Response) => Unit): Unit = {
    Spark.before(path, convertToFilter(path, f))
  }

  def after(path: String)(implicit f: (Request, Response) => Unit): Unit = {
    Spark.after(path, convertToFilter(path, f))
  }

  def get(path: String, acceptType: String)(implicit f: (Request, Response) => AnyRef): Unit = {
    Spark.get(path, acceptType, convertToRequest(path, f))
  }

  def post(path: String, acceptType: String)(implicit f: (Request, Response) => AnyRef): Unit = {
    Spark.post(path, acceptType, convertToRequest(path, f))
  }

  def put(path: String, acceptType: String)(implicit f: (Request, Response) => AnyRef): Unit = {
    Spark.put(path, acceptType, convertToRequest(path, f))
  }

  def patch(path: String, acceptType: String)(implicit f: (Request, Response) => AnyRef): Unit = {
    Spark.patch(path, acceptType, convertToRequest(path, f))
  }

  def delete(path: String, acceptType: String)(implicit f: (Request, Response) => AnyRef): Unit = {
    Spark.delete(path, acceptType, convertToRequest(path, f))
  }

  def head(path: String, acceptType: String)(implicit f: (Request, Response) => AnyRef): Unit = {
    Spark.head(path, acceptType, convertToRequest(path, f))
  }

  def trace(path: String, acceptType: String)(implicit f: (Request, Response) => AnyRef): Unit = {
    Spark.trace(path, acceptType, convertToRequest(path, f))
  }

  def connect(path: String, acceptType: String)(implicit f: (Request, Response) => AnyRef): Unit = {
    Spark.connect(path, acceptType, convertToRequest(path, f))
  }

  def options(path: String, acceptType: String)(implicit f: (Request, Response) => AnyRef): Unit = {
    Spark.options(path, acceptType, convertToRequest(path, f))
  }

  def before(path: String, acceptType: String)(implicit f: (Request, Response) => Unit): Unit = {
    Spark.before(path, acceptType, convertToFilter(path, f))
  }

  def after(path: String, acceptType: String)(implicit f: (Request, Response) => Unit): Unit = {
    Spark.after(path, acceptType, convertToFilter(path, f))
  }
}